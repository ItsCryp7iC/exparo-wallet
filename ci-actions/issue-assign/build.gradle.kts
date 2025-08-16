plugins {
    id("exparo.script")
    application
}

application {
    mainClass = "exparo.automate.issue.MainKt"
}

dependencies {
    implementation(projects.ciActions.base)
}
