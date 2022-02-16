package com.example.interviewtestproject.addRecipe


import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.core.view.get
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.interviewtestproject.R
import com.example.interviewtestproject.addStep.addStep
import com.example.interviewtestproject.home.RecipeItemListView
import com.example.interviewtestproject.home.StepItemListView
import com.example.interviewtestproject.home.recipeDetails
import com.example.interviewtestproject.model.RecipeClass
import com.example.interviewtestproject.model.RecipeModel
import com.example.interviewtestproject.model.StepClass
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_add_recipe.*
import kotlinx.android.synthetic.main.activity_recipe_details.*
import kotlinx.android.synthetic.main.activity_update_recipe.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class add_recipe : AppCompatActivity() {
    val REQUEST_CODE = 100
    var selectedPhotoUri: Uri? = null
    var thesingleurl = ""

    val date = getCurrentDateTime()
    val dateInString = date.toString("yyyy-MM-dd HH:mm:ss")
    val filenameRecipeUID = UUID.randomUUID().toString()
    var filenameRecipe = "$dateInString $filenameRecipeUID"

    var stepDataList :ArrayList<String> = arrayListOf()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_recipe)

        //actionbar
        val actionbar = supportActionBar
        //set actionbar title
        actionbar!!.title = "Add Recipe Activity"
        //set back button
        actionbar.setDisplayHomeAsUpEnabled(true)
        actionbar.setDisplayHomeAsUpEnabled(true)

        val spinner: Spinner = findViewById(R.id.addRecipe_planets_spinner)
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

        val stepData = intent.getParcelableExtra<StepClass>("stepData")
        if (stepData != null) {

            addRecipe_recipeName_editText.setText(stepData.recipeData.recipeName)
            stepDataList = stepData.stepData
            Log.d("importnat!!!!!", "${stepDataList.size}")
            //current no solution from this part
//            if(stepData.recipeData.recipeImageUri != null){
//                addRecipe_imageView.isVisible = true
//                addRecipe_imageButton.isVisible = false
//                addRecipe_imageView.setImageURI(stepData.recipeData.recipeImageUri)
//
//            }

            addRecipe_ingredient_editText.setText(stepData.recipeData.recipeIngredient)
            Log.d("Important !!!!!", "${ stepData.recipeData.recipeType}")
            if(stepData.recipeData.recipeType != null){

                addRecipe_planets_spinner.setSelection(
                    (addRecipe_planets_spinner.getAdapter() as ArrayAdapter<String?>).getPosition(
                        stepData.recipeData.recipeType
                    )
                )
            }

        }

        if(stepDataList.size > 0){
            addRecipe_step_linear.isVisible = true
            Log.d("importnat!!!!!", "got come in to this function")
            callRecycleView(stepDataList)
        }else{
            Log.d("importnat!!!!!", "no no no no come in ")
        }



        addRecipe_imageView.isVisible = false

        addRecipe_imageButton.setOnClickListener {
            Log.d("mainAcitivity", "image button")
            openGalleryForImage()
        }

        addRecipe_addButton.setOnClickListener {
            if(addRecipe_recipeName_editText.text.isNotEmpty()){
                Log.d("Add Recipe", "${addRecipe_recipeName_editText.text} ${spinner.selectedItem}")
                addRecipe_progressBar.isVisible = true
                getWindow().setFlags(
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                uploadImageToFirebaseStorage()

            }else{
                Toast.makeText(this, "Please Enter the Recipe Name", Toast.LENGTH_SHORT).show()
            }

        }
        addRecipe_new_step_button.setOnClickListener {
            var recipeData = RecipeClass(
                stepData = stepDataList,
                recipeImageUri = selectedPhotoUri,
                recipeImage = "",
                recipeName = "${addRecipe_recipeName_editText.text}",
                recipeType = addRecipe_planets_spinner.selectedItem.toString(),
                recipeUid = filenameRecipe,
                recipeIngredient = addRecipe_ingredient_editText.text.toString()
            )
            val intent = Intent(this, addStep::class.java)
            intent.putExtra("recipeData",recipeData)
            finish()
            startActivity(intent)
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
                addRecipe_imageView.isVisible = true
                addRecipe_imageButton.isVisible = false
                selectedPhotoUri = data?.data
                addRecipe_imageView.setImageURI(data?.data) // handle chosen image
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
                    saveUserToFirebaseDatabase(thesingleurl)
//                    saveUserToFirebaseDatabase(it.toString())
                }
            }
            .addOnFailureListener {
                Log.d("Add Recipe", "Failed to upload image to storage: ${it.message}")
            }
    }

    private fun saveUserToFirebaseDatabase(profileImageUrl: String) {


        val ref = FirebaseDatabase.getInstance().getReference("recipes/$filenameRecipe")

        val recipe = RecipeModel(

            recipeImage = profileImageUrl,
            recipeName = "${addRecipe_recipeName_editText.text}",
            recipeType = addRecipe_planets_spinner.selectedItem.toString(),
            recipeUid = filenameRecipe,
            recipeIngredient = addRecipe_ingredient_editText.text.toString()
        )

        ref.setValue(recipe)
            .addOnSuccessListener {
                if(saveStepIntoFirebase(stepDataList)){
                    addRecipe_progressBar.isVisible = false
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    Toast.makeText(this, "Add Successfull", Toast.LENGTH_SHORT).show()
                    Log.d("Add Recipe", "Finally we saved the user to Firebase Database")
                }

            }
            .addOnFailureListener {
                addRecipe_progressBar.isVisible = false
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                Toast.makeText(this , "Fail to add to firebase", Toast.LENGTH_SHORT).show()
                Log.d("Add Recipe", "Failed to set value to database: ${it.message}")
            }
    }

    fun saveStepIntoFirebase(stepDatalist: ArrayList<String>):Boolean{


        for(item in stepDatalist){
            val stepdate = getCurrentDateTime()
            val stepdateInString = stepdate.toString("yyyy-MM-dd HH:mm:ss")
            val stepfilenameRecipeUID = UUID.randomUUID().toString()
            var stepfileUid = "$stepdateInString $stepfilenameRecipeUID"

            val ref = FirebaseDatabase.getInstance().getReference("recipes/$filenameRecipe/stepData/$stepfileUid")

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
        val recyclerview = findViewById<RecyclerView>(R.id.addRecipe_recycleView)

        // this creates a vertical layout Manager
        recyclerview.layoutManager = LinearLayoutManager(this)

        val adapter = GroupAdapter<ViewHolder>()


        for(item in stepDatalist){
            adapter.add(StepItemListView(item))
        }


        recyclerview.adapter = adapter

    }


}


