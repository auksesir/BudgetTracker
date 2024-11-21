package entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "category_totals")
data class CategoryTotal(
    @PrimaryKey val category: String,
    var total: Double
)