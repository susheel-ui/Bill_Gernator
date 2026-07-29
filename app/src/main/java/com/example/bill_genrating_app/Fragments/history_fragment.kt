package com.example.bill_genrating_app.Fragments

import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.bill_genrating_app.Activities.FinalOrderActivity
import com.example.bill_genrating_app.Adapters.HistoryOrderAdapter
import com.example.bill_genrating_app.Api.response.Order
import com.example.bill_genrating_app.Roomdb.DBHelper
import com.example.bill_genrating_app.databinding.FragmentHistoryFragmentBinding
import com.example.bill_genrating_app.viewModels.OrderState
import com.example.bill_genrating_app.viewModels.OrdersState
import com.example.bill_genrating_app.viewModels.OrdersViewModel
import com.example.bill_genrating_app.viewModels.ViewOrderViewModel
import kotlinx.coroutines.launch
import java.util.Locale

class history_fragment : Fragment() {

    private var _binding: FragmentHistoryFragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: HistoryOrderAdapter
    private var allOrders: List<Order> = emptyList()

    private val viewModel: OrdersViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHistoryFragmentBinding.inflate(inflater, container, false)

        viewModel.allOrderLiveData.observe(viewLifecycleOwner) { orders ->
            when (orders) {
                is OrdersState.Failed -> {

                }
                is OrdersState.Loading -> {
//                    AlertDialog.Builder(requireContext())
//                        .setTitle("Loading")
//                        .setMessage("Please wait while we load the orders.")
//                        .setCancelable(false)
//                        .show()
                }
                is OrdersState.Success -> {
                    allOrders = orders.data?.reversed() ?: emptyList()
                    adapter.updateData(allOrders)
                }
            }
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupSearch()
//        loadOrders()
    }

    private fun setupRecyclerView() {
        adapter = HistoryOrderAdapter(emptyList()) { order ->
            val intent = Intent(requireContext(), FinalOrderActivity::class.java)
            intent.putExtra("OrderId", order.id.toString())
            startActivity(intent)
        }
        binding.historyRecyclerView.adapter = adapter
    }

    private fun setupSearch() {
        val searchView =
            binding.searchBar.root.findViewById<SearchView>(com.example.bill_genrating_app.R.id.searchBox)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean = false

            override fun onQueryTextChange(newText: String?): Boolean {
                filterOrders(newText)
                return true
            }
        })
    }

    private fun filterOrders(query: String?) {
        val filteredList = if (query.isNullOrEmpty()) {
            allOrders
        } else {
            allOrders.filter {
                it.clientName.lowercase(Locale.getDefault())
                    .contains(query.lowercase(Locale.getDefault()))
            }
        }
        adapter.updateData(filteredList)
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
