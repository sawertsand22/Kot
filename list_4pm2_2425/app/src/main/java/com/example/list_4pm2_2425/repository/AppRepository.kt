package com.example.list_4pm2_2425.repository


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import com.example.list_4pm2_2425.ListApp4PM_1_2425
import com.example.list_4pm2_2425.data.CarModel
import com.example.list_4pm2_2425.data.Catalog
import com.example.list_4pm2_2425.data.Sparepart
import com.example.list_4pm2_2425.database.ListDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import java.util.UUID

class AppRepository {
    companion object{
        private var INSTANCE: AppRepository?=null

        fun getInstance(): AppRepository{
            if(INSTANCE == null){
                INSTANCE = AppRepository()
            }
            return INSTANCE ?:
            throw IllegalStateException("Репозиторий не инициализирован")
        }
    }

   // var listOfFaculty: MutableLiveData<ListOfFaculty?> = MutableLiveData()
    var carModel: MutableLiveData<CarModel> = MutableLiveData()
    //var listOfGroup: MutableLiveData<ListOfGroup> = MutableLiveData()
    var catalog: MutableLiveData<Catalog> = MutableLiveData()
    //var listOfStudent: MutableLiveData<ListOfStudent> = MutableLiveData()
    var sparepart: MutableLiveData<Sparepart> = MutableLiveData()



    fun getCarModelPosition(carModel: CarModel): Int = listOfCarModel.value?.indexOfFirst{
        it.id==carModel.id } ?:-1

    fun getFaculltyPosition()=getCarModelPosition(carModel.value?: CarModel())

    fun setCurrentCarModel(position: Int){
        if(listOfCarModel.value==null || position<0 ||
            (listOfCarModel.value?.size!!<=position))
            return
        setCurrentCarModel(listOfCarModel.value!![position])
    }

    fun setCurrentCarModel(_carModel: CarModel){
        carModel.postValue(_carModel)
    }



    fun loadData(){

    }



    fun getCatalogPosition(catalog: Catalog): Int = listOfCatalog.value?.indexOfFirst {
        it.id==catalog.id} ?:-1

    fun getCatalogPosition()=getCatalogPosition(catalog.value?: Catalog())

    fun setCurrentCatalog(position: Int){
        if(listOfCatalog.value==null || position<0 ||
            (listOfCatalog.value?.size!!<=position))
            return
        setCurrentCatalog(listOfCatalog.value!![position])
    }

    fun setCurrentCatalog(_catalog: Catalog){
        catalog.postValue(_catalog)
    }



    val carModelCatalog
        get()=listOfCatalog.value?.filter{it.carModelID == (carModel.value?.id ?: 0)}?.sortedBy { it.name } ?: listOf()

    fun getCarModelGroups(facultyID: UUID) =
        (listOfCatalog.value?.filter{ it.carModelID == facultyID }?.sortedBy { it.name } ?: listOf())



    fun getSparepartPosition(sparepart: Sparepart): Int = listOfSparepart.value?.indexOfFirst {
        it.id==sparepart.id} ?:-1

    fun getSparepartPosition()=getSparepartPosition(sparepart.value?: Sparepart())

    fun setCurrentSparePart(position: Int){
        if(listOfSparepart.value==null || position<0 ||
            (listOfSparepart.value?.size!!<=position))
            return
        setCurrentSparePart(listOfSparepart.value!![position])
    }

    fun setCurrentSparePart(_sparepart: Sparepart){
        sparepart.postValue(_sparepart)
    }

    fun getCatalogSpareParts(catalogID: UUID): Flow<List<Sparepart>> {
        return listDB.getCatalogSpareparts(catalogID)
    }






    val groupStudents
        get()=listOfSparepart.value?.filter{it.catalogID == (catalog.value?.id ?: 0)}?.sortedBy { it.shortName } ?: listOf()




    private val listDB by lazy { OfflineDBRepository(ListDatabase.getDatabase(ListApp4PM_1_2425.context).listDAO()) }

    private val myCoroutineScope = CoroutineScope(Dispatchers.Main)

    fun onDestroy(){
        myCoroutineScope.cancel()
    }

    val listOfCarModel : LiveData<List<CarModel>> = listDB.getCarModel()
        .asLiveData()

    fun addCarModel(carModel: CarModel){
        myCoroutineScope.launch {
            listDB.insertCarModel(carModel)
            setCurrentCarModel(carModel)
        }
    }

    fun updateCarModel(carModel: CarModel){
        addCarModel(carModel)
    }

    fun deleteCarModel(carModel: CarModel){
        myCoroutineScope.launch {
            listDB.deleteCarModel(carModel)
            setCurrentCarModel(0)
        }
    }

    val listOfCatalog: LiveData<List<Catalog>> = listDB.getAllCatalogs().asLiveData()

    fun addCatalog(catalog: Catalog){
        myCoroutineScope.launch {
            listDB.insertCatalog(catalog)
            setCurrentCatalog(catalog)
        }
    }

    fun updateCatalog(catalog: Catalog){
        addCatalog(catalog)
    }

    fun deleteCatalog(catalog: Catalog){
        myCoroutineScope.launch {
            listDB.deleteCatalog(catalog)
            setCurrentCatalog(0)
        }
    }

    val listOfSparepart: LiveData<List<Sparepart>> = listDB.getAllSpareparts().asLiveData()

    fun addSparepart(sparepart: Sparepart){
        myCoroutineScope.launch {
            listDB.insertSparepart(sparepart)
            setCurrentSparePart(sparepart)
        }

    }

    fun updateSparePart(sparepart: Sparepart){
        addSparepart(sparepart)
    }

    fun deleteSparePart(sparepart: Sparepart){
        myCoroutineScope.launch {
            listDB.deleteSparepart(sparepart)
            setCurrentSparePart(0)
        }
    }

}