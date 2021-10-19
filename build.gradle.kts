import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    val kotlinVersion = "1.5.31"
    val palantirDockerVersion = "0.30.0"
    val springBootVersion = "2.5.5"
    val dependencyManagementVersion = "1.0.11.RELEASE"

    id("org.springframework.boot") version springBootVersion
    id("io.spring.dependency-management") version dependencyManagementVersion
    kotlin("jvm") version kotlinVersion
    kotlin("plugin.spring") version kotlinVersion

    id("com.palantir.docker") version palantirDockerVersion
}

group = "io.uvera"
version = "0.1.0"
java.sourceCompatibility = JavaVersion.VERSION_17

configurations {
    all {
        exclude(group = "org.apache.tomcat")
        exclude(module = "spring-boot-starter-tomcat")
    }
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    mavenCentral()
}

dependencies {
    val jdbiVersion = "3.23.0"

    implementation("org.springframework.boot:spring-boot-starter-data-jdbc")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-undertow")
    implementation("org.springframework.boot:spring-boot-starter-cache")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.liquibase:liquibase-core")
    implementation("org.jdbi:jdbi3-core:${jdbiVersion}")
    implementation("org.jdbi:jdbi3-postgres:${jdbiVersion}")
    implementation("org.jdbi:jdbi3-sqlobject:${jdbiVersion}")
    implementation("org.jdbi:jdbi3-kotlin:${jdbiVersion}")
    implementation("org.jdbi:jdbi3-kotlin-sqlobject:${jdbiVersion}")
    implementation("org.jdbi:jdbi3-stringtemplate4:${jdbiVersion}")
    developmentOnly("org.springframework.boot:spring-boot-devtools")
    runtimeOnly("org.postgresql:postgresql")
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.security:spring-security-test")
    // region jwt
    implementation("io.jsonwebtoken:jjwt-api:0.11.2")
    runtimeOnly("io.jsonwebtoken:jjwt-impl:0.11.2")
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.11.2")
    // endregion jwt
    // spring doc
    implementation("org.springdoc:springdoc-openapi-ui:1.5.+")
    implementation("org.springdoc:springdoc-openapi-data-rest:1.5.+")
    implementation("org.springdoc:springdoc-openapi-kotlin:1.5.+")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "16"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

//region DockerSetup

val bootJarTask = tasks.bootJar.get()
val archivePath = bootJarTask.archiveFileName.get()
val dockerFilePath = "${projectDir.path}/docker/Dockerfile"
val appName = project.name.toLowerCase()
val projectName = "${project.group}/${appName}"
val fullName = "$projectName:${project.version}"
val dockerBuildArgs = mapOf(
    "JAR_FILE" to archivePath
)

// workaround from https://github.com/palantir/gradle-docker/issues/413
tasks.docker {
    inputs.file(dockerFilePath)
}

docker {
    name = fullName
    tag("latest", "$projectName:latest")
    pull(true)
    setDockerfile(file(dockerFilePath))
    files(bootJarTask.outputs)
    buildArgs(dockerBuildArgs)
}

//endregion DockerSetup
