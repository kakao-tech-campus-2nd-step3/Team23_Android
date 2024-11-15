android {
    namespace = "com.kappzzang.jeongsan.main"
}

dependencies {
    implementation(project(":domain:group"))
    implementation(project(":domain:common-user"))
    implementation(project(":ui:data"))
    implementation(project(":common:dispatcher"))
    implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")
}
