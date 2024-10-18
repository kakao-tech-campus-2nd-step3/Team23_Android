plugins {
    id("java-library")
    id("org.jetbrains.kotlin.jvm")
}

dependencies {
    // Test Dependencies
    testImplementation("org.assertj:assertj-core:3.25.3")
    testImplementation("junit:junit:4.13.2")
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}
