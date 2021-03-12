import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.4.31"
    kotlin("plugin.serialization") version "1.4.31"
}

group = "me.OsinniyApps"
version = "1.0"

repositories {
    mavenCentral()
    maven("https://jitpack.io")
    maven("https://kotlin.bintray.com/kotlinx/")
}

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core-jvm:1.4.2")
    implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.1.1")

    implementation("io.github.kotlin-telegram-bot.kotlin-telegram-bot:telegram:6.0.4")

    implementation("io.ktor:ktor-server-core:1.5.1")
    implementation("io.ktor:ktor-server-netty:1.5.1")

    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.1.0")
    implementation("com.jakewharton.retrofit:retrofit2-kotlinx-serialization-converter:0.8.0")

    implementation("org.xerial:sqlite-jdbc:3.34.0")

    implementation("com.google.auth:google-auth-library-oauth2-http:0.24.1")

    testImplementation(kotlin("test-junit5"))
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.6.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.6.0")
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.apply {
        jvmTarget = "1.8"
        languageVersion = "1.4"
        useIR = true
    }
}
