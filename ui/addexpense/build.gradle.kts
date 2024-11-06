android {
    namespace = "com.kappzzang.jeongsan.addexpense"
}

dependencies {
    implementation("androidx.navigation:navigation-fragment-ktx:2.8.1")
    implementation("androidx.navigation:navigation-ui-ktx:2.8.1")
    implementation("androidx.hilt:hilt-navigation-fragment:1.1.0")

    implementation(project(":domain:ocr"))
    implementation(project(":domain:expense"))
    implementation(project(":ui:data"))
    implementation(project(":common:dispatcher"))
}
