package com.example.list_4pm2_2425.repository

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.list_4pm2_2425.data.CarModel
//import com.example.list_4pm2_2425.data.Faculty
import com.example.list_4pm2_2425.data.Group
import com.example.list_4pm2_2425.data.Student
import com.example.list_4pm2_2425.database.ListDAO
import kotlinx.coroutines.flow.Flow
import java.util.UUID

class OfflineDBRepository(val dao: ListDAO): DBRepository {

    override fun getFaculty(): Flow<List<CarModel>> = dao.getFaculty()
    override suspend fun insertFaculty(faculty: CarModel) = dao.insertFaculty(faculty)
    override suspend fun updateFaculty(faculty: CarModel) = dao.updateFaculty(faculty)
    override suspend fun insertAllFaculty(facultyList: List<CarModel>) = dao.insertAllFaculty(facultyList)
    override suspend fun deleteFaculty(faculty: CarModel) = dao.deleteFaculty(faculty)
    override suspend fun deleteAllFaculties() = dao.deleteAllFaculties()


    override fun getAllGroups(): Flow<List<Group>> = dao.getAllGroups()
    override fun getFacultyGroups(facultyID: UUID): Flow<List<Group>> = dao.getFacultyGroups(facultyID)
    override suspend fun insertGroup(group: Group) = dao.insertGroup(group)
    override suspend fun deleteGroup(group: Group) = dao.deleteGroup(group)
    override suspend fun deleteAllGroups() = dao.deleteAllGroups()



    override fun getAllStudents(): Flow<List<Student>> = dao.getAllStudents()
    override fun getGroupStudents(groupID: UUID): Flow<List<Student>> = dao.getGroupStudents(groupID)
    override suspend fun insertStudent(student: Student) = dao.insertStudent(student)
    override suspend fun deleteStudent(student: Student) = dao.deleteStudent(student)
    override suspend fun deleteAllStudents() = dao.deleteAllStudents()

}