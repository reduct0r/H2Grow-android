pluginManagement {
    repositories {
        maven {
            url = uri("http://192.168.1.6:9081/repository/maven-public/")
            isAllowInsecureProtocol = true
        }

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
    plugins {
        kotlin("jvm") version "2.2.21"
    }
}
plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)

    repositories {
        maven {
            url = uri("http://192.168.1.6:9081/repository/maven-public/")
            isAllowInsecureProtocol = true
        }

        google()
        mavenCentral()
    }
}

rootProject.name = "H2Grow"
include(":app")