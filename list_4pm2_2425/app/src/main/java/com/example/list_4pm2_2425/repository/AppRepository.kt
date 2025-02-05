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
    var faculty: MutableLiveData<CarModel> = MutableLiveData()
    //var listOfGroup: MutableLiveData<ListOfGroup> = MutableLiveData()
    var catalog: MutableLiveData<Catalog> = MutableLiveData()
    //var listOfStudent: MutableLiveData<ListOfStudent> = MutableLiveData()
    var sparepart: MutableLiveData<Sparepart> = MutableLiveData()

//    fun addFaculty(faculty: Faculty){
//        val listTmp = (listOfFaculty.value ?: ListOfFaculty()).apply {
//            items.add(faculty)
//        }
//        listOfFaculty.postValue(listTmp)
//        setCurrentFaculty(faculty)
//    }
//
//    fun updateFaculty(faculty: Faculty){
//        val position = getFacultyPosition(faculty)
//        if (position < 0) addFaculty(faculty)
//        else{
//            val listTmp = listOfFaculty.value!!
//            listTmp.items[position]=faculty
//            listOfFaculty.postValue(listTmp)
//        }
//    }
//
//    fun deleteFaculty(faculty: Faculty){
//        val listTmp = listOfFaculty.value!!
//        if(listTmp.items.remove(faculty)){
//            listOfFaculty.postValue(listTmp)
//        }
//        setCurrentFaculty(0)
//    }

    fun getFacultyPosition(faculty: CarModel): Int = listOfFaculty.value?.indexOfFirst{
        it.id==faculty.id } ?:-1

    fun getFaculltyPosition()=getFacultyPosition(faculty.value?: CarModel())

    fun setCurrentFaculty(position: Int){
        if(listOfFaculty.value==null || position<0 ||
            (listOfFaculty.value?.size!!<=position))
            return
        setCurrentFaculty(listOfFaculty.value!![position])
    }

    fun setCurrentFaculty(_faculty: CarModel){
        faculty.postValue(_faculty)
    }

    fun saveData(){
//        val context = ListApp4PM_1_2425.context
//        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
//        sharedPreferences.edit().apply{
//            val gson = Gson()
//            val lst=listOfFaculty.value?.items ?: listOf<Faculty>()
//            val jsonString = gson.toJson(lst)
//            putString(context.getString(R.string.preference_key_faculty_list), jsonString)
//            putString(context.getString(R.string.preference_key_group_list),
//                gson.toJson(listOfStudent.value?.items ?: listOf<Group>()))
//            putString(context.getString(R.string.preference_key_student_list),
//                gson.toJson(listOfStudent.value?.items ?: listOf<Student>()))
//            apply()
//        }
    }

    fun loadData(){
//        val context = ListApp4PM_1_2425.context
//        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
//        sharedPreferences.apply{
//            val jsonString = getString(context.getString(R.string.preference_key_faculty_list), null)
//            if (jsonString!=null) {
//                val listType = object: TypeToken<List<Faculty>>() {}.type
//                val tempList = Gson().fromJson<List<Faculty>>(jsonString, listType)
//                val temp = ListOfFaculty()
//                temp.items = tempList.toMutableList()
//                listOfFaculty.postValue(temp)
//            }
//            val jsonStringG = getString(context.getString(R.string.preference_key_group_list), null)
//            if (jsonStringG!=null) {
//                val listTypeG = object: TypeToken<List<Group>>() {}.type
//                val tempListG = Gson().fromJson<List<Group>>(jsonStringG, listTypeG)
//                val tempG = ListOfGroup()
//                tempG.items = tempListG.toMutableList()
//                listOfGroup.postValue(tempG)
//            }
//            val jsonStringS = getString(context.getString(R.string.preference_key_student_list), null)
//            if (jsonStringS!=null) {
//                val listTypeS = object: TypeToken<List<Student>>() {}.type
//                val tempListS = Gson().fromJson<List<Student>>(jsonStringS, listTypeS)
//                val tempS = ListOfStudent()
//                tempS.items = tempListS.toMutableList()
//                listOfStudent.postValue(tempS)
//            }
//        }
    }

//    fun addGroup(group: Group){
//        val listTmp = (listOfGroup.value ?: ListOfGroup()).apply{
//            items.add(group)
//        }
//        listOfGroup.postValue(listTmp)
//        setCurrentGroup(group)
//    }

    fun getGroupPosition(catalog: Catalog): Int = listOfCatalog.value?.indexOfFirst {
        it.id==catalog.id} ?:-1

    fun getGroupPosition()=getGroupPosition(catalog.value?: Catalog())

    fun setCurrentGroup(position: Int){
        if(listOfCatalog.value==null || position<0 ||
            (listOfCatalog.value?.size!!<=position))
            return
        setCurrentGroup(listOfCatalog.value!![position])
    }

    fun setCurrentGroup(_catalog: Catalog){
        catalog.postValue(_catalog)
    }

