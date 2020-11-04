package ngo.ganaur.skys.skysngo.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ngo.ganaur.skys.skysngo.R

import com.squareup.picasso.MemoryPolicy
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_image.*
import android.view.MotionEvent
import android.graphics.PointF
import android.graphics.Matrix
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.view.View.OnTouchListener
import android.widget.Toast
import com.google.android.gms.ads.*


class ImageActivity : AppCompatActivity() {
    // These matrices will be used to move and zoom image
    var matrix = Matrix()
    var savedMatrix = Matrix()

    // We can be in one of these 3 states
    val NONE = 0
    val DRAG = 1
    val ZOOM = 2
    var mode = NONE

    // Remember some things for zooming
    var start = PointF()
    var mid = PointF()
    var oldDist = 1f
    var savedItemClicked: String? = null

    lateinit var mAdView : AdView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image)

        iv_image.setOnTouchListener { v, event ->
            onTouch(v,event)
        }


        Picasso.with(this)
                .load(intent.extras?.getString("url"))
                .fit()
                .placeholder(R.drawable.skys)
                .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
                .into(iv_image)




        MobileAds.initialize(this) {}

        mAdView = findViewById(R.id.adView)
//        mAdView.adUnitId = "ca-app-pub-8118826265420904/6697271721"
        val adRequest = AdRequest.Builder().build()
        mAdView.loadAd(adRequest)



//        mAdView.adListener = object: AdListener() {
//            override fun onAdLoaded() {
//            Toast.makeText(applicationContext,"onAdLoaded",Toast.LENGTH_SHORT).show()
//            }
//
//            override fun onAdFailedToLoad(adError : LoadAdError) {
//                Toast.makeText(applicationContext,"onAdFailedToLoad",Toast.LENGTH_SHORT).show()
//            }
//
//            override fun onAdOpened() {
//                Toast.makeText(applicationContext,"onAdOpened",Toast.LENGTH_SHORT).show()
//                // Code to be executed when an ad opens an overlay that
//                // covers the screen.
//            }
//
//            override fun onAdClicked() {
//                Toast.makeText(applicationContext,"onAdClicked",Toast.LENGTH_SHORT).show()
//                // Code to be executed when the user clicks on an ad.
//            }
//
//            override fun onAdLeftApplication() {
//                Toast.makeText(applicationContext,"onAdLeftApplication",Toast.LENGTH_SHORT).show()
//                // Code to be executed when the user has left the app.
//            }
//
//            override fun onAdClosed() {
//                Toast.makeText(applicationContext,"onAdClosed",Toast.LENGTH_SHORT).show()
//                // Code to be executed when the user is about to return
//                // to the app after tapping on an ad.
//            }
//        }

    }

    override fun onResume() {
        super.onResume()



            }



     fun onTouch(v: View, event: MotionEvent): Boolean {
        // TODO Auto-generated method stub

        val view = v as ImageView
        dumpEvent(event)

        // Handle touch events here...
        when (event.action and MotionEvent.ACTION_MASK) {
            MotionEvent.ACTION_DOWN -> {
                savedMatrix.set(matrix)
                start.set(event.x, event.y)
                mode = DRAG.toInt()
            }
            MotionEvent.ACTION_POINTER_DOWN -> {
                oldDist = spacing(event)

                if (oldDist > 10f) {
                    savedMatrix.set(matrix)
                    midPoint(mid, event)
                    mode = ZOOM

                }
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_POINTER_UP -> {
                mode = NONE

            }
            MotionEvent.ACTION_MOVE -> if (mode == DRAG) {
                // ...
                matrix.set(savedMatrix)
                matrix.postTranslate(event.x - start.x, event.y - start.y)
            } else if (mode == ZOOM) {
                val newDist = spacing(event)
                if (newDist > 10f) {
                    matrix.set(savedMatrix)
                    val scale = newDist / oldDist
                    matrix.postScale(scale, scale, mid.x, mid.y)
                }
            }
        }

        view.setImageMatrix(matrix)
        return true
    }

    private fun dumpEvent(event: MotionEvent) {
        val names = arrayOf("DOWN", "UP", "MOVE", "CANCEL", "OUTSIDE", "POINTER_DOWN", "POINTER_UP", "7?", "8?", "9?")
        val sb = StringBuilder()
        val action = event.action
        val actionCode = action and MotionEvent.ACTION_MASK
        sb.append("event ACTION_").append(names[actionCode])
        if (actionCode == MotionEvent.ACTION_POINTER_DOWN || actionCode == MotionEvent.ACTION_POINTER_UP) {
            sb.append("(pid ").append(
                    action shr MotionEvent.ACTION_POINTER_ID_SHIFT)
            sb.append(")")
        }
        sb.append("[")
        for (i in 0 until event.pointerCount) {
            sb.append("#").append(i)
            sb.append("(pid ").append(event.getPointerId(i))
            sb.append(")=").append(event.getX(i).toInt())
            sb.append(",").append(event.getY(i).toInt())
            if (i + 1 < event.pointerCount)
                sb.append(";")
        }
        sb.append("]")

    }

    /** Determine the space between the first two fingers  */
    private fun spacing(event: MotionEvent): Float {
        val x = event.getX(0) - event.getX(1)
        val y = event.getY(0) - event.getY(1)
        return Math.sqrt(x * x + y * y.toDouble()).toFloat()
    }

    /** Calculate the mid point of the first two fingers  */
    private fun midPoint(point: PointF, event: MotionEvent) {
        val x = event.getX(0) + event.getX(1)
        val y = event.getY(0) + event.getY(1)
        point.set(x / 2, y / 2)
    }



}
