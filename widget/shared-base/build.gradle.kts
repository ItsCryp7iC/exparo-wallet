plugins {
    id("exparo.widget")
}

android {
    namespace = "com.exparo.widget"
}

dependencies {
    implementation(projects.shared.base)
    implementation(projects.shared.domain)
}
