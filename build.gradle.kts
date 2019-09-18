import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.hidetake.gradle.swagger.generator.GenerateSwaggerCode

val versions = mapOf(
    "kotlin" to "1.3.41",
    "ktor" to "1.2.2",
    "typesafeConfig" to "1.3.4"
)

plugins {
    kotlin("jvm").version("1.3.41")
    id("org.hidetake.swagger.generator").version("2.18.1")
    id("net.ltgt.apt-idea").version("0.21")
    application
    java
}

group = "thale.info"
version = "1.0.0-SNAPSHOT"

repositories {
    mavenCentral()
    jcenter()
}

application {
    mainClassName = "thale.info.Main"
}

dependencies {

    // used in the generation of the api model
    implementation("com.sun.xml.ws:rt:2.3.2")

    // kotlin
    implementation(kotlin("stdlib-jdk8"))

    // database
    implementation("org.litote.kmongo:kmongo-core:3.11.0")
    implementation("org.litote.kmongo:kmongo-property:3.11.0")
    implementation("org.litote.kmongo:kmongo-jackson-mapping:3.11.0")

    // logging
    implementation("org.slf4j:slf4j-api:1.7.26")
    implementation("ch.qos.logback:logback-classic:1.0.13")
    implementation("io.github.microutils:kotlin-logging:1.7.6")

    // koin
    implementation("org.koin:koin-core:2.0.1")

    // http4k
    implementation("org.http4k:http4k-core:3.179.1")
    implementation("org.http4k:http4k-server-netty:3.179.1")
    implementation("org.http4k:http4k-format-gson:3.179.1")

    // swagger
    implementation ("io.swagger.core.v3:swagger-annotations:2.0.9")
    "swaggerCodegen"("io.swagger.codegen.v3:swagger-codegen-cli:3.0.11")

    // config
    implementation("com.typesafe:config:${versions["typesafeConfig"]}")

    // test
    testImplementation("io.kotlintest:kotlintest-runner-junit5:3.3.2")
    testImplementation("io.mockk:mockk:1.9.3")
}

tasks.withType<KotlinCompile> {
    swaggerSources.asMap.values.forEach { value -> dependsOn(value.code) }
    kotlinOptions.jvmTarget = "1.8"
}

configurations.all {
    resolutionStrategy {
        failOnVersionConflict()
        force("org.jetbrains.kotlin:kotlin-stdlib:${versions["kotlin"]}")
        force("org.jetbrains.kotlin:kotlin-stdlib-jdk8:${versions["kotlin"]}")
        force("org.jetbrains.kotlin:kotlin-reflect:${versions["kotlin"]}")
        force("org.jetbrains.kotlin:kotlin-stdlib-common:${versions["kotlin"]}")
        force("org.jetbrains.kotlin:kotlin-stdlib-jdk7:${versions["kotlin"]}")
        force("com.typesafe:config:${versions["typesafeConfig"]}")
        force("com.fasterxml.jackson.core:jackson-databind:2.9.9")
        force("org.slf4j:slf4j-api:1.7.26")
        force("io.mockk:mockk:1.9.3")
    }
}

if (!sourceSets.main.isPresent || !sourceSets.main.get().resources.srcDirs.iterator().hasNext()) {
    throw GradleException("unable to determine resource directory")
}
val resourceFolder : File = sourceSets.main.get().resources.srcDirs.iterator().next()

swaggerSources {
    create("api").apply {
        setInputFile(file("api.yaml"))
        code(closureOf<GenerateSwaggerCode> {
            language = "java"
            components = listOf("models")
            configFile = file("${resourceFolder}/api-config.json")
        })
    }
}

sourceSets {
    main {
        java {
            swaggerSources.asMap.values.forEach { value -> srcDir("${value.code.outputDir}/src/main/java") }
        }
        resources {
            swaggerSources.asMap.values.forEach { value -> srcDir("${value.code.outputDir}/src/main/resources") }
        }
    }
}