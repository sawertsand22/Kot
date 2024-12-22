package com.example.list_4pm2_2425.app_view_models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.list_4pm2_2425.data.Group
import com.example.list_4pm2_2425.data.Student
import com.example.list_4pm2_2425.repository.AppRepository

class StudentsViewModel : ViewModel() {
    var studentList: MutableLiveData<List<Student>> = MutableLiveData()
    private var _student: Student? = null
    val student
        get()=_student

    lateinit var group : Group

    fun set_Group(group: Group){
        this.group = group
        AppRepository.getInstance().listOfStudent.observeForever{
            studentList.postValue(AppRepository.getInstance().getGroupStudents(group.id))
        }
        AppRepository.getInstance().student.observeForever{
            _student = it
        }
    }

    fun deleteStudent(){
        if(student != null)
            AppRepository.getInstance().deleteStudent(student!!)
    }

    fun setCurrentStudent(student: Student){
        AppRepository.getInstance().setCurrentStudent(student)
    }


}