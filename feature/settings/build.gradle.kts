plugins {
    id("exparo.feature")
}

android {
    namespace = "com.exparo.settings"
}

dependencies {
    implementation(projects.shared.base)
    implementation(projects.shared.data.core)
    implementation(projects.shared.domain)
    implementation(projects.shared.ui.core)
    implementation(projects.shared.ui.navigation)
    implementation(projects.temp.legacyCode)
    implementation(projects.temp.oldDesign)
    implementation(projects.widget.balance)
    implementation(projects.feature.driveBackup)

    testImplementation(projects.shared.ui.testing)
}
