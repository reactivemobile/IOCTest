plugins {
    java
    kotlin("jvm")
    kotlin("kapt")
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))

    implementation(project(":annotations"))

    implementation( "com.squareup:kotlinpoet:0.7.0")
    implementation("com.google.auto.service:auto-service:1.0-rc2")
    kapt("com.google.auto.service:auto-service:1.0-rc2")
}
