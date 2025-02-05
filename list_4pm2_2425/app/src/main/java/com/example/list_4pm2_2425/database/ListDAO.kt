package com.example.list_4pm2_2425.database

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
    fun getFaculty(): Flow<List<CarModel>>

    @Insert(entity = CarModel::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFaculty(faculty: CarModel)

    @Update(entity = CarModel::class)
    suspend fun updateFaculty(faculty: CarModel)

    @Insert(entity = CarModel::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllFaculty(facultyList: List<CarModel>)

    @Delete(entity = CarModel::class)
    suspend fun deleteFaculty(faculty: CarModel)

    @Query("delete from CarModels")
    suspend fun deleteAllFaculties()



    @Query("select * from catalog")
    fun getAllGroups(): Flow<List<Catalog>>

    @Query("select * from catalog where carModel_id=:facultyID")
    fun getFacultyGroups(facultyID: UUID): Flow<List<Catalog>>

    @Insert(entity = Catalog::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGroup(catalog: Catalog)

    @Delete(entity = Catalog::class)
    suspend fun deleteGroup(catalog: Catalog)

    @Query("delete from catalog")
    suspend fun deleteAllGroups()



    @Query("select * from spareparts")
    fun getAllStudents(): Flow<List<Sparepart>>

    @Query("select * from spareparts where catalog_id=:groupID")
    fun getGroupStudents(groupID: UUID): Flow<List<Sparepart>>

    @Insert(entity = Sparepart::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStudent(sparepart: Sparepart)

    @Delete(entity = Sparepart::class)
    suspend fun deleteStudent(sparepart: Sparepart)

    @Query("delete from spareparts")
    suspend fun deleteAllStudents()

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