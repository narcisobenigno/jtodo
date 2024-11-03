plugins {
    id("jtodo.kotlin-application-conventions")
}

dependencies {
    implementation("org.slf4j:slf4j-simple:2.0.10")
    implementation("io.javalin:javalin:6.1.3")
    implementation("org.apache.commons:commons-text")
    implementation(project(":domain"))
}

application {
    // Define the main class for the application.
    mainClass.set("jtodo.app.AppKt")
}
