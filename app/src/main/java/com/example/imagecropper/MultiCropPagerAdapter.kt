package com.example.imagecropper

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Environment
import android.support.v4.view.PagerAdapter
import android.util.Log
import android.view.*
import android.widget.Button
import android.widget.Toast
import com.fenchtose.nocropper.CropperCallback
import com.fenchtose.nocropper.CropperView
import java.io.File
import java.io.FileInputStream


class MultiCropPagerAdapter(
    private val context: Context,
    private var imgList: ArrayList<String>
) :
    PagerAdapter() {
    object savedView : ArrayList<CropperView>() {

    }
    var cropperView:CropperView?=null
            private var originalBitmap: Bitmap? = null
    private var mBitmap: Bitmap? = null
    private var isSnappedToCenter = false
    //public  var savedView:ArrayList<CropperView> =ArrayList()

    private var rotationCount = 0
    override fun isViewFromObject(view: View, objects: Any): Boolean {
        return view === objects
    }

    override fun getCount(): Int {
        return imgList.size
    }

    override fun instantiateItem(collection: ViewGroup, position: Int): Any {

        val inflater = LayoutInflater.from(context)
        val layout = inflater.inflate(R.layout.item_crop_image, collection, false) as ViewGroup

         cropperView = layout.findViewById(R.id.cropper_view)

        //cropperView.maxZoom = 1.5f
       // cropperView.minZoom = 1.2f
        cropperView!!.cropToCenter()
        //cropperView!!.isGestureEnabled = true
        //cropperView!!.paddingColor = Color.parseColor("#ff282828")

        loadNewImage(imgList[position],cropperView!!)
       // cropperView.setImageBitmap(getBitmapFromFile(imgList[position]))
        /*btnCrop.setOnClickListener(View.OnClickListener {

performCrop(cropperView)        })*/

       /* cropperView!!.setOnTouchListener(object :View.OnTouchListener
        {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {

                Log.e("BeforeCrop page","Crop Page"+position)

                savedView.set(position,cropperView!!)

                Log.e("AfterCrop page","Crop Page"+position)

                return false
            }

        })*/
        collection.addView(layout)
        return layout
    }

    /*private fun getBitmapFromFile(filepath: String): Bitmap {

        val o = BitmapFactory.Options()
        o.inJustDecodeBounds = true
        BitmapFactory.decodeStream(FileInputStream(filepath), null, o)

        //The new size we want to scale to
        val REQUIRED_SIZE = 90

        //Find the correct scale value. It should be the power of 2.
        var scale = 1
        while (o.outWidth / scale / 2 >= REQUIRED_SIZE && o.outHeight / scale / 2 >= REQUIRED_SIZE)
            scale *= 2

        //Decode with inSampleSize
        val o2 = BitmapFactory.Options()
        o2.inSampleSize = scale
        return BitmapFactory.decodeStream(FileInputStream(filepath), null, o2)
    }
*/
    override fun destroyItem(collection: ViewGroup, position: Int, view: Any) {
        collection.removeView(view as View)
        //mBitmap!!.recycle()
    }


    private fun loadNewImage(filePath: String, cropperView: CropperView) {
        rotationCount = 0
        Log.i(TAG, "load image: $filePath")
        mBitmap = BitmapFactory.decodeFile(filePath)
        originalBitmap = mBitmap
        Log.i(TAG, "bitmap: " + mBitmap!!.getWidth() + " " + mBitmap!!.getHeight())

        val maxP = Math.max(mBitmap!!.getWidth(), mBitmap!!.getHeight())
        val scale1280 = maxP.toFloat() / 1280
        Log.i(TAG, "scaled: " + scale1280 + " - " + 1 / scale1280)

        if (cropperView.getWidth() != 0) {
            cropperView.setMaxZoom(cropperView.getWidth() * 2 / 1280f)
        } else {

            val vto = cropperView.getViewTreeObserver()
            vto.addOnPreDrawListener(object : ViewTreeObserver.OnPreDrawListener {
                override fun onPreDraw(): Boolean {
                    cropperView.getViewTreeObserver().removeOnPreDrawListener(this)
                    cropperView.setMaxZoom(cropperView.getWidth() * 2 / 1280f)
                    return true
                }
            })

        }

        mBitmap = Bitmap.createScaledBitmap(
            mBitmap, (mBitmap!!.getWidth() / scale1280).toInt(),
            (mBitmap!!.getHeight() / scale1280).toInt(), true
        )

        cropperView.setImageBitmap(mBitmap)

        savedView.add(cropperView)
    }






}