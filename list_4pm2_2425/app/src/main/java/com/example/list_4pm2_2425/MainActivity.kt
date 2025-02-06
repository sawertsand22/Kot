package com.example.list_4pm2_2425

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.activity.addCallback
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.list_4pm2_2425.data.NamesOfFragment
import com.example.list_4pm2_2425.data.Sparepart
import com.example.list_4pm2_2425.fragments.CarModelFragment
import com.example.list_4pm2_2425.fragments.CatalogFragment
import com.example.list_4pm2_2425.fragments.SparePartInfoFragment
import com.example.list_4pm2_2425.interfaces.ActivityCallbacks
import com.example.list_4pm2_2425.app_view_models.CatalogViewModel
import com.example.list_4pm2_2425.fragments.LoginFragment
import com.example.list_4pm2_2425.fragments.RegisterFragment
import com.example.list_4pm2_2425.utils.SessionManager

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
        checkUserState()


//        supportFragmentManager.addOnBackStackChangedListener {
//            // Обновляем кнопки авторизации при изменении back stack
//            updateAuthButtons()
//        }









        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        showFragment(NamesOfFragment.CARMODEL)

        onBackPressedDispatcher.addCallback(this) {
            if (supportFragmentManager.backStackEntryCount > 0) {

                supportFragmentManager.popBackStack()
                when (activeFragment){
                    NamesOfFragment.CARMODEL ->{
                        finish()
                    }
                    NamesOfFragment.CATALOG ->{
                        activeFragment=NamesOfFragment.CARMODEL
                    }
                    NamesOfFragment.REGISTER ->{
                        activeFragment=NamesOfFragment.CARMODEL
                    }
                    else -> {
                        activeFragment=NamesOfFragment.CATALOG
                    }
                }
                showFragment(activeFragment)
            }
            else {
                finish()
            }
        }
    }





    var activeFragment: NamesOfFragment = NamesOfFragment.CARMODEL

    private var _miAppendCarModel: MenuItem? = null
    private var _miUpdateCarModel: MenuItem? = null
    private var _miDeleteCarModel: MenuItem? = null
    private var _miAppendCatalog: MenuItem? = null
    private var _miUpdateCatalog: MenuItem? = null
    private var _miDeleteCatalog: MenuItem? = null

    private val catalogViewModel: CatalogViewModel by viewModels()

