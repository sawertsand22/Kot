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
            if (INSTANCE == null) INSTANCE = CatalogFragment()
            return INSTANCE ?: throw Exception("GroupFragment не создан")
        }

        fun newInstance(): CatalogFragment {
            INSTANCE = CatalogFragment()
            return INSTANCE!!
        }
    }
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
        // Дополнительная настройка ViewModel может быть добавлена здесь
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCatalogBinding.inflate(inflater, container, false)
        return binding.root
    }

    private inner class GroupPageAdapter(
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

        btnLoginRegister = view.findViewById(R.id.btnLoginRegister)
        btnLogout = view.findViewById(R.id.btnLogout)
        val isAuthorized = isUserAuthorized()
        updBtn()
        // Наблюдение за изменениями в списке групп
        viewModel.catalogList.observe(viewLifecycleOwner) { groupList ->
            createUI(groupList)

            if (groupList.isNotEmpty()) {
                val selectedGroup = groupList[tabPosition]
                sparePartsViewModel.set_Group(selectedGroup)
            }

        }
        val btnLogout: Button? = view.findViewById(R.id.btnLogout)
        btnLogout?.setOnClickListener {
            SessionManager.saveUserState(requireContext(), false)
            Toast.makeText(context, "Вы вышли из аккаунта", Toast.LENGTH_SHORT).show()

            //logoutUser()
            updBtn()  // Обновление состояния кнопок после выхода
            // Переход к экрану входа
            activity?.supportFragmentManager?.beginTransaction()
                ?.replace(R.id.fcvMain, CatalogFragment())
                ?.commit()
        }
        // Настройка поиска
        binding.etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                sparePartsViewModel.filterStudentsByName(s.toString()) // Изменено: вызов метода фильтрации
            }
        })


        btnLoginRegister = view.findViewById(R.id.btnLoginRegister)

        btnLoginRegister.setOnClickListener {
            // Открытие фрагмента регистрации
            val transaction = activity?.supportFragmentManager?.beginTransaction()
            transaction?.replace(R.id.fcvMain, RegisterFragment()) // Замена текущего фрагмента на RegisterFragment
            transaction?.addToBackStack(null)
            transaction?.commit()

            updBtn()
        }
        //openLoginRegisterFragment()

           //binding.tlGroups.setOnLongClickListener {
           // showSortDialog()
          //  true
       // }
        activityCallback?.newTitle(viewModel.faculty?.name ?: "")



    }

   //private fun showSortDialog() {
   //    val items = arrayOf("По имени", "По дате", "По другому атрибуту")
   //    AlertDialog.Builder(requireContext())
   //        .setTitle("Сортировать по:")
   //        .setItems(items) { dialog, which ->
   //            when (which) {
   //                0 -> sortByName()
   //                1 -> sortByDate()
   //                2 -> sortByAnotherAttribute()
   //            }
   //        }
   //        .show()
   //}

   // private fun sortByName() {
   //     val sortedGroups = viewModel.groupList.value?.sortedBy { it.name } ?: emptyList()
   //     createUI(sortedGroups)
   // }
//
   // private fun sortByDate() {
   //     // Предположим, что у вас есть поле date в классе Group
   //     val sortedGroups = viewModel.groupList.value?.sortedBy { it.name } ?: emptyList()
   //     createUI(sortedGroups)
   // }
