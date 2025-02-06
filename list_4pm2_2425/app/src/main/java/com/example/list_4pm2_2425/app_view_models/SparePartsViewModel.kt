package com.example.list_4pm2_2425.app_view_models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.list_4pm2_2425.data.Catalog
import com.example.list_4pm2_2425.data.Sparepart
import com.example.list_4pm2_2425.repository.AppRepository
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class SparePartsViewModel : ViewModel() {
    private val repository = AppRepository.getInstance() // Получаем репозиторий

    private val _sparepartList = MutableLiveData<List<Sparepart>>() //  Основной список запчастей
    val sparepartList: LiveData<List<Sparepart>> get() = _sparepartList

    private val _selectedSparePart = MutableLiveData<Sparepart?>() // Выбранная запчасть
    val selectedSparePart: LiveData<Sparepart?> get() = _selectedSparePart

    private val _searchQuery = MutableLiveData<String>() //  Поисковый запрос
    val searchQuery: LiveData<String> get() = _searchQuery

    var catalog: Catalog? = null
    private var allSpareParts: List<Sparepart> = emptyList() // Полный список запчастей

    // Загружаем запчасти для каталога
    fun set_Catalog(catalog: Catalog) {
        this.catalog = catalog
        viewModelScope.launch {
            repository.getCatalogSpareParts(catalog.id).collectLatest { spareParts ->


                if (spareParts.isNotEmpty()) {
                    allSpareParts = spareParts.toList() //  список для поиска
                    _sparepartList.postValue(spareParts) //  Обновляем UI

                }
            }
        }
    }



    // Фильтрация списка
    fun filterSparePartsByName(searchQuery: String) {


        if (allSpareParts.isEmpty()) {

            return
        }

        val filteredList = allSpareParts.filter {
            it.sparePartName.contains(searchQuery, ignoreCase = true) ||
                    it.manufacturer.contains(searchQuery, ignoreCase = true) ||
                    it.numberCatalog.contains(searchQuery, ignoreCase = true)
        }


        _sparepartList.value = filteredList // Обновляе UI
    }





    // Удаление запчасти
    fun deleteSparePart() {
        val partToDelete = _selectedSparePart.value ?: run {

            return
        }

        viewModelScope.launch {


            repository.deleteSparePart(partToDelete)

            // Обновляем список из БД
            val catalogId = catalog?.id ?: return@launch
            repository.getCatalogSpareParts(catalogId).collect { spareParts ->
                allSpareParts = spareParts
                _sparepartList.postValue(spareParts)

            }
        }
    }

    // Устанавливаем выбранную запчасть
    fun setCurrentSparePart(sparepart: Sparepart) {
        _selectedSparePart.value = sparepart

    }

    //Сортировка
    fun sortByPartName() {
        _sparepartList.value = _sparepartList.value?.sortedBy { it.sparePartName }
    }

    fun sortByManufacturer() {
        _sparepartList.value = _sparepartList.value?.sortedBy { it.manufacturer }
    }

    fun sortByNumberCatalog() {
        _sparepartList.value = _sparepartList.value?.sortedBy { it.numberCatalog }
    }
}
