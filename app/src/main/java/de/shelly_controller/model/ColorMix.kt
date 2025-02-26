package de.shelly_controller.model

import java.security.MessageDigest

data class ColorMix(
    val id: Int,
    val red: Int,
    val green: Int,
    val blue: Int,
    val white: Int,
    val gain: Int
) {
    val hash: String = generateHash()

    private fun generateHash(): String {
        val input = "${red}_${green}_${blue}_${white}_${gain}"
        return input.toSHA256()
    }

    private fun String.toSHA256(): String {
        val bytes = MessageDigest.getInstance("SHA-256").digest(this.toByteArray())
        return bytes.joinToString("") { "%02x".format(it) }
    }
}
