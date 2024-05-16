import groovy.lang.Closure
import io.swagger.v3.oas.models.servers.Server
import org.hidetake.gradle.swagger.generator.GenerateSwaggerUI

plugins {
    id("com.epages.restdocs-api-spec") version "0.18.2"
    id("org.hidetake.swagger.generator") version "2.18.2"
}

val queryDslVersion = "5.0.0"
val querydslDir = "src/main/generated"
extra["snippetsDir"] = file("build/generated-snippets")


dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-oauth2-client")
    implementation("org.springframework.boot:spring-boot-starter-validation")

    /**
     * QueryLog
     */
    implementation("p6spy:p6spy:3.9.1")
    implementation("com.github.gavlyukovskiy:p6spy-spring-boot-starter:1.9.1")

    /**
     * JWTs
     */
    implementation("io.jsonwebtoken:jjwt-api:0.11.5")
    runtimeOnly("io.jsonwebtoken:jjwt-impl:0.11.5")
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.11.5")

    /**
     * JPA & Database
     */
    runtimeOnly("com.mysql:mysql-connector-j")
    runtimeOnly("com.h2database:h2")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-data-redis")

    /**
     * QueryDSL
     */
    implementation ("com.querydsl:querydsl-jpa:${queryDslVersion}:jakarta")
    annotationProcessor("com.querydsl:querydsl-apt:${queryDslVersion}:jakarta")
    annotationProcessor("jakarta.annotation:jakarta.annotation-api")
    annotationProcessor("jakarta.persistence:jakarta.persistence-api")
    implementation("jakarta.servlet:jakarta.servlet-api:6.0.0")

    /**
     * Test
     */
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.security:spring-security-test")
    testImplementation("org.springframework.restdocs:spring-restdocs-mockmvc")

    /**
     * SwaggerUI
     */
    testImplementation("com.epages:restdocs-api-spec-mockmvc:0.18.2")
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.2.0")
    swaggerUI("org.webjars:swagger-ui:4.11.1")
}

sourceSets {
    getByName("main").java.srcDirs(querydslDir)
}

tasks.withType<JavaCompile> {
    options.generatedSourceOutputDirectory.set(file(querydslDir))
    dependsOn("spotlessCheck")
}

swaggerSources {
    create("sample") {
        setInputFile(file("${layout.buildDirectory.get()}/resources/main/static/docs/openapi3.yaml"))
    }
}

openapi3 {
    val stagServer: Closure<Server> = closureOf<Server> {
        this.url = "https://dev.ptu.com/"
    } as Closure<Server>
    val localServer: Closure<Server> = closureOf<Server> {
        this.url = "http://localhost:8080"
    } as Closure<Server>
    setServers(listOf(stagServer, localServer))
    title = "Spring Rest Docs + Swagger-UI + Open-API 3"
    description = "Swagger"
    version = "0.0.1"
    format = "yaml"
    outputFileNamePrefix = "openapi3"
    outputDirectory = "${layout.buildDirectory.get()}/resources/main/static/docs"
}

tasks.withType<GenerateSwaggerUI> {
    dependsOn("openapi3")

    doFirst {
        val swaggerUIFile = file("${layout.buildDirectory.get()}/resources/main/static/docs/openapi3.yaml")
    }
}

tasks {
    test {
        useJUnitPlatform()
    }

    register("cleanAll") {
        dependsOn(":module-core:clean", "clean")
    }

    clean {
        doLast {
            file(querydslDir).deleteRecursively()
        }
    }

    build {
        dependsOn("createSwaggerDirectory")
    }

    bootJar {
        dependsOn("createSwaggerDirectory")
        dependsOn("generateSwaggerUISample")

        archiveFileName.set("api-shop.jar")
        enabled = true
        launchScript()

//        delete(file("src/main/resources/static/docs"))
//        copy {
//            from("${layout.buildDirectory.get()}/resources/main/static/docs")
//            into("src/main/resources/static/docs/")
//        }
    }

    register("createSwaggerDirectory") {
        doLast {
            val directory = file("${layout.buildDirectory.get()}/resources/main/static/docs")
            if (!directory.exists()) {
                directory.mkdirs()
            }
        }
    }
}
