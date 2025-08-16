plugins {
    id("exparo.script")
    application
}

application {
    mainClass = "exparo.automate.compose.stability.MainKt"
}

dependencies {
    implementation(projects.ciActions.base)
}
