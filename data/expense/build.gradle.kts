android {
    namespace = "com.kappzzang.jeongsan.expense"
}
dependencies {
    implementation(project(":domain:expense"))
    implementation("androidx.room:room-ktx:2.6.1")
    testImplementation("androidx.room:room-testing:2.6.1")
}
