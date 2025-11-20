package com.example.heart2heart.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.example.heart2heart.ECGExtraction.data.ECGExtractionService
import com.example.heart2heart.ECGExtraction.data.remote.BpmDataAPI
import com.example.heart2heart.ECGExtraction.data.remote.ECGExtractionAPI
import com.example.heart2heart.ECGExtraction.data.repository.BpmRepositoryImpl
import com.example.heart2heart.ECGExtraction.data.repository.ECGRepositoryImpl
import com.example.heart2heart.ECGExtraction.model.ECGDataProcessingService
import com.example.heart2heart.ECGExtraction.repository.ECGRepository
import com.example.heart2heart.EmergencyBroadcast.data.remote.ReportAPI
import com.example.heart2heart.EmergencyBroadcast.data.repository.ReportRepository
import com.example.heart2heart.EmergencyBroadcast.domain.EmergencyBroadcastService
import com.example.heart2heart.auth.data.remote.AuthAPI
import com.example.heart2heart.auth.data.remote.ProfileAPI
import com.example.heart2heart.auth.domain.data.AuthInterceptor
import com.example.heart2heart.auth.repository.AuthRepository
import com.example.heart2heart.auth.repository.ProfileRepository
import com.example.heart2heart.bluetooth.BluetoothController
import com.example.heart2heart.bluetooth.BluetoothServiceECG
import com.example.heart2heart.common.data.database.AppDB
import com.example.heart2heart.contacts.data.remote.ContactAPI
import com.example.heart2heart.contacts.data.repository.ContactRepository
import com.example.heart2heart.home.domain.LiveLocationService
import com.example.heart2heart.report.data.repository.StatisticRepository
import com.example.heart2heart.websocket.data.repository.WebsocketImpl
import com.example.heart2heart.websocket.repository.WebSocketRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
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

    @Singleton
    @Provides
    fun provideLocationService(@ApplicationContext context: Context, profileRepository: ProfileRepository,webSocketRepository: WebSocketRepository): LiveLocationService {
        return LiveLocationService(context, profileRepository, webSocketRepository)
    }



    @Singleton
    @Provides
    fun provideECGExtractionService(ecgRepository: ECGRepository,
                                    bpmRepositoryImpl: BpmRepositoryImpl,
                                    profileRepository: ProfileRepository,
                                    emergencyBroadcastService: EmergencyBroadcastService,
                                    bluetoothServiceECG: BluetoothServiceECG,
                                    webSocketRepository: WebSocketRepository
    ): ECGDataProcessingService {
        return ECGDataProcessingService(ecgRepository, bpmRepositoryImpl, profileRepository, emergencyBroadcastService, bluetoothServiceECG, webSocketRepository)
    }

    @Provides
    @Singleton
    fun provideEmergencyBroadcastService(@ApplicationContext context: Context, profileRepository: ProfileRepository): EmergencyBroadcastService {
        return EmergencyBroadcastService(context, profileRepository)
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
            .addInterceptor(authInterceptor)
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

    @Provides
    @Singleton
    fun provideECGApi(okHttpClient: OkHttpClient): ECGExtractionAPI {
        return Retrofit.Builder()
            .baseUrl(ECGExtractionAPI.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ECGExtractionAPI::class.java)
    }

    @Provides
    @Singleton
    fun provideBpmDataAPI(okHttpClient: OkHttpClient): BpmDataAPI {
        return Retrofit.Builder()
            .baseUrl(BpmDataAPI.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(BpmDataAPI::class.java)
    }

    @Provides
    @Singleton
    fun provideReportAPI(okHttpClient: OkHttpClient): ReportAPI {
        return Retrofit.Builder()
            .baseUrl(ReportAPI.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ReportAPI::class.java)
    }

    @Provides
    @Singleton
    fun provideContactAPI(okHttpClient: OkHttpClient): ContactAPI {
        return Retrofit.Builder()
            .baseUrl(ProfileAPI.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ContactAPI::class.java)
    }


    @Singleton
    @Provides
    fun provideECGRepository(appDB: AppDB, ecgExtractionAPI: ECGExtractionAPI): ECGRepository {
        return ECGRepositoryImpl(appDB.ecgSignalDataDao, ecgExtractionAPI)
    }

    @Singleton
    @Provides
    fun provideContactRepository(contactAPI: ContactAPI): ContactRepository {
        return ContactRepository(contactAPI)
    }

    @Singleton
    @Provides
    fun provideReportRepository(reportAPI: ReportAPI): ReportRepository {
        return ReportRepository(reportAPI)
    }

    @Singleton
    @Provides
    fun provideBpmRepository(bpmDataAPI: BpmDataAPI): BpmRepositoryImpl {
        return BpmRepositoryImpl(bpmDataAPI)
    }

    @Singleton
    @Provides
    fun provideStatisticRepository(reportRepository: ReportRepository,
                                   profileRepository: ProfileRepository,
                                   ecgRepository: ECGRepository,
                                   bpmRepositoryImpl: BpmRepositoryImpl,
                                   @ApplicationContext context: Context
    ): StatisticRepository {
        return StatisticRepository(
            context,
            reportRepository,
            profileRepository,
            ecgRepository,
            bpmRepositoryImpl
        )
    }

    @Singleton
    @Provides
    fun provideWebSocketRepository(
        authRepository: AuthRepository,
        profileRepository: ProfileRepository,
        bluetoothServiceECG: BluetoothServiceECG,
    ): WebSocketRepository {
        return WebsocketImpl(profileRepository, authRepository, bluetoothServiceECG)
    }

}