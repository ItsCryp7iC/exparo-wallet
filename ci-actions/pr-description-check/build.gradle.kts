plugins {
    id("exparo.script")
    application
}

application {
    mainClass = "exparo.automate.pr.MainKt"
}

dependencies {
    implementation(projects.ciActions.base)
}
