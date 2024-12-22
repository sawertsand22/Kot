package com.example.list_4pm2_2425.fragments

import android.app.AlertDialog
import android.content.Context
import androidx.fragment.app.viewModels
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.list_4pm2_2425.MainActivity
import com.example.list_4pm2_2425.R
import com.example.list_4pm2_2425.app_view_models.GroupViewModel
import com.example.list_4pm2_2425.data.Group
import com.example.list_4pm2_2425.databinding.FragmentGroupBinding
import com.example.list_4pm2_2425.interfaces.ActivityCallbacks
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import java.lang.Exception

class GroupFragment : Fragment(), MainActivity.Edit {

    companion object {
        private var INSTANCE: GroupFragment? = null
        fun getInstance(): GroupFragment{
            if (INSTANCE == null) INSTANCE = GroupFragment()
            return INSTANCE ?: throw Exception("GroupFragment не создан")
        }
        fun newInstance(): GroupFragment{
            INSTANCE = GroupFragment()
            return INSTANCE!!
        }
    }

    private var tabPosition: Int = 0
    private lateinit var _binding: FragmentGroupBinding
    private val binding get() = _binding!!

    private val viewModel: GroupViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // TODO: Use the ViewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGroupBinding.inflate(inflater, container, false)
        return binding.root
    }

    private inner class GroupPageAdapter(fa: FragmentActivity, private val groups: List<Group>?): FragmentStateAdapter(fa) {
        override fun getItemCount(): Int {
            return (groups?.size ?: 0)
        }

        override fun createFragment(position: Int): Fragment {
            return StudentsFragment.newInstance(groups!![position])
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.groupList.observe(viewLifecycleOwner
        ) {
            createUI(it)
        }
        activityCallback?.newTitle(viewModel.faculty?.name ?: "")
    }

    private fun createUI(groupList: List<Group>){
        binding.tlGroups.clearOnTabSelectedListeners()
        binding.tlGroups.removeAllTabs()

        for (i in 0 until (groupList.size)){
            binding.tlGroups.addTab(binding.tlGroups.newTab().apply{
                text = groupList.get(i).name
            })
        }
        val adapter = GroupPageAdapter(requireActivity(), viewModel.groupList.value)
        binding.vpStudents.adapter = adapter
        TabLayoutMediator(binding.tlGroups, binding.vpStudents, true, true){
                tab, pos ->
            tab.text = groupList.get(pos).name
        }.attach()
        tabPosition=0
        if (viewModel.group != null)
            tabPosition = if(viewModel.getGroupListPosition>=0)
                viewModel.getGroupListPosition
            else
                0
        viewModel.setCurrentGroup(tabPosition)
        binding.tlGroups.selectTab(binding.tlGroups.getTabAt(tabPosition), true)

        binding.tlGroups.addOnTabSelectedListener(object: TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab?) {
                tabPosition = tab?.position!!
                viewModel.setCurrentGroup(groupList[tabPosition])
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }
        })
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

    private fun deleteDialog(){
        if (viewModel.group == null) return
        AlertDialog.Builder(requireContext())
            .setTitle("Удаление!")
            .setMessage("Вы действительно хотите удалить группу ${viewModel.group?.name ?: ""}?")
            .setPositiveButton("ДА") {_, _ ->
                viewModel.deleteGroup()
            }
            .setNegativeButton("НЕТ", null)
            .setCancelable(true)
            .create()
            .show()
    }

    private fun editGroup(groupName: String=""){
        val mDialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_string, null)
        val messageText = mDialogView.findViewById<TextView>(R.id.tvInfo)
        val inputString = mDialogView.findViewById<EditText>(R.id.etString)
        inputString.setText(groupName)
        messageText.text="Укажите наименование группы"

        AlertDialog.Builder(requireContext())
            .setTitle("ИЗМЕНЕНИЕ ДАННЫХ")
            .setView(mDialogView)
            .setPositiveButton("подтверждаю") {_, _ ->
                Log.d("Info", inputString.text.toString())
                if (inputString.text.isNotBlank()){
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

    private var activityCallback: ActivityCallbacks? = null

    override fun onAttach(context: Context) {
        activityCallback = (context as ActivityCallbacks)
        super.onAttach(context)
    }
}