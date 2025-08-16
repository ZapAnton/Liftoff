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

    implementation("com.google.api-client:google-api-client:2.0.0")
    implementation("com.google.oauth-client:google-oauth-client-jetty:1.34.1")
    implementation("com.google.apis:google-api-services-drive:v3-rev197-1.25.0")
    implementation("com.google.apis:google-api-services-gmail:v1-rev20220404-2.0.0")

    implementation("com.dropbox.core:dropbox-core-sdk:7.0.0")

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
