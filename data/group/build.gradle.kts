android {
    namespace = "com.kappzzang.jeongsan.group"
}

dependencies {
    implementation(project(":data"))
    implementation(project(":domain:group"))
    implementation(project(":domain:expense"))
    implementation(project(":data:data-expense"))
}