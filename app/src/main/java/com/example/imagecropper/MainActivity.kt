package com.example.imagecropper

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Bundle
import android.os.Environment
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import android.widget.Toast
import com.fenchtose.nocropper.CropperCallback
import com.fenchtose.nocropper.CropperView
import java.io.File


class MainActivity : AppCompatActivity() {

    private var cropperView: CropperView? = null
    private var btnCrop: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        cropperView = findViewById(R.id.cropper_view)
        btnCrop = findViewById(R.id.btnCrop)

        cropperView!!.maxZoom = 1.5f
        cropperView!!.minZoom = 1.2f

        cropperView!!.cropToCenter()

        cropperView!!.isGestureEnabled = true
        cropperView!!.paddingColor = Color.parseColor("#ff282828")

        val icon = BitmapFactory.decodeResource(
            resources,
            R.drawable.fb
        )

        cropperView!!.setImageBitmap(icon)

        btnCrop!!.setOnClickListener {

            val state=cropperView!!.getCroppedBitmapAsync(object :CropperCallback(){

                override fun onCropped(bitmap: Bitmap?) {

                    if(bitmap!=null){

                        val file=File(Environment.getExternalStorageDirectory().toString() + "/crop_test.jpg")

                        Toast.makeText(this@MainActivity, "Image Saved $file",Toast.LENGTH_SHORT).show()

                        BitmapUtils.writeBitmapToFile(
                            bitmap,
                            file,
                            100)
                    }else{

                        Toast.makeText(this@MainActivity,"Image Not Saved",Toast.LENGTH_SHORT).show()
                    }

                }
            })
        }
    }

}
