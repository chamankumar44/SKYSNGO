package ngo.ganaur.skys.skysngo.view

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.InterstitialAd;
import com.mopub.common.MoPub
import com.mopub.common.SdkConfiguration
import com.mopub.mobileads.MoPubErrorCode
import com.mopub.mobileads.MoPubInterstitial
import com.mopub.mobileads.MoPubView
import com.mopub.mobileads.MoPubView.MoPubAdSize
import kotlinx.android.synthetic.main.activity_donate.*
import ngo.ganaur.skys.skysngo.R


class DonateActivity : AppCompatActivity() , MoPubView.BannerAdListener {

     var name : String = ""
     var amount : String =""

    internal val UPI_PAYMENT = 0

    private lateinit var mInterstitialAd: InterstitialAd

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_donate)

        MobileAds.initialize(this) {
            mInterstitialAd = InterstitialAd(this);
            mInterstitialAd.adUnitId = "ca-app-pub-8118826265420904/7221057852"
            mInterstitialAd.loadAd(AdRequest.Builder().build())

            if (mInterstitialAd.isLoaded) {
                mInterstitialAd.show()
            }

        }

        mInterstitialAd = InterstitialAd(this);
        mInterstitialAd.adUnitId = "ca-app-pub-8118826265420904/7221057852"
        mInterstitialAd.loadAd(AdRequest.Builder().build())

        mInterstitialAd.adListener = object : AdListener() {
            override fun onAdClosed() {
                mInterstitialAd.loadAd(AdRequest.Builder().build())
            }
        }

        if (mInterstitialAd.isLoaded) {
            mInterstitialAd.show()
        }


        bt_pay.setOnClickListener {
            //Getting the values from the EditTexts
            val amount = et_amount.text.toString()
            val note = "Test  "+et_name.text.toString()
            val name = "Chaman"
            val upiId = "9899206384@icici"
//            val upiId = "chamankumar44@axisbank"
//            val upiId = "rajesh.panchal5@icici"
            payUsingUpi(amount, upiId, name, note)
        }

        checkForNameAndAmountLenght()
    }


    fun loadMoPubads(){

//        mopub_add.setAdUnitId('YOUR_AD_UNIT_ID');
//        mopub_add.setAdSize(MoPubAdSize); // Call this if you want to set an ad size programmatically
//        mopub_add.loadAd();
//        mopub_add.setBannerAdListener(this);
//        mopub_add.setBannerAdListener(this);
//
//        val mInterstitial: MoPubInterstitial
//        mInterstitial = MoPubInterstitial(this, AD_UNIT_ID)
//        mInterstitial.setInterstitialAdListener(this);

    }

    fun checkForNameAndAmountLenght(){
        et_name.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                enablePayButton()
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                name = p0.toString()
            }
        })

        et_amount.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                enablePayButton()
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                amount = p0.toString()
            }
        })


    }

    fun enablePayButton (){
        if(name.length>0&&amount.length>0){
            bt_pay.isEnabled= true
            bt_pay.isClickable = true}
        else{
            bt_pay.isEnabled= false
        bt_pay.isClickable = false }
    }



    fun payUsingUpi(amount: String, upiId: String, name: String, note: String) {

        val uri = Uri.parse("upi://pay").buildUpon()
                .appendQueryParameter("pa", upiId)
                .appendQueryParameter("pn", name)
                .appendQueryParameter("tn", note)
                .appendQueryParameter("am", amount)
                .appendQueryParameter("cu", "INR")
                .build()


        val upiPayIntent = Intent(Intent.ACTION_VIEW)
        upiPayIntent.data = uri

        // will always show a dialog to user to choose an app
        val chooser = Intent.createChooser(upiPayIntent, "Pay with")

        // check if intent resolves
        if (null != chooser.resolveActivity(packageManager)) {
            startActivityForResult(chooser, UPI_PAYMENT)
        } else {
            Toast.makeText(this@DonateActivity, "No UPI app found, please install one to continue", Toast.LENGTH_SHORT).show()
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            UPI_PAYMENT -> if (Activity.RESULT_OK == resultCode || resultCode == 11) {
                if (data != null) {
                    val trxt = data.getStringExtra("response")
                    Log.d("UPI", "onActivityResult: $trxt")
                    val dataList = ArrayList<String>()
                    trxt?.let { dataList.add(it) }
                    upiPaymentDataOperation(dataList)
                } else {
                    Log.d("UPI", "onActivityResult: " + "Return data is null")
                    val dataList = ArrayList<String>()
                    dataList.add("nothing")
                    upiPaymentDataOperation(dataList)
                }
            } else {
                Log.d("UPI", "onActivityResult: " + "Return data is null") //when user simply back without payment
                val dataList = ArrayList<String>()
                dataList.add("nothing")
                upiPaymentDataOperation(dataList)
            }
        }
    }

    private fun upiPaymentDataOperation(data: ArrayList<String>) {
        if (isConnectionAvailable(this@DonateActivity)) {
            var str: String? = data[0]
            Log.d("UPIPAY", "upiPaymentDataOperation: " + str!!)
            var paymentCancel = ""
            if (false) str = "discard"
            var status = ""
            var approvalRefNo = ""
            val response = str.split("&".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            for (i in response.indices) {
                val equalStr = response[i].split("=".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                if (equalStr.size >= 2) {
                    if (equalStr[0].toLowerCase() == "Status".toLowerCase()) {
                        status = equalStr[1].toLowerCase()
                    } else if (equalStr[0].toLowerCase() == "ApprovalRefNo".toLowerCase() || equalStr[0].toLowerCase() == "txnRef".toLowerCase()) {
                        approvalRefNo = equalStr[1]
                    }
                } else {
                    paymentCancel = "Payment cancelled by user."
                }
            }

            if (status == "success") {
                //Code to handle successful transaction here.
                Toast.makeText(this@DonateActivity, "Transaction successful.", Toast.LENGTH_SHORT).show()
                Log.d("UPI", "responseStr: $approvalRefNo")
            } else if ("Payment cancelled by user." == paymentCancel) {
                Toast.makeText(this@DonateActivity, "Payment cancelled by user.", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this@DonateActivity, "Transaction failed.Please try again", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this@DonateActivity, "Internet connection is not available. Please check and try again", Toast.LENGTH_SHORT).show()
        }
    }

    companion object {

        @SuppressLint("ServiceCast")
        fun isConnectionAvailable(context: Context): Boolean {
            val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            if (true) {
                val netInfo = connectivityManager.activeNetworkInfo
                if (netInfo != null && netInfo.isConnected
                        && netInfo.isConnectedOrConnecting
                        && netInfo.isAvailable) {
                    return true
                }
            }
            return false
        }
    }
//
//    fun loadMopubAdd(){
//
//        mopub_add.setAdUnitId("xxxxxxxxxxx"); // Enter your Ad Unit ID from www.mopub.com
//        mopub_add.setAdSize(MoPubAdSize); // Call this if you are not setting the ad size in XML or wish to use an ad size other than what has been set in the XML. Note that multiple calls to `setAdSize()` will override one another, and the MoPub SDK only considers the most recent one.
//        mopub_add.loadAd(MoPubAdSize); // Call this if you are not calling setAdSize() or setting the size in XML, or if you are using the ad size that has not already been set through either setAdSize() or in the XML
//        mopub_add.loadAd();
//
//
//        val configBuilder = SdkConfiguration.Builder("YOUR_AD_UNIT_ID")
//
//        MoPub.initializeSdk(this, configBuilder.build(), initSdkListener())
//
//    }

    override fun onBannerExpanded(banner: MoPubView?) {
        TODO("Not yet implemented")
    }

    override fun onBannerLoaded(banner: MoPubView) {
        TODO("Not yet implemented")
    }

    override fun onBannerCollapsed(banner: MoPubView?) {
        TODO("Not yet implemented")
    }

    override fun onBannerFailed(banner: MoPubView?, errorCode: MoPubErrorCode?) {
        TODO("Not yet implemented")
    }

    override fun onBannerClicked(banner: MoPubView?) {
        TODO("Not yet implemented")
    }
}



