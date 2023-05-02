# Project Structure

- 프로젝트의 구성은 아래와 같습니다.

```bash
.
└── main
    └── java
        └── com
            └── example
                ├── aggregates
                │   └── App.java
                ├── constant
                │   └── Constant.java
                ├── model
                │   ├── Address.java
                │   ├── Addresses.java
                │   ├── Customer.java
                │   ├── CustomerAddressAggregate.java
                │   ├── DefaultId.java
                │   ├── EventType.java
                │   └── LatestAddress.java
                └── serdes
                    ├── JsonHybridDeserializer.java
                    ├── JsonPojoSerializer.java
                    └── SerdeFactory.java
```

| 항목       | 설명   |
|----------|------|
| App.java | 실행 파일 |
| Constant.java | Kafka-Streams 설정들이 정의된 파일  |
| model/*.java | Pojo 클래스들이 정의된 파일 |
| serdes/*.java | Serialization, Deserialization을 구현한 파일 |