package com.example.interviewtestproject.home

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.interviewtestproject.R
import com.example.interviewtestproject.addRecipe.add_recipe
import com.example.interviewtestproject.model.RecipeClass
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.item_list.view.*
import java.util.*
import android.widget.AdapterView
import kotlin.collections.ArrayList


class MainActivity : AppCompatActivity(){

    var selectedItem: String = ""
    var stepDataList: ArrayList<String> = arrayListOf()
    override fun onCreate(savedInstanceState: Bundle?) {



//        val adapter = GroupAdapter<ViewHolder>()
//
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //actionbar
        val actionbar = supportActionBar
        //set actionbar title
        actionbar!!.title = "Recipe Application"


        fetchUsers()
//
//
//        // getting the recyclerview by its id
//        val recyclerview = findViewById<RecyclerView>(R.id.main_recycleView)
//
//        // this creates a vertical layout Manager
//        recyclerview.layoutManager = LinearLayoutManager(this)
//
//        // ArrayList of class ItemsViewModel
//        val data = ArrayList<String>()
//
//        // This loop will create 20 Views containing
//        // the image with the count of view
//        for (i in 1..20) {
//            adapter.add(RecipeView("$i"))
//        }
//
//        adapter.setOnItemClickListener { item, view ->
//
//            val intent = Intent(view.context, recipeDetails::class.java)
////            intent.putExtra("timeslotData", tSlot1.ts)
////            intent.putExtra("testingHostelinfo", currentdata1)
//            startActivity(intent)
//        }
//
////        adapter.add(HobbyItem(data))
//
//
//
//        // This will pass the ArrayList to our Adapter
////        val adapter = MainActivity_Adapter( Supplier.hobbies)
//
//
//
//
//        // Setting the Adapter with the recyclerview
//        recyclerview.adapter = adapter

        val spinner: Spinner = findViewById(R.id.planets_spinner)
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter.createFromResource(
            this,
            R.array.planets_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner


            spinner.adapter = adapter
        }



        spinner?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                Log.d("MainAcitivty", "${spinner.selectedItem}")
                selectedItem = spinner.selectedItem.toString()
                fetchUsers()
            }

        }


    }



    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.action_addRecipe -> {
            // User chose the "Settings" item, show the app settings UI...
            val intent = Intent(this, add_recipe::class.java)

            startActivity(intent)
            true
        }



        else -> {
            // If we got here, the user's action was not recognized.
            // Invoke the superclass to handle it.
            super.onOptionsItemSelected(item)
        }
    }

    private fun fetchUsers() {
                // getting the recyclerview by its id
        val recyclerview = findViewById<RecyclerView>(R.id.main_recycleView)

        // this creates a vertical layout Manager
        recyclerview.layoutManager = LinearLayoutManager(this)

        val ref = FirebaseDatabase.getInstance().getReference("/recipes")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {

            override fun onDataChange(p0: DataSnapshot) {
                val adapter = GroupAdapter<ViewHolder>()
//                lateinit var adapter2: Pageviewer
                var checkingNull = 0

                p0.children.forEach {
                    Log.d("HomeFragment", it.toString() + "========================")
                    val recipeList = it.getValue(RecipeClass::class.java)

//                    val currentmerchantID = FirebaseAuth.getInstance().uid ?: ""
//                    val DBMerchantId = user?.merchantuid.toString()
//                    Log.d(
//                        TAG,
//                        "display the merchant id form database ans current $DBMerchantId this is current> $currentmerchantID >>>??"
//



                    if(recipeList!= null && selectedItem.isNotEmpty()&&selectedItem == "All"){
                        Log.d("MainActivity", "selectedItem is empty")
                        adapter.add(RecipeItemListView(recipeList))
                    }
                    if (recipeList != null && selectedItem.isNotEmpty() && recipeList.recipeType == selectedItem) {
                        Log.d("HomeFragment", "not null")
                        adapter.add(RecipeItemListView(recipeList))

//                        fetchTestingPic(user.filenameHostel.toString())
                    }else{

                    }
                }



                adapter.setOnItemClickListener { item, view ->

                    val userItem = item as RecipeItemListView

                    val intent = Intent(view.context, recipeDetails::class.java)
                    intent.putExtra("RecipeDetails", userItem.recipeData)
                    startActivity(intent)


                }

                recyclerview.adapter = adapter

//                if(checkingNull != 0){
//                    recyclerview_newmessage.adapter = adapter
//                    display_emptyImG.visibility = View.GONE
//                    display_textview_empty.visibility = View.GONE
//                }else{
//                    // display null image
//                    display_emptyImG.visibility = View.VISIBLE
//                    display_textview_empty.visibility = View.VISIBLE
//                }

//                testing()
            }

            override fun onCancelled(p0: DatabaseError) {

            }
        })
    }



}









