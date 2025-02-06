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

class LoginFragment : Fragment() {

    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnLogin: Button
    private lateinit var btnGoToRegister: Button
    private lateinit var repository: OfflineDBRepository

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        etEmail = view.findViewById(R.id.etEmail)
        etPassword = view.findViewById(R.id.etPassword)
        btnLogin = view.findViewById(R.id.btnLogin)
        btnGoToRegister = view.findViewById(R.id.btnGoToRegister)

        repository = OfflineDBRepository(ListDatabase.getDatabase(requireContext()).listDAO())

        // Авторизация
        btnLogin.setOnClickListener {
            val email = etEmail.text.toString()
            val password = etPassword.text.toString()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(context, "Введите email и пароль", Toast.LENGTH_SHORT).show()
                return@setOnClickListener

            }

            lifecycleScope.launch {
                val success = repository.loginUser(email, password)

                if (success) {
                    SessionManager.saveUserState(requireContext(), true)
                    Toast.makeText(context, "Вход выполнен", Toast.LENGTH_SHORT).show()

                    // Переход к основному экрану
                 activity?.supportFragmentManager?.beginTransaction()
                     ?.replace(R.id.fcvMain, CarModelFragment.getInstance())
                        ?.commit()
                   // activity?.supportFragmentManager?.popBackStack()
                } else {
                    Toast.makeText(context, "Неверный email или пароль", Toast.LENGTH_SHORT).show()
                }
            }
        }

        // Переход на экран регистрации
        btnGoToRegister.setOnClickListener {
            activity?.supportFragmentManager?.beginTransaction()
                ?.replace(R.id.fcvMain, RegisterFragment())
                ?.addToBackStack(null)
                ?.commit()
        }


        // 🔥 Обработчик кнопки "Назад" → возвращает в `CarModelFragment`
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.fcvMain, CarModelFragment.getInstance())
                .commit()

            // 🔥 Обновляем меню и кнопки
            requireActivity().invalidateOptionsMenu()
        }
    }
}
