android {
    namespace = "com.kappzzang.jeongsan.expense"
}
dependencies {
    implementation(project(":domain:expense"))
    implementation("androidx.room:room-common:2.6.1")
}
