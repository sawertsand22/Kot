package com.example.list_4pm2_2425.repository

import com.example.list_4pm2_2425.data.Faculty
import com.example.list_4pm2_2425.data.Group
import com.example.list_4pm2_2425.data.Student
import com.example.list_4pm2_2425.database.ListDAO
import kotlinx.coroutines.flow.Flow
import java.util.UUID

interface DBRepository{

    fun getFaculty(): Flow<List<Faculty>>
    suspend fun insertFaculty(faculty: Faculty)
    suspend fun updateFaculty(faculty: Faculty)
    suspend fun insertAllFaculty(facultyList: List<Faculty>)
    suspend fun deleteFaculty(faculty: Faculty)
    suspend fun deleteAllFaculties()


    fun getAllGroups(): Flow<List<Group>>
    fun getFacultyGroups(facultyID: UUID): Flow<List<Group>>
    suspend fun insertGroup(group: Group)
    suspend fun deleteGroup(group: Group)
    suspend fun deleteAllGroups()


    fun getAllStudents() : Flow<List<Student>>
    fun getGroupStudents(groupID: UUID) : Flow<List<Student>>
    suspend fun insertStudent(student: Student)
    suspend fun deleteStudent(student: Student)
    suspend fun deleteAllStudents()

}