package com.example.kimcuong

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.Display
import android.view.Window
import android.view.WindowManager
import android.widget.ImageButton
import android.widget.TableLayout
import android.widget.TableRow
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_main)
        var tn: TableLayout = findViewById(R.id.table)
        var worldTiles: Array<Array<ImageButton>>
        worldTiles = Array(10, {
            Array(10, {
                ImageButton(this)
            })
        })

    //    getScreen()
    }

    fun getScreen() {
        val displayMetrics = DisplayMetrics()
        val windowsManager = applicationContext.getSystemService(WINDOW_SERVICE) as WindowManager
        windowsManager.defaultDisplay.getMetrics(displayMetrics)
        val deviceWidth = displayMetrics.widthPixels
        val deviceHeight = displayMetrics.heightPixels
        print("Height : " + deviceHeight)
    }

}




