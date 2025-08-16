plugins {
    id("exparo.feature")
}

android {
    namespace = "com.exparo.data.model.testing"
}

dependencies {
    implementation(projects.shared.data.model)

    implementation(libs.bundles.testing)
}