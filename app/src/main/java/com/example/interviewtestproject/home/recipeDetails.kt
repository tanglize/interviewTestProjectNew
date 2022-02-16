package com.example.interviewtestproject.home

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.interviewtestproject.R
import com.example.interviewtestproject.addRecipe.add_recipe
import com.example.interviewtestproject.model.RecipeClass
import com.example.interviewtestproject.model.RecipeModel
import com.example.interviewtestproject.model.StepClass
import com.example.interviewtestproject.updateRecipe.updateRecipe
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_recipe_details.*

class recipeDetails : AppCompatActivity() {
    var stepDataList : ArrayList<String> = arrayListOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipe_details)
        //actionbar
        val actionbar = supportActionBar
        //set actionbar title
        actionbar!!.title = "Recipe Details Activity"
        //set back button
        actionbar.setDisplayHomeAsUpEnabled(true)
        actionbar.setDisplayHomeAsUpEnabled(true)

       var recipeDetails = intent.getParcelableExtra<RecipeClass>("RecipeDetails")


        recipeDetails_recipeName_textView.text = "${recipeDetails?.recipeName}"
        recipeDetails_recipeIngredient_textView.text = "${recipeDetails?.recipeIngredient}"
        if (recipeDetails != null) {
            fetchStepData(recipeDetails)
            Log.d("importnat ~~~ " ,"${recipeDetails.stepData.size}")
        }
        Picasso.get().load("${recipeDetails?.recipeImage}").into(recipeDetails_imageView)

        recipeDetails_name_edit_button.setOnClickListener {
            if(recipeDetails!= null ){
                recipeDetails.stepData = stepDataList
                val intent = Intent(this, updateRecipe::class.java)
                intent.putExtra("recipeDetails", recipeDetails)
                finish()
                startActivity(intent)

            }



        }

        recipeDetails_delete_button.setOnClickListener {
            if (recipeDetails != null) {
                deleteFirebase(recipeDetails)
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }



    private fun deleteFirebase(recipeDetails: RecipeClass){

        val refDelete = FirebaseDatabase.getInstance().getReference("/recipes/${recipeDetails.recipeUid}")

        refDelete.removeValue()
            .addOnSuccessListener {
                Log.d("Recipe Details", "Finally we delete the event form Firebase Database")
                val intent = Intent(this,  MainActivity::class.java)
                finish()
                startActivity(intent)

            }
            .addOnFailureListener {
                Log.d("Recipe Details", "Failed to set value to database: ${it.message}")
            }
    }

    private fun fetchStepData(recipeDetails: RecipeClass) {
        // getting the recyclerview by its id
        val recyclerview = findViewById<RecyclerView>(R.id.recipeDetails_step_recycleView)

        // this creates a vertical layout Manager
        recyclerview.layoutManager = LinearLayoutManager(this)

        val ref = FirebaseDatabase.getInstance().getReference("/recipes/${recipeDetails.recipeUid}/step")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {

            override fun onDataChange(p0: DataSnapshot) {
                val adapter = GroupAdapter<ViewHolder>()

                p0.children.forEach {
                    Log.d("recipeDetails", it.toString() + "~~~~~~~~~~~~~~~~~~~~~")
                    val stepData = it.getValue()


                    if(stepData!= null ){
                        Log.d("MainActivity", "selectedItem is empty")
                        adapter.add(StepItemListView(stepData = stepData.toString()))
                        stepDataList.add(stepData.toString())
                    }

                }



                adapter.setOnItemClickListener { item, view ->

                    val userItem = item as StepItemListView




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