package com.christopherromano.culinarycompanion.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.christopherromano.culinarycompanion.data.dao.*
import com.christopherromano.culinarycompanion.data.entity.*

@Database(entities = [
                        RecipeEntity::class,
                        IngredientEntity::class,
                        RecipeIngredientJoinEntity::class,
                        InstructionEntity::class,
                        FilterEntity::class,
                        RecipeFiltersJoinEntity::class,
                        SearchEntity::class,
                        UserEntity::class,
                        ShoppingListCustomItemsEntity::class,
                        CommentsEntity::class,
                        QuantitiesTableEntity::class,

                     ],
    version = 1,
    exportSchema = true
    )
public abstract class RecipeAppDatabase : RoomDatabase() {

    abstract fun HomeScreenDao(): HomeScreenDao
    abstract fun DetailsScreenDao(): DetailsScreenDao
    abstract fun MenuScreenDao(): MenuScreenDao
    abstract fun ShoppingListScreenDao(): ShoppingListScreenDao
    abstract fun SearchScreenDao(): SearchScreenDao
    abstract fun TopBarDao(): TopBarDao
    abstract fun ProfileScreenDao(): ProfileScreenDao
    abstract fun CommentScreenDao(): CommentScreenDao
    abstract fun AppDao(): AppDao
    abstract fun SettingsScreenDao(): SettingsScreenDao

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
                RecipeAppDatabase::class.java, "app_database78.db"
            )
              //  .allowMainThreadQueries()
                .createFromAsset("database/app_database78.db")
                .build()
    }
}
