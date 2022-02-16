package com.example.interviewtestproject.updateRecipe

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import androidx.core.view.get
import com.example.interviewtestproject.R
import com.example.interviewtestproject.model.RecipeClass
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_recipe_details.*
import kotlinx.android.synthetic.main.activity_update_recipe.*
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.interviewtestproject.addStep.addStep
import com.example.interviewtestproject.addStep.editStep
import com.example.interviewtestproject.home.MainActivity
import com.example.interviewtestproject.home.RecipeItemListView
import com.example.interviewtestproject.home.StepItemListView
import com.example.interviewtestproject.home.recipeDetails
import com.example.interviewtestproject.model.RecipeModel
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_add_recipe.*
import java.text.SimpleDateFormat
import java.util.*


class updateRecipe : AppCompatActivity() {

    val REQUEST_CODE = 100
    var selectedPhotoUri: Uri? = null
    var thesingleurl = ""
    var recipeDetails :RecipeClass ? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_recipe)

        //actionbar
        val actionbar = supportActionBar
        //set actionbar title
        actionbar!!.title = "Update Recipe Activity"
        //set back button
        actionbar.setDisplayHomeAsUpEnabled(true)
        actionbar.setDisplayHomeAsUpEnabled(true)

        val spinner: Spinner = findViewById(R.id.updateRecipe_planets_spinner)
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter.createFromResource(
            this,
            R.array.addRecipe_planets_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            spinner.adapter = adapter
        }

        recipeDetails  = intent.getParcelableExtra<RecipeClass>("recipeDetails")

        if(recipeDetails != null){
            callRecycleView(recipeDetails!!.stepData)
            if(recipeDetails?.stepData?.size!! > 0){
                updateRecipe_step_linear.isVisible = true
            }
        }


        updateRecipe_ingredient_editText.setText(recipeDetails?.recipeIngredient)
        updateRecipe_recipeName_editText.setText("${recipeDetails?.recipeName}")
        Picasso.get().load("${recipeDetails?.recipeImage}").into(updateRecipe_imageView)
        updateRecipe_planets_spinner.setSelection(
                (updateRecipe_planets_spinner.getAdapter() as ArrayAdapter<String?>).getPosition(
                    recipeDetails?.recipeType
                )
                )

        updateRecipe_imageView.setOnClickListener {
         openGalleryForImage()
        }

        updateRecipe_updateButton.setOnClickListener {
            updateRecipe_progressBar.isVisible = true
            getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            if(selectedPhotoUri != null){
                uploadImageToFirebaseStorage()
            }else{
                thesingleurl = recipeDetails?.recipeImage.toString()
                saveRecipeToFirebaseDatabase()
            }
        }

        updateRecipe_new_step_button.setOnClickListener {
            if(recipeDetails!= null){
                var recipeData = RecipeClass(
                    stepData = recipeDetails!!.stepData,
                    recipeImageUri = null,
                    recipeImage = recipeDetails!!.recipeImage,
                    recipeName = updateRecipe_recipeName_editText.text.toString(),
                    recipeType = updateRecipe_planets_spinner.selectedItem.toString(),
                    recipeUid = recipeDetails!!.recipeUid,
                    recipeIngredient = updateRecipe_ingredient_editText.text.toString()
                )
                val intent = Intent(this, updateRecipe_addStep::class.java)
                intent.putExtra("recipeData",recipeData)
                finish()
                startActivity(intent)
            }

        }

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun openGalleryForImage() {

        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE){
            selectedPhotoUri = data?.data
            updateRecipe_imageView.setImageURI(data?.data) // handle chosen image
        }
    }

    private fun uploadImageToFirebaseStorage() {
        if (selectedPhotoUri == null) return
        val date = getCurrentDateTime()
        val dateInString = date.toString("yyyy-MM-dd HH:mm:ss")
        val filenameUid = UUID.randomUUID().toString()
        val filename = "$dateInString $filenameUid"
        val ref = FirebaseStorage.getInstance().getReference("/images/$filename")
        Log.d("Add Recipe", "$selectedPhotoUri >>>>>>>>>>>>>>>>>> this is the image url if not mistake")
        ref.putFile(selectedPhotoUri!!)
            .addOnSuccessListener {
                Log.d("Add Recipe", "Successfully uploaded image: ${it.metadata?.path}")

                ref.downloadUrl.addOnSuccessListener {
                    Log.d("Add Recipe", "File Location: $it")
                    thesingleurl = it.toString()
                    saveRecipeToFirebaseDatabase()
//                    saveUserToFirebaseDatabase(it.toString())
                }
            }
            .addOnFailureListener {
                Log.d("Add Recipe", "Failed to upload image to storage: ${it.message}")
            }
    }

    private fun saveRecipeToFirebaseDatabase() {

//        val address = hostel_address.text.toString()
//        val propertyType = Hostel_propertyType.selectedItem.toString()

//        val uid = FirebaseAuth.getInstance().uid ?: ""
        val date = getCurrentDateTime()
        val dateInString = date.toString("yyyy-MM-dd HH:mm:ss")
        val filenameRecipeUID = UUID.randomUUID().toString()
        val filenameRecipe = "$dateInString $filenameRecipeUID"
//        theHostelID = filenameHostel.toString()
        val status = 0
//        Log.d("Add Recipe", "$merchantPic >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>polopolo>>>>>>>>>>>>>>>")
        val ref = FirebaseDatabase.getInstance().getReference("recipes/${recipeDetails?.recipeUid}")

        val recipe = RecipeModel(
            recipeImage = thesingleurl,
            recipeName = "${updateRecipe_recipeName_editText.text.toString()}",
            recipeType = updateRecipe_planets_spinner.selectedItem.toString(),
            recipeUid = recipeDetails!!.recipeUid,
            recipeIngredient = updateRecipe_ingredient_editText.text.toString()
        )

        ref.setValue(recipe)
            .addOnSuccessListener {
                //if changed image will cause the step issues dk why.
                if(saveStepIntoFirebase(recipeDetails!!.stepData)) {
                    recipeDetails = RecipeClass(
                        recipeName = updateRecipe_recipeName_editText.text.toString(),
                        recipeImage = thesingleurl,
                        recipeType = updateRecipe_planets_spinner.selectedItem.toString(),
                        recipeUid = recipeDetails!!.recipeUid,
                        recipeImageUri = null,
                        stepData = recipeDetails!!.stepData,
                        recipeIngredient = updateRecipe_ingredient_editText.text.toString()
                    )
                    updateRecipe_progressBar.isVisible = false
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    Toast.makeText(this, "Add Successfull", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, MainActivity::class.java)
                    intent.putExtra("RecipeDetails", recipeDetails)
                    finish()
                    startActivity(intent)
                    Log.d("Add Recipe", "Finally we saved the user to Firebase Database")
                }

            }
            .addOnFailureListener {
//                progressBar.visibility = View.GONE
                Toast.makeText(this , "Fail to add to firebase", Toast.LENGTH_SHORT).show()
                Log.d("Add Recipe", "Failed to set value to database: ${it.message}")
            }
    }

    fun saveStepIntoFirebase(stepDatalist: ArrayList<String>):Boolean{


        for(item in stepDatalist){
            val stepdate = getCurrentDateTime()
            val stepdateInString = stepdate.toString("yyyy-MM-dd HH:mm:ss")
            val stepfilenameRecipeUID = UUID.randomUUID().toString()
            var stepfileUid = "${recipeDetails?.stepData?.indexOf(item)} $stepdateInString $stepfilenameRecipeUID"

            val ref = FirebaseDatabase.getInstance().getReference("recipes/${recipeDetails?.recipeUid}/step/$stepfileUid")

            ref.setValue(item)
        }

        return true

    }


    fun getCurrentDateTime(): Date {
        return Calendar.getInstance().time
    }

    fun Date.toString(format: String, locale: Locale = Locale.getDefault()): String {
        val formatter = SimpleDateFormat(format, locale)
        return formatter.format(this)
    }

    fun callRecycleView( stepDatalist : ArrayList<String>){
        val recyclerview = findViewById<RecyclerView>(R.id.updateRecipe_recycleView)

        // this creates a vertical layout Manager
        recyclerview.layoutManager = LinearLayoutManager(this)

        val adapter = GroupAdapter<ViewHolder>()


        for(item in stepDatalist){
            adapter.add(StepItemListView(item))
        }

        adapter.setOnItemClickListener { item, view ->

            val userItem = item as StepItemListView


            val intent = Intent(view.context, editStep::class.java)
            intent.putExtra("stepDataString", userItem.stepData)
            intent.putExtra("recipeDetails", recipeDetails)
            finish()
            startActivity(intent)
        }


        recyclerview.adapter = adapter

    }

}