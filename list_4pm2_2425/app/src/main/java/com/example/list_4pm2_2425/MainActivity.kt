package com.example.list_4pm2_2425

import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.addCallback
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.list_4pm2_2425.data.NamesOfFragment
import com.example.list_4pm2_2425.data.Student
import com.example.list_4pm2_2425.fragments.FacultyFragment
import com.example.list_4pm2_2425.fragments.GroupFragment
import com.example.list_4pm2_2425.fragments.StudentInfoFragment
import com.example.list_4pm2_2425.fragments.StudentsFragment
import com.example.list_4pm2_2425.interfaces.ActivityCallbacks
import com.example.list_4pm2_2425.repository.AppRepository
import com.example.list_4pm2_2425.app_view_models.GroupViewModel

class MainActivity : AppCompatActivity(), ActivityCallbacks {

    interface Edit {
        fun append()
        fun update()
        fun delete()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        showFragment(NamesOfFragment.FACULTY)

        onBackPressedDispatcher.addCallback(this) {
            if (supportFragmentManager.backStackEntryCount > 0) {

                supportFragmentManager.popBackStack()
                when (activeFragment){
                    NamesOfFragment.FACULTY ->{
                        finish()
                    }
                    NamesOfFragment.GROUP ->{
                        activeFragment=NamesOfFragment.FACULTY
                    }
                    else -> {
                        activeFragment=NamesOfFragment.GROUP
                    }
                }
                showFragment(activeFragment)
            }
            else {
                finish()
            }
        }
    }
    var activeFragment: NamesOfFragment = NamesOfFragment.FACULTY

    private var _miAppendFaculty: MenuItem? = null
    private var _miUpdateFaculty: MenuItem? = null
    private var _miDeleteFaculty: MenuItem? = null
    private var _miAppendGroup: MenuItem? = null
    private var _miUpdateGroup: MenuItem? = null
    private var _miDeleteGroup: MenuItem? = null

    private val groupViewModel: GroupViewModel by viewModels()

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        _miAppendFaculty = menu?.findItem(R.id.miNewFaculty)
        _miUpdateFaculty = menu?.findItem(R.id.miUpdateFaculty)
        _miDeleteFaculty = menu?.findItem(R.id.miDeleteFaculty)
        _miAppendGroup = menu?.findItem(R.id.miNewGroup)
        _miUpdateGroup = menu?.findItem(R.id.miUpdateGroup)
        _miDeleteGroup = menu?.findItem(R.id.miDeleteGroup)
        updateMenu(activeFragment)
        return true
    }

    private fun updateMenu(fragmentType: NamesOfFragment){
        _miAppendFaculty?.isVisible = fragmentType==NamesOfFragment.FACULTY
        _miUpdateFaculty?.isVisible = fragmentType==NamesOfFragment.FACULTY
        _miDeleteFaculty?.isVisible = fragmentType==NamesOfFragment.FACULTY
        _miAppendGroup?.isVisible = fragmentType==NamesOfFragment.GROUP
        _miUpdateGroup?.isVisible = fragmentType==NamesOfFragment.GROUP
        _miDeleteGroup?.isVisible = fragmentType==NamesOfFragment.GROUP
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId){
            R.id.miNewFaculty -> {
                FacultyFragment.getInstance().append()
                true
            }
            R.id.miUpdateFaculty -> {
                FacultyFragment.getInstance().update()
                true
            }
            R.id.miDeleteFaculty -> {
                FacultyFragment.getInstance().delete()
                true
            }
            R.id.miNewGroup -> {
                GroupFragment.getInstance().append()
                true
            }
            R.id.miUpdateGroup -> {
                GroupFragment.getInstance().update()
                true
            }
            R.id.miDeleteGroup -> {
                GroupFragment.getInstance().delete()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun newTitle(_title: String) {
        title = _title
    }

    override fun showFragment(fragmentType: NamesOfFragment, student: Student?) {
        when (fragmentType){
            NamesOfFragment.FACULTY ->{
                supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.fcvMain, FacultyFragment.getInstance())
                    .addToBackStack(null)
                    .commit()
            }
            NamesOfFragment.GROUP ->{
                supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.fcvMain, GroupFragment.newInstance())
                    .addToBackStack(null)
                    .commit()
            }

            NamesOfFragment.STUDENT -> {
                if(groupViewModel.group != null && student != null){
                    supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.fcvMain, StudentInfoFragment.newInstance(student))
                        .addToBackStack(null)
                        .commit()
                }
            }
        }
        activeFragment=fragmentType
        updateMenu(fragmentType)
    }
}