package com.example.list_4pm2_2425

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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        _miAppendCarModel = menu?.findItem(R.id.miNewCarModel)
        _miUpdateCarModel = menu?.findItem(R.id.miUpdateCarModel)
        _miDeleteCarModel = menu?.findItem(R.id.miDeleteCarModel)
        _miAppendCatalog = menu?.findItem(R.id.miNewCatalog)
        _miUpdateCatalog = menu?.findItem(R.id.miUpdateCatalog)
        _miDeleteCatalog = menu?.findItem(R.id.miDeleteCatalog)
        updateMenu(activeFragment)
        return true
    }

    private fun updateMenu(fragmentType: NamesOfFragment){
        _miAppendCarModel?.isVisible = fragmentType==NamesOfFragment.CARMODEL
        _miUpdateCarModel?.isVisible = fragmentType==NamesOfFragment.CARMODEL
        _miDeleteCarModel?.isVisible = fragmentType==NamesOfFragment.CARMODEL
        _miAppendCatalog?.isVisible = fragmentType==NamesOfFragment.CATALOG
        _miUpdateCatalog?.isVisible = fragmentType==NamesOfFragment.CATALOG
        _miDeleteCatalog?.isVisible = fragmentType==NamesOfFragment.CATALOG
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
            }
            NamesOfFragment.CATALOG -> {
                supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.fcvMain, CatalogFragment.newInstance())
                    .addToBackStack(null)
                    .commit()
            }
            NamesOfFragment.LOGIN -> {
                supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.fcvMain, LoginFragment())
                    .addToBackStack(null)
                    .commit()
            }
            NamesOfFragment.REGISTER -> {
                supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.fcvMain, RegisterFragment())
                    .addToBackStack(null)
                    .commit()
            }
            NamesOfFragment.SPAREPART -> {
                if (catalogViewModel.catalog != null && sparepart != null) {
                    supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.fcvMain, SparePartInfoFragment.newInstance(sparepart))
                        .addToBackStack(null)
                        .commit()
                }
            }
        }
        activeFragment = fragmentType
        updateMenu(fragmentType)
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