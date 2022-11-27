package com.example.bearrecipebookapp.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

// Annotates class to be a Room Database with a table (entity) of the Word class
@Database(entities = [
                        RecipeEntity::class,
                        IngredientEntity::class,
                        RecipeIngredientJoinEntity::class,
                        InstructionEntity::class,
                        FilterEntity::class,
                        RecipeFiltersJoinEntity::class,
                        DetailsScreenTargetEntity::class,

                     ],
    version = 1,
    exportSchema = true
    )
public abstract class RecipeAppDatabase : RoomDatabase() {

    abstract fun RecipeDao(): RecipeDao
    abstract fun HomeScreenDao(): HomeScreenDao
    abstract fun DetailsScreenDao(): DetailsScreenDao
    abstract fun MenuScreenDao(): MenuScreenDao
    abstract fun ShoppingListScreenDao(): ShoppingListScreenDao
    abstract fun SearchScreenDao(): SearchScreenDao
    abstract fun TopBarDao(): TopBarDao

    companion object {

        @Volatile
        private var INSTANCE: RecipeAppDatabase? = null

        fun getInstance(context: Context): RecipeAppDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
            }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                RecipeAppDatabase::class.java, "app_database25.db"
            )
              //  .allowMainThreadQueries()
                .createFromAsset("database/app_database25.db")
                .build()
                // prepopulate the database after onCreate was called
//                .addCallback(object : Callback() {
//                    override fun onCreate(db: SupportSQLiteDatabase) {
//                        super.onCreate(db)
//                        // insert the data on the IO Thread
//                        suspend {
//                            getInstance(context).RecipeDao()
//                        }
//                    }
//                })

//        val PREPOPULATE_DATA = listOf(Data("1", "val"), Data("2", "val 2"))
    }
}


//    companion object {
//        // Singleton prevents multiple instances of database opening at the
//        // same time.
//        @Volatile
//        private var INSTANCE: RecipeAppDatabase? = null
//
//        fun getDatabase(context: Context): RecipeAppDatabase {
//            // if the INSTANCE is not null, then return it,
//            // if it is, then create the database
//            return INSTANCE ?: synchronized(this) {
//                val instance = Room.databaseBuilder(
//                    context.applicationContext,
//                    RecipeAppDatabase::class.java,
//                    "app_database"
//                ).createFromAsset("database/app_database.db").build()
//                INSTANCE = instance
//                // return instance
//                instance
//            }
//        }
//    }
//}