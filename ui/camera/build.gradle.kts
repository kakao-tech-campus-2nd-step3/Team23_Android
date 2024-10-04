android {
    namespace = "com.kappzzang.jeongsan.camera"
}

dependencies {
    implementation(project(":domain:ocr"))
    implementation("androidx.camera:camera-core:1.3.4")
    implementation("com.github.bumptech.glide:glide:4.14.2")
    implementation("androidx.camera:camera-lifecycle:1.3.4")
    implementation("androidx.camera:camera-view:1.3.4")
    implementation(project(":ui:data"))
}
