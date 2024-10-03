android {
    namespace = "com.kappzzang.jeongsan.expenselist"
}

dependencies {
    implementation("androidx.navigation:navigation-fragment-ktx:2.8.1")
    implementation("androidx.navigation:navigation-ui-ktx:2.8.1")

    implementation(project(":ui:creategroup"))
    implementation(project(":ui:addexpense"))
    implementation(project(":ui:camera"))
    implementation(project(":domain:group"))
    implementation(project(":domain:ocr"))
    implementation(project(":domain:expense"))
}
