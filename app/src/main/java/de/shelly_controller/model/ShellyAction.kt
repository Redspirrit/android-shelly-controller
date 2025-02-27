package de.shelly_controller.model

import java.security.MessageDigest

data class ShellyAction(
    val id: Int,
    val shelly: MutableList<Shelly>,
    val isEnabled: Boolean,
    val isLight: Boolean, // defines if action turns light on or off
    val color: ColorMix
)
