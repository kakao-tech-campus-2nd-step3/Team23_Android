android {
    namespace = "com.kappzzang.jeongsan.expensedetail"
}

dependencies {
    implementation("androidx.hilt:hilt-navigation-fragment:1.1.0")
    implementation("androidx.navigation:navigation-fragment-ktx:2.8.1")
    implementation("com.github.bumptech.glide:glide:4.14.2")
    implementation(project(":ui:data"))
    annotationProcessor("com.github.bumptech.glide:compiler:4.14.2")
    implementation(project(":domain:expense"))
    implementation(project(":common:dispatcher"))
}
