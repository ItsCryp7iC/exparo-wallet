plugins {
    id("exparo.feature")
    id("exparo.room")
    id("exparo.integration.testing")
}

android {
    namespace = "com.exparo.data"
}

dependencies {
    implementation(projects.shared.base)
    api(projects.shared.data.model)

    api(libs.datastore)
    implementation(libs.bundles.ktor)

    testImplementation(projects.shared.data.modelTesting)
    androidTestImplementation(libs.bundles.integration.testing)
}
