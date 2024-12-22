package com.example.list_4pm2_2425.interfaces

import com.example.list_4pm2_2425.data.NamesOfFragment
import com.example.list_4pm2_2425.data.Student

interface ActivityCallbacks {
    fun newTitle(_title: String)
    fun showFragment(fragmentType: NamesOfFragment, student: Student? = null)
}