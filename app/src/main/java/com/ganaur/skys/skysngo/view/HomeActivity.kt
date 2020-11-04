package ngo.ganaur.skys.skysngo.view

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ngo.ganaur.skys.skysngo.R
import ngo.ganaur.skys.skysngo.adapter.ImageAdapter
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager

    var dataList = ArrayList<String> ()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
//        setSupportActionBar(toolbar!!)

        init()


        fab.setOnClickListener { view ->
            donateActivityIntent()
        }
    }




    fun init(){
        loadArrayList()
        viewManager = LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false)
        viewAdapter = ImageAdapter(this,dataList)

        recyclerView = findViewById<RecyclerView>(R.id.rec_images).apply {
            setHasFixedSize(true);
           setItemViewCacheSize(60);
           setDrawingCacheEnabled(true);
            setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

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
                startActivity(Intent(this, AboutUsActivity::class.java))
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    fun donateActivityIntent(){
        startActivity(Intent(this, DonateActivity::class.java))
    }




    fun loadArrayList(){
        for(i in 0 until 58){
            dataList.add(("http://test.bluequator.com" .plus(
                    "/my/skys/skys_img/img").plus(String.format("%02d", i)).plus(".jpg")))

        }

    }

}
