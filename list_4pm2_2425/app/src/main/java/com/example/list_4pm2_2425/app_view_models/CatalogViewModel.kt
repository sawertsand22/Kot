package com.example.list_4pm2_2425.app_view_models

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.list_4pm2_2425.data.Catalog
import com.example.list_4pm2_2425.repository.AppRepository

class CatalogViewModel : ViewModel() {

    var catalogList: MutableLiveData<List<Catalog>> = MutableLiveData()
    private var _catalog: Catalog? = null
    val group
        get()=_catalog

    init {
        AppRepository.getInstance().listOfCatalog.observeForever {
            catalogList.postValue(AppRepository.getInstance().facultyGroups)
        }

        AppRepository.getInstance().catalog.observeForever{
            _catalog=it
        }

        AppRepository.getInstance().faculty.observeForever {
            catalogList.postValue(AppRepository.getInstance().facultyGroups)
        }
    }

    fun deleteGroup(){
        if(group!=null)
            AppRepository.getInstance().deleteGroup(group!!)
    }

    fun appendGroup(groupName: String){
        val catalog=Catalog()
        catalog.name=groupName
        catalog.carModelID=faculty?.id
        AppRepository.getInstance().updateGroup(catalog)
    }

    fun updateGroup(groupName: String){
        if(_catalog!=null){
            _catalog!!.name=groupName
            AppRepository.getInstance().updateGroup(_catalog!!)
        }
    }

    fun setCurrentGroup(position: Int){
        if ((catalogList.value?.size ?: 0) > position)
            catalogList.value?.let { AppRepository.getInstance().setCurrentGroup(it.get(position))}
    }

    fun setCurrentGroup(catalog: Catalog){
        AppRepository.getInstance().setCurrentGroup(catalog)
    }

    val getGroupListPosition
        get()=catalogList.value?.indexOfFirst { it.id==group?.id } ?: -1

    val faculty
        get()=AppRepository.getInstance().faculty.value
}