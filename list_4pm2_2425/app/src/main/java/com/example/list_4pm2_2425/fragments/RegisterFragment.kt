package com.example.list_4pm2_2425.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.list_4pm2_2425.R
import com.example.list_4pm2_2425.database.ListDatabase
import com.example.list_4pm2_2425.repository.OfflineDBRepository
import com.example.list_4pm2_2425.utils.SessionManager
import kotlinx.coroutines.launch

class RegisterFragment : Fragment() {

    private lateinit var etUsername: EditText
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnRegister: Button
    private lateinit var repository: OfflineDBRepository
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_register, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        etUsername = view.findViewById(R.id.etUsername)
        etEmail = view.findViewById(R.id.etEmail)
        etPassword = view.findViewById(R.id.etPassword)
        btnRegister = view.findViewById(R.id.btnRegister)
        repository = OfflineDBRepository(ListDatabase.getDatabase(requireContext()).listDAO())

        btnRegister.setOnClickListener {
            val username = etUsername.text.toString()
            val email = etEmail.text.toString()
            val password = etPassword.text.toString()

            if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(context, "Заполните все поля", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Сохранение состояния пользователя
//            SessionManager.saveUserState(requireContext(), true)
//            Toast.makeText(context, "Registration successful", Toast.LENGTH_SHORT).show()
//
//            // Переход на основной экран
//            activity?.supportFragmentManager?.popBackStack()
            lifecycleScope.launch {
                val userExists = repository.registerUser(email, password)
                if (userExists) {
                    SessionManager.saveUserState(requireContext(), true)
                    Toast.makeText(context, "Регистрация успешна", Toast.LENGTH_SHORT).show()
                    activity?.supportFragmentManager?.popBackStack()
                } else {
                    Toast.makeText(context, "Пользователь уже зарегистрирован", Toast.LENGTH_SHORT).show()
                }
            }

        }


        // Обработчик кнопки "Назад"
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            activity?.supportFragmentManager?.popBackStack()
        }

    }

}
