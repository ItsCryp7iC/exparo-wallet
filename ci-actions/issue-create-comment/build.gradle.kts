plugins {
    id("exparo.script")
    application
}

application {
    mainClass = "exparo.automate.issue.create.MainKt"
}

dependencies {
    implementation(projects.ciActions.base)
}
