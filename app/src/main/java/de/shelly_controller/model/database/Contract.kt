package de.shelly_controller.model.database

import android.provider.BaseColumns

object Contract {
    object ShellyContract : BaseColumns {
        const val TABLE_NAME = "Shelly"
        const val COLUMN_NAME = "name"
        const val COLUMN_IP = "ip"
    }

    object ColorMixContract : BaseColumns {
        const val TABLE_NAME = "ColorMix"
        const val COLUMN_HASH = "hash"
        const val COLUMN_RED = "red"
        const val COLUMN_GREEN = "green"
        const val COLUMN_BLUE = "blue"
        const val COLUMN_WHITE = "white"
        const val COLUMN_GAIN = "gain"
    }

    object ShellyActionContract : BaseColumns {
        const val TABLE_NAME = "ShellyAction"
        const val COLUMN_IS_ENABLED = "isEnabled"
        const val COLUMN_IS_LIGHT = "isLight"
        const val COLUMN_COLOR_ID = "colorId"
    }

    object ShellyActionShellyContract : BaseColumns {
        const val TABLE_NAME = "ShellyActionShelly"
        const val COLUMN_SHELLY_ACTION_ID = "shellyActionId"
        const val COLUMN_SHELLY_ID = "shellyId"
    }
}