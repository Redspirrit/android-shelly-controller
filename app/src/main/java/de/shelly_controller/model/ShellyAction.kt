package de.shelly_controller.model

data class ShellyAction(
    val shelly: MutableList<Shelly>,
    val isLight: Boolean,
    val isEnabled: Boolean,
    val color: ColorMix
)
