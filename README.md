# Kotlin Vert.x

In this project an API rest with Vert.x, Arrow, JDBC was created. Using **Type-Classes** and **Tagless Final**.

## Requirements

* JDK >= 1.8
* Kotlin
* Docker
* Docker Compose

## Run Containers
```shell
$ docker-compose up -d
```

## Apply Formatter
```shell
$ gradle ktlintFormat
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