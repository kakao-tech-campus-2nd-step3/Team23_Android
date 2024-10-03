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

include(":app")
include(":data:group")
include(":data:expense")
include(":data:ocr")
include(":data:user")
include(":domain:group")
include(":domain:expense")
include(":domain:user")
include(":domain:ocr")
include(":ui:creategroup")
include(":ui:addexpense")
include(":ui:camera")
include(":ui:expensedetail")
include(":ui:expenselist")
include(":ui:login")
include(":ui:main")
include(":common:util")
include(":common:androidutil")
include(":common:resource")
include(":ui:data")

project(":data").children.forEach { module -> module.name = "data-${module.name}"}
