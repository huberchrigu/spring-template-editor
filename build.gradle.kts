import kotlin.io.path.Path

val jteVersion = "3.1.16"

plugins {
    kotlin("jvm") version "1.9.25"
    kotlin("plugin.spring") version "1.9.25"
    id("io.spring.dependency-management") version "1.1.7"
    id("gg.jte.gradle") version "3.1.16"
}

group = "tech.chrigu.spring"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    mavenCentral()
}

dependencyManagement {
    imports {
        mavenBom("org.springframework.boot:spring-boot-dependencies:3.5.4")
    }
}

dependencies {
    compileOnly("org.springframework:spring-webmvc")
    compileOnly("org.springframework.security:spring-security-web")
    compileOnly("jakarta.servlet:jakarta.servlet-api")
    compileOnly("org.springframework.boot:spring-boot-autoconfigure")
    compileOnly("org.springframework.boot:spring-boot-test")
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")

    // Custom dependencies
    implementation("org.jsoup:jsoup:1.16.1")
    implementation("com.helger:ph-css:7.0.4")
    implementation("gg.jte:jte-kotlin:$jteVersion")
}

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict")
    }
}

jte {
    generate()
    binaryStaticContent = true
    sourceDirectory.set(Path("src/main/kte"))
}
