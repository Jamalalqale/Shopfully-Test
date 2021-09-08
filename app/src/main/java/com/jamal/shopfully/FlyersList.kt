package com.jamal.shopfully

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.View
import android.widget.GridView
import android.widget.LinearLayout
import android.widget.Switch
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.json.JSONArray
import org.json.JSONException
import java.lang.reflect.Type


class FlyersList : AppCompatActivity() {


    private lateinit var customAdapter: CustomAdapter
    val flyersListAdapter: List<Items> = ArrayList<Items>()
    var jsonArray: JSONArray? = null
    private var requestQueue: RequestQueue? = null
    private lateinit var seen_note_linearlayout: LinearLayout
    private lateinit var emptylist_linearlayout: LinearLayout
    private lateinit var gridview: GridView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.flyers_list_layout)


        seen_note_linearlayout = findViewById(R.id.seen_note_linearlayout)
        seen_note_linearlayout?.visibility = View.GONE


        emptylist_linearlayout = findViewById(R.id.emptylist_linearlayout)
        emptylist_linearlayout?.visibility = View.GONE


        gridview = findViewById(R.id.gridview)
        gridview?.visibility = View.VISIBLE



        requestQueue = Volley.newRequestQueue(this)
        requestFlyersAPI()


    }


    private fun requestFlyersAPI() {

        val url = "https://run.mocky.io/v3/94da1ce3-3d3f-414c-8857-da813df3bb05"
        val request = JsonObjectRequest(Request.Method.GET, url, null, { response ->
            try {

                jsonArray = response.getJSONArray("data")
                parseFullJSON()

            } catch (e: JSONException) {

                e.printStackTrace()
                Log.d("Error", e.printStackTrace().toString())
            }
        }, { error -> error.printStackTrace() })
        requestQueue?.add(request)
    }


    private fun parseFullJSON() {

        customAdapter = CustomAdapter(this, flyersListAdapter as ArrayList<Items>)
        customAdapter.clearAdapter()

        for (i in 0 until jsonArray!!.length()) {

            val employee = jsonArray!!.getJSONObject(i)
            val id = employee.getString("id")
            val retailer_id = employee.getString("retailer_id")
            val title = employee.getString("title")
            val image_url = "https://it-it-media.shopfully.cloud/images/volantini/" + id + "@3x.jpg"

            //checkcache(id): Check if the id of the flyers exists  in the cache which it has been seen.
            // the return values are True or False

            flyersListAdapter.add(Items(id, retailer_id, title, image_url, checkcache(id)))


        }

        if (flyersListAdapter.size>0)
        {
            seen_note_linearlayout?.visibility = View.GONE
            emptylist_linearlayout?.visibility = View.GONE
            gridview?.visibility = View.VISIBLE

            gridview.adapter = customAdapter

        }


            else
        {
            gridview.adapter = customAdapter
            gridview?.visibility = View.GONE
            seen_note_linearlayout?.visibility = View.GONE
            emptylist_linearlayout?.visibility = View.VISIBLE

        }

    }


    private fun parseFilteredJSON() {

        customAdapter = CustomAdapter(this, flyersListAdapter as ArrayList<Items>)
        customAdapter.clearAdapter()

        for (i in 0 until jsonArray!!.length()) {

            val employee = jsonArray!!.getJSONObject(i)
            val id = employee.getString("id")
            val retailer_id = employee.getString("retailer_id")
            val title = employee.getString("title")
            val image_url = "https://it-it-media.shopfully.cloud/images/volantini/" + id + "@3x.jpg"

            // push to the listAdapter only the unseen flyers
            if (checkcache(id) != true) {

                flyersListAdapter.add(Items(id, retailer_id, title, image_url, checkcache(id)))


            }


        }


        if (flyersListAdapter.size>0)
        {
            seen_note_linearlayout?.visibility = View.GONE
            emptylist_linearlayout?.visibility = View.GONE
            gridview?.visibility = View.VISIBLE

            gridview.adapter = customAdapter
        }

        else
            {
                gridview.adapter = customAdapter
                gridview?.visibility = View.GONE
                seen_note_linearlayout?.visibility = View.VISIBLE
                emptylist_linearlayout?.visibility = View.GONE
            }






    }


    private fun checkcache(id: String): Boolean {

        //get from shared prefs
        val gson = Gson()
        val prefs = getSharedPreferences("sharedPreferences", MODE_PRIVATE)
        val storedHashMapString = prefs.getString("hashString", null)

        // check if there's is cached values
        if (storedHashMapString != null) {

            val type: Type = object : TypeToken<HashMap<String?, Boolean?>?>() {}.getType()
            val hashMapOfIds: HashMap<String, Boolean> = gson.fromJson(storedHashMapString, type)

            if (hashMapOfIds.contains(id))
                if (id in hashMapOfIds)
                    return true
                else
                    return false
            else
                return false

        }

        return false

    }


    @SuppressLint("UseSwitchCompatOrMaterialCode")
    override fun onCreateOptionsMenu(menu: Menu): Boolean {

        menuInflater.inflate(R.menu.actionbar_menu, menu)
        val itemswitch = menu.findItem(R.id.action_id)
        itemswitch.setActionView(R.layout.seen_switch)
        val sw = menu.findItem(R.id.action_id).actionView.findViewById<View>(R.id.seen_switch) as Switch
        sw.setOnCheckedChangeListener { buttonView, isChecked ->

            if (isChecked)
               parseFilteredJSON()

            else

                parseFullJSON()


        }
        return true
    }


}