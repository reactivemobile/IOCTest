plugins {
    java
    kotlin("jvm")
    kotlin("kapt")
}

group = "org.example"
version = "1.0-SNAPSHOT"

sourceSets {
    main {
        java {
            srcDir("${buildDir.absolutePath}/generated/source/kaptKotlin/")
        }
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
    compileOnly(project(":annotations"))
    kapt(project(":processor"))
    testCompile("junit", "junit", "4.12")
}
