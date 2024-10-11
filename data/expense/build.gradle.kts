android {
    namespace = "com.kappzzang.jeongsan.expense"
}
dependencies {
    implementation(project(":domain:expense"))
    implementation("androidx.room:room-ktx:2.6.1")
    implementation("com.kakao.sdk:v2-talk:2.20.6")
    testImplementation("androidx.room:room-testing:2.6.1")
}
