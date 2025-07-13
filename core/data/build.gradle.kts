plugins {
    alias(libs.plugins.gloom.library)
}

android {
    namespace = "dev.materii.gloom.core.data"
}

dependencies {
    api(projects.core.graphql)
    implementation(libs.koin.core)
}