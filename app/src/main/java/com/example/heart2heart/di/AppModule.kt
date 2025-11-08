package com.example.heart2heart.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.example.heart2heart.auth.data.remote.AuthAPI
import com.example.heart2heart.auth.data.remote.ProfileAPI
import com.example.heart2heart.auth.domain.data.AuthInterceptor
import com.example.heart2heart.auth.repository.AuthRepository
import com.example.heart2heart.bluetooth.BluetoothController
import com.example.heart2heart.bluetooth.BluetoothServiceECG
import com.example.heart2heart.common.data.database.AppDB
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
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

    @Singleton
    @Provides
    fun provideAuthInterceptor(authRepository: AuthRepository): AuthInterceptor {
        return AuthInterceptor(authRepository)
    }

    @Provides
    @Singleton
    fun provideAuthAPI(): AuthAPI {
        return Retrofit.Builder()
            .baseUrl(AuthAPI.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(AuthAPI::class.java)
    }

    @Singleton
    @Provides
    fun provideOkHttpClient(authInterceptor: AuthInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
            // Add your AuthInterceptor
            .addInterceptor(authInterceptor)
            // You can add other interceptors here (like Logging)
            .build()
    }

    @Provides
    @Singleton
    fun provideProfileAPI(okHttpClient: OkHttpClient): ProfileAPI {
        return Retrofit.Builder()
            .baseUrl(ProfileAPI.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ProfileAPI::class.java)
    }

}