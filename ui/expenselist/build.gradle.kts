android {
    namespace = "com.kappzzang.jeongsan.expenselist"
}

dependencies {
    implementation("androidx.navigation:navigation-fragment-ktx:2.8.1")
    implementation("androidx.navigation:navigation-ui-ktx:2.8.1")
    implementation("nl.dionsegijn:konfetti-xml:2.0.4")
    implementation(project(":domain:group"))
    implementation(project(":domain:expense"))
    implementation(project(":domain:ocr"))
    implementation(project(":ui:data"))
    implementation(project(":ui:addexpense"))
    implementation(project(":ui:camera"))
}
