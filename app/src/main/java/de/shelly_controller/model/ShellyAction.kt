package de.shelly_controller.model

data class ShellyAction(
    val shelly: MutableList<Shelly>,
    val isEnabled: Boolean,
    val isLight: Boolean,
    val color: ColorMix
)
