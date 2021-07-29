import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "2.5.2"
    id("io.spring.dependency-management") version "1.0.11.RELEASE"
    kotlin("jvm") version "1.5.20"
    kotlin("plugin.spring") version "1.5.20"
    kotlin("plugin.jpa") version "1.5.20"

    id("com.palantir.docker") version "0.27.0"
}

group = "io.uvera"
version = "0.0.1"
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
val appName = project.name
val fullName = "${project.group}/${appName}:${project.version}"
val dockerBuildArgs = mapOf(
    "JAR_FILE" to archivePath
)

fun printInfo(string: String) = println("[Docker plugin]: $string")

// workaround from https://github.com/palantir/gradle-docker/issues/413
tasks.docker {
    inputs.file(dockerFilePath)
}

docker {
    printInfo("with app name: $appName")
    printInfo("with full name: $fullName")
    name = fullName
    tag("latest", "latest")
    pull(true)
    printInfo("with Dockerfile: $dockerFilePath")
    setDockerfile(file(dockerFilePath))
    files(bootJarTask.outputs)
    printInfo("with archive: $archivePath")
    buildArgs(dockerBuildArgs)
}

//endregion DockerSetup