package dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import entities.CategoryTotal

@Dao
//Data Access Object for CategoryTotal in Room database
interface CategoryTotalDao {

    //automatically updates UI when data changes
    @Query("SELECT * FROM category_totals ")
    fun getCategoryTotals(): LiveData<List<CategoryTotal>>

    // if already exists in the db should be replaced
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(categoryTotals: List<CategoryTotal>)

    @Query("SELECT * FROM category_totals WHERE category = :category")
    suspend fun getCategoryTotalByName(category: String): CategoryTotal?

    @Update
    suspend fun update(categoryTotal: CategoryTotal)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(categoryTotal: CategoryTotal)

    @Delete
    suspend fun delete(categoryTotal: CategoryTotal)

    @Query("DELETE FROM category_totals")
    suspend fun deleteAll()

}
