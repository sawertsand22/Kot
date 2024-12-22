package com.example.list_4pm2_2425

import android.app.Application
import android.content.Context
import androidx.lifecycle.ViewModelProvider.NewInstanceFactory.Companion.instance
import com.example.list_4pm2_2425.repository.AppRepository

class ListApp4PM_1_2425: Application() {

    override fun onCreate() {
        super.onCreate()
        AppRepository.getInstance().loadData()
    }

    init {
        instance = this
    }

    companion object {
        private var instance: ListApp4PM_1_2425? = null

        val context
            get()= applicationContext()

        private fun applicationContext(): Context {
            return instance!!.applicationContext
        }
    }
}