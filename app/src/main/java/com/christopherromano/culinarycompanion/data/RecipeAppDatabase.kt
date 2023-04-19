package com.christopherromano.culinarycompanion.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.christopherromano.culinarycompanion.data.dao.AppDao
import com.christopherromano.culinarycompanion.data.dao.CommentScreenDao
import com.christopherromano.culinarycompanion.data.dao.DetailsScreenDao
import com.christopherromano.culinarycompanion.data.dao.HomeScreenDao
import com.christopherromano.culinarycompanion.data.dao.MenuScreenDao
import com.christopherromano.culinarycompanion.data.dao.ProfileScreenDao
import com.christopherromano.culinarycompanion.data.dao.SearchScreenDao
import com.christopherromano.culinarycompanion.data.dao.SettingsScreenDao
import com.christopherromano.culinarycompanion.data.dao.ShoppingListScreenDao
import com.christopherromano.culinarycompanion.data.dao.TopBarDao
import com.christopherromano.culinarycompanion.data.entity.CommentsEntity
import com.christopherromano.culinarycompanion.data.entity.FilterEntity
import com.christopherromano.culinarycompanion.data.entity.IngredientEntity
import com.christopherromano.culinarycompanion.data.entity.InstructionEntity
import com.christopherromano.culinarycompanion.data.entity.QuantitiesTableEntity
import com.christopherromano.culinarycompanion.data.entity.RecipeEntity
import com.christopherromano.culinarycompanion.data.entity.RecipeFiltersJoinEntity
import com.christopherromano.culinarycompanion.data.entity.RecipeIngredientJoinEntity
import com.christopherromano.culinarycompanion.data.entity.SearchEntity
import com.christopherromano.culinarycompanion.data.entity.ShoppingListCustomItemsEntity
import com.christopherromano.culinarycompanion.data.entity.UserEntity

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
                RecipeAppDatabase::class.java, "app_database81.db"
            )
              //  .allowMainThreadQueries()
                .createFromAsset("database/app_database81.db")
                .build()
    }
}
