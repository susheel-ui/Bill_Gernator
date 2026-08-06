package com.example.bill_genrating_app.Fragments

import android.app.Application
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity.RESULT_OK
import androidx.core.content.edit
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.transition.Visibility
import com.example.bill_genrating_app.Activities.LoginActivity
import com.example.bill_genrating_app.Activities.RegisterUserActivity
import com.example.bill_genrating_app.Activities.ShopDetailsEditPage
import com.example.bill_genrating_app.Api.response.Shop
import com.example.bill_genrating_app.R
import com.example.bill_genrating_app.Roomdb.Repos.shopDetailsService

import com.example.bill_genrating_app.Roomdb.entities.shopDetails
import com.example.bill_genrating_app.UtilClasses.UtilString
import com.example.bill_genrating_app.databinding.FragmentClientsFragmentsBinding
import com.example.bill_genrating_app.viewModels.ShopState
import com.example.bill_genrating_app.viewModels.UserProfileViewModel
import com.example.bill_genrating_app.viewModels.UserState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.DisposableHandle
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import com.example.bill_genrating_app.Api.response.User
import com.example.bill_genrating_app.UtilClasses.SharePreferences

/**
 * A simple [Fragment] subclass.
 * Use the [clients_fragments.newInstance] factory method to
 * create an instance of this fragment.
 */
class clients_fragments() : Fragment() {
    // TODO: Rename and change types of parameters
    lateinit var clientsFragmentsBinding: FragmentClientsFragmentsBinding

    val viewModel: UserProfileViewModel by viewModels()
    val launcherActivity = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            // Restart the fragment by calling onCreate again

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        clientsFragmentsBinding = FragmentClientsFragmentsBinding.inflate(layoutInflater)


        clientsFragmentsBinding.topBaritemBar.searchIcon.visibility = View.GONE
        clientsFragmentsBinding.topBaritemBar.userCardView.visibility = View.GONE
        clientsFragmentsBinding.btnLogOut.setOnClickListener {
            val sharedPred = SharePreferences(requireActivity().application)
            sharedPred.logout (false)
            startActivity(Intent(requireContext(), LoginActivity::class.java))
            activity?.finish()
        }
    }

    override fun onStart() {
        super.onStart()
        viewModel.shopDetails.observe(viewLifecycleOwner) {
            when (it) {
                is ShopState.Loading -> {
                    Toast.makeText(requireContext(), "Loading Shop Details", Toast.LENGTH_SHORT)
                        .show()
                }

                is ShopState.Success -> {
                    setShopDetails(it.data)
                }

                is ShopState.Failes -> {
                    Toast.makeText(requireContext(), "Error: ${it.message}", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
        viewModel.user.observe(viewLifecycleOwner) {
            when (it) {
                is UserState.Loading -> {

                }

                is UserState.Success -> {
                    setUserDetails(it.data)
                }

                is UserState.Failes -> {

                }
            }
        }
    }

    private fun setUserDetails(data: User) {
        clientsFragmentsBinding.UserNameTF.text = data.username
//        clientsFragmentsBinding..text = data.email
        clientsFragmentsBinding.MobileNoTF.text = data.mobileNo
    }

    private fun setShopDetails(shopDetails: Shop) {
        clientsFragmentsBinding.ShopNameTF.text = shopDetails.shopName
        clientsFragmentsBinding.GSTIN.text = shopDetails.GSTNumber
        clientsFragmentsBinding.AddressTF.text = shopDetails.address
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        return clientsFragmentsBinding.root
    }


}