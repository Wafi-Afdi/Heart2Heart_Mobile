package com.example.heart2heart.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.example.heart2heart.bluetooth.BluetoothController
import com.example.heart2heart.bluetooth.BluetoothServiceECG
import com.example.heart2heart.common.data.database.AppDB
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideHeart2HeartDB(app: Application): AppDB {
        return Room.databaseBuilder(
            app,
            AppDB::class.java,
            "heart2heart_db"
        )
            .build()
    }

    @Provides
    @Singleton
    fun provideBluetoothController(@ApplicationContext context: Context): BluetoothServiceECG {
        return BluetoothServiceECG(context)
    }

}