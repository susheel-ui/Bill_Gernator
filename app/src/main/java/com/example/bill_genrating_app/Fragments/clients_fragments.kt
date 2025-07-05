package com.example.bill_genrating_app.Fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity.RESULT_OK
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.bill_genrating_app.Activities.RegisterUserActivity
import com.example.bill_genrating_app.Activities.ShopDetailsEditPage
import com.example.bill_genrating_app.R
import com.example.bill_genrating_app.Roomdb.Repos.shopDetailsService
import com.example.bill_genrating_app.Roomdb.entities.User
import com.example.bill_genrating_app.Roomdb.entities.shopDetails
import com.example.bill_genrating_app.databinding.FragmentClientsFragmentsBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.DisposableHandle
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * A simple [Fragment] subclass.
 * Use the [clients_fragments.newInstance] factory method to
 * create an instance of this fragment.
 */
class clients_fragments(private val user: User?) : Fragment() {
    // TODO: Rename and change types of parameters
  lateinit var clientsFragmentsBinding: FragmentClientsFragmentsBinding
  val launcherActivity = registerForActivityResult(
      ActivityResultContracts.StartActivityForResult()
  ) { result ->
      if (result.resultCode == RESULT_OK) {
        // Restart the fragment by calling onCreate again
       setDetails()
      }
  }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        clientsFragmentsBinding = FragmentClientsFragmentsBinding.inflate(layoutInflater)
        setDetails()
        clientsFragmentsBinding.btnUpdateClient.setOnClickListener {
            if(user!=null){
                launcherActivity.launch(Intent(this.context,ShopDetailsEditPage::class.java).putExtra("_id",user.id?.toLong()))
            }
        }
    }
    fun setDetails(){
        if(user!=null){
            lifecycleScope.launch {
                var shopDetails = CoroutineScope(Dispatchers.IO).async{
                    user.id?.let { shopDetailsService(requireContext()).getShopDetails(it) }!!
                }.await()
                CoroutineScope(Dispatchers.Main).launch{
                    clientsFragmentsBinding.ShopNameTF.text = shopDetails.shopName
                    clientsFragmentsBinding.GSTIN.text = shopDetails.GSTIN
                    clientsFragmentsBinding.UserNameTF.text = user.username
                    clientsFragmentsBinding.AddressTF.text = shopDetails.address
                    clientsFragmentsBinding.BusinessHoursTF.text = shopDetails.businessHours
                }
            }
        }else{
            Log.d("Error in User:clientFragment", "onCreate: Null Object retrieved")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        return clientsFragmentsBinding.root
    }

}