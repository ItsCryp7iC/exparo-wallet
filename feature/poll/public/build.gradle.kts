plugins {
  id("exparo.feature")
}

android {
  namespace = "com.exparo.poll"
}

dependencies {
  implementation(projects.shared.domain)
}