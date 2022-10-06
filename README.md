# Kotlin Vert.x

In this project an API rest with Ktot, Arrow, JDBC was created.

## Requirements

* JDK >= 17
* Kotlin
* Docker
* Docker Compose

## Run Containers
```shell
$ docker-compose up -d
```

## Apply Formatter
```shell
$ gradle formatKotlin
$ gradle lintKotlin
```

## Run With Gradle
```shell
$ gradle run
```

## Run Jar

### Generate Jar 
```shell
$ gradle shadowJar
```

### Java -jar
```shell
$ java -jar main/build/libs/main-1.0-SNAPSHOT-all.jar
```