plugins {
    id("exparo.feature")
}

android {
    namespace = "com.exparo.navigation"
}

dependencies {
    implementation(projects.shared.base)
    implementation(projects.shared.domain)
    implementation(projects.shared.ui.core)
}
