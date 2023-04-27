package com.example.constant;

public final class Constant {

  // kafka-stream-application-config
  public static final String APPLICATION_ID_CONFIG = "aggregates";
  public static final String GROUP_ID_CONFIG = "group1";
  public static final String BOOTSTRAP_SERVERS_CONFIG = "kafka:9092";
  public static final int CACHE_MAX_BYTES_BUFFERING_CONFIG = 10 * 1024;
  public static final int COMMIT_INTERVAL_MS_CONFIG = 60;
  public static final int FLUSH_MESSAGES_INTERVAL_CONFIG = 60;
  public static final int METADATA_MAX_AGE_CONFIG = 500;
  public static final String AUTO_OFFSET_RESET_CONFIG = "earliest";
  public static final boolean ENABLE_AUTO_COMMIT_CONFIG = true;

  // state-store-config (for changelog)
  public static final String SEGMENT_BYTES_CONFIG = "3000"; // file-rolling

  // topic for productin
  public static final String TOPIC_FOR_PRODUCE = "final_ddd_aggregate";

  private Constant() {}
}
