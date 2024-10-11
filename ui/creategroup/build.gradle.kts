android {
    namespace = "com.kappzzang.jeongsan.creategroup"
}

dependencies {
    implementation(project(":ui:data"))
    implementation(project(":domain:group"))
    implementation("com.kakao.sdk:v2-friend:2.20.6") // 피커 API 모듈
}
