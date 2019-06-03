package com.example.imagecropper

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.graphics.Bitmap
import android.os.AsyncTask
import android.os.Bundle
import android.os.Environment
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import com.fenchtose.nocropper.CropState
import com.fenchtose.nocropper.CropperCallback
import com.fenchtose.nocropper.CropperView
import java.io.File
import java.io.IOException
import java.util.*

class MultiImageCropperActivity : AppCompatActivity() {

    private var viewPager: ViewPager? = null
    private var btnCrop: Button? = null
    private var imgList: ArrayList<String>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_multi_image_cropper)

        initiateViews()
    }

    private fun initiateViews() {

        getIntentData()

        viewPager = findViewById(R.id.viewPager)
        btnCrop = findViewById(R.id.btnCrop)

        viewPager!!.adapter = MultiCropPagerAdapter(this, imgList!!)
        viewPager!!.offscreenPageLimit=imgList!!.size

        btnCrop!!.setOnClickListener(
            View.OnClickListener {
                cropImage(MultiCropPagerAdapter.savedView)
            }
        )

    }

    private fun getIntentData() {

        val intent = intent

        if (intent != null) {

            imgList = intent.getStringArrayListExtra("List")
        }

}
 fun performCrop(cropperViewList:ArrayList<CropperView>) {


     runOnUiThread(Runnable {
         for (i in 0 until cropperViewList.size) {
              cropperViewList.get(i)!!.getCroppedBitmapAsync(object : CropperCallback() {

                 override fun onCropped(bitmap: Bitmap?) {

                     if (bitmap != null) {

                         val file =
                             File(Environment.getExternalStorageDirectory().toString() + "/crop_test_" + Date().time + ".jpg")

                         Toast.makeText(this@MultiImageCropperActivity, "Image Saved $file", Toast.LENGTH_SHORT)
                             .show()

                         BitmapUtils.writeBitmapToFile(
                             bitmap,
                             file,
                             100
                         )
                     } else {

                         Toast.makeText(this@MultiImageCropperActivity, "Image Not Saved", Toast.LENGTH_SHORT)
                             .show()
                     }

                 }


             })

         }

     })








 }

     fun cropImage(cropperViewList:ArrayList<CropperView>) {


         class AsynLoader : AsyncTask<Void, String, String>()
         {
             var dialog:ProgressDialog ?=null

             override fun onPreExecute() {
                 super.onPreExecute()
                 dialog= ProgressDialog(this@MultiImageCropperActivity)
                 dialog!!.setTitle("Loading....")
                 dialog!!.show()


             }
             @SuppressLint("WrongThread")
             override fun doInBackground(vararg params: Void?): String {


                 for (i in 0 until cropperViewList.size) {
                     var result = cropperViewList.get(i).croppedBitmap


                     var bitmap = result.getBitmap()

                     if (bitmap != null) {
                         Log.d("Cropper", "crop1 bitmap: " + bitmap!!.getWidth() + ", " + bitmap!!.getHeight())
                         try {
                             BitmapUtils.writeBitmapToFile(
                                 bitmap,
                                 File(Environment.getExternalStorageDirectory().toString() + "/crop_test_" + i +".jpg"),
                                 90
                             )
                         } catch (e: IOException) {
                             e.printStackTrace()
                         }

                     }


                 }

                 return ""

             }


             override fun onPostExecute(result: String?) {
                 super.onPostExecute(result)

                 dialog!!.dismiss()

             }
             }

         AsynLoader().execute()



     }
}
