# Kotlin Vert.x

In this project an API rest with Vert.x, Arrow, JDBC was created. Using **Type-Classes** and **Tagless Final**.

Requirements:

* JDK >= 1.8
* Kotlin
* Docker
* Docker Compose

1. Run Containers:
    `docker-compose up -d`

2. Run With Gradle:
    `gradle run`
    
3. Run Jar:
    * Generate Jar: `gradle shadowJar`
    * Java -jar: `java -jar main/build/libs/main-1.0-SNAPSHOT-all.jar`