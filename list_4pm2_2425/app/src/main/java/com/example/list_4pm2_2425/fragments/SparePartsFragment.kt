package com.example.list_4pm2_2425.fragments

import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.list_4pm2_2425.R
import com.example.list_4pm2_2425.app_view_models.SparePartsViewModel
import com.example.list_4pm2_2425.data.Group
import com.example.list_4pm2_2425.data.NamesOfFragment
import com.example.list_4pm2_2425.data.Student
import com.example.list_4pm2_2425.databinding.FragmentSparepartsBinding
import com.example.list_4pm2_2425.interfaces.ActivityCallbacks
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SparePartsFragment : Fragment() {

    companion object {
        private lateinit var group: Group
        fun newInstance(group: Group): SparePartsFragment{
            this.group=group
            return SparePartsFragment()
        }
    }

    private lateinit var viewModel: SparePartsViewModel

    private lateinit var _binding: FragmentSparepartsBinding
    val binding
        get()=_binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // TODO: Use the ViewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSparepartsBinding.inflate(inflater, container, false)
        binding.rvSpareParts.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        return binding.root
        //return inflater.inflate(R.layout.fragment_students, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(SparePartsViewModel::class.java)
        viewModel.set_Group(group)
        viewModel.studentList.observe(viewLifecycleOwner){
            binding.rvSpareParts.adapter=StudentAdapter(it)
        }
        binding.fabAppendSparePart.setOnClickListener {
            editStudent(Student().apply { groupID = viewModel.group.id })
        }
    }

    private fun deleteDialog(){
        AlertDialog.Builder(requireContext())
            .setTitle("Удаление")
            .setMessage("Вы действительно хотите удалить запчасть ${viewModel.student?.shortName ?: ""}?")
            .setPositiveButton("Да"){_, _ ->
                viewModel.deleteStudent()
            }
            .setNegativeButton("Нет", null)
            .setCancelable(true)
            .create()
            .show()
    }

    private fun editStudent(student: Student){
        (requireActivity() as ActivityCallbacks).showFragment(NamesOfFragment.SPAREPART, student)
        (requireActivity() as ActivityCallbacks).newTitle("Группа ${viewModel.group.name}")
    }

    private inner class StudentAdapter(private val items: List<Student>):
        RecyclerView.Adapter<StudentAdapter.ItemHolder>() {
        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): StudentAdapter.ItemHolder {
            val view = layoutInflater.inflate(R.layout.element_sparepart_list, parent, false)
            return ItemHolder(view)
        }

        override fun getItemCount(): Int = items.size
        override fun onBindViewHolder(holder: StudentAdapter.ItemHolder, position: Int) {
            holder.bind(viewModel.studentList.value!![position])
        }
        private var lastView : View? = null
        private fun updateCurrentView(view: View){
            val ll = lastView?.findViewById<LinearLayout>(R.id.llSparePartButtons)
            ll?.visibility = View.INVISIBLE
            ll?.layoutParams=ll?.layoutParams.apply { this?.width=1 }
            val ib = lastView?.findViewById<ImageButton>(R.id.ibCall)
            ib?.visibility=View.INVISIBLE
            ib?.layoutParams=ib?.layoutParams.apply { this?.width=1 }

            lastView?.findViewById<ConstraintLayout>(R.id.clStudent)?.setBackgroundColor(
                ContextCompat.getColor(requireContext(), R.color.white)
            )
            view.findViewById<ConstraintLayout>(R.id.clStudent).setBackgroundColor(
                ContextCompat.getColor(requireContext(), R.color.myBlue)
            )
            view.findViewById<TextView>(R.id.tvSparePartName).setTextColor(
                ContextCompat.getColor(requireContext(), R.color.white)
            )
            lastView = view
        }

        private inner class ItemHolder(view: View):RecyclerView.ViewHolder(view){
            private lateinit var student: Student

            @OptIn(DelicateCoroutinesApi::class)
            fun bind(student: Student){
                this.student=student
                if(student==viewModel.student)
                    updateCurrentView(itemView)
                val tv = itemView.findViewById<TextView>(R.id.tvSparePartName)
                tv.text =student.longName
                val cl = itemView.findViewById<ConstraintLayout>(R.id.clStudent)
                cl.setOnClickListener {
                    tv.text =student.longName
                    viewModel.setCurrentStudent(student)
                    updateCurrentView(itemView)
                }
                itemView.findViewById<ImageButton>(R.id.ibEditSparePart).setOnClickListener {
                    editStudent(student)
                }

                itemView.findViewById<ImageButton>(R.id.ibDeleteSparePart).setOnClickListener {
                    deleteDialog()
                }

                val llb = itemView.findViewById<LinearLayout>(R.id.llSparePartButtons)
                llb.visibility = View.INVISIBLE
                llb?.layoutParams=llb?.layoutParams.apply { this?.width=1 }
                val ib = itemView.findViewById<ImageButton>(R.id.ibCall)
                ib.visibility = View.VISIBLE
                cl.setOnLongClickListener {
                    cl.callOnClick()
                    llb.visibility=View.VISIBLE
                    if(student.VIN?.isNotBlank() == true)
                        ib.visibility = View.VISIBLE
                    MainScope()
                        .launch {
                            tv.text = student.shortName
                            val lp=llb?.layoutParams
                            lp?.width = 1
                            val ip = ib.layoutParams
                            ip.width = 1
                            while(lp?.width!! < 350){
                                lp?.width = lp?.width!! + 35
                                llb?.layoutParams=lp
                                ip.width=ip.width+10
                                if(ib.visibility==View.VISIBLE)
                                    ib.layoutParams=ip
                                delay(50)
                            }
                        }
                    true
                }
                itemView.findViewById<ImageButton>(R.id.ibCall).setOnClickListener {
                    if(ContextCompat.checkSelfPermission(requireContext(),
                            Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED){
                        val intent = Intent(Intent.ACTION_CALL, Uri.parse("tel:${student.VIN}"))
                        startActivity(intent)
                    } else {
                        ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.CALL_PHONE), 2)
                    }
                }
            }
        }
    }
}