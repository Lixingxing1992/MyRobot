package com.myrobot.org

import android.Manifest
import android.content.ComponentName
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.util.Log
import android.view.View
import android.widget.Toast
import com.myrobot.org.base.BaseActivity
import com.myrobot.org.common.SysConfig
import com.myrobot.org.main.AppMainActivity
import com.myrobot.org.server.redis.RedisUtils
import com.myrobot.org.scan.ScanActivity
import com.myrobot.org.server.rabbit.RabbitMQUtils
import com.myrobot.org.test.TestBannerActivity
import com.myrobot.org.util.SpeechUtils
import com.tbruyelle.rxpermissions2.RxPermissions
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.startActivity
import org.redisson.api.listener.MessageListener

class MainActivity : BaseActivity() {
    val TAG = "MainActivity"

    var mHandler = Handler()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    public fun btnClick(view: View){
        when(view){
            btn_review->{
                startActivity<TestBannerActivity>()
            }
            btn_scan->{
                startActivity<ScanActivity>()
            }
            btn_redis_face->{
                RedisUtils.subscribeTopic("sys:cam:face",object :MessageListener<String>{
                    override fun onMessage(channel: CharSequence, msg: String) {
                        Log.d(TAG, "btn_redis_face = $msg")
//                        BaseToastUtils.toast(msg)
                    }
                })
            }
            btn_redis_sn->{
                runOnUiThread {
                    Toast.makeText(this@MainActivity, SysConfig.KEY_DEVICESN ,Toast.LENGTH_SHORT).show()
                }
            }
            btn_redis_stock->{
                var sn = RedisUtils.getReJsonValue("state-tree",".robot.stocks")
                runOnUiThread {
                    Toast.makeText(this@MainActivity, sn,Toast.LENGTH_SHORT).show()
                }
            }
            btn_rabbit->{
                doAsync {
                    RabbitMQUtils.subscribe("robot-a", "1" , mHandler)
                }
            }
            btn_redis_speech->{
                SpeechUtils.speech("你好，我是小圈圈")
            }
            btn_setting->{
                val compName = ComponentName("com.eventec.settings", "com.eventec.settings.MainActivity")
                val intent = Intent()
                intent.component = compName
//                intent.putExtras(bundle)
                startActivity(intent)
            }
            btn_main->{
                startActivity<AppMainActivity>()
            }
        }
    }





    fun checkPermissionRequest() {
        val permissions = RxPermissions(this)
        permissions.setLogging(true)
        permissions.request(Manifest.permission.CAMERA)
            .subscribe{ aBoolean ->
                if (aBoolean!!) {
                    setContentView(R.layout.activity_main)
                }
            }
    }
}
