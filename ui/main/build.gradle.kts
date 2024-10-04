android {
    namespace = "com.kappzzang.jeongsan.main"
}

dependencies {
    implementation(project(":ui:expenselist"))
    implementation(project(":ui:creategroup"))
    implementation(project(":domain:group"))
    implementation(project(":domain:user"))
    implementation(project(":ui:data"))
}
