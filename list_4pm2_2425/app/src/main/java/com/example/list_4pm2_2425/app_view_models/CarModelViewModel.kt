package com.example.list_4pm2_2425.app_view_models

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.list_4pm2_2425.data.CarModel
import com.example.list_4pm2_2425.repository.AppRepository

class CarModelViewModel : ViewModel() {
    var CarModelList: LiveData<List<CarModel>> = AppRepository.getInstance().listOfCarModel

    private var _carModel: CarModel? = null

    val carModel
        get()=_carModel

//    private val facultyListObserver = Observer<ListOfFaculty?>{
//            list ->
//        facultyList.postValue(list)
//    }

    init {
  //      AppRepository.getInstance().listOfFaculty.observeForever(facultyListObserver)
        AppRepository.getInstance().carModel.observeForever {
            _carModel=it
        }
    }

    fun deleteCarModel(){
        if(carModel!=null)
            AppRepository.getInstance().deleteCarModel(carModel!!)
    }

    fun appendCarModel(carModelName: String){
        val carModel=CarModel()
        carModel.name=carModelName
        AppRepository.getInstance().updateCarModel(carModel)
    }

    fun updateCarModel(CarModelName: String){
        if (_carModel!=null){
            _carModel!!.name=CarModelName
            AppRepository.getInstance().updateCarModel(_carModel!!)
        }
    }

    fun setCurrentCarModel(carModel: CarModel){
        AppRepository.getInstance().setCurrentCarModel(carModel)
    }
}