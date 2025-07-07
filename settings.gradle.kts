pluginManagement {
    includeBuild("build-logic")
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
}

rootProject.name = "Gloom"
include(":app")

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

include(
    ":app",

    // Core
    ":core:graphql",

    // Tooling
    ":lint:rules"
)

include(":api")
include(":shared")
include(":ui")