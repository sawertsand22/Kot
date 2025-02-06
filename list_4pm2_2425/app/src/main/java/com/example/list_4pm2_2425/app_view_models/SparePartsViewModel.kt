package com.example.list_4pm2_2425.app_view_models

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.list_4pm2_2425.data.Catalog
import com.example.list_4pm2_2425.data.Sparepart
import com.example.list_4pm2_2425.repository.AppRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class SparePartsViewModel : ViewModel() {
    private val repository = AppRepository.getInstance() // Получаем репозиторий

    private val _sparepartList = MutableLiveData<List<Sparepart>>() // 🔥 Основной список запчастей
    val sparepartList: LiveData<List<Sparepart>> get() = _sparepartList

    private val _selectedSparePart = MutableLiveData<Sparepart?>() // 🔥 Выбранная запчасть
    val selectedSparePart: LiveData<Sparepart?> get() = _selectedSparePart

    private val _searchQuery = MutableLiveData<String>() // 🔥 Поисковый запрос
    val searchQuery: LiveData<String> get() = _searchQuery

    var catalog: Catalog? = null
    private var allSpareParts: List<Sparepart> = emptyList() // 🔥 Полный список запчастей

    // ✅ Загружаем запчасти для каталога
    fun set_Catalog(catalog: Catalog) {
        this.catalog = catalog
        viewModelScope.launch {
            repository.getCatalogSpareParts(catalog.id).collectLatest { spareParts ->
                Log.d("SparePartsViewModel", "🔄 Загружено ${spareParts.size} запчастей")

                if (spareParts.isNotEmpty()) {
                    allSpareParts = spareParts.toList() // ✅ Сохраняем список для поиска
                    _sparepartList.postValue(spareParts) // ✅ Обновляем UI
                    Log.d("SparePartsViewModel", "✅ allSpareParts теперь содержит ${allSpareParts.size} элементов")
                } else {
                    Log.d("SparePartsViewModel", "❌ Ошибка: Получен пустой список запчастей")
                }
            }
        }
    }



    // ✅ Фильтрация списка
    fun filterSparePartsByName(searchQuery: String) {
        Log.d("FilterSpareParts", "📊 Текущий список запчастей перед поиском: ${allSpareParts.size} элементов")

        if (allSpareParts.isEmpty()) {
            Log.d("FilterSpareParts", "❌ Ошибка: allSpareParts пуст! Ждём данные...")
            return
        }

        val filteredList = allSpareParts.filter {
            it.sparePartName.contains(searchQuery, ignoreCase = true) ||
                    it.manufacturer.contains(searchQuery, ignoreCase = true) ||
                    it.numberCatalog.contains(searchQuery, ignoreCase = true)
        }

        Log.d("FilterSpareParts", "✅ Отфильтровано: ${filteredList.size} из ${allSpareParts.size}")
        _sparepartList.value = filteredList // 🔥 Обновляем UI
    }





    // ✅ Удаление запчасти
    fun deleteSparePart() {
        val partToDelete = _selectedSparePart.value ?: run {
            Log.d("DeleteSparePart", "❌ Ошибка: sparePart == null, удалять нечего!")
            return
        }

        viewModelScope.launch {
            Log.d("DeleteSparePart", "🔥 Пытаемся удалить: ${partToDelete.sparePartName}")

            repository.deleteSparePart(partToDelete)

            // 🔥 Обновляем список из БД
            val catalogId = catalog?.id ?: return@launch
            repository.getCatalogSpareParts(catalogId).collect { spareParts ->
                allSpareParts = spareParts
                _sparepartList.postValue(spareParts)
                Log.d("DeleteSparePart", "✅ Обновленный список после удаления: ${spareParts.size} запчастей")
            }
        }
    }

    // ✅ Устанавливаем выбранную запчасть
    fun setCurrentSparePart(sparepart: Sparepart) {
        _selectedSparePart.value = sparepart
        Log.d("SetSparePart", "🔹 Установили текущую запчасть: ${sparepart.sparePartName}")
    }

    // ✅ Сортировка
    fun sortByName() {
        _sparepartList.value = _sparepartList.value?.sortedBy { it.sparePartName }
    }

    fun sortByMiddleName() {
        _sparepartList.value = _sparepartList.value?.sortedBy { it.manufacturer }
    }

    fun sortByLastName() {
        _sparepartList.value = _sparepartList.value?.sortedBy { it.numberCatalog }
    }
}
