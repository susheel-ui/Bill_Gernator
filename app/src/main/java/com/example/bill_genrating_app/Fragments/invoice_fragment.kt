package com.example.bill_genrating_app.Fragments

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.bill_genrating_app.Activities.LoginActivity
import com.example.bill_genrating_app.Adapters.MyOrdersViewItemAdapter
import com.example.bill_genrating_app.Api.response.Metrices
import com.example.bill_genrating_app.R
import com.example.bill_genrating_app.Roomdb.DBHelper
import com.example.bill_genrating_app.databinding.FragmentInvoiceFragmentBinding
import com.example.bill_genrating_app.viewModels.HomeViewModel
import com.example.bill_genrating_app.viewModels.MetriceState
import com.example.bill_genrating_app.viewModels.OrdersState
import kotlin.random.Random

class InvoiceFragment : Fragment() {

    private var _binding: FragmentInvoiceFragmentBinding? = null
    private val binding get() = _binding!!

    private lateinit var db: DBHelper

    private val viewModel: HomeViewModel by viewModels()

    private var orderData = mutableListOf<com.example.bill_genrating_app.Api.response.Order>()

    private lateinit var adapter: MyOrdersViewItemAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        db = DBHelper.getDatabase(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentInvoiceFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViews()
        observeData()

        // Load data only once
        viewModel.refreshUi()
    }

    override fun onStart() {
        super.onStart()
    }

    override fun onResume() {
        super.onResume()
        viewModel.refreshUi()
    }

    private fun setupViews() {

        binding.invoiceSearchbar.searchIcon.visibility = View.GONE

        adapter = MyOrdersViewItemAdapter(requireContext(), orderData)
        binding.ordersListsview.adapter = adapter

        binding.swipeRefreshLayout.setOnRefreshListener {
            viewModel.refreshUi()
        }

        binding.fragmentSeeAllTag.setOnClickListener {
            // startActivity(Intent(requireContext(), ViewOrdersActivity::class.java))
        }
    }

    private fun observeData() {

        viewModel.DataLoading.observe(viewLifecycleOwner) {
            binding.swipeRefreshLayout.isRefreshing = it
        }

        viewModel.recentTransactionLiveData.observe(viewLifecycleOwner) { state ->

            when (state) {

                is OrdersState.Loading -> {
                    Log.d(TAG, "Loading recent transactions...")
                }

                is OrdersState.Success -> {
                    Log.d(TAG, "Transactions Loaded: ${state.data}")

                    orderData.clear()
                    orderData.addAll(state.data ?: emptyList())

                    // Recreate adapter (works with your current adapter)
                    adapter = MyOrdersViewItemAdapter(requireContext(), orderData)
                    binding.ordersListsview.adapter = adapter
                }

                is OrdersState.Failed -> {

                    Log.e(TAG, state.message)

                    if (state.code == 403) {
                        startActivity(Intent(requireContext(), LoginActivity::class.java))
                        requireActivity().finish()
                    }
                }
            }
        }

        viewModel.metricesLiveData.observe(viewLifecycleOwner) { state ->

            when (state) {

                is MetriceState.Loading -> {
                    Log.d(TAG, "Loading metrics...")
                }

                is MetriceState.Success -> {
                    state.data?.let {
                        setMetrics(it)
                    }
                }

                is MetriceState.Failed -> {
                    Log.e(TAG, state.message)
                }
            }
        }
    }

    private fun setMetrics(data: Metrices) {
        val rupee = "₹"
        binding.incomeAmount.text = rupee.plus(data.todayTotalIncome.toString())
        binding.pendingCount.text = data.pendingOrdersCount.toString()
        binding.invoiceCount.text = data.totalInvoicesCount.toString()
        binding.lowStockCount.text = data.lowStocksCount.toString()
        binding.inventoryCount.text = data.totalInventoriesCount.toString()
        binding.increamentPercentage.text = data.incomeIncrementPercentage.toString().plus("%")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}