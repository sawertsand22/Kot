package com.example.list_4pm2_2425.app_view_models

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.list_4pm2_2425.data.Catalog
import com.example.list_4pm2_2425.data.Sparepart
import com.example.list_4pm2_2425.repository.AppRepository

class SparePartsViewModel : ViewModel() {
    var sparepartList: MutableLiveData<List<Sparepart>> = MutableLiveData()
    private var _sparepart: Sparepart? = null
    val sparePart
        get()=_sparepart

    var catalog: Catalog? = null
    private var allSpareparts: List<Sparepart> = emptyList()
    fun set_Catalog(catalog: Catalog){
        this.catalog = catalog
        AppRepository.getInstance().listOfSparepart.observeForever { catalogs ->
            if (sparepartList.value.isNullOrEmpty()) {
                allSpareparts = AppRepository.getInstance().getCatalogSpareParts(catalog.id)
                Log.d("SparePartsDebug", "All spare parts loaded: ${allSpareparts.size}")
                sparepartList.value = allSpareparts
            }
           // allSpareparts = AppRepository.getInstance().getCatalogSpareParts(catalog.id) // Инициализируем исходный список



         //   Log.d("SparePartsDebug", "All spare parts reloaded: ${allSpareparts.size}")
           // sparepartList.postValue(allSpareparts) // Изначально отображаем полный список
        }
        AppRepository.getInstance().sparepart.observeForever{
            _sparepart = it
        }
    }

    fun deleteSparePart(){
        if(sparePart != null)
            AppRepository.getInstance().deleteSparePart(sparePart!!)
    }

    fun setCurrentSparePart(sparepart: Sparepart){
        AppRepository.getInstance().setCurrentSparePart(sparepart)
    }

    // Фильтрация студентов по имени
    fun filterSparePartsByName(name: String) {
        Log.d("FilterSpareParts", "Filtering by name: $name")

        val filteredList = allSpareparts.filter { // Фильтруем ИСХОДНЫЙ список!
            Log.d("FilterSpareParts", "Checking: ${it.sparePartName} - Match: ${it.sparePartName.contains(name, ignoreCase = true)}")
            it.sparePartName.trim().contains(name, ignoreCase = true) ||
                    it.manufacturer.trim().contains(name, ignoreCase = true) ||
                    it.numberCatalog.trim().contains(name, ignoreCase = true)
        }

        Log.d("FilterSpareParts", "Filtered list size: ${filteredList.size}")
        sparepartList.value = filteredList
        // ⬅️ Меняем на value вместо postValue
            //sparepartList.postValue(filteredList) // Обновляем LiveData ОТФИЛЬТРОВАННЫМ списком!
    }




    // Фильтрация студентов по VIN

    fun sortByName() {
        val sortedList = sparepartList.value?.sortedBy { it.sparePartName }
        sparepartList.postValue(sortedList)
    }

    // Сортировка студентов по middleName
    fun sortByMiddleName() {
        val sortedList = sparepartList.value?.sortedBy { it.manufacturer }
        sparepartList.postValue(sortedList)
    }

    // Сортировка студентов по lastName
    fun sortByLastName() {
        val sortedList = sparepartList.value?.sortedBy { it.numberCatalog }
        sparepartList.postValue(sortedList)
    }
}