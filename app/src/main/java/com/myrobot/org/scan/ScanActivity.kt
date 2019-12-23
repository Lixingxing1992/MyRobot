package com.myrobot.org.scan

import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import com.myrobot.org.R
import com.myrobot.org.base.BaseActivity
import com.myrobot.org.common.AppConfig
import com.myrobot.org.util.BaseToastUtils
import com.myrobot.org.util.SpeechUtils
import com.myrobot.org.util.qr.QrUtils
import kotlinx.android.synthetic.main.scan_activity.*
import org.jetbrains.anko.doAsync
import org.zeromq.ZMQ



/**
 * @author Lixingxing
 */
class ScanActivity : BaseActivity() {
    val TAG = "ScanActivity"
    var flag = true
    var result:String? = null
    var bitMap:Bitmap?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.scan_activity)
        SpeechUtils.speech("请把二维码对准摄像头")
        btn_back.setOnClickListener {
            flag = false
            finish()
        }
        doAsync {
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
                            SpeechUtils.speech("扫描成功")
                            flag = false
                            BaseToastUtils.toast(result)
                            Log.d(TAG," scan is success , result = " + result)
                        }
                    }
                }
                socket.close()
                context.term()
                finish()
            } catch (e: Exception) {
                e.printStackTrace()
            }
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
