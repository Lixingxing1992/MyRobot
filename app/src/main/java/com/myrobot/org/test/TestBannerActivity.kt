package com.myrobot.org.test

import android.os.Bundle
import android.widget.ImageView
import com.myrobot.org.R
import com.myrobot.org.base.BaseActivity
import kotlinx.android.synthetic.main.double_banner.*

/**
 *
 * @author Lixingxing
 */
class TestBannerActivity : BaseActivity(){

    var listBanner = arrayListOf<Int>(R.mipmap.test1,R.mipmap.test2,R.mipmap.test3)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.double_banner)
        btn_back.setOnClickListener {
            finish()
        }
        initBanner()
    }

    fun initBanner(){
        //添加广告数据
        bannerLayout.setData(listBanner, null)
        bannerLayout.setmAdapter { banner, model, view, position ->
            (view as ImageView).setImageResource(model as Int)
        }
    }

}