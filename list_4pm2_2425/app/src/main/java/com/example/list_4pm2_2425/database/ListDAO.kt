package com.example.list_4pm2_2425.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.list_4pm2_2425.data.CarModel
import com.example.list_4pm2_2425.data.Group
import com.example.list_4pm2_2425.data.Student
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



    @Query("select * from groups")
    fun getAllGroups(): Flow<List<Group>>

    @Query("select * from groups where carModel_id=:facultyID")
    fun getFacultyGroups(facultyID: UUID): Flow<List<Group>>

    @Insert(entity = Group::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGroup(group: Group)

    @Delete(entity = Group::class)
    suspend fun deleteGroup(group: Group)

    @Query("delete from groups")
    suspend fun deleteAllGroups()



    @Query("select * from students")
    fun getAllStudents(): Flow<List<Student>>

    @Query("select * from students where group_id=:groupID")
    fun getGroupStudents(groupID: UUID): Flow<List<Student>>

    @Insert(entity = Student::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStudent(student: Student)

    @Delete(entity = Student::class)
    suspend fun deleteStudent(student: Student)

    @Query("delete from students")
    suspend fun deleteAllStudents()

}