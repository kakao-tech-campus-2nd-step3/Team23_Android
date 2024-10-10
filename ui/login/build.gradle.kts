android {
    namespace = "com.kappzzang.jeongsan.login"
}

dependencies {
    implementation(project(":domain:user"))
    implementation("com.kakao.sdk:v2-user:2.20.6")
}
