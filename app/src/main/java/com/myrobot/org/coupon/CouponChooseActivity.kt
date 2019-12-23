package com.myrobot.org.coupon

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.myrobot.org.R
import com.myrobot.org.base.BaseActivity
import com.myrobot.org.base.superadapter.BaseSuperAdapter
import com.myrobot.org.base.superadapter.SuperViewHolder
import com.myrobot.org.buy.model.GoodModel
import com.myrobot.org.util.BaseToastUtils
import com.myrobot.org.util.DoubleUtils
import com.myrobot.org.util.image.BaseImageUtils
import com.myrobot.org.view.MyDrawView
import com.study.xuan.shapebuilder.shape.ShapeBuilder
import kotlinx.android.synthetic.main.coupon_choose_activity.*
import kotlinx.android.synthetic.main.coupon_item.*

/**
 *
 * @author Lixingxing
 */
class CouponChooseActivity : BaseActivity(){
    var code = MyDrawView.MyDrawResultListener.RESULT_DEFAULT
    var listCoupon = arrayListOf<CouponModel>(
        CouponModel("1小时停车券","1小时停车券",R.mipmap.coupon_1),
        CouponModel("20元优惠券","20元优惠券",R.mipmap.coupon_2),
        CouponModel("10元优惠券","10元优惠券",R.mipmap.coupon_3))

    var listCouponTextView = arrayListOf<TextView>()
    var listCouponImgView = arrayListOf<ImageView>()
    var listCouponBgView = arrayListOf<View>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.coupon_choose_activity)

        code = intent.getIntExtra("code",0)
        when(code){
            MyDrawView.MyDrawResultListener.RESULT_DEFAULT ->{
                coupon_choose_top_text1.text = "惊喜悄悄来临，满满元气送给你！"
            }
            MyDrawView.MyDrawResultListener.RESULT_CRY ->{
                coupon_choose_top_text1.text = "不要不开心啦，小贩给你准备了惊喜，快点开看看吧！"
            }
            MyDrawView.MyDrawResultListener.RESULT_LAUGH ->{
                coupon_choose_top_text1.text = "心情不错呦，送你一个小礼物，继续保持好心情！"
            }
        }

        // 返回
        btn_back.setOnClickListener {
            finish()
        }

        listCouponTextView = arrayListOf(coupon_text1,coupon_text2,coupon_text3)
        listCouponImgView = arrayListOf(coupon_1,coupon_2,coupon_3)
        listCouponBgView = arrayListOf(coupon_bg1,coupon_bg2,coupon_bg3)
        getCoupon()

    }

    // 获取优惠券
    private fun getCoupon() {
        listCoupon.forEachIndexed { index, couponModel ->
            var item = listCoupon[index]
            item.apply {
                listCouponImgView[index].setImageResource(couponRes)
                listCouponTextView[index].text = couponName
            }
            listCouponBgView[index].setOnClickListener {
                showCouponDetail(item)
            }
        }
    }


    fun showCouponDetail(item: CouponModel?){
        item?.apply {
            layout_top.visibility = View.GONE
            layout_coupon.visibility = View.GONE
            layout_coupon_detail.visibility = View.VISIBLE

            coupon_detail_name.text = couponName
            coupon_code.setImageResource(R.mipmap.test_code)
        }
    }
}