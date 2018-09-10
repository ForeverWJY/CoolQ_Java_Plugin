@cd target
@call java -Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=y,address=5005 -jar coolq-spring-boot-0.0.1-SNAPSHOT.jar
@pause