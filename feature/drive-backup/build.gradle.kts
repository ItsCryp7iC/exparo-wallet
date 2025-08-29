plugins {
    id("exparo.feature")
}

android {
    namespace = "com.exparo.drivebackup"
}

dependencies {
    implementation(projects.shared.base)
    implementation(projects.shared.data.core)
    implementation(projects.shared.domain)
    implementation(projects.shared.ui.core)
    implementation(projects.shared.ui.navigation)
    implementation(projects.temp.legacyCode)
    implementation(projects.temp.oldDesign)

    // Room
    implementation(libs.bundles.room)
    ksp(libs.room.compiler)
    
    // Compose
    implementation(libs.bundles.compose)
    
    // Hilt Work - CRITICAL: Required for @HiltWorker
    implementation(libs.hilt.work)
    implementation(libs.androidx.work)

    // Google Drive API
    implementation("com.google.android.gms:play-services-auth:21.2.0")
    implementation("com.google.apis:google-api-services-drive:v3-rev20231128-2.0.0")
    implementation("com.google.api-client:google-api-client-android:2.6.0")
    implementation("com.google.http-client:google-http-client-gson:1.45.0")
    implementation("com.google.http-client:google-http-client-android:1.45.0")

    testImplementation(projects.shared.ui.testing)
}