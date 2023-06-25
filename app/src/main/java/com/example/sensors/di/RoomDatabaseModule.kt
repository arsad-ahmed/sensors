package com.example.sensors.di

import android.content.Context
import androidx.room.Room
import com.example.sensors.database.LocationDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class RoomDatabaseModule
{
    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context:Context) =
        Room.databaseBuilder(context, LocationDatabase::class.java, "locationDB").build()


    @Singleton
    @Provides
    fun provideYourDao(db: LocationDatabase) = db.getDao()
}