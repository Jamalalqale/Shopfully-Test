package com.jamal.shopfully




class Items(private var id:String, private var retailer_id: String, private var title: String, private var image: String, private var isRead: Boolean) {

    fun get_id(): String { return id  }
    fun get_retailer_id(): String { return retailer_id }
    fun get_title(): String { return title  }
    fun get_image(): String { return image  }
    fun get_isRead(): Boolean { return isRead  }
}