plugins {
    id("jtodo.kotlin-application-conventions")
}

dependencies {
    implementation("org.slf4j:slf4j-simple:2.0.10")
    implementation("io.javalin:javalin")
    implementation("org.apache.commons:commons-text")
    implementation(project(":domain"))
    implementation(project(":oss"))
}

application {
    // Define the main class for the application.
    mainClass.set("todo.app.AppKt")
}
