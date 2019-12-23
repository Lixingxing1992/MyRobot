package com.myrobot.org.main

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.myrobot.org.R
import com.myrobot.org.base.BaseActivity
import com.myrobot.org.buy.BuyActivity
import com.myrobot.org.coupon.CouponChooseActivity
import com.myrobot.org.customer.CustormerActivity
import com.myrobot.org.util.BaseToastUtils
import com.myrobot.org.util.BaseViewOnClickAnimUtils
import com.myrobot.org.view.MyDrawView
import com.myrobot.org.view.MyDrawView.MyDrawResultListener.*
import kotlinx.android.synthetic.main.main_activity.*
import org.jetbrains.anko.startActivity

/**
 *
 * @author Lixingxing
 */
class AppMainActivity :BaseActivity(){
    var isStartOther = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)


        BaseViewOnClickAnimUtils().initView(main_btn_kefu).lightImageView()
        BaseViewOnClickAnimUtils().initView(main_btn_qianggou).lightImageView()
        BaseViewOnClickAnimUtils().initView(main_btn_sure).lightImageView()
        BaseViewOnClickAnimUtils().initView(main_btn_reset).lightImageView()

        main_logo.setOnClickListener {
            finish()
        }

        // 客服
        main_btn_kefu.setOnClickListener {
            startActivity<CustormerActivity>()
        }

        // 抢购
        main_btn_qianggou.setOnClickListener {
            startActivity<BuyActivity>()
        }

        // 确定
        main_btn_sure.setOnClickListener {
            onDrawResult()
        }

        // 重画
        main_btn_reset.setOnClickListener {
            onReDraw()
        }

        myDrawView.setMyDrawResultListener(object :MyDrawView.MyDrawResultListener{
            override fun onResult(code: Int) {
                isStartOther = true
                startActivity<CouponChooseActivity>("code" to code)
            }
            override fun onDrawFinish() {
                onDrawFinishs()
            }
        })
    }
    // 绘画完成时
    fun onDrawFinishs(){
        // 不能再继续画画
        myDrawView.setCanDraw(false)
        // 不显示背景框
        myDrawView.setShowBg(false)

        main_bg2.visibility = View.VISIBLE
        main_btn_sure.visibility = View.VISIBLE
        main_btn_reset.visibility = View.VISIBLE

        main_dialog.visibility = View.GONE
        main_btn_qianggou.visibility = View.GONE
        main_btn_kefu.visibility = View.GONE

    }

    // 重画
    fun onReDraw(){
        main_bg2.visibility = View.GONE
        main_btn_sure.visibility = View.GONE
        main_btn_reset.visibility = View.GONE

        main_dialog.visibility = View.VISIBLE
        main_btn_qianggou.visibility = View.VISIBLE
        main_btn_kefu.visibility = View.VISIBLE

        myDrawView.reset()
    }

    // 确定
    fun onDrawResult(){
        myDrawView.checkResult()
    }

    override fun onStop() {
        super.onStop()
        if(isStartOther){
            onReDraw()
        }
        myBatterView.pause()
    }
    override fun onRestart() {
        super.onRestart()
        isStartOther = false
        myBatterView.reStart()
    }
    override fun onDestroy() {
        super.onDestroy()
        myBatterView.destory()
    }


}