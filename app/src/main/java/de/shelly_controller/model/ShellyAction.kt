package de.shelly_controller.model

import java.security.MessageDigest

data class ShellyAction(
    val id: Int,
    val shelly: MutableList<Shelly>,
    val isEnabled: Boolean,
    val isLight: Boolean, // defines if action turns light on or off
    val color: ColorMix
) {
    val hash: String = generateHash()

    private fun generateHash(): String {
        val input = "${isEnabled}_${isLight}_${color}"
        return input.toSHA256()
    }

    private fun String.toSHA256(): String {
        val bytes = MessageDigest.getInstance("SHA-256").digest(this.toByteArray())
        return bytes.joinToString("") { "%02x".format(it) }
    }
}
