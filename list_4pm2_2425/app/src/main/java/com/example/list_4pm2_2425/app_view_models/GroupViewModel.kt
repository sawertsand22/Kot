package com.example.list_4pm2_2425.app_view_models

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.list_4pm2_2425.data.Group
import com.example.list_4pm2_2425.repository.AppRepository

class GroupViewModel : ViewModel() {

    var groupList: MutableLiveData<List<Group>> = MutableLiveData()
    private var _group: Group? = null
    val group
        get()=_group

    init {
        AppRepository.getInstance().listOfGroup.observeForever {
            groupList.postValue(AppRepository.getInstance().facultyGroups)
        }

        AppRepository.getInstance().group.observeForever{
            _group=it
        }

        AppRepository.getInstance().faculty.observeForever {
            groupList.postValue(AppRepository.getInstance().facultyGroups)
        }
    }

    fun deleteGroup(){
        if(group!=null)
            AppRepository.getInstance().deleteGroup(group!!)
    }

    fun appendGroup(groupName: String){
        val group=Group()
        group.name=groupName
        group.facultyID=faculty?.id
        AppRepository.getInstance().updateGroup(group)
    }

    fun updateGroup(groupName: String){
        if(_group!=null){
            _group!!.name=groupName
            AppRepository.getInstance().updateGroup(_group!!)
        }
    }

    fun setCurrentGroup(position: Int){
        if ((groupList.value?.size ?: 0) > position)
            groupList.value?.let { AppRepository.getInstance().setCurrentGroup(it.get(position))}
    }

    fun setCurrentGroup(group: Group){
        AppRepository.getInstance().setCurrentGroup(group)
    }

    val getGroupListPosition
        get()=groupList.value?.indexOfFirst { it.id==group?.id } ?: -1

    val faculty
        get()=AppRepository.getInstance().faculty.value
}