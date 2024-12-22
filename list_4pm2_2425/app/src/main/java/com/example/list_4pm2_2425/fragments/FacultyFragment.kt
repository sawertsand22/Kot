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
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.list_4pm2_2425.MainActivity
import com.example.list_4pm2_2425.R
import com.example.list_4pm2_2425.app_view_models.FacultyViewModel
import com.example.list_4pm2_2425.data.Faculty
import com.example.list_4pm2_2425.data.NamesOfFragment
import com.example.list_4pm2_2425.databinding.FragmentFacultyBinding
import com.example.list_4pm2_2425.interfaces.ActivityCallbacks

class FacultyFragment : Fragment(), MainActivity.Edit {

    companion object {
        //fun newInstance() = FacultyFragment()
        private var INSTANCE: FacultyFragment? = null
        fun getInstance(): FacultyFragment {
            if (INSTANCE == null) INSTANCE = FacultyFragment()
            return INSTANCE ?: throw Exception("FacultyFragment не создан")
        }
    }

    private lateinit var viewModel: FacultyViewModel
    private lateinit var _binding: FragmentFacultyBinding
    val binding
        get()=_binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFacultyBinding.inflate(inflater, container, false)
        binding.rvFaculty.layoutManager=
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(FacultyViewModel::class.java)
        activityCallbacks?.newTitle("Список факультетов")
        viewModel.facultyList.observe(viewLifecycleOwner){
            binding.rvFaculty.adapter=FacultyAdapter(it?:emptyList())
        }
    }

    private inner class FacultyAdapter(private val items: List<Faculty>)
        : RecyclerView.Adapter<FacultyAdapter.ItemHolder>() {

        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): FacultyAdapter.ItemHolder{
            val view = layoutInflater.inflate(R.layout.element_faculty_list, parent, false)
            return ItemHolder(view)
        }
        override fun getItemCount(): Int = items.size
        override fun onBindViewHolder(holder: FacultyAdapter.ItemHolder, position: Int) {
            holder.bind(items[position])
        }

        private var lastView: View? = null
        private fun updateCurrentView(view: View){
            lastView?.findViewById<ConstraintLayout>(R.id.clFaculty)?.setBackgroundColor(
                ContextCompat.getColor(requireContext(), R.color.white)
            )
            lastView?.findViewById<TextView>(R.id.tvFaculty)?.setTextColor(
                ContextCompat.getColor(requireContext(), R.color.black)
            )
            view.findViewById<ConstraintLayout>(R.id.clFaculty)?.setBackgroundColor(
                ContextCompat.getColor(requireContext(), R.color.myBlue)
            )
            view.findViewById<TextView>(R.id.tvFaculty)?.setTextColor(
                ContextCompat.getColor(requireContext(), R.color.white)
            )
            lastView=view
        }

        private inner class ItemHolder(view: View)
            : RecyclerView.ViewHolder(view) {
                private lateinit var faculty : Faculty

                fun bind(faculty : Faculty) {
                    this.faculty=faculty
                    val tv = itemView.findViewById<TextView>(R.id.tvFaculty)
                    tv.text=faculty.name


                    val cl = View.OnClickListener {
                        viewModel.setCurrentFaculty(faculty)
                        updateCurrentView(itemView)
                    }

                    val icl=itemView.findViewById<ConstraintLayout>(R.id.clFaculty)
                    icl.setOnClickListener(cl)
                    icl.setBackgroundColor(
                        ContextCompat.getColor(requireContext(), R.color.white))

                    if (faculty==viewModel.faculty)
                        updateCurrentView(itemView)

                    icl.setOnLongClickListener {
                        icl.callOnClick()
                        activityCallbacks?.showFragment(NamesOfFragment.GROUP)
                        true
                    }

                }
            }
        }

    override fun append() {
        editFaculty()
    }

    override fun update() {
        editFaculty(viewModel.faculty?.name ?: "")
    }

    override fun delete() {
        deleteDialog()
    }

    private fun deleteDialog(){
        AlertDialog.Builder(requireContext())
            .setTitle("Удаление!")
            .setMessage("Вы действительно хотите удалить факультет ${viewModel.faculty?.name ?: ""}?")
            .setPositiveButton("ДА") {_, _ ->
                viewModel.deleteFaculty()
            }
            .setNegativeButton("НЕТ", null)
            .setCancelable(true)
            .create()
            .show()
    }

    private fun editFaculty(facultyName: String=""){
        val mDialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_string, null)
        val messageText = mDialogView.findViewById<TextView>(R.id.tvInfo)
        val inputString = mDialogView.findViewById<EditText>(R.id.etString)
        inputString.setText(facultyName)
        messageText.text="Укажите наименование факультета"

        AlertDialog.Builder(requireContext())
            .setTitle("ИЗМЕНЕНИЕ ДАННЫХ")
            .setView(mDialogView)
            .setPositiveButton("подтверждаю") {_, _ ->
                Log.d("Info", inputString.text.toString())
                if (inputString.text.isNotBlank()){
                    if (facultyName.isBlank())
                        viewModel.appendFaculty(inputString.text.toString())
                    else
                        viewModel.updateFaculty(inputString.text.toString())
                }
            }
            .setNegativeButton("отмена", null)
            .setCancelable(true)
            .create()
            .show()
    }

    private var activityCallbacks : ActivityCallbacks? = null
    override fun onAttach(context: Context) {
        activityCallbacks = context as ActivityCallbacks
        super.onAttach(context)
    }
}