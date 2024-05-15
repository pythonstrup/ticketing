import org.springframework.boot.gradle.tasks.bundling.BootJar

plugins {
    id("java")
    id("org.springframework.boot") version "3.2.5"
    id("io.spring.dependency-management") version "1.1.5"
    id("com.diffplug.spotless") version "6.25.0"
    id("jacoco")
}

java {
    sourceCompatibility = JavaVersion.VERSION_21
}

allprojects {
    group = "com.ptu"
    version = "1.0"

    repositories {
        mavenCentral()
    }
}

subprojects{
    val querydslDir = "**/src/main/generated/**"

    apply(plugin = "java")
    apply(plugin = "org.springframework.boot")
    apply(plugin = "io.spring.dependency-management")
    apply(plugin = "com.diffplug.spotless")

    spotless {
        java {
            targetExclude(querydslDir)
            importOrder()
            removeUnusedImports()
            googleJavaFormat()
            trimTrailingWhitespace()
            endWithNewline()
        }
    }

    dependencies {
        testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.1")
        testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.1")

        implementation("org.springframework.boot:spring-boot-starter-log4j2")
        implementation("org.bgee.log4jdbc-log4j2:log4jdbc-log4j2-jdbc4.1:1.16")

        compileOnly("org.projectlombok:lombok")
        annotationProcessor("org.projectlombok:lombok")
    }

    tasks.test {
        useJUnitPlatform()
    }

    configurations {
        compileOnly {
            extendsFrom(configurations.annotationProcessor.get())
        }
    }
}

project(":api-shop") {
    dependencies {
        implementation(project(":module-core"))
    }
}

project(":module-core") {

    val jar: Jar by tasks
    val bootJar: BootJar by tasks

    bootJar.enabled = false
    jar.enabled = true
}