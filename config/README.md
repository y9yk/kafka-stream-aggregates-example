# Connector Configuration

- mysql
- mongodb

## Source Connector (MySQL)

| 항목                                              | 설명                                                                                   |
|-------------------------------------------------|--------------------------------------------------------------------------------------|
| connector.class                                 | 기본적으로 `io.debezium.connector.mysql.MySqlConnector`을 사용한다.                            |
| tasks.max                                       | Connector가 생성하는 task의 최대값, 참고로 MySQL connector는 항상 1개의 task를 생성한다. 즉, 이 값을 사용하지 않는다. |
| database.hostname                               | 접속 호스트 정보                                                                            |
| database.port                                   | 접속 포트                                                                                |
| database.user                                   | 접속 아이디                                                                               |
| database.password                               | 접속 비밀번호                                                                              |
| database.server.id                              | 접속 클라이언트 아이디                                                                         |
| topic.prefix                                    | 토픽 접두어                                                                               |
| table.include.list                              | cdc 대상 테이블 목록 (comma separator 사용한다.)                                                |
| schema.history.internal.kafka.bootstrap.servers | kafka bootstrap servers 정보                                                           |
| schema.history.internal.kafka.topic             | schema-history를 저장하는 kafka 토픽 이름                                                     |
| transforms                                      | 토픽에 넘어오는 메시지 transform 방식                                                            |
| transforms.unwrap.type                          | transform에 사용되는 클래스                                                                  |
| transforms.unwrap.drop.tombstones               | 삭제 레코드에 대한  처리 방식                                                                    |

## Sink Connector (MongoDB)

| 항목                            | 설명                                                             |
|-------------------------------|----------------------------------------------------------------|
| connector.class               | 기본적으로 `io.debezium.connector.mongodb.MongoDbConnector`을 사용한다.  |
| tasks.max                     | Connector가 생성하는 task의 최대값, MongoDB의 shards 수를 참고해서 설정하는 것이 좋다. |
| topics                        | 소비하는 토픽들 (comma separator를 사용한다.)                              |
| mongodb.connection.uri        | 접속 URI                                                         |
| mongodb.collection            | 싱크 타겟 테이블                                                      |
| mongodb.document.id.strategy  | 아이디 생성 방법                                                      |
| mongodb.delete.on.null.values | null 레코드 처리 방법                                                 |
