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

    override fun getCarModel(): Flow<List<CarModel>> = dao.getCarModel()
    override suspend fun insertCarModel(carModel: CarModel) = dao.insertCarModel(carModel)
    override suspend fun updateCarModel(carModel: CarModel) = dao.updateCarModel(carModel)
    override suspend fun insertAllCarModel(carModelList: List<CarModel>) = dao.insertAllCarModel(carModelList)
    override suspend fun deleteCarModel(carModel: CarModel) = dao.deleteCarModel(carModel)
    override suspend fun deleteAllCarModel() = dao.deleteAllCarModel()


    override fun getAllCatalogs(): Flow<List<Catalog>> = dao.getAllCatalog()
    override fun getCarModelCatalogs(carModelID: UUID): Flow<List<Catalog>> = dao.getCarModelCatalogs(carModelID)
    override suspend fun insertCatalog(catalog: Catalog) = dao.insertCatalog(catalog)
    override suspend fun deleteCatalog(catalog: Catalog) = dao.deleteCatalog(catalog)
    override suspend fun deleteAllCatalogs() = dao.deleteAllCatalogs()



    override fun getAllSpareparts(): Flow<List<Sparepart>> = dao.getAllSparepart()
    override fun getCatalogSpareparts(catalogID: UUID): Flow<List<Sparepart>> = dao.getCatalogSparepart(catalogID)
    override suspend fun insertSparepart(sparepart: Sparepart) = dao.insertSparepart(sparepart)
    override suspend fun deleteSparepart(sparepart: Sparepart) = dao.deleteSparepart(sparepart)
    override suspend fun deleteAllSpareparts() = dao.deleteAllSparepart()

}