//    fun updateGroup(group: Group){
//        val position = getGroupPosition(group)
//        if (position<0) addGroup(group)
//        else {
//            val listTmp = listOfGroup.value!!
//            listTmp.items[position]=group
//            listOfGroup.postValue(listTmp)
//        }
//    }
//
//    fun deleteGroup(group: Group){
//        val listTmp = listOfGroup.value ?: ListOfGroup()
//        if (listTmp.items.remove(group))
//            listOfGroup.postValue(listTmp)
//        setCurrentGroup(0)
//    }

    val facultyGroups
        get()=listOfCatalog.value?.filter{it.carModelID == (faculty.value?.id ?: 0)}?.sortedBy { it.name } ?: listOf()

    fun getFacultyGroups(facultyID: UUID) =
        (listOfCatalog.value?.filter{ it.carModelID == facultyID }?.sortedBy { it.name } ?: listOf())

//    fun addStudent(student: Student){
//        val listTmp = (listOfStudent.value ?: ListOfStudent()).apply {
//            items.add(student)
//        }
//        listOfStudent.postValue(listTmp)
//        setCurrentStudent(student)
//    }

    fun getStudentPosition(sparepart: Sparepart): Int = listOfSparepart.value?.indexOfFirst {
        it.id==sparepart.id} ?:-1

    fun getStudentPosition()=getStudentPosition(sparepart.value?: Sparepart())

    fun setCurrentStudent(position: Int){
        if(listOfSparepart.value==null || position<0 ||
            (listOfSparepart.value?.size!!<=position))
            return
        setCurrentStudent(listOfSparepart.value!![position])
    }

    fun setCurrentStudent(_sparepart: Sparepart){
        sparepart.postValue(_sparepart)
    }

//    fun updateStudent(student: Student){
//        val position = getStudentPosition(student)
//        if (position<0) addStudent(student)
//        else {
//            val listTmp = listOfStudent.value!!
//            listTmp.items[position]=student
//            listOfStudent.postValue(listTmp)
//        }
//    }
//
//    fun deleteStudent(student: Student){
//        val listTmp = listOfStudent.value ?: ListOfStudent()
//        if (listTmp.items.remove(student))
//            listOfStudent.postValue(listTmp)
//        setCurrentStudent(0)
//    }

    val groupStudents
        get()=listOfSparepart.value?.filter{it.catalogID == (catalog.value?.id ?: 0)}?.sortedBy { it.shortName } ?: listOf()

    fun getGroupStudents(groupID: UUID) =
        (listOfSparepart.value?.filter{ it.catalogID == groupID }?.sortedBy { it.shortName } ?: listOf())


    private val listDB by lazy { OfflineDBRepository(ListDatabase.getDatabase(ListApp4PM_1_2425.context).listDAO()) }

    private val myCoroutineScope = CoroutineScope(Dispatchers.Main)

    fun onDestroy(){
        myCoroutineScope.cancel()
    }

    val listOfFaculty : LiveData<List<CarModel>> = listDB.getCarModel()
        .asLiveData()

    fun addFaculty(faculty: CarModel){
        myCoroutineScope.launch {
            listDB.insertCarModel(faculty)
            setCurrentFaculty(faculty)
        }
    }

    fun updateFaculty(faculty: CarModel){
        addFaculty(faculty)
    }

    fun deleteFaculty(faculty: CarModel){
        myCoroutineScope.launch {
            listDB.deleteCarModel(faculty)
            setCurrentFaculty(0)
        }
    }

    val listOfCatalog: LiveData<List<Catalog>> = listDB.getAllCatalogs().asLiveData()

    fun addGroup(catalog: Catalog){
        myCoroutineScope.launch {
            listDB.insertCatalog(catalog)
            setCurrentGroup(catalog)
        }
    }

    fun updateGroup(catalog: Catalog){
        addGroup(catalog)
    }

    fun deleteGroup(catalog: Catalog){
        myCoroutineScope.launch {
            listDB.deleteCatalog(catalog)
            setCurrentGroup(0)
        }
    }

    val listOfSparepart: LiveData<List<Sparepart>> = listDB.getAllSpareparts().asLiveData()

    fun addStudent(sparepart: Sparepart){
        myCoroutineScope.launch {
            listDB.insertSparepart(sparepart)
            setCurrentStudent(sparepart)
        }

    }

    fun updateStudent(sparepart: Sparepart){
        addStudent(sparepart)
    }

    fun deleteStudent(sparepart: Sparepart){
        myCoroutineScope.launch {
            listDB.deleteSparepart(sparepart)
            setCurrentStudent(0)
        }
    }
}