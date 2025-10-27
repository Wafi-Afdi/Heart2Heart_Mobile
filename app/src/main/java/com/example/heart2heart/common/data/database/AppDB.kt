package com.example.heart2heart.common.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.heart2heart.common.data.converter.Converters
import com.example.heart2heart.common.data.dao.CookieDao
import com.example.heart2heart.common.data.dao.ECGDeviceDao
import com.example.heart2heart.common.data.dao.ECGSignalDao
import com.example.heart2heart.common.data.dao.MonitoringAccessDao
import com.example.heart2heart.common.data.dao.UserDao
import com.example.heart2heart.common.domain.model.Cookie
import com.example.heart2heart.common.domain.model.ECGDevice
import com.example.heart2heart.common.domain.model.ECGSignal
import com.example.heart2heart.common.domain.model.MonitoringAccess
import com.example.heart2heart.common.domain.model.User

@Database(
    entities = [User::class, ECGSignal::class, Cookie::class, MonitoringAccess::class, ECGDevice::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDB: RoomDatabase() {
    abstract val userDao: UserDao
    abstract val ecgSignalDao: ECGSignalDao
    abstract val monitoringAccessDao: MonitoringAccessDao
    abstract val ecgDeviceDao: ECGDeviceDao
    abstract val cookieDao: CookieDao

}