//    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
//        menuInflater.inflate(R.menu.main_menu, menu)
//        _miAppendCarModel = menu?.findItem(R.id.miNewCarModel)
//        _miUpdateCarModel = menu?.findItem(R.id.miUpdateCarModel)
//        _miDeleteCarModel = menu?.findItem(R.id.miDeleteCarModel)
//        _miAppendCatalog = menu?.findItem(R.id.miNewCatalog)
//        _miUpdateCatalog = menu?.findItem(R.id.miUpdateCatalog)
//        _miDeleteCatalog = menu?.findItem(R.id.miDeleteCatalog)
//
//
//        val currentFragment = supportFragmentManager.findFragmentById(R.id.fcvMain)
//
//        val isCatalogOrCarModel = currentFragment is CatalogFragment || currentFragment is CarModelFragment
//
//        // Показываем меню только если это CatalogFragment или CarModelFragment
//        menu?.setGroupVisible(0, isCatalogOrCarModel)
//
//        updateMenu(activeFragment)
//        return true
//    }
//
//    private fun updateMenu(fragmentType: NamesOfFragment){
//        _miAppendCarModel?.isVisible = fragmentType==NamesOfFragment.CARMODEL
//        _miUpdateCarModel?.isVisible = fragmentType==NamesOfFragment.CARMODEL
//        _miDeleteCarModel?.isVisible = fragmentType==NamesOfFragment.CARMODEL
//        _miAppendCatalog?.isVisible = fragmentType==NamesOfFragment.CATALOG
//        _miUpdateCatalog?.isVisible = fragmentType==NamesOfFragment.CATALOG
//        _miDeleteCatalog?.isVisible = fragmentType==NamesOfFragment.CATALOG
//    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)

        _miAppendCarModel = menu?.findItem(R.id.miNewCarModel)
        _miUpdateCarModel = menu?.findItem(R.id.miUpdateCarModel)
        _miDeleteCarModel = menu?.findItem(R.id.miDeleteCarModel)
        _miAppendCatalog = menu?.findItem(R.id.miNewCatalog)
        _miUpdateCatalog = menu?.findItem(R.id.miUpdateCatalog)
        _miDeleteCatalog = menu?.findItem(R.id.miDeleteCatalog)

        // Проверяем, какой фрагмент сейчас активен
        val currentFragment = supportFragmentManager.findFragmentById(R.id.fcvMain)

        when (currentFragment) {
            is CarModelFragment -> {
                showCarModelMenu(true)
                showCatalogMenu(false)
            }
            is CatalogFragment -> {
                showCarModelMenu(false)
                showCatalogMenu(true)
            }
            else -> {
                showCarModelMenu(false)
                showCatalogMenu(false)
            }
        }

        return true
    }
    private fun showCarModelMenu(isVisible: Boolean) {
        _miAppendCarModel?.isVisible = isVisible
        _miUpdateCarModel?.isVisible = isVisible
        _miDeleteCarModel?.isVisible = isVisible
    }

    private fun showCatalogMenu(isVisible: Boolean) {
        _miAppendCatalog?.isVisible = isVisible
        _miUpdateCatalog?.isVisible = isVisible
        _miDeleteCatalog?.isVisible = isVisible
    }



    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId){
            R.id.miNewCarModel -> {
                CarModelFragment.getInstance().append()
                true
            }
            R.id.miUpdateCarModel -> {
                CarModelFragment.getInstance().update()
                true
            }
            R.id.miDeleteCarModel -> {
                CarModelFragment.getInstance().delete()
                true
            }
            R.id.miNewCatalog -> {
                CatalogFragment.getInstance().append()
                true
            }
            R.id.miUpdateCatalog -> {
                CatalogFragment.getInstance().update()
                true
            }
            R.id.miDeleteCatalog -> {
                CatalogFragment.getInstance().delete()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun showAuthButtons(isVisible: Boolean) {
        val btnLogin = findViewById<Button>(R.id.btnLogin)
        val btnLogout = findViewById<Button>(R.id.btnLogout)

        if (isVisible) {
            updateAuthButtons() // Проверяем авторизацию и обновляем кнопки
        } else {
            btnLogin.visibility = View.GONE
            btnLogout.visibility = View.GONE
        }

        btnLogin.setOnClickListener {
            showFragment(NamesOfFragment.LOGIN) // Переход на экран регистрации
        }

        btnLogout.setOnClickListener {
            SessionManager.saveUserState(this, false) // Выход из аккаунта
            updateAuthButtons()
            Toast.makeText(this, "Вы вышли из аккаунта", Toast.LENGTH_SHORT).show()

        }
    }


    private fun updateAuthButtons() {
        val btnLogin = findViewById<Button>(R.id.btnLogin)
        val btnLogout = findViewById<Button>(R.id.btnLogout)
        val isAuthorized = SessionManager.isUserAuthorized(this)

        btnLogin.visibility = if (isAuthorized) View.GONE else View.VISIBLE
        btnLogout.visibility = if (isAuthorized) View.VISIBLE else View.GONE

        invalidateOptionsMenu() // 🔥 ОБНОВЛЯЕМ МЕНЮ ПОСЛЕ ИЗМЕНЕНИЙ
    }







    override fun onDestroy() {
        super.onDestroy()
    }

    override fun newTitle(_title: String) {
        title = _title
    }

    // Метод для отображения фрагмента
    override fun showFragment(fragmentType: NamesOfFragment, sparepart: Sparepart?) {
        when (fragmentType) {
            NamesOfFragment.CARMODEL -> {
                supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.fcvMain, CarModelFragment.getInstance())
                    .addToBackStack(null)
                    .commit()
                showAuthButtons(true) // Показываем кнопки только здесь
                //updateMenu(NamesOfFragment.CARMODEL)
                // Обновляем меню сразу

                invalidateOptionsMenu()
            }
            NamesOfFragment.CATALOG -> {
                supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.fcvMain, CatalogFragment.newInstance())
                    .addToBackStack(null)
                    .commit()
                showAuthButtons(false) // Скрываем кнопки
            }
            NamesOfFragment.LOGIN -> {
                supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.fcvMain, LoginFragment())
                    .addToBackStack(null)
                    .commit()
                showAuthButtons(false) // Скрываем кнопки
            }
            NamesOfFragment.REGISTER -> {
                supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.fcvMain, RegisterFragment())
                    .addToBackStack(null)
                    .commit()
                showAuthButtons(false) // Скрываем кнопки

            }
            NamesOfFragment.SPAREPART -> {
                if (catalogViewModel.catalog != null && sparepart != null) {
                    supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.fcvMain, SparePartInfoFragment.newInstance(sparepart))
                        .addToBackStack(null)
                        .commit()
                    showAuthButtons(false) // Скрываем кнопки
                }
            }
        }
        activeFragment = fragmentType
        //updateMenu(fragmentType)

    }

    private fun checkUserState() {
        val isLoggedIn = SessionManager.isUserAuthorized(this)
        val fragment = if (isLoggedIn) {
            CatalogFragment.getInstance() // Переход к основному экрану
        } else {
            LoginFragment() // Переход к экрану входа
        }

        supportFragmentManager.beginTransaction()
            .replace(R.id.fcvMain, fragment)
            .commit()
    }


}