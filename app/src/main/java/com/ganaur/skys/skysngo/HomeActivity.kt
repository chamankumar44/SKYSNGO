package com.ganaur.skys.skysngo

import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Menu
import android.view.MenuItem
import com.ganaur.skys.skysngo.adapter.ImageAdapter
import com.ganaur.skys.skysngo.network_http.HttpNetwork
import kotlinx.android.synthetic.main.activity_home.*
import java.io.File

class HomeActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager

    var dataList = ArrayList<String> ()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        setSupportActionBar(toolbar)

        init()


        fab.setOnClickListener { view ->
            donateActivityIntent()
        }
    }




    fun init(){
        loadArrayList()
        viewManager = LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false)
        viewAdapter = ImageAdapter(dataList)

        recyclerView = findViewById<RecyclerView>(R.id.rec_images).apply {
            setHasFixedSize(true)

            // use a linear layout manager
            layoutManager = viewManager

            // specify an viewAdapter (see also next example)
            adapter = viewAdapter
    }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_home, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.about_us ->{
                startActivity(Intent(this,AboutUsActivity::class.java))
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    fun donateActivityIntent(){
        startActivity(Intent(this,DonateActivity::class.java))
    }




    fun loadArrayList(){
        dataList.add("http://test.bluequator.com/images/icon_clock.png")
        dataList.add("http://test.bluequator.com/images/icon_star.png")
        dataList.add("http://test.bluequator.com/images/404_background.jpg")

    }

}
