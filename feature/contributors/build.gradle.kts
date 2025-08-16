plugins {
    id("exparo.feature")
}

android {
    namespace = "com.exparo.contributors"
}

dependencies {
    implementation(projects.shared.base)
    implementation(projects.shared.domain)
    implementation(projects.shared.ui.core)
    implementation(projects.shared.ui.navigation)

    implementation(libs.bundles.ktor)

    testImplementation(projects.shared.ui.testing)
}
