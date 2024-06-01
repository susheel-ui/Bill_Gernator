package com.example.bill_genrating_app

import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.transition.Visibility
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import androidx.room.Room
import androidx.room.RoomDatabase

import com.example.bill_genrating_app.Adapters.AdapterItems
import com.example.bill_genrating_app.Roomdb.DBHelper
import com.example.bill_genrating_app.Roomdb.entities.items
import com.example.bill_genrating_app.databinding.FragmentItemsFragmentBinding
import com.example.bill_genrating_app.databinding.SearchBarBinding
import com.google.android.material.search.SearchBar
import com.google.android.material.search.SearchView

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [items_fragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class items_fragment : Fragment() {
    lateinit var thisFagementBinding :FragmentItemsFragmentBinding
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        thisFagementBinding = FragmentItemsFragmentBinding.inflate(layoutInflater)
        // Inflate the layout for this fragment
        thisFagementBinding.itemsSearchbar.searchBox.setOnQueryTextListener(object : android.widget.SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(str: String?): Boolean {
                searchByName(str.toString())
                return false
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                ShowItems(requireContext().applicationContext,fetchItemsRoom())
                return false
            }

        })


        return thisFagementBinding.root


    }

    override fun onStart() {
        super.onStart()
        thisFagementBinding.btnaddItem.setOnClickListener {
            var activityIntent = Intent(context,AddItem::class.java)
            startActivity(activityIntent)
        }
        ShowItems(requireContext().applicationContext,fetchItemsRoom())
//        searchByName("classmate");




    }

    fun ShowItems(context: Context,paralist:List<items>?){
        try {
            val list = paralist
            Log.d(TAG, "ShowItems: $list")
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
            Log.d(TAG, "ShowItems: ${exception.message}")
        }
        



    }

    fun fetchItemsRoom():List<items>?{
        val db = context?.let {
            Room.databaseBuilder(
                it.applicationContext,
                DBHelper::class.java,"DatabaseBillGenerator"
            ).fallbackToDestructiveMigration().allowMainThreadQueries().build()
        }
        val itemDao = db?.itemDao()
        val list = itemDao?.getall()
        return list
    }
    fun fetchDb():DBHelper?{
         return context?.let {
            Room.databaseBuilder(
                it.applicationContext,
                DBHelper::class.java,"DatabaseBillGenerator"
            ).fallbackToDestructiveMigration().allowMainThreadQueries().build();
        }
    }

    fun searchByName(str:String){
        val db = fetchDb();
        val result = db?.itemDao()?.getByname(str);
        Log.d(TAG, "searchByName: $result")
        try {
                ShowItems(requireContext().applicationContext, result)
        }catch (e:Exception){
            Log.d(TAG, "searchByName: error ${e.message}")
        }
        }


}