package com.example.list_4pm2_2425.fragments

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Button
import android.widget.Toast

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.list_4pm2_2425.MainActivity
import com.example.list_4pm2_2425.R
import com.example.list_4pm2_2425.app_view_models.CatalogViewModel
import com.example.list_4pm2_2425.data.Catalog
import com.example.list_4pm2_2425.databinding.FragmentCatalogBinding
import com.example.list_4pm2_2425.interfaces.ActivityCallbacks
import com.example.list_4pm2_2425.utils.SessionManager
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import java.lang.Exception
import com.example.list_4pm2_2425.app_view_models.SparePartsViewModel

class CatalogFragment : Fragment(), MainActivity.Edit {

    companion object {
        private var INSTANCE: CatalogFragment? = null
        fun getInstance(): CatalogFragment {
            val existingFragment = INSTANCE
            if (existingFragment != null) {
                return existingFragment
            }

            val newFragment = CatalogFragment()
            INSTANCE = newFragment
            return newFragment
        }

        fun newInstance(): CatalogFragment {
            INSTANCE = CatalogFragment()
            return INSTANCE!!
        }
    }
    private var currentSearchQuery: String = ""



    private lateinit var btnLoginRegister: Button
    private var tabPosition: Int = 0

    private lateinit var btnLogout: Button

    private lateinit var _binding: FragmentCatalogBinding
    private val binding get() = _binding!!

    private val viewModel: CatalogViewModel by viewModels()
    private var activityCallback: ActivityCallbacks? = null
    private val sparePartsViewModel: SparePartsViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCatalogBinding.inflate(inflater, container, false)
        return binding.root
    }

    private inner class CatalogPageAdapter(
        fa: FragmentActivity,
        private val catalogs: List<Catalog>
    ) : FragmentStateAdapter(fa) {
        override fun getItemCount(): Int {
            return catalogs.size ?: 0
        }

        override fun createFragment(position: Int): Fragment {
            return SparePartsFragment.newInstance(catalogs!![position])
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)








        viewModel.catalogList.observe(viewLifecycleOwner) { catalogList ->
            createUI(catalogList)

            if (catalogList.isNotEmpty()) {
                val selectedCatalog = catalogList[tabPosition]
                sparePartsViewModel.set_Catalog(selectedCatalog)
            }

        }
//
        activityCallback?.newTitle(viewModel.carModel?.name ?: "")



    }







    private fun isUserAuthorized(): Boolean {
        val sharedPrefs = requireContext().getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)
        return sharedPrefs.getBoolean("isAuthorized", false)
    }




    private fun createUI(catalogList: List<Catalog>) {
        binding.tlCatalogs.clearOnTabSelectedListeners()
        binding.tlCatalogs.removeAllTabs()


        catalogList.forEach { catalog ->
            binding.tlCatalogs.addTab(binding.tlCatalogs.newTab().apply { text = catalog.name })
        }


        val adapter = CatalogPageAdapter(requireActivity(), catalogList)
        binding.vpSpareParts.adapter = adapter
        TabLayoutMediator(binding.tlCatalogs, binding.vpSpareParts) { tab, pos ->
            tab.text = catalogList[pos].name
        }.attach()

        // Сохранение позиции
        tabPosition = viewModel.getCatalogListPosition.coerceAtLeast(0)
        viewModel.setCurrentCatalog(tabPosition)
        binding.tlCatalogs.selectTab(binding.tlCatalogs.getTabAt(tabPosition), true)


        binding.tlCatalogs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                tabPosition = tab?.position!!
                viewModel.setCurrentCatalog(catalogList[tabPosition])

                //sparePartsViewModel.set_Group(groupList[tabPosition])

                val selectedCatalog = catalogList[tabPosition]
                sparePartsViewModel.set_Catalog(selectedCatalog)
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {
                sortCatalogs()
            }
        })
    }



    private fun sortCatalogs() {
        val sortedCatalogs = viewModel.catalogList.value?.sortedBy { it.name } ?: emptyList()
        createUI(sortedCatalogs)
    }

    override fun append() {
        editCatalog()
    }

    override fun update() {
        editCatalog(viewModel.catalog?.name ?: "")
    }

    override fun delete() {
        deleteDialog()
    }

    private fun deleteDialog() {
        if (!isUserAuthorized()) {
            Toast.makeText(requireContext(), "Требуется авторизация для изменения", Toast.LENGTH_SHORT).show()
            return
        }
        if (viewModel.catalog == null) return
        AlertDialog.Builder(requireContext())
            .setTitle("Удаление!")
            .setMessage("Вы действительно хотите удалить группу ${viewModel.catalog?.name ?: ""}?")
            .setPositiveButton("ДА") { _, _ ->
                viewModel.deleteCatalog()
            }
            .setNegativeButton("НЕТ", null)
            .setCancelable(true)
            .create()
            .show()
    }

    private fun editCatalog(catalogName: String = "") {
        if (!isUserAuthorized()) {
            Toast.makeText(requireContext(), "Требуется авторизация для изменения", Toast.LENGTH_SHORT).show()
            return
        }
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_string, null)
        val messageText = dialogView.findViewById<TextView>(R.id.tvInfo)
        val inputString = dialogView.findViewById<EditText>(R.id.etString)
        inputString.setText(catalogName)
        messageText.text = "Укажите наименование группы"

        AlertDialog.Builder(requireContext())
            .setTitle("ИЗМЕНЕНИЕ ДАННЫХ")
            .setView(dialogView)
            .setPositiveButton("подтверждаю") { _, _ ->
                Log.d("Info", inputString.text.toString())
                if (inputString.text.isNotBlank()) {
                    if (catalogName.isBlank())
                        viewModel.appendCatalog(inputString.text.toString())
                    else
                        viewModel.updateCatalog(inputString.text.toString())
                }
            }
            .setNegativeButton("отмена", null)
            .setCancelable(true)
            .create()
            .show()
    }



    override fun onAttach(context: Context) {
        activityCallback = context as ActivityCallbacks
        super.onAttach(context)
    }
}
