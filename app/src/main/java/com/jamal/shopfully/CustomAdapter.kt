package com.jamal.shopfully


import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.squareup.picasso.Picasso


class CustomAdapter(private var c: Context, private var itemsArrayList: ArrayList<Items>) : BaseAdapter() {




    override fun getCount(): Int   {  return itemsArrayList.size  }
    override fun getItem(i: Int): Any {  return itemsArrayList[i] }
    override fun getItemId(i: Int): Long { return i.toLong()}

    override fun getView(i: Int, view: View?, viewGroup: ViewGroup): View? {
        var view = view
        if (view == null) {
            //inflate layout resource if its null
            view = LayoutInflater.from(c).inflate(R.layout.gridview_item, viewGroup, false)
        }


        val items = this.getItem(i) as Items

        val flyer_title = view?.findViewById<TextView>(R.id.flyer_title_main)
        val is_seen_icon = view?.findViewById<ImageView>(R.id.is_seen_icon)
        val backgroundImage = view?.findViewById<ImageView>(R.id.backgroundImage)




        flyer_title?.setText(items.get_title())
        if(items.get_isRead())
            is_seen_icon?.visibility = View.VISIBLE
        else
            is_seen_icon?.visibility = View.GONE

        Picasso.get().load(items.get_image()).into(backgroundImage);

        //handle itemclicks for the GridView
        view?.setOnClickListener {

            val intent = Intent(c, DisplayFlyer::class.java)
            intent.putExtra("id", items.get_id())
            intent.putExtra("title", items.get_title())
            intent.putExtra("image", items.get_image())
            c.startActivity(intent)
        }

        return view
    }


    fun clearAdapter() {
        itemsArrayList.clear()
        notifyDataSetChanged()
    }




}
