//package com.example.list_4pm2_2425.fragments
//
//import android.os.Bundle
//import android.view.View
//import android.widget.Button
//import android.widget.Toast
//import androidx.fragment.app.Fragment
//import com.example.list_4pm2_2425.R
//import com.example.list_4pm2_2425.repository.OfflineDBRepository
//import kotlinx.coroutines.CoroutineScope
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.launch
//
//class ProfileFragment : Fragment(R.layout.fragment_profile) {
//
//    private lateinit var dbRepository: OfflineDBRepository
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//        dbRepository = OfflineDBRepository(/* передаем DAO */)
//
//        val logoutButton = view.findViewById<Button>(R.id.btnLogout)
//
//        logoutButton.setOnClickListener {
//            CoroutineScope(Dispatchers.IO).launch {
//                dbRepository.logoutUser()
//                CoroutineScope(Dispatchers.Main).launch {
//                    Toast.makeText(context, "Выход из аккаунта успешен", Toast.LENGTH_SHORT).show()
//                    // Перенаправление на экран входа
//                }
//            }
//        }
//    }
//}
