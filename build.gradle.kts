import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    val kotlinVersion = "1.5.21"
    val palantirDockerVersion = "0.27.0"
    val springBootVersion = "2.5.3"
    val dependencyManagementVersion = "1.0.11.RELEASE"

    id("org.springframework.boot") version springBootVersion
    id("io.spring.dependency-management") version dependencyManagementVersion
    kotlin("jvm") version kotlinVersion
    kotlin("plugin.spring") version kotlinVersion
    kotlin("plugin.jpa") version kotlinVersion

    id("com.palantir.docker") version palantirDockerVersion
}

group = "io.uvera"
version = "0.0.2"
java.sourceCompatibility = JavaVersion.VERSION_16

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-cache")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.liquibase:liquibase-core")
    developmentOnly("org.springframework.boot:spring-boot-devtools")
    runtimeOnly("org.postgresql:postgresql")
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.security:spring-security-test")
    // jwt
    implementation("io.jsonwebtoken:jjwt:0.9.1")
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