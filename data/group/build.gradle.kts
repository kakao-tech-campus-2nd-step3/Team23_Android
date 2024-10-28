android {
    namespace = "com.kappzzang.jeongsan.group"
}

dependencies {
    implementation(project(":data"))
    implementation(project(":domain:group"))
    implementation(project(":domain:expense"))
    implementation("com.kakao.sdk:v2-talk:2.20.6")
}
