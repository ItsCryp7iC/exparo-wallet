plugins {
    id("exparo.feature")
}

android {
    namespace = "com.exparo.design"
}

dependencies {
    implementation(projects.shared.base)
    implementation(projects.shared.ui.core)

    implementation(projects.shared.domain)
}