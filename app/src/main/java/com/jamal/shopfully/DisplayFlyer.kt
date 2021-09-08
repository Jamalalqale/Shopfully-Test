package com.jamal.shopfully

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.squareup.picasso.Picasso
import java.lang.reflect.Type

class DisplayFlyer : AppCompatActivity() {


    val gson = Gson()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.display_flyer)


        //actionbar
        val actionbar = supportActionBar
        //set actionbar title
        actionbar!!.title = "Alqale ShopFully Test"
        //set back button
        actionbar.setDisplayHomeAsUpEnabled(true)
        actionbar.setDisplayHomeAsUpEnabled(true)

        // receive values from FlyersList Activity
        val id: String = getIntent().getStringExtra("id").toString()
        val title: String =getIntent().getStringExtra("title").toString()
        val image: String =getIntent().getStringExtra("image").toString()

        val flyer_image: ImageView = findViewById(R.id.flyer_image)
        val flyer_title: TextView = findViewById(R.id.flyer_title)

        flyer_title?.setText(title)
        Picasso.get().load(image).into(flyer_image);

        val prefs = getSharedPreferences("sharedPreferences", MODE_PRIVATE)
        val storedHashMapString = prefs.getString("hashString", null)


        if (storedHashMapString!=null)
        {

            val storedHashMapString = prefs.getString("hashString", null)
            val type: Type = object : TypeToken<HashMap<String?, Boolean?>?>() {}.getType()
            val hashMapOfIds: HashMap<String, Boolean> = gson.fromJson(storedHashMapString, type)
            hashMapOfIds[id] = true
            val hashMapString: String = gson.toJson(hashMapOfIds)
            prefs.edit().putString("hashString", hashMapString).apply()

        }

        else
        {
            val hashMapOfIds = HashMap<String, Boolean>()
            hashMapOfIds[id] = true
            val hashMapString: String = gson.toJson(hashMapOfIds)
            prefs.edit().putString("hashString", hashMapString).apply()

        }
        //Toast.makeText(this, storedHashMapString, Toast.LENGTH_SHORT).show()










    }


    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        val intent = Intent(this, FlyersList::class.java)
        startActivity(intent)
        return true
    }

}