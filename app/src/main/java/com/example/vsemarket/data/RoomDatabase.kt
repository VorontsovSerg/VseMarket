package com.example.vsemarket.data

import android.content.Context
import androidx.room.*

/**
 * Абстрактный класс базы данных Room.
 * Описывает конфигурацию локальной БД и доступ к DAO-объектам.
 */

@Entity(tableName = "orders")
data class OrderData(
    @PrimaryKey(autoGenerate = true) val orderId: Int = 0,
    val userId: String,
    val orderDate: Long
)

@Entity(primaryKeys = ["orderId", "productId"], tableName = "order_products")
data class OrderProductCrossRef(
    val orderId: Int,
    val productId: Int,
    val quantity: Int
)

data class OrderWithProducts(
    @Embedded val order: OrderData,
    @Relation(
        parentColumn = "orderId",
        entityColumn = "id",
        associateBy = Junction(OrderProductCrossRef::class)
    )
    val products: List<Product>
)

@Dao
interface ProductDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAll(products: List<Product>)

    @Query("SELECT * FROM products WHERE id = :id")
    suspend fun findById(id: Int): Product?

    @Query("SELECT * FROM products")
    suspend fun getAll(): List<Product>
}

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(user: ProfileData)

    @Query("SELECT * FROM users WHERE userId = :id")
    suspend fun findById(id: String): ProfileData?
}

@Dao
interface OrderDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrder(order: OrderData): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrderProducts(items: List<OrderProductCrossRef>)

    @Transaction
    @Query("SELECT * FROM orders WHERE orderId = :orderId")
    suspend fun getOrderWithProducts(orderId: Int): OrderWithProducts?

    @Query("SELECT * FROM orders WHERE userId = :userId")
    suspend fun findByUser(userId: String): List<OrderData>
}

@Database(
    entities = [Product::class, ProfileData::class, OrderData::class, OrderProductCrossRef::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun productDao(): ProductDao
    abstract fun userDao(): UserDao
    abstract fun orderDao(): OrderDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "vsemarket_db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
