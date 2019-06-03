package com.example.imagecropper

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import com.coursion.freakycoder.mediapicker.galleries.Gallery

class ChooseImageActivity : AppCompatActivity() {

    private var btnChoose: Button? = null

    companion object {
        private const val GALLERY_REQUEST_CODE = 200
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choose_image)

        initiateViews()
    }

    private fun initiateViews() {

        btnChoose = findViewById(R.id.btnChoose)

        btnChoose!!.setOnClickListener {

            val intent = Intent(this, Gallery::class.java)
            intent.putExtra("title", "Select media")
            intent.putExtra("mode", 2)
            intent.putExtra("maxSelection", 10)
            startActivityForResult(intent, GALLERY_REQUEST_CODE)
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {

            if (requestCode == GALLERY_REQUEST_CODE) {

                val selectionResult = data!!.getStringArrayListExtra("result")

                val intent = Intent(this, MultiImageCropperActivity::class.java)
                intent.putStringArrayListExtra("List", selectionResult)
                startActivity(intent)

            }
        }
    }
}