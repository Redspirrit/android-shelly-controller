package de.shelly_controller

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import de.shelly_controller.model.ShellyRepository
import de.shelly_controller.model.database.DatabaseHelper
import de.shelly_controller.model.database.ShellyDao

@Module
@InstallIn(SingletonComponent::class)
class AppModule {
    @Provides
    fun providerDatabaseHelper(@ApplicationContext context: Context): DatabaseHelper {
        return DatabaseHelper(context)
    }

    @Provides
    fun provideShellyDao(): ShellyDao {
        return ShellyDao()
    }

    @Provides
    fun provideShellyRepository(dbHelper: DatabaseHelper, shellyDao: ShellyDao): ShellyRepository {
        return ShellyRepository(dbHelper, shellyDao)
    }
}