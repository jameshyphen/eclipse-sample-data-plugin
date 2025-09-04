plugins {
    id("java")

    id("org.springframework.boot") version "3.4.5"
}

group = "com.hyphen"
version = "1.0.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":domain"))
    implementation("org.springframework.boot:spring-boot-starter-web:3.4.5")

    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")

    testImplementation("org.springframework.boot:spring-boot-starter-test:3.4.5")

}

tasks.test {
    useJUnitPlatform()
}