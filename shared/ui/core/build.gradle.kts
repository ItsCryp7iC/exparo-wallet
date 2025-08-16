plugins {
    id("exparo.feature")
}

android {
    namespace = "com.exparo.ui"
}

dependencies {
    implementation(projects.shared.base)
    implementation(projects.shared.domain)
}