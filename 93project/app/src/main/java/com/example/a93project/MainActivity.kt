package com.example.a93project

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.provider.MediaStore
import android.view.View
import android.widget.*
import java.lang.Exception
import kotlin.math.pow

class MainActivity : AppCompatActivity() {
    //建立兩個數值，用於計算體重與身高的進度
    private var progressweight = 0


    //建立變數以利後續綁定元件
    private lateinit var btn_start: Button
    private lateinit var sb_weight: EditText
    private lateinit var sb_height: EditText
    private lateinit var btn_boy: RadioButton
    private lateinit var progressBar2: ProgressBar
    private lateinit var tv_weight: TextView
    private lateinit var tv_fat: TextView
    private lateinit var tv_progress: TextView
    private lateinit var ll_progress: LinearLayout


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        btn_start = findViewById(R.id.btn_calculate)
        sb_weight = findViewById(R.id.ed_weight)
        sb_height = findViewById(R.id.ed_height)
        btn_boy = findViewById(R.id.btn_boy)
        tv_weight = findViewById(R.id.tv_weight)
        progressBar2 = findViewById(R.id.progressBar2)
        tv_fat = findViewById(R.id.tv_fat)
        tv_progress = findViewById(R.id.tv_progress)
        ll_progress = findViewById(R.id.ll_progress)
        //對開始按鈕設定監聽器
        btn_start.setOnClickListener {
            val msg = Message()
            when {
                sb_height.length() < 1 -> msg.what = 1
                sb_weight.length() < 1 -> msg.what = 2

                else -> runRabbit()
            }
            handler.sendMessage(msg)
        }
    }


    //建立 showToast 方法顯示 Toast 訊息
    private fun showToast(msg: String) =
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()

    //建立 Handler 物件接收訊息
    private val handler = Handler(Looper.getMainLooper()) { msg ->
        //判斷編號，並更新兔子的進度

        if (msg.what == 1) {
            showToast("你沒有身高")
        }
        if (msg.what == 2) {
            showToast("你沒有體重")
        }
        if (msg.what == 5) {
            progressBar2.progress = progressweight
            tv_progress.text = "$progressweight"
        }
        if (msg.what == 10){
            progressBar2.progress = 0
            tv_progress.text = "0%"
            ll_progress.visibility = View.GONE
            val height = sb_height.text.toString().toDouble() //身高
            val weight = sb_height.text.toString().toDouble() //體重
            val stand_weight :Double
            val body_fat :Double
            if (btn_boy.isChecked){
                stand_weight = (height - 80)*0.7
                body_fat = (weight -0.88*stand_weight) / weight*100
            } else {
                stand_weight = (height - 70)*0.6
                body_fat = (weight -0.82*stand_weight) / weight*100
            }

            tv_weight.text = "標準體重 \n${String.format("%.2f", stand_weight)}"
            tv_fat.text = "體脂肪 \n${String.format("%.2f", body_fat)}"
        }
        true
    }


    private fun runRabbit() {
        ll_progress.visibility = View.VISIBLE
        Thread {
            progressBar2.progress = 0
            tv_progress.text = "0%"
            progressweight = 0
            while (progressweight < 100) {
                Thread.sleep(100)
                progressweight++
                val msg = Message()
                msg.what = 5
                handler.sendMessage(msg)
            }
            val msg = Message()
            msg.what = 10
            handler.sendMessage(msg)

        }.start() //啟動 Thread
    }
}
