import org.springframework.boot.gradle.tasks.bundling.BootJar

plugins {
    id("java")
    id("org.springframework.boot") version "3.2.5"
    id("io.spring.dependency-management") version "1.1.5"
    id("com.diffplug.spotless") version "6.25.0"
    id("jacoco")
}

val querydslDir = "**/src/main/generated/**"
val bootJar: BootJar by tasks
bootJar.enabled = false


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
    }
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}