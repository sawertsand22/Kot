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
    val student
        get()=_sparepart

    var catalog: Catalog? = null
    private var allSpareparts: List<Sparepart> = emptyList()
    fun set_Group(catalog: Catalog){
        this.catalog = catalog
        AppRepository.getInstance().listOfSparepart.observeForever { catalogs ->
            allSpareparts = AppRepository.getInstance().getGroupStudents(catalog.id) // Инициализируем исходный список
            sparepartList.postValue(allSpareparts) // Изначально отображаем полный список
        }
        AppRepository.getInstance().sparepart.observeForever{
            _sparepart = it
        }
    }

    fun deleteStudent(){
        if(student != null)
            AppRepository.getInstance().deleteStudent(student!!)
    }

    fun setCurrentStudent(sparepart: Sparepart){
        AppRepository.getInstance().setCurrentStudent(sparepart)
    }

    // Фильтрация студентов по имени
    fun filterStudentsByName(name: String) {
        Log.d("FilterStudents", "Filtering by name: $name")

        val filteredList = allSpareparts.filter { // Фильтруем ИСХОДНЫЙ список!
            it.sparePartName.trim().contains(name, ignoreCase = true) ||
                    it.manufacturer.trim().contains(name, ignoreCase = true) ||
                    it.numberCatalog.trim().contains(name, ignoreCase = true)
        }

        Log.d("FilterStudents", "Filtered list size: ${filteredList.size}")
        sparepartList.postValue(filteredList) // Обновляем LiveData ОТФИЛЬТРОВАННЫМ списком!
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