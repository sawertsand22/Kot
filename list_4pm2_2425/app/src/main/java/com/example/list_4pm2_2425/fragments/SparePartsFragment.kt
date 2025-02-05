package com.example.list_4pm2_2425.fragments

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.list_4pm2_2425.R
import com.example.list_4pm2_2425.app_view_models.SparePartsViewModel
import com.example.list_4pm2_2425.data.Catalog
import com.example.list_4pm2_2425.data.NamesOfFragment
import com.example.list_4pm2_2425.data.Sparepart
import com.example.list_4pm2_2425.databinding.FragmentSparepartsBinding
import com.example.list_4pm2_2425.interfaces.ActivityCallbacks
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class SparePartsFragment : Fragment() {

    companion object {
        private lateinit var catalog: Catalog
        fun newInstance(catalog: Catalog): SparePartsFragment{
            this.catalog=catalog
            return SparePartsFragment()
        }
    }
    private lateinit var studentAdapter: StudentAdapter  // Объявляем адаптер
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
        viewModel.set_Group(catalog)

        studentAdapter = StudentAdapter(emptyList()) // Инициализируем адаптер ПУСТЫМ списком
        binding.rvSpareParts.adapter = studentAdapter


        viewModel.sparepartList.observe(viewLifecycleOwner) { students ->
            Log.d("FragmentObserve", "Students list updated in fragment: ${students.size}, first item: ${students.firstOrNull()?.sparePartName}")
            if (students != null) {
                Log.d("FragmentObserve", "Students list is not null")
                studentAdapter.updateData(students)
            } else {
                Log.d("FragmentObserve", "Students list is null")
            }
        }

        binding.fabAppendSparePart.setOnClickListener {
            editStudent(Sparepart().apply { catalogID = viewModel.catalog?.id })
        }
    }

    private fun isUserAuthorized(): Boolean {
        val sharedPrefs = requireContext().getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)
        return sharedPrefs.getBoolean("isAuthorized", false)
    }

    private fun deleteDialog(){
        if (!isUserAuthorized()) {
            Toast.makeText(requireContext(), "Требуется авторизация для изменения модели", Toast.LENGTH_SHORT).show()
            return
        }
        AlertDialog.Builder(requireContext())
            .setTitle("Удаление")
            .setMessage("Вы действительно хотите удалить запчасть ${viewModel.student?.manufacturer ?: ""}?")
            .setPositiveButton("Да"){_, _ ->
                viewModel.deleteStudent()
            }
            .setNegativeButton("Нет", null)
            .setCancelable(true)
            .create()
            .show()
    }

    private fun editStudent(sparepart: Sparepart){
        (requireActivity() as ActivityCallbacks).showFragment(NamesOfFragment.SPAREPART, sparepart)
        (requireActivity() as ActivityCallbacks).newTitle("Группа ${viewModel.catalog?.name}")
    }

    private inner class StudentAdapter(private var items: List<Sparepart>) :
        RecyclerView.Adapter<StudentAdapter.ItemHolder>() {

        inner class StudentDiffCallback(private val oldList: List<Sparepart>, private val newList: List<Sparepart>) : DiffUtil.Callback() {
            override fun getOldListSize(): Int = oldList.size
            override fun getNewListSize(): Int = newList.size

            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                val oldItem = oldList[oldItemPosition]
                val newItem = newList[newItemPosition]
                return oldItem.id == newItem.id // Or your unique ID comparison
            }

            override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                val oldItem = oldList[oldItemPosition]
                val newItem = newList[newItemPosition]
                return oldItem == newItem // Or compare relevant content properties
            }
        }


        fun updateData(newItems: List<Sparepart>) {
            // 1. Создаем новый список для элементов RecyclerView
            val newList = newItems.map { it.copy() } // Глубокое копирование

            // 2. Сравниваем старый и новый списки с помощью DiffUtil
            val diffResult = DiffUtil.calculateDiff(object : DiffUtil.Callback() {
                override fun getOldListSize(): Int = items.size
                override fun getNewListSize(): Int = newList.size

                override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                    val oldItem = items[oldItemPosition]
                    val newItem = newList[newItemPosition]
                    return oldItem.id == newItem.id // Сравниваем ID
                }

                override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                    val oldItem = items[oldItemPosition]
                    val newItem = newList[newItemPosition]
                    return oldItem == newItem // Сравниваем содержимое (можно заменить на более точное сравнение полей)
                }
            })

            // 3. Обновляем список items и применяем изменения с помощью DiffUtil
            items = newList // Присваиваем НОВЫЙ список
            diffResult.dispatchUpdatesTo(this)
        }


        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
            val view = layoutInflater.inflate(R.layout.element_sparepart_list, parent, false)
            return ItemHolder(view)
        }

        override fun getItemCount(): Int = items.size

        override fun onBindViewHolder(holder: ItemHolder, position: Int) {
            holder.bind(items[position])
        }

        private var lastView: View? = null

        private fun updateCurrentView(view: View) {
            val ll = lastView?.findViewById<LinearLayout>(R.id.llSparePartButtons)
            ll?.visibility = View.INVISIBLE
            ll?.layoutParams = ll?.layoutParams?.apply { this?.width = 1 }
            lastView?.findViewById<ConstraintLayout>(R.id.clStudent)?.setBackgroundColor(
                ContextCompat.getColor(requireContext(), R.color.white)
            )
            view.findViewById<ConstraintLayout>(R.id.clStudent).setBackgroundColor(
                ContextCompat.getColor(requireContext(), R.color.myBlue)
            )
            lastView = view
        }

        private inner class ItemHolder(view: View) : RecyclerView.ViewHolder(view) {
            private lateinit var sparepart: Sparepart

            @OptIn(DelicateCoroutinesApi::class)
            fun bind(sparepart: Sparepart) {
                this.sparepart = sparepart
                if (sparepart == viewModel.student) {
                    updateCurrentView(itemView)
                }
                val tv = itemView.findViewById<TextView>(R.id.tvSparePartName)
                tv.text = sparepart.sparePartName
                val cl = itemView.findViewById<ConstraintLayout>(R.id.clStudent)
                cl.setOnClickListener {
                    tv.text = sparepart.sparePartName
                    viewModel.setCurrentStudent(sparepart)
                    updateCurrentView(itemView)
                }
                tv.setOnLongClickListener {
                    viewModel.sortByName()
                    true
                }
                val tv2 = itemView.findViewById<TextView>(R.id.tvSparePartName2)
                tv2.text = sparepart.manufacturer
                val cl2 = itemView.findViewById<ConstraintLayout>(R.id.clStudent)
                cl2.setOnClickListener {
                    tv2.text = sparepart.manufacturer
                    viewModel.setCurrentStudent(sparepart)
                    updateCurrentView(itemView)
                }
                tv2.setOnLongClickListener {
                    viewModel.sortByMiddleName()
                    true
                }
                val tv3 = itemView.findViewById<TextView>(R.id.tvSparePartName3)
                tv3.text = sparepart.numberCatalog
                val cl3 = itemView.findViewById<ConstraintLayout>(R.id.clStudent)
                cl3.setOnClickListener {
                    tv3.text = sparepart.numberCatalog
                    viewModel.setCurrentStudent(sparepart)
                    updateCurrentView(itemView)
                }
                tv3.setOnLongClickListener {
                    viewModel.sortByLastName()
                    true
                }
                itemView.findViewById<ImageButton>(R.id.ibEditSparePart).setOnClickListener {
                    editStudent(sparepart)
                }
                itemView.findViewById<ImageButton>(R.id.ibDeleteSparePart).setOnClickListener {
                    deleteDialog()
                }
                val llb = itemView.findViewById<LinearLayout>(R.id.llSparePartButtons)
                llb.visibility = View.INVISIBLE
                llb.layoutParams = llb.layoutParams?.apply { this.width = 1 }
                val ib = itemView.findViewById<ImageButton>(R.id.ibEditSparePart)
                ib.visibility = View.VISIBLE
                cl.setOnLongClickListener {
                    cl.callOnClick()
                    llb.visibility = View.VISIBLE
                    if (sparepart.VIN?.isNotBlank() == true) {
                        ib.visibility = View.VISIBLE
                    }
                    MainScope().launch {
                        tv.text = sparepart.manufacturer
                        val lp = llb.layoutParams
                        lp?.width = 1
                        val ip = ib.layoutParams
                        ip.width = 1
                        while (lp?.width!! < 350) {
                            lp?.width = lp?.width!! + 35
                            llb.layoutParams = lp
                            ip.width = ip.width + 10
                            if (ib.visibility == View.VISIBLE) {
                                ib.layoutParams = ip
                            }
                            delay(50)
                        }
                    }
                    true
                }
            }
        }
    }
}