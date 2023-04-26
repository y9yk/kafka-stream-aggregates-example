package com.example.aggregates;

import com.example.constant.Constant;
import com.example.model.Address;
import com.example.model.Addresses;
import com.example.model.Customer;
import com.example.model.CustomerAddressAggregate;
import com.example.model.DefaultId;
import com.example.model.EventType;
import com.example.model.LatestAddress;
import com.example.serdes.SerdeFactory;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.inf.ArgumentParser;
import net.sourceforge.argparse4j.inf.ArgumentParserException;
import net.sourceforge.argparse4j.inf.Namespace;
import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.config.TopicConfig;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.utils.Bytes;
import org.apache.kafka.streams.Consumed;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.kstream.Printed;
import org.apache.kafka.streams.kstream.Produced;
import org.apache.kafka.streams.kstream.Serialized;
import org.apache.kafka.streams.state.KeyValueStore;

public class App {

  // final aggregates (goal) = join(parent, children)
  public static void main(String[] args) {
    ArgumentParser parser = ArgumentParsers
      .newFor("App")
      .build()
      .defaultHelp(true)
      .description(
        "Aggregate streaming messages from kafka and Produce its result to new topic."
      );
    parser
      .addArgument("-parent", "--parent")
      .help("Specify parent's topic name");
    parser
      .addArgument("-children", "--children")
      .help("Specify children' topic name");
    parser
      .addArgument("-bootstrap_servers", "--bootstrap_servers")
      .help("Specify bootstrap servers (e.g, host1:port1,host2:port2)");

    // parsing arguments
    Namespace ns = null;
    try {
      ns = parser.parseArgs(args);
    } catch (ArgumentParserException e) {
      parser.handleError(e);
      System.exit(1);
    }

    final String parentTopic = ns.getString("parent");
    final String childrenTopic = ns.getString("children");
    final String bootstrapServers = ns.getString("bootstrap_servers");
    final String TABLE_TEMP = childrenTopic + "_table_temporary";
    final String TABLE_AGGREGATE = childrenTopic + "_table_aggregate";

    // properties
    Properties props = new Properties();
    props.put(
      StreamsConfig.APPLICATION_ID_CONFIG,
      Constant.APPLICATION_ID_CONFIG
    );
    props.put(ConsumerConfig.GROUP_ID_CONFIG, Constant.GROUP_ID_CONFIG);
    props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
    props.put(
      StreamsConfig.CACHE_MAX_BYTES_BUFFERING_CONFIG,
      Constant.CACHE_MAX_BYTES_BUFFERING_CONFIG
    );
    props.put(
      StreamsConfig.COMMIT_INTERVAL_MS_CONFIG,
      Constant.COMMIT_INTERVAL_MS_CONFIG
    );
    props.put(
      TopicConfig.FLUSH_MESSAGES_INTERVAL_CONFIG,
      Constant.FLUSH_MESSAGES_INTERVAL_CONFIG
    );
    props.put(
      CommonClientConfigs.METADATA_MAX_AGE_CONFIG,
      Constant.METADATA_MAX_AGE_CONFIG
    );
    props.put(
      ConsumerConfig.AUTO_OFFSET_RESET_CONFIG,
      Constant.AUTO_OFFSET_RESET_CONFIG
    );
    props.put(
      ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG,
      Constant.ENABLE_AUTO_COMMIT_CONFIG
    );

    // serde
    final Serde<DefaultId> defaultIdSerde = SerdeFactory.createDbzEventJsonPojoSerdeFor(
      DefaultId.class,
      true
    );
    final Serde<Customer> customerSerde = SerdeFactory.createDbzEventJsonPojoSerdeFor(
      Customer.class,
      false
    );
    final Serde<Address> addressSerde = SerdeFactory.createDbzEventJsonPojoSerdeFor(
      Address.class,
      false
    );
    final Serde<Addresses> addressesSerde = SerdeFactory.createDbzEventJsonPojoSerdeFor(
      Addresses.class,
      false
    );
    final Serde<LatestAddress> latestAddressSerde = SerdeFactory.createDbzEventJsonPojoSerdeFor(
      LatestAddress.class,
      false
    );
    final Serde<CustomerAddressAggregate> customerAddressAggregateSerde = SerdeFactory.createDbzEventJsonPojoSerdeFor(
      CustomerAddressAggregate.class,
      false
    );

    // -----------------
    // stream processing
    // -----------------

    // state-store-config (for view changelog)
    Map<String, String> stateStoreConfig = new HashMap<>();
    stateStoreConfig.put(TopicConfig.SEGMENT_BYTES_CONFIG, "3000");

    StreamsBuilder builder = new StreamsBuilder();

    // customer table
    KTable<DefaultId, Customer> customerTable = builder.table(
      parentTopic,
      Consumed.with(defaultIdSerde, customerSerde)
    );

    // address stream
    KStream<DefaultId, Address> addressStream = builder.stream(
      childrenTopic,
      Consumed.with(defaultIdSerde, addressSerde)
    );

    // 1) latest-address-table
    KTable<DefaultId, LatestAddress> latestAddressTable = addressStream
      .groupByKey(Serialized.with(defaultIdSerde, addressSerde))
      .aggregate(
        LatestAddress::new,
        (DefaultId addressId, Address address, LatestAddress latest) -> {
          latest.update(
            addressId,
            new DefaultId(address.getCustomerId()),
            address
          );
          return latest;
        },
        Materialized
          .<DefaultId, LatestAddress, KeyValueStore<Bytes, byte[]>>as(
            TABLE_TEMP
          )
          .withKeySerde(defaultIdSerde)
          .withValueSerde(latestAddressSerde)
          .withLoggingEnabled(stateStoreConfig)
      );

    // 2) address-table (group by customer-id from latest-address-table)
    KTable<DefaultId, Addresses> addressTable = latestAddressTable
      .toStream()
      .map((DefaultId addressId, LatestAddress latestAddress) ->
        new KeyValue<>(latestAddress.getCustomerId(), latestAddress)
      )
      .groupByKey(Serialized.with(defaultIdSerde, latestAddressSerde))
      .aggregate(
        Addresses::new,
        (
          DefaultId customerId,
          LatestAddress latestAddress,
          Addresses addresses
        ) -> {
          addresses.update(latestAddress);
          return addresses;
        },
        Materialized
          .<DefaultId, Addresses, KeyValueStore<Bytes, byte[]>>as(
            TABLE_AGGREGATE
          )
          .withKeySerde(defaultIdSerde)
          .withValueSerde(addressesSerde)
          .withLoggingEnabled(stateStoreConfig)
      );
    // 3) customer & address table join (inner join)
    KTable<DefaultId, CustomerAddressAggregate> aggregateTable = customerTable.join(
      addressTable,
      (Customer customer, Addresses addresses) -> {
        if (customer.get_eventType() == EventType.DELETE) {
          return null;
        } else {
          return new CustomerAddressAggregate(customer, addresses.getEntries());
        }
      }
      // Materialized View (not used on this table)
    );

    // produce message
    aggregateTable
      .toStream()
      .to(
        Constant.TOPIC_FOR_PRODUCE,
        Produced.with(
          defaultIdSerde,
          (Serde<CustomerAddressAggregate>) customerAddressAggregateSerde
        )
      );

    // print-out
    aggregateTable.toStream().print(Printed.toSysOut());

    // execution
    final KafkaStreams streams = new KafkaStreams(builder.build(), props);
    streams.cleanUp();
    streams.start();

    // dispatch close event
    Runtime.getRuntime().addShutdownHook(new Thread(streams::close));
  }
}
