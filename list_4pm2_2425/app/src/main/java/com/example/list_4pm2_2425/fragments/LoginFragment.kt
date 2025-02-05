package com.example.list_4pm2_2425.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.list_4pm2_2425.R
import com.example.list_4pm2_2425.databinding.FragmentLoginBinding
import com.example.list_4pm2_2425.utils.SessionManager

class LoginFragment : Fragment() {

    private lateinit var etUsername: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnLogin: Button
    private lateinit var tvRegister: TextView
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentLoginBinding.bind(view)

        // Получаем доступ к элементам
        val etUsername = binding.etUsername
        val etPassword = binding.etPassword
        val btnLogin = binding.btnLogin
        val tvRegister = binding.tvLogin

        btnLogin.setOnClickListener {
            val username = etUsername.text.toString()
            val password = etPassword.text.toString()

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(context, "Please enter both username and password", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Проверка данных и сохранение состояния
            SessionManager.saveUserState(requireContext(), true)
            Toast.makeText(context, "Login successful", Toast.LENGTH_SHORT).show()

            // Переход на основной экран
            activity?.supportFragmentManager?.beginTransaction()
                ?.replace(R.id.fcvMain, CatalogFragment.getInstance())
                ?.commit()
        }



        // Переход на экран регистрации
        tvRegister.setOnClickListener {
            val transaction = activity?.supportFragmentManager?.beginTransaction()
            transaction?.replace(R.id.fcvMain, RegisterFragment())
            transaction?.addToBackStack(null)
            transaction?.commit()
        }
    }
}
