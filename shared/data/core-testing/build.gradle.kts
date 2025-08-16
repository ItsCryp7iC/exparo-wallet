plugins {
    id("exparo.feature")
    id("exparo.room")
}

android {
    namespace = "com.exparo.data.testing"
}

dependencies {
    implementation(projects.shared.data.core)
}
