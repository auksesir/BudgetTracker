package database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import dao.CategoryTotalDao
import dao.ExpenseDao
import entities.CategoryTotal
import entities.Expense

@Database(entities = [Expense::class, CategoryTotal::class], version = 3, exportSchema = false)
abstract class ExpenseDatabase : RoomDatabase() {

    abstract fun expenseDao(): ExpenseDao

    abstract fun categoryTotalDao(): CategoryTotalDao

    companion object {
        @Volatile
        private var INSTANCE: ExpenseDatabase? = null

        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {

            }
        }

        private val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(db: SupportSQLiteDatabase) {
                // Drop the existing category_totals table
                db.execSQL("DROP TABLE IF EXISTS `category_totals`")

                // Create the new category_totals table without the id column
                db.execSQL(
                    "CREATE TABLE IF NOT EXISTS `category_totals` (" +
                            "`category` TEXT PRIMARY KEY NOT NULL, " +
                            "`total` REAL NOT NULL)"
                )

                // Check if the 'newColumn' exists in the 'Expense' table
                val cursor = db.query("PRAGMA table_info(Expense)")
                val columnIndex = cursor.getColumnIndex("name") // Get the column index once

                // Ensure columnIndex is valid before using it
                if (columnIndex != -1) {
                    val columnNames = mutableListOf<String>()
                    while (cursor.moveToNext()) {
                        columnNames.add(cursor.getString(columnIndex))
                    }
                    // If 'newColumn' exists, drop it
                    if (columnNames.contains("newColumn")) {
                        db.execSQL("ALTER TABLE `Expense` DROP COLUMN `newColumn`")
                    }
                }
                cursor.close()


            }
        }

        fun getDatabase(context: Context): ExpenseDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ExpenseDatabase::class.java,
                    "expense_database"
                )
                    .addMigrations(MIGRATION_1_2, MIGRATION_2_3) // Add all migrations here
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
