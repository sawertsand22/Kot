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
    private lateinit var sparePartAdapter: SparePartAdapter  // Объявляем адаптер
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

//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//        viewModel = ViewModelProvider(this).get(SparePartsViewModel::class.java)
//        viewModel.set_Catalog(catalog)
//
//        Log.d("RecyclerViewDebug до кода", "Adapter attached: ${binding.rvSpareParts.adapter != null}")
//
//        sparePartAdapter = SparePartAdapter(emptyList()) // Инициализируем адаптер ПУСТЫМ списком
//        binding.rvSpareParts.adapter = sparePartAdapter
//        Log.d("RecyclerViewDebug пссле кода ", "Adapter attached: ${binding.rvSpareParts.adapter != null}")
//
//
//        viewModel.sparepartList.observe(viewLifecycleOwner) { spareparts ->
//            Log.d("FragmentObserve", "SparePart list updated in fragment: ${spareparts.size}, first item: ${spareparts.firstOrNull()?.sparePartName}")
//            sparePartAdapter.updateData(spareparts)
//                //sparePartAdapter = SparePartAdapter(spareparts)  // ⬅️ Создаем новый адаптер
//            //binding.rvSpareParts.adapter = sparePartAdapter  // ⬅️ Присваиваем заново
//                // binding.rvSpareParts.adapter?.notifyDataSetChanged()  // ⬅️ Принудительное обновление
//            binding.rvSpareParts.requestLayout()
//        }
//            //binding.rvSpareParts.adapter?.notifyDataSetChanged()
//
//        binding.fabAppendSparePart.setOnClickListener {
//            editSparePart(Sparepart().apply { catalogID = viewModel.catalog?.id })
//        }
//    }
override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    viewModel = ViewModelProvider(this).get(SparePartsViewModel::class.java)
    viewModel.set_Catalog(catalog)

    // Инициализация адаптера ПУСТЫМ списком
    sparePartAdapter = SparePartAdapter(emptyList())
    binding.rvSpareParts.adapter = sparePartAdapter

    // Наблюдение за LiveData
    viewModel.sparepartList.observe(viewLifecycleOwner) { spareparts ->
        Log.d("FragmentObserve", "Обновляем UI. Количество: ${spareparts.size}")
        //updateRecyclerView(spareParts)
        sparePartAdapter.updateData(spareparts)
//        binding.rvSpareParts.requestLayout()
    }

    binding.etSearch.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            val query = s.toString().trim()
            Log.d("SearchInput", "🔎 Поиск: $query")
            viewModel.filterSparePartsByName(query)
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
    })

    binding.fabAppendSparePart.setOnClickListener {
        editSparePart(Sparepart().apply { catalogID = viewModel.catalog?.id })
    }
}

    private fun isUserAuthorized(): Boolean {
        val sharedPrefs = requireContext().getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)
        return sharedPrefs.getBoolean("isAuthorized", false)
    }

    private fun deleteDialog(sparepart: Sparepart) {
        Log.d("DeleteDialog", "⚡️ Открываем диалог удаления!")

        if (!isUserAuthorized()) {
            Log.d("DeleteDialog", "❌ Ошибка: пользователь не авторизован!")
            Toast.makeText(requireContext(), "Требуется авторизация", Toast.LENGTH_SHORT).show()
            return
        }

        AlertDialog.Builder(requireContext())
            .setTitle("Удаление")
            .setMessage("Удалить запчасть ${sparepart.sparePartName}?")
            .setPositiveButton("Да") { _, _ ->
                Log.d("DeleteDialog", "✅ Нажали 'Да', запускаем удаление")

                viewModel.setCurrentSparePart(sparepart) // 🔥 Устанавливаем текущую запчасть
                viewModel.deleteSparePart()
            }
            .setNegativeButton("Нет", null)
            .setCancelable(true)
            .create()
            .show()
    }




    private fun editSparePart(sparepart: Sparepart){
        (requireActivity() as ActivityCallbacks).showFragment(NamesOfFragment.SPAREPART, sparepart)
        (requireActivity() as ActivityCallbacks).newTitle("Каталог ${viewModel.catalog?.name}")
    }

    private inner class SparePartAdapter(private var items: List<Sparepart>) :
        RecyclerView.Adapter<SparePartAdapter.ItemHolder>() {

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

        fun removeItem(sparePart: Sparepart) {
            Log.d("RecyclerViewDebug", "Удаляем из адаптера: ${sparePart.sparePartName}")

            // 🔥 Ждем обновления LiveData
            notifyDataSetChanged()
        }


        fun updateData(newItems: List<Sparepart>) {
            Log.d("RecyclerViewDebug", "updateData() вызван! Новый список: ${newItems.size} элементов")

            val diffCallback = StudentDiffCallback(items, newItems)
            val diffResult = DiffUtil.calculateDiff(diffCallback)

            items = newItems
            diffResult.dispatchUpdatesTo(this) // 🔥 Используем DiffUtil для обновления
        }


        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
            val view = layoutInflater.inflate(R.layout.element_sparepart_list, parent, false)
            return ItemHolder(view)
        }

        override fun getItemCount(): Int = items.size

        override fun onBindViewHolder(holder: ItemHolder, position: Int) {
            Log.d("RecyclerViewDebug", "Binding item: ${items[position].sparePartName}")
            holder.bind(items[position])
            Log.d("RecyclerViewDebug", "Binding item: ${items[position].sparePartName}")
        }

        private var lastView: View? = null

        private fun updateCurrentView(view: View) {
            val ll = lastView?.findViewById<LinearLayout>(R.id.llSparePartButtons)
            ll?.visibility = View.INVISIBLE
            ll?.layoutParams = ll?.layoutParams?.apply { this?.width = 1 }
            lastView?.findViewById<ConstraintLayout>(R.id.clSparePart)?.setBackgroundColor(
                ContextCompat.getColor(requireContext(), R.color.white)
            )
            view.findViewById<ConstraintLayout>(R.id.clSparePart).setBackgroundColor(
                ContextCompat.getColor(requireContext(), R.color.myBlue)
            )
            lastView = view
        }

        private inner class ItemHolder(view: View) : RecyclerView.ViewHolder(view) {
            private lateinit var sparepart: Sparepart

            @OptIn(DelicateCoroutinesApi::class)
            fun bind(sparepart: Sparepart) {
                this.sparepart = sparepart
                if (sparepart == viewModel.selectedSparePart.value) {
                    updateCurrentView(itemView)
                }
                val tv = itemView.findViewById<TextView>(R.id.tvSparePartName)
                tv.text = sparepart.sparePartName
                val cl = itemView.findViewById<ConstraintLayout>(R.id.clSparePart)
                cl.setOnClickListener {
                    tv.text = sparepart.sparePartName
                    viewModel.setCurrentSparePart(sparepart)
                    updateCurrentView(itemView)
                }
                tv.setOnLongClickListener {
                    viewModel.sortByName()
                    true
                }
                val tv2 = itemView.findViewById<TextView>(R.id.tvSparePartName2)
                tv2.text = sparepart.manufacturer
                val cl2 = itemView.findViewById<ConstraintLayout>(R.id.clSparePart)
                cl2.setOnClickListener {
                    tv2.text = sparepart.manufacturer
                    viewModel.setCurrentSparePart(sparepart)
                    updateCurrentView(itemView)
                }
                tv2.setOnLongClickListener {
                    viewModel.sortByMiddleName()
                    true
                }
                val tv3 = itemView.findViewById<TextView>(R.id.tvSparePartName3)
                tv3.text = sparepart.numberCatalog
                val cl3 = itemView.findViewById<ConstraintLayout>(R.id.clSparePart)
                cl3.setOnClickListener {
                    tv3.text = sparepart.numberCatalog
                    viewModel.setCurrentSparePart(sparepart)
                    updateCurrentView(itemView)
                }
                tv3.setOnLongClickListener {
                    viewModel.sortByLastName()
                    true
                }
                itemView.findViewById<ImageButton>(R.id.ibEditSparePart).setOnClickListener {
                    editSparePart(sparepart)
                }
                itemView.findViewById<ImageButton>(R.id.ibDeleteSparePart).setOnClickListener {
                    deleteDialog(sparepart)
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