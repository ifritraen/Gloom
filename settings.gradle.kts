pluginManagement {
    includeBuild("build-logic")
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
}

rootProject.name = "Gloom"

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

include(
    ":app",

    // Core
    ":core:data",
    ":core:graphql",

    // Tooling
    ":lint:rules"
)

include(":api")
include(":shared")
include(":ui")