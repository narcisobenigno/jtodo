/*
 * This Kotlin source file was generated by the Gradle 'init' task.
 */
package todo.app

import io.javalin.Javalin
import org.apache.commons.text.WordUtils

fun main() {
    Javalin.create()
        .get("/") { ctx -> ctx.result(WordUtils.capitalize("hello!")) }
        .start(8080)
}
