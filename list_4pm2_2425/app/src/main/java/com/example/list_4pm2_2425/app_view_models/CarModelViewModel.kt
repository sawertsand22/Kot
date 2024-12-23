package com.example.list_4pm2_2425.app_view_models

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.list_4pm2_2425.data.CarModel
import com.example.list_4pm2_2425.repository.AppRepository

class CarModelViewModel : ViewModel() {
    var facultyList: LiveData<List<CarModel>> = AppRepository.getInstance().listOfFaculty

    private var _faculty: CarModel? = null

    val faculty
        get()=_faculty

//    private val facultyListObserver = Observer<ListOfFaculty?>{
//            list ->
//        facultyList.postValue(list)
//    }

    init {
  //      AppRepository.getInstance().listOfFaculty.observeForever(facultyListObserver)
        AppRepository.getInstance().faculty.observeForever {
            _faculty=it
        }
    }

    fun deleteFaculty(){
        if(faculty!=null)
            AppRepository.getInstance().deleteFaculty(faculty!!)
    }

    fun appendFaculty(facultyName: String){
        val faculty=CarModel()
        faculty.name=facultyName
        AppRepository.getInstance().updateFaculty(faculty)
    }

    fun updateFaculty(facultyName: String){
        if (_faculty!=null){
            _faculty!!.name=facultyName
            AppRepository.getInstance().updateFaculty(_faculty!!)
        }
    }

    fun setCurrentFaculty(faculty: CarModel){
        AppRepository.getInstance().setCurrentFaculty(faculty)
    }
}