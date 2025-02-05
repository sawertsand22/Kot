package com.example.list_4pm2_2425.fragments

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.list_4pm2_2425.MainActivity
import com.example.list_4pm2_2425.R
import com.example.list_4pm2_2425.app_view_models.CarModelViewModel
import com.example.list_4pm2_2425.data.CarModel
import com.example.list_4pm2_2425.data.NamesOfFragment
import com.example.list_4pm2_2425.databinding.FragmentCarmodelBinding
import com.example.list_4pm2_2425.interfaces.ActivityCallbacks

class CarModelFragment : Fragment(), MainActivity.Edit {

    companion object {
        //fun newInstance() = FacultyFragment()
        private var INSTANCE: CarModelFragment? = null
        fun getInstance(): CarModelFragment {
            if (INSTANCE == null) INSTANCE = CarModelFragment()
            return INSTANCE ?: throw Exception("CarModelFragment не создан")
        }
    }

    private lateinit var viewModel: CarModelViewModel
    private lateinit var _binding: FragmentCarmodelBinding
    //val btnLoginRegister = requireActivity().findViewById<Button>(R.id.btnLoginRegister)
    //val btnLogout = requireActivity().findViewById<Button>(R.id.btnLogout)
    private var tabPosition: Int = 0



    private val binding get() = _binding!!


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    private var isAuthorized = false//


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCarmodelBinding.inflate(inflater, container, false)
        binding.rvCarModel.layoutManager=
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)


        checkAuthorization()  //
        return binding.root
    }

    //
    private fun checkAuthorization() {
        // Получаем состояние авторизации из SharedPreferences
        val sharedPrefs = requireActivity().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        isAuthorized = sharedPrefs.getBoolean("isAuthorized", false)
    }

    private fun enableEditing(enabled: Boolean) {
        // Включаем или отключаем редактирование
        binding.rvCarModel.isEnabled = enabled
    }

    //
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {


        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(CarModelViewModel::class.java)
        activityCallbacks?.newTitle("Список моделей авто")

        //btnLoginRegister = view.findViewById(R.id.btnLoginRegister)
        //btnLogout = view.findViewById(R.id.btnLogout)
        val isAuthorized = isUserAuthorized()





        //btnLoginRegister = view.findViewById(R.id.btnLoginRegister)




        viewModel.CarModelList.observe(viewLifecycleOwner){
            binding.rvCarModel.adapter=CarModelAdapter(it?:emptyList())
        }
    }




    private fun isUserAuthorized(): Boolean {
        val sharedPrefs = requireContext().getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)
        return sharedPrefs.getBoolean("isAuthorized", false)
    }


    private inner class CarModelAdapter(private val items: List<CarModel>)
        : RecyclerView.Adapter<CarModelAdapter.ItemHolder>() {

        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): CarModelAdapter.ItemHolder{
            val view = layoutInflater.inflate(R.layout.element_carmodel_list, parent, false)
            return ItemHolder(view)
        }
        override fun getItemCount(): Int = items.size
        override fun onBindViewHolder(holder: CarModelAdapter.ItemHolder, position: Int) {
            holder.bind(items[position])
        }

        private var lastView: View? = null
        private fun updateCurrentView(view: View){
            lastView?.findViewById<ConstraintLayout>(R.id.clCarModel)?.setBackgroundColor(
                ContextCompat.getColor(requireContext(), R.color.white)
            )
            lastView?.findViewById<TextView>(R.id.tvCarModel)?.setTextColor(
                ContextCompat.getColor(requireContext(), R.color.black)
            )
            view.findViewById<ConstraintLayout>(R.id.clCarModel)?.setBackgroundColor(
                ContextCompat.getColor(requireContext(), R.color.myBlue)
            )
            view.findViewById<TextView>(R.id.tvCarModel)?.setTextColor(
                ContextCompat.getColor(requireContext(), R.color.white)
            )
            lastView=view
        }

        private inner class ItemHolder(view: View)
            : RecyclerView.ViewHolder(view) {
                private lateinit var carModel : CarModel

                fun bind(carModel : CarModel) {
                    this.carModel=carModel
                    val tv = itemView.findViewById<TextView>(R.id.tvCarModel)
                    tv.text=carModel.name


                    val cl = View.OnClickListener {
                        if (isAuthorized) {
                            // Можно редактировать
                            editCarModel(carModel.name)
                        } else {
                            // Только просмотр
                            viewModel.setCurrentCarModel(carModel)
                        }
                       // viewModel.setCurrentFaculty(faculty)
                        //updateCurrentView(itemView)
                    }

                    val icl=itemView.findViewById<ConstraintLayout>(R.id.clCarModel)
                    icl.setOnClickListener(cl)
                    icl.setBackgroundColor(

                        ContextCompat.getColor(requireContext(), R.color.white))

                    if (carModel==viewModel.carModel)



                        updateCurrentView(itemView)

                    icl.setOnLongClickListener {
                        icl.callOnClick()
                        activityCallbacks?.showFragment(NamesOfFragment.CATALOG)
                        true
                    }




                }
            }
        }

    override fun append() {
        editCarModel()
    }

    override fun update() {
        editCarModel(viewModel.carModel?.name ?: "")
    }

    override fun delete() {
        deleteDialog()
    }

    private fun deleteDialog(){
        if (!isUserAuthorized()) {
            Toast.makeText(requireContext(), "Требуется авторизация для изменения модели", Toast.LENGTH_SHORT).show()
            return
        }
        AlertDialog.Builder(requireContext())
            .setTitle("Удаление!")
            .setMessage("Вы действительно хотите удалить модель авто ${viewModel.carModel?.name ?: ""}?")
            .setPositiveButton("ДА") {_, _ ->
                viewModel.deleteCarModel()
            }
            .setNegativeButton("НЕТ", null)
            .setCancelable(true)
            .create()
            .show()
    }

    private fun editCarModel(carModelName: String=""){
        if (!isUserAuthorized()) {
            Toast.makeText(requireContext(), "Требуется авторизация для изменения модели", Toast.LENGTH_SHORT).show()
            return
        }
        val mDialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_string, null)
        val messageText = mDialogView.findViewById<TextView>(R.id.tvInfo)
        val inputString = mDialogView.findViewById<EditText>(R.id.etString)
        inputString.setText(carModelName)
        messageText.text="Укажите модель автомобиля"

        AlertDialog.Builder(requireContext())
            .setTitle("ИЗМЕНЕНИЕ ДАННЫХ")
            .setView(mDialogView)
            .setPositiveButton("подтверждаю") {_, _ ->
                Log.d("Info", inputString.text.toString())
                if (inputString.text.isNotBlank()){
                    if (carModelName.isBlank())
                        viewModel.appendCarModel(inputString.text.toString())
                    else
                        viewModel.updateCarModel(inputString.text.toString())
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