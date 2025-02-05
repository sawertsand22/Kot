package com.example.list_4pm2_2425.repository

import com.example.list_4pm2_2425.data.CarModel
//import com.example.list_4pm2_2425.data.Faculty
import com.example.list_4pm2_2425.data.Catalog
import com.example.list_4pm2_2425.data.Sparepart
import com.example.list_4pm2_2425.data.User
import com.example.list_4pm2_2425.database.ListDAO
import kotlinx.coroutines.flow.Flow
import java.util.UUID

class OfflineDBRepository(val dao: ListDAO): DBRepository {

    // Проверка на авторизацию
    suspend fun isUserLoggedIn(): Boolean {
        val user = dao.getLoggedInUser()
        return user != null
    }

    // Логика регистрации нового пользователя
    suspend fun registerUser(email: String, password: String): Boolean {
        // Проверка, существует ли уже такой email
        val existingUser = dao.getUserByEmail(email)
        if (existingUser != null) {
            return false // Пользователь с таким email уже зарегистрирован
        }

        // Добавляем нового пользователя
        val newUser = User(email = email, password = password, isLoggedIn = false)
        dao.insertUser(newUser)
        return true
    }

    // Логика авторизации
    suspend fun loginUser(email: String, password: String): Boolean {
        val user = dao.getUserByEmail(email)
        return if (user != null && user.password == password) {
            // Устанавливаем статус авторизации
            dao.updateUser(user.copy(isLoggedIn = true))
            true
        } else {
            false
        }
    }

    // Логика выхода из аккаунта
    suspend fun logoutUser() {
        val loggedInUser = dao.getLoggedInUser()
        loggedInUser?.let {
            dao.updateUser(it.copy(isLoggedIn = false))
        }
    }

    override fun getCarModel(): Flow<List<CarModel>> = dao.getFaculty()
    override suspend fun insertCarModel(faculty: CarModel) = dao.insertFaculty(faculty)
    override suspend fun updateCarModel(faculty: CarModel) = dao.updateFaculty(faculty)
    override suspend fun insertAllCarModel(facultyList: List<CarModel>) = dao.insertAllFaculty(facultyList)
    override suspend fun deleteCarModel(faculty: CarModel) = dao.deleteFaculty(faculty)
    override suspend fun deleteAllCarModel() = dao.deleteAllFaculties()


    override fun getAllCatalogs(): Flow<List<Catalog>> = dao.getAllGroups()
    override fun getFacultyCatalogs(facultyID: UUID): Flow<List<Catalog>> = dao.getFacultyGroups(facultyID)
    override suspend fun insertCatalog(catalog: Catalog) = dao.insertGroup(catalog)
    override suspend fun deleteCatalog(catalog: Catalog) = dao.deleteGroup(catalog)
    override suspend fun deleteAllCatalogs() = dao.deleteAllGroups()



    override fun getAllSpareparts(): Flow<List<Sparepart>> = dao.getAllStudents()
    override fun getGroupSpareparts(groupID: UUID): Flow<List<Sparepart>> = dao.getGroupStudents(groupID)
    override suspend fun insertSparepart(sparepart: Sparepart) = dao.insertStudent(sparepart)
    override suspend fun deleteSparepart(sparepart: Sparepart) = dao.deleteStudent(sparepart)
    override suspend fun deleteAllSpareparts() = dao.deleteAllStudents()

}