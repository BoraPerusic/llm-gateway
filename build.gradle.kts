import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "3.2.6"
    id("io.spring.dependency-management") version "1.1.4"
    kotlin("jvm") version "1.9.23"
    kotlin("plugin.spring") version "1.9.23"
    kotlin("plugin.serialization") version "1.9.23"
    id("com.github.node-gradle.node") version "7.0.2"
    id("com.google.protobuf") version "0.9.4"
}

group = "org.tatrman.llmgateway"
version = "0.0.1-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_21
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    mavenCentral()
    maven { url = uri("https://repo.spring.io/milestone") }
    maven { url = uri("https://repo.spring.io/snapshot") }
}

extra["springAiVersion"] = "1.0.0-M1"

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-data-jdbc")
    implementation("org.springframework.boot:spring-boot-starter-oauth2-resource-server")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.ai:spring-ai-openai-spring-boot-starter")
    
    // Kotlin
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.3")

    // DB
    implementation("org.flywaydb:flyway-core")
    runtimeOnly("org.postgresql:postgresql")
    runtimeOnly("com.microsoft.sqlserver:mssql-jdbc")
    
    // Messaging
    implementation("io.nats:nats-spring-boot-starter:0.5.7") // Check version
    
    // gRPC
    implementation("net.devh:grpc-spring-boot-starter:3.1.0.RELEASE") 
    implementation("io.grpc:grpc-stub:1.63.0")
    implementation("io.grpc:grpc-protobuf:1.63.0")
    implementation("io.grpc:grpc-kotlin-stub:1.4.1")
    implementation("com.google.protobuf:protobuf-java:3.25.1")
    if (JavaVersion.current().isJava9Compatible) {
        implementation("javax.annotation:javax.annotation-api:1.3.2")
    }

    // Config
    implementation("com.typesafe:config:1.4.3")
    
    // Dev calls
    developmentOnly("org.springframework.boot:spring-boot-docker-compose")
    
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.security:spring-security-test")
    testImplementation("io.kotest:kotest-runner-junit5:5.8.1")
    testImplementation("io.kotest:kotest-assertions-core:5.8.1")
    testImplementation("io.kotest:kotest-property:5.8.1")
}

dependencyManagement {
    imports {
        mavenBom("org.springframework.ai:spring-ai-bom:${property("springAiVersion")}")
    }
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs += "-Xjsr305=strict"
        jvmTarget = "21"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

node {
    version.set("20.19.0")
    download.set(true)
    nodeProjectDir.set(file("src/main/frontend"))
}

val cleanFrontend by tasks.registering(Delete::class) {
    delete("src/main/resources/static")
    delete("src/main/frontend/dist")
}

val npmBuild by tasks.registering(com.github.gradle.node.npm.task.NpmTask::class) {
    dependsOn(tasks.npmInstall)
    args.set(listOf("run", "build"))
    inputs.dir("src/main/frontend/src")
    inputs.file("src/main/frontend/package.json")
    inputs.file("src/main/frontend/tsconfig.json")
    outputs.dir("src/main/frontend/dist")
}

val copyFrontend by tasks.registering(Copy::class) {
    dependsOn(npmBuild)
    from("src/main/frontend/dist")
    into("src/main/resources/static")
}

tasks.processResources {
    dependsOn(copyFrontend)
}

tasks.clean {
    dependsOn(cleanFrontend)
}

protobuf {
    protoc {
        artifact = "com.google.protobuf:protoc:3.25.1"
    }
    plugins {
        create("grpc") {
            artifact = "io.grpc:protoc-gen-grpc-java:1.63.0"
        }
        create("grpckt") {
            artifact = "io.grpc:protoc-gen-grpc-kotlin:1.4.1:jdk8@jar"
        }
    }
    generateProtoTasks {
        all().forEach {
            it.plugins {
                create("grpc")
                create("grpckt")
            }
        }
    }
}
