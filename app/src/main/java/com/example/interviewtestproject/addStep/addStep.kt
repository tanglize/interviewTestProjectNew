package com.example.interviewtestproject.addStep

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import android.widget.Toast
import androidx.core.view.isVisible
import com.example.interviewtestproject.R
import com.example.interviewtestproject.addRecipe.add_recipe
import com.example.interviewtestproject.model.RecipeClass
import com.example.interviewtestproject.model.RecipeModel
import com.example.interviewtestproject.model.StepClass
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_add_recipe.*
import kotlinx.android.synthetic.main.activity_add_step.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class addStep : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_step)

        //actionbar
        val actionbar = supportActionBar
        //set actionbar title
        actionbar!!.title = "Add Step Activity"
        //set back button
        actionbar.setDisplayHomeAsUpEnabled(true)
        actionbar.setDisplayHomeAsUpEnabled(true)

        var stepDataList:ArrayList<String> = arrayListOf()
        var recipeData = intent.getParcelableExtra<RecipeClass>("recipeData")
        if (recipeData != null) {
            stepDataList = recipeData.stepData
        }

        addStep_button.setOnClickListener {
            stepDataList.add(addStep_description_editText.text.toString())

            if (recipeData != null) {
//                saveStepsToFirebaseDatabase(previosUid)
                var addStep = StepClass(
                    stepData = stepDataList,
                    recipeData = recipeData

                )

                var intent = Intent(this, add_recipe::class.java)
                intent.putExtra("stepData", addStep)
                finish()
                startActivity(intent)



            }
        }


    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun saveStepsToFirebaseDatabase(previosFileUid : String) {

        val date = getCurrentDateTime()
        val dateInString = date.toString("yyyy-MM-dd HH:mm:ss")
        val filenameRecipeUID = UUID.randomUUID().toString()
        val fileNameUid = "$dateInString $filenameRecipeUID"

        val ref = FirebaseDatabase.getInstance().getReference("recipes/$previosFileUid/step/$fileNameUid")

        var deteils = addStep_description_editText.text.toString()

        ref.setValue(deteils)
            .addOnSuccessListener {
                var intent = Intent(this, add_recipe::class.java)
                intent.putExtra("currentUid", previosFileUid)
                finish()
                startActivity(intent)
//                addRecipe_progressBar.isVisible = false
//                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
//                Toast.makeText(this, "Add Successfull", Toast.LENGTH_SHORT).show()
                Log.d("Add Recipe", "Finally we saved the user to Firebase Database")
//                if(theHostelID.isNotEmpty()) {
//                    Log.d("Add Recipe", "peform add mutiple image ????????????????")
//                    performCreatetheAmenties(filenameHostel)
//                    performCreateAddReport(theHostelID,hostel_name.text.toString())
//                    MultipleUploadImageToFirebaseStorage()
//                }else{
//                    Log.d(TAG, "${theHostelID.toString()} is empty")
//                }
            }
            .addOnFailureListener {
//                progressBar.visibility = View.GONE
                Toast.makeText(this , "Fail to add to firebase", Toast.LENGTH_SHORT).show()
                Log.d("Add Recipe", "Failed to set value to database: ${it.message}")
            }
    }


    fun getCurrentDateTime(): Date {
        return Calendar.getInstance().time
    }

    fun Date.toString(format: String, locale: Locale = Locale.getDefault()): String {
        val formatter = SimpleDateFormat(format, locale)
        return formatter.format(this)
    }
}