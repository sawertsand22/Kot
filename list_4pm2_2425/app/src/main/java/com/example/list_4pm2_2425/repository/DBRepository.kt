package com.example.list_4pm2_2425.repository

import com.example.list_4pm2_2425.data.CarModel
import com.example.list_4pm2_2425.data.Catalog
import com.example.list_4pm2_2425.data.Sparepart
import kotlinx.coroutines.flow.Flow
import java.util.UUID

interface DBRepository{

    fun getCarModel(): Flow<List<CarModel>>
    suspend fun insertCarModel(carModel: CarModel)
    suspend fun updateCarModel(carModel: CarModel)
    suspend fun insertAllCarModel(carModelList: List<CarModel>)
    suspend fun deleteCarModel(carModel: CarModel)
    suspend fun deleteAllCarModel()


    fun getAllCatalogs(): Flow<List<Catalog>>
    fun getCarModelCatalogs(carModelID: UUID): Flow<List<Catalog>>
    suspend fun insertCatalog(catalog: Catalog)
    suspend fun deleteCatalog(catalog: Catalog)
    suspend fun deleteAllCatalogs()


    fun getAllSpareparts() : Flow<List<Sparepart>>
    fun getCatalogSpareparts(catalogID: UUID) : Flow<List<Sparepart>>
    suspend fun insertSparepart(sparepart: Sparepart)
    suspend fun deleteSparepart(sparepart: Sparepart)
    suspend fun deleteAllSpareparts()

}