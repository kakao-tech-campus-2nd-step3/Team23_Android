pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "jeongsan"
include(":app")
include(":data:group")
include(":data:expense")
include(":data:ocr")
include(":data:user")
include(":domain:group")
include(":domain:expense")
include(":domain:user")
include(":domain:ocr")
include(":ui:user")
include(":ui:expense")
include(":ui:group")
include(":ui:ocr")
