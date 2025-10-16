plugins {
    java
    id("org.springframework.boot") version "3.5.4"
    id("io.spring.dependency-management") version "1.1.7"
}

group = "alik"
version = "0.0.1-SNAPSHOT"
description = "receipt-maker"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

repositories {
    mavenCentral()
}

extra["springAiVersion"] = "1.0.1"

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    compileOnly("org.projectlombok:lombok:1.18.38")
    annotationProcessor("org.projectlombok:lombok:1.18.38")
    implementation("org.springframework.ai:spring-ai-starter-model-openai")

    implementation("com.fasterxml.jackson.core:jackson-databind:2.19.2")

    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.postgresql:postgresql:42.7.4")
    implementation("org.liquibase:liquibase-core")

    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("com.nimbusds:nimbus-jose-jwt:10.0.1")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    implementation("org.springframework.security:spring-security-oauth2-client")

    implementation("org.springframework.boot:spring-boot-starter-mail")

    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.8.12")

    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310")

    implementation("com.google.api-client:google-api-client:2.7.2")
    implementation("com.google.api-client:google-api-client-jackson2:1.20.0")
    implementation("com.google.oauth-client:google-oauth-client-jetty:1.39.0")
    implementation("com.google.apis:google-api-services-gmail:v1-rev20230925-2.0.0")

}

dependencyManagement {
    imports {
        mavenBom("org.springframework.ai:spring-ai-bom:${property("springAiVersion")}")
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}
