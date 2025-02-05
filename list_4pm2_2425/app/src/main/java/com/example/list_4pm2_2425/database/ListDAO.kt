package com.example.list_4pm2_2425.database

import android.icu.text.StringSearch
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.list_4pm2_2425.data.CarModel
import com.example.list_4pm2_2425.data.Catalog
import com.example.list_4pm2_2425.data.Sparepart
import com.example.list_4pm2_2425.data.User
import kotlinx.coroutines.flow.Flow
import java.util.UUID

@Dao
interface ListDAO {

    @Query("select * from CarModels order by carModel_name")
    fun getCarModel(): Flow<List<CarModel>>

    @Insert(entity = CarModel::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCarModel(carModel: CarModel)

    @Update(entity = CarModel::class)
    suspend fun updateCarModel(carModel: CarModel)

    @Insert(entity = CarModel::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllCarModel(carModelList: List<CarModel>)

    @Delete(entity = CarModel::class)
    suspend fun deleteCarModel(carModel: CarModel)

    @Query("delete from CarModels")
    suspend fun deleteAllCarModel()



    @Query("select * from catalog")
    fun getAllCatalog(): Flow<List<Catalog>>

    @Query("select * from catalog where carModel_id=:carModelID")
    fun getCarModelCatalogs(carModelID: UUID): Flow<List<Catalog>>

    @Insert(entity = Catalog::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCatalog(catalog: Catalog)

    @Delete(entity = Catalog::class)
    suspend fun deleteCatalog(catalog: Catalog)

    @Query("delete from catalog")
    suspend fun deleteAllCatalogs()



    @Query("select * from spareparts where sparePartName like '%' | :search | '%' or 1")
    fun getAllSparepart(search: String = ""): Flow<List<Sparepart>>

    @Query("select * from spareparts where catalog_id=:catalogID")
    fun getCatalogSparepart(catalogID: UUID): Flow<List<Sparepart>>

    @Insert(entity = Sparepart::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSparepart(sparepart: Sparepart)

    @Delete(entity = Sparepart::class)
    suspend fun deleteSparepart(sparepart: Sparepart)

    @Query("delete from spareparts")
    suspend fun deleteAllSparepart()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User)

    // Получение пользователя по email
    @Query("SELECT * FROM users WHERE email = :email LIMIT 1")
    suspend fun getUserByEmail(email: String): User?

    // Обновление статуса пользователя
    @Update
    suspend fun updateUser(user: User)

    // Получение текущего авторизованного пользователя
    @Query("SELECT * FROM users WHERE isLoggedIn = 1 LIMIT 1")
    suspend fun getLoggedInUser(): User?

}