package com.example.list_4pm2_2425.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.example.list_4pm2_2425.R
import com.example.list_4pm2_2425.data.Sparepart
import com.example.list_4pm2_2425.databinding.FragmentSparepartInfoBinding
import com.example.list_4pm2_2425.repository.AppRepository
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.text.SimpleDateFormat
import android.content.Context
import android.widget.Toast

private const val ARG_PARAM1 = "sparepart_param"

class SparePartInfoFragment : Fragment() {

    private lateinit var sparepart: Sparepart
    private lateinit var _binding: FragmentSparepartInfoBinding
    val binding get() = _binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            val param1 = it.getString(ARG_PARAM1)
            sparepart = if (param1 == null) Sparepart() else {
                val paramType = object : TypeToken<Sparepart>() {}.type
                Gson().fromJson(param1, paramType)
            }
        }
    }

    companion object {
        fun newInstance(sparepart: Sparepart) = SparePartInfoFragment().apply {
            arguments = Bundle().apply {
                putString(ARG_PARAM1, Gson().toJson(sparepart))
            }
        }
    }

    @SuppressLint("SimpleDateFormat")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSparepartInfoBinding.inflate(inflater, container, false)

        // Проверка, авторизован ли пользователь
        val isAuthorized = isUserAuthorized()

        // Отключаем поля для неавторизованных пользователей
        setFieldsEnabled(isAuthorized)

        // Скрыть кнопку сохранения для неавторизованных пользователей
        binding.btnSave.visibility = if (isAuthorized) View.VISIBLE else View.GONE

        // Настройка спиннера с полами
        val sexArray = resources.getStringArray(R.array.SEX)
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, sexArray)
        binding.spSex.adapter = adapter
        binding.spSex.setSelection(sparepart.sex)

        // Обработчик выбора пола
        binding.spSex.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                sparepart.sex = position
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Ничего не делаем
            }
        }

        // Обработчик изменения даты рождения
        binding.cwProductionDate.setOnDateChangeListener { view, year, month, dayOfMonth ->
            sparepart.birthDate.time = SimpleDateFormat("yyyy.MM.dd")
                .parse("$year.${month + 1}.$dayOfMonth")?.time ?: sparepart.birthDate.time
        }

        // Заполняем поля из данных студента
        binding.etFirstName.setText(sparepart.sparePartName)
        binding.etNumberCatalog.setText(sparepart.numberCatalog)
        binding.etVIN.setText(sparepart.VIN)
        binding.etMiddleName.setText(sparepart.manufacturer)
        binding.etQuality.setText(sparepart.quantity)
        binding.etCount.setText(sparepart.count_part)
        binding.cwProductionDate.date = sparepart.birthDate.time

        // Обработчик кнопки отмены
        binding.btnCancel.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        // Обработчик кнопки сохранения
        binding.btnSave.setOnClickListener {
            saveSparePartData()
        }

        return binding.root
    }





    private fun isUserAuthorized(): Boolean {
        // Проверка авторизации через SharedPreferences
        val sharedPrefs = requireContext().getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)
        return sharedPrefs.getBoolean("isAuthorized", false)
    }

    private fun setFieldsEnabled(isEnabled: Boolean) {
        // Отключаем или включаем все поля в зависимости от авторизации
        binding.etFirstName.isEnabled = isEnabled
        binding.etNumberCatalog.isEnabled = isEnabled
        binding.etVIN.isEnabled = isEnabled
        binding.etMiddleName.isEnabled = isEnabled
        binding.etQuality.isEnabled = isEnabled
        binding.etCount.isEnabled = isEnabled
        binding.spSex.isEnabled = isEnabled
        binding.cwProductionDate.isEnabled = isEnabled
    }

    private fun saveSparePartData() {
        if (binding.etFirstName.text.isNullOrBlank() ||
            binding.etNumberCatalog.text.isNullOrBlank() ||
            binding.etVIN.text.isNullOrBlank() ||
            binding.etMiddleName.text.isNullOrBlank() ||
            binding.etQuality.text.isNullOrBlank() ||
            binding.etCount.text.isNullOrBlank()) {

            Toast.makeText(requireContext(), "️ Заполните все поля!", Toast.LENGTH_SHORT).show()
            return
        }
        // Сохраняем данные студента в репозитории
        sparepart.numberCatalog = binding.etNumberCatalog.text.toString()
        sparepart.sparePartName = binding.etFirstName.text.toString()
        sparepart.manufacturer = binding.etMiddleName.text.toString()
        sparepart.VIN = binding.etVIN.text.toString()
        sparepart.quantity = binding.etQuality.text.toString()
        sparepart.count_part = binding.etCount.text.toString()

        AppRepository.getInstance().updateSparePart(sparepart)
        requireActivity().onBackPressedDispatcher.onBackPressed()
    }
}
