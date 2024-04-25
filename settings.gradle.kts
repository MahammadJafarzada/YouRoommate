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

rootProject.name = "YouRoommate"
include(":app")
include(":common")
include(":features")
include(":entities")
include(":data")
include(":domain")
include(":network")
include(":features:login")
include(":features:splash")
include(":features:register")
include(":features:account")
include(":features:chat")
include(":features:favourite")
include(":features:homeScreen")
include(":features:addCard")
