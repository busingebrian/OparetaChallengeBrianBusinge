package com.leroy.oparetachallenge.databases

import androidx.lifecycle.LiveData
import androidx.room.*
import com.leroy.oparetachallenge.App
import com.leroy.oparetachallenge.BuildConfig
import com.leroy.oparetachallenge.models.ConversionData
import com.leroy.oparetachallenge.utils.RoomTypeConverters
import java.util.*

@TypeConverters(RoomTypeConverters::class)
@Database(entities = [ConversionData::class], version = BuildConfig.VERSION_CODE, exportSchema = false)
abstract class ConversionsDb: RoomDatabase() {

    abstract fun conversionsDao(): ConversionsDao

    @Dao
    interface ConversionsDao {

        @Insert(onConflict = OnConflictStrategy.REPLACE)
        fun insertConversion(conversionData: ConversionData)

        @Query("SELECT * FROM ConversionData WHERE amount= :amount AND symbol= :symbol AND convert= :convert ORDER BY updatedAt DESC LIMIT 1")
        fun getConversionData(amount: Int, symbol: String, convert: String): LiveData<ConversionData?>

        @Query("SELECT updatedAt FROM ConversionData WHERE amount= :amount AND symbol= :symbol AND convert= :convert ORDER BY updatedAt DESC LIMIT 1")
        fun getUpdatedAt(amount: Int, symbol: String, convert: String): Date?

        @Query("DELETE FROM ConversionData")
        fun deleteConversionData()
    }

    companion object {
        private var instance: ConversionsDb? = null

        fun get(): ConversionsDb {
            return instance ?: synchronized(ConversionsDb::class) {
                instance ?: Room.databaseBuilder(App.context, ConversionsDb::class.java, "conversion_db")
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build()
            }
        }

    }
}