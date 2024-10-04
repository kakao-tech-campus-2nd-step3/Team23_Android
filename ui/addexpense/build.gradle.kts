android {
    namespace = "com.kappzzang.jeongsan.addexpense"
}

dependencies {
    implementation(project(":domain:ocr"))
    implementation(project(":ui:expensedetail"))
    implementation(project(":domain:expense"))
    implementation(project(":ui:data"))
}
