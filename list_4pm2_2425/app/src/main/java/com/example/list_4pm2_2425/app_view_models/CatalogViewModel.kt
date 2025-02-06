package com.example.list_4pm2_2425.app_view_models

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.list_4pm2_2425.data.Catalog
import com.example.list_4pm2_2425.repository.AppRepository

class CatalogViewModel : ViewModel() {




    var catalogList: MutableLiveData<List<Catalog>> = MutableLiveData()
    private var _catalog: Catalog? = null
    val catalog
        get()=_catalog
    val searchQuery = MutableLiveData<String>() // 🔥 Храним строку поиска
    init {
        AppRepository.getInstance().listOfCatalog.observeForever {
            catalogList.postValue(AppRepository.getInstance().carModelCatalog)
        }

        AppRepository.getInstance().catalog.observeForever{
            _catalog=it
        }

        AppRepository.getInstance().carModel.observeForever {
            catalogList.postValue(AppRepository.getInstance().carModelCatalog)
        }
    }




    fun setSearchQuery(query: String) {
        searchQuery.value = query // 🔥 Обновляем поисковый запрос
    }


    fun deleteCatalog(){
        if(catalog!=null)
            AppRepository.getInstance().deleteCatalog(catalog!!)
    }

    fun appendCatalog(catalogName: String){
        val catalog=Catalog()
        catalog.name=catalogName
        catalog.carModelID=carModel?.id
        AppRepository.getInstance().updateCatalog(catalog)
    }

    fun updateCatalog(catalogName: String){
        if(_catalog!=null){
            _catalog!!.name=catalogName
            AppRepository.getInstance().updateCatalog(_catalog!!)
        }
    }

    fun setCurrentCatalog(position: Int){
        if ((catalogList.value?.size ?: 0) > position)
            catalogList.value?.let { AppRepository.getInstance().setCurrentCatalog(it.get(position))}
    }

    fun setCurrentCatalog(catalog: Catalog){
        AppRepository.getInstance().setCurrentCatalog(catalog)
    }

    val getCatalogListPosition
        get()=catalogList.value?.indexOfFirst { it.id==catalog?.id } ?: -1

    val carModel
        get()=AppRepository.getInstance().carModel.value



}