//
   // private fun sortByAnotherAttribute() {
   //     // Реализуйте сортировку по другому атрибуту
   //     val sortedGroups = viewModel.groupList.value?.sortedBy { it.facultyID } ?: emptyList()
   //     createUI(sortedGroups)
   // }



    private fun updBtn()
    {val isAuthorized = isUserAuthorized()
        if (isAuthorized) {
            btnLoginRegister.visibility = View.GONE
            btnLogout.visibility = View.VISIBLE
        } else {
            btnLoginRegister.visibility = View.VISIBLE
            btnLogout.visibility = View.GONE
        }
    }


    private fun isUserAuthorized(): Boolean {
        val sharedPrefs = requireContext().getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)
        return sharedPrefs.getBoolean("isAuthorized", false)
    }


    private fun openLoginRegisterFragment() {
        val loginFragment = RegisterFragment()
        activity?.supportFragmentManager?.beginTransaction()
            ?.replace(R.id.fcvMain, loginFragment)
            ?.addToBackStack(null)
            ?.commit()
    }

    private fun logoutUser() {

        val sharedPrefs = requireContext().getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)
        val editor = sharedPrefs.edit()
        editor.putBoolean("isAuthorized", false)
        editor.apply()

        // Переход к экрану входа
        activity?.supportFragmentManager?.beginTransaction()
            ?.replace(R.id.fcvMain, LoginFragment())
            ?.commit()

        Toast.makeText(context, "Вы вышли из аккаунта", Toast.LENGTH_SHORT).show()
    }


    private fun createUI(catalogList: List<Catalog>) {
        binding.tlGroups.clearOnTabSelectedListeners()
        binding.tlGroups.removeAllTabs()


        catalogList.forEach { group ->
            binding.tlGroups.addTab(binding.tlGroups.newTab().apply { text = group.name })
        }


        val adapter = GroupPageAdapter(requireActivity(), catalogList)
        binding.vpSpareParts.adapter = adapter
        TabLayoutMediator(binding.tlGroups, binding.vpSpareParts) { tab, pos ->
            tab.text = catalogList[pos].name
        }.attach()

        // Сохранение позиции
        tabPosition = viewModel.getGroupListPosition.coerceAtLeast(0)
        viewModel.setCurrentGroup(tabPosition)
        binding.tlGroups.selectTab(binding.tlGroups.getTabAt(tabPosition), true)


        binding.tlGroups.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                tabPosition = tab?.position!!
                viewModel.setCurrentGroup(catalogList[tabPosition])

                //sparePartsViewModel.set_Group(groupList[tabPosition])

                val selectedGroup = catalogList[tabPosition]
                sparePartsViewModel.set_Group(selectedGroup)
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {
                sortGroups()
            }
        })
    }

    private fun searchGroups(query: String) {
        val filteredGroups = viewModel.catalogList.value?.filter {
            it.name.contains(query, ignoreCase = true)
        } ?: emptyList()
        createUI(filteredGroups)
    }

    private fun sortGroups() {
        val sortedGroups = viewModel.catalogList.value?.sortedBy { it.name } ?: emptyList()
        createUI(sortedGroups)
    }

    override fun append() {
        editGroup()
    }

    override fun update() {
        editGroup(viewModel.group?.name ?: "")
    }

    override fun delete() {
        deleteDialog()
    }

    private fun deleteDialog() {
        if (!isUserAuthorized()) {
            Toast.makeText(requireContext(), "Требуется авторизация для изменения модели", Toast.LENGTH_SHORT).show()
            return
        }
        if (viewModel.group == null) return
        AlertDialog.Builder(requireContext())
            .setTitle("Удаление!")
            .setMessage("Вы действительно хотите удалить группу ${viewModel.group?.name ?: ""}?")
            .setPositiveButton("ДА") { _, _ ->
                viewModel.deleteGroup()
            }
            .setNegativeButton("НЕТ", null)
            .setCancelable(true)
            .create()
            .show()
    }

    private fun editGroup(groupName: String = "") {
        if (!isUserAuthorized()) {
            Toast.makeText(requireContext(), "Требуется авторизация для изменения модели", Toast.LENGTH_SHORT).show()
            return
        }
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_string, null)
        val messageText = dialogView.findViewById<TextView>(R.id.tvInfo)
        val inputString = dialogView.findViewById<EditText>(R.id.etString)
        inputString.setText(groupName)
        messageText.text = "Укажите наименование группы"

        AlertDialog.Builder(requireContext())
            .setTitle("ИЗМЕНЕНИЕ ДАННЫХ")
            .setView(dialogView)
            .setPositiveButton("подтверждаю") { _, _ ->
                Log.d("Info", inputString.text.toString())
                if (inputString.text.isNotBlank()) {
                    if (groupName.isBlank())
                        viewModel.appendGroup(inputString.text.toString())
                    else
                        viewModel.updateGroup(inputString.text.toString())
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
