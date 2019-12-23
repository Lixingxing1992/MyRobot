package com.myrobot.org.customer

import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.support.annotation.UiThread
import android.util.Log
import com.myrobot.org.R
import com.myrobot.org.base.BaseActivity
import com.myrobot.org.common.AppConfig
import com.myrobot.org.util.BaseToastUtils
import com.myrobot.org.util.SpeechUtils
import com.myrobot.org.util.qr.QrUtils
import com.study.xuan.shapebuilder.shape.ShapeBuilder
import kotlinx.android.synthetic.main.buy_activity.*
import kotlinx.android.synthetic.main.custor_activity.*
import org.jetbrains.anko.uiThread
import org.zeromq.ZMQ

/**
 * 客服
 * @author Lixingxing
 */
class CustormerActivity : BaseActivity(){
    val TAG = "CustormerActivity"
    var flag = true
    var result:String? = null
    var bitMap: Bitmap?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.custor_activity)
        SpeechUtils.speech("请把条码对准摄像头进行优惠券核销")
        custormer_close.setOnClickListener {
            flag = false
            finish()
        }
        Thread{
            try {
                val context = ZMQ.context(1)
                val socket = context.socket(ZMQ.SUB)
                socket.connect("tcp://" + AppConfig.SERVER_ZMQ_IP + ":" + AppConfig.SERVER_ZMQ_PORT)
                socket.subscribe("")
                while (!Thread.currentThread().isInterrupted && flag) {
                    try {
                        Thread.sleep(50)
                    } catch (e: InterruptedException) {
                        e.printStackTrace()
                    }
                    val reply = socket.recv()
                    if (reply != null && reply.size > 0) {
                        bitMap = QrUtils.byteForCompressBitmap(reply)
                        result = QrUtils.decodeQRcode(bitMap)
                        if(result == null  || result == ""){
                            result = QrUtils.decodeBarcode(bitMap)
                        }
                        bitMap?.recycle()
                        if(result == null  || result == ""){
                            Log.e(TAG," scan is fail")
                        }else{
                            flag = false
                            SpeechUtils.speech("扫描成功,正在核销")
                            Log.d(TAG," scan is success , result = " + result)
                            checkResult(result!!)
                        }
                    }
                }
                socket.close()
                context.term()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }.start()

    }

    private fun checkResult(result: String) {
        runOnUiThread {
            Handler().postDelayed({
                flag = false
                CustormerToast.showText(this,"核销成功",true)
                finish()
            },3000)
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        if(bitMap != null && !bitMap!!.isRecycled){
            bitMap!!.recycle()
            bitMap = null
        }
    }
}
