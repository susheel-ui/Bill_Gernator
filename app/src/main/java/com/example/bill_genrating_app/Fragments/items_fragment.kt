package com.example.bill_genrating_app.Fragments

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.example.bill_genrating_app.Activities.AddItem
import com.example.bill_genrating_app.Adapters.AdapterItems
import com.example.bill_genrating_app.Roomdb.DBHelper
import com.example.bill_genrating_app.Roomdb.DB_Repo
import com.example.bill_genrating_app.Roomdb.entities.items
import com.example.bill_genrating_app.ViewModels.FagmentsViewModels.Items.Items_ViewModelFactory
import com.example.bill_genrating_app.ViewModels.FagmentsViewModels.Items.Items_viewModel
import com.example.bill_genrating_app.databinding.FragmentItemsFragmentBinding

/**
 * A simple [Fragment] subclass.
 * Use the [items_fragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class items_fragment : Fragment() {
    lateinit var thisFagementBinding : FragmentItemsFragmentBinding
    lateinit var Items_viewModel :Items_viewModel
    // TODO: Rename and change types of parameters

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        thisFagementBinding = FragmentItemsFragmentBinding.inflate(layoutInflater)
        val DB = DB_Repo(DBHelper.getInstance(requireContext().applicationContext))
        Items_viewModel = ViewModelProvider(this,Items_ViewModelFactory(DB)).get(Items_viewModel::class.java)
//        thisFagementBinding.topBaritemBar.searchBar.setOnQueryTextListener(object : android.widget.SearchView.OnQueryTextListener{
//            override fun onQueryTextSubmit(str: String?): Boolean {
//                searchByName(str.toString())
//                return false
//            }
//
//            override fun onQueryTextChange(p0: String?): Boolean {
//                ShowItems(requireContext().applicationContext,fetchItemsRoom())
//                return false
//            }
//
//        })
        thisFagementBinding.topBaritemBar.searchBar.setOnQueryTextListener( object :androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {

                searchByName(query.toString())
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                ShowItems(requireContext().applicationContext,fetchItemsRoom())
                return false
            }

        })

        return thisFagementBinding.root
    }

    override fun onStart() {
        super.onStart()
        thisFagementBinding.btnaddItem.setOnClickListener {
            val activityIntent = Intent(context, AddItem::class.java)
            startActivity(activityIntent)
        }
        ShowItems(requireContext().applicationContext,fetchItemsRoom())
//        searchByName("classmate");




    }

    fun ShowItems(context: Context, paralist:List<items>?){
        try {
            val list = paralist
            Log.d(ContentValues.TAG, "ShowItems: $list")
            if(list?.isEmpty() == true){
                thisFagementBinding.RecyclerListView.visibility = View.INVISIBLE;
                thisFagementBinding.resultError.text = "Not Found"
                thisFagementBinding.resultError.visibility = View.VISIBLE
            }
            else{
                thisFagementBinding.resultError.visibility = View.INVISIBLE
                thisFagementBinding.RecyclerListView.visibility = View.VISIBLE
            }
            val adapter = list?.let { AdapterItems(context, it) }
            val layoutManager = LinearLayoutManager(context)
            layoutManager.orientation = LinearLayoutManager.VERTICAL
            thisFagementBinding.RecyclerListView.layoutManager = layoutManager
            thisFagementBinding.RecyclerListView.adapter = adapter
        }catch (exception:Exception){
            Log.d(ContentValues.TAG, "ShowItems: ${exception.message}")
        }




    }

    fun fetchItemsRoom():List<items>?{
        val db = context?.let {
            Room.databaseBuilder(
                it.applicationContext,
                DBHelper::class.java, "DatabaseBillGenerator"
            ).fallbackToDestructiveMigration().allowMainThreadQueries().build()
        }
        val itemDao = db?.itemDao()
        val list = itemDao?.getall()
        return list
    }
    fun fetchDb(): DBHelper?{
         return context?.let {
            Room.databaseBuilder(
                it.applicationContext,
                DBHelper::class.java, "DatabaseBillGenerator"
            ).fallbackToDestructiveMigration().allowMainThreadQueries().build();
        }
    }

    fun searchByName(str:String){
        val db = fetchDb();
        val result = db?.itemDao()?.getByname(str);
        Log.d(ContentValues.TAG, "searchByName: $result")
        try {
                ShowItems(requireContext().applicationContext, result)
        }catch (e:Exception){
            Log.d(ContentValues.TAG, "searchByName: error ${e.message}")
        }
        }


}