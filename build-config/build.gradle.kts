import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("org.jlleitschuh.gradle.ktlint")
}

fun getApiKey(key: String): String = gradleLocalProperties(rootDir, providers).getProperty(key)

android {
    compileSdk = 34
    namespace = "com.kappzzang.jeongsan.build_config"

    buildTypes {
        debug {
            buildConfigField("String", "KAKAO_REST_API_KEY", getApiKey("KAKAO_REST_API_KEY"))
            buildConfigField("String", "KAKAO_API_KEY", getApiKey("KAKAO_API_KEY"))
            buildConfigField("String", "KEYSTORE_NAME", getApiKey("KEYSTORE_NAME"))
            resValue("string", "KAKAO_API_KEY", getApiKey("KAKAO_API_KEY"))
            resValue("string", "KAKAO_API_KEY_MANIFEST", getApiKey("KAKAO_API_KEY_MANIFEST"))

            buildConfigField("String", "KAKAO_API_URL", getApiKey("KAKAO_API_URL"))
            buildConfigField("String", "SERVICE_URL", getApiKey("SERVICE_URL"))
            buildConfigField("String", "KAKAO_AUTH_URL", getApiKey("KAKAO_AUTH_URL"))
        }

        release {
            buildConfigField("String", "KAKAO_REST_API_KEY", getApiKey("KAKAO_REST_API_KEY"))
            buildConfigField("String", "KAKAO_API_KEY", getApiKey("KAKAO_API_KEY"))
            buildConfigField("String", "KEYSTORE_NAME", getApiKey("KEYSTORE_NAME"))
            resValue("string", "KAKAO_API_KEY", getApiKey("KAKAO_API_KEY"))
            resValue("string", "KAKAO_API_KEY_MANIFEST", getApiKey("KAKAO_API_KEY_MANIFEST"))

            buildConfigField("String", "KAKAO_API_URL", getApiKey("KAKAO_API_URL"))
            buildConfigField("String", "SERVICE_URL", getApiKey("SERVICE_URL"))
            buildConfigField("String", "KAKAO_AUTH_URL", getApiKey("KAKAO_AUTH_URL"))
        }
    }
    buildFeatures {
        buildConfig = true
    }
}
