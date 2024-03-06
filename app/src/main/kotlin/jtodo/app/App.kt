/*
 * This Kotlin source file was generated by the Gradle 'init' task.
 */
package jtodo.app

import jtodo.utilities.StringUtils

import io.javalin.Javalin
import org.apache.commons.text.WordUtils

fun main() {
    val tokens = StringUtils.split(MessageUtils.getMessage())
    val result = StringUtils.join(tokens)
    Javalin.create()
        .get("/") { ctx -> ctx.result(WordUtils.capitalize(result)) }
        .start(8080)
}
