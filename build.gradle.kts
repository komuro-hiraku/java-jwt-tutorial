plugins {
    kotlin("jvm") version "1.6.10"
}

group = "dev.com4dc"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))

    // java-jwt
    implementation("com.auth0:java-jwt:3.18.3")

    testImplementation("org.junit.jupiter:junit-jupiter:5.8.2")
}

tasks.test {
    useJUnitPlatform()
}
