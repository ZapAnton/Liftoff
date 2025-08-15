plugins {
    application
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.eclipse.angus:angus-mail:2.0.3")
    implementation("com.azure:azure-identity:1.17.0")
    implementation("com.microsoft.graph:microsoft-graph:6.49.0")
    testImplementation(libs.junit.jupiter)
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

application {
    mainClass = "com.example.liftoff.App"
}

tasks.named<Test>("test") {
    useJUnitPlatform()
}
