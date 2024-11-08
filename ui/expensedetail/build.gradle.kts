android {
    namespace = "com.kappzzang.jeongsan.expensedetail"
}

dependencies {
    implementation("com.github.bumptech.glide:glide:4.14.2")
    implementation(project(":ui:data"))
    annotationProcessor("com.github.bumptech.glide:compiler:4.14.2")
    implementation(project(":domain:expense"))
    implementation(project(":common:dispatcher"))
}
