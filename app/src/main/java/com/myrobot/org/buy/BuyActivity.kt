package com.myrobot.org.buy

import android.app.ProgressDialog
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.view.View
import com.myrobot.org.R
import com.myrobot.org.base.BaseActivity
import com.myrobot.org.base.superadapter.BaseSuperAdapter
import com.myrobot.org.base.superadapter.SuperViewHolder
import com.myrobot.org.buy.model.GoodModel
import com.study.xuan.shapebuilder.shape.ShapeBuilder
import kotlinx.android.synthetic.main.buy_activity.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import android.text.Spanned
import android.widget.TextView
import android.text.style.ForegroundColorSpan
import android.text.SpannableString
import android.text.style.RelativeSizeSpan
import android.util.Base64
import android.util.Base64.NO_WRAP
import android.util.Base64.URL_SAFE
import android.util.Log
import com.google.gson.Gson
import com.myrobot.org.buy.model.OrderGoodModel
import com.myrobot.org.buy.model.OrderModel
import com.myrobot.org.buy.model.ScheduleModel
import com.myrobot.org.common.AppConfig
import com.myrobot.org.common.Substates
import com.myrobot.org.common.SysConfig
import com.myrobot.org.server.redis.RedisUtils
import com.myrobot.org.util.*
import com.myrobot.org.util.image.BaseImageUtils
import com.myrobot.org.util.qr.QrUtils
import org.redisson.api.listener.MessageListener
import java.math.BigDecimal
import java.nio.charset.Charset


/**
 * 抢购
 * @author Lixingxing
 */
class BuyActivity : BaseActivity() {
    val TAG = "BuyActivity"
    // 是否开始购买
    var isStartBuy = false
    var listGoods = ArrayList<GoodModel>()

    var goodModel:GoodModel?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.buy_activity)

        buy_close.setOnClickListener {
            finish()
        }

        ShapeBuilder.create().Radius(20f).Solid(Color.WHITE).build(good_detail_bg)
        ShapeBuilder.create().Stroke(6, Color.parseColor("#f8d24a")).build(good_detail_good_pic_bg)

        ShapeBuilder.create().Radius(20f).Solid(Color.WHITE).build(good_result_bg)

        getGoods()


        subBuy()

    }

    // 获取商品列表
    private fun getGoods() {
        doAsync {
            listGoods = GoodUtils.getGoodModels() as ArrayList<GoodModel>
            // TODO 复制数据
            listGoods.addAll(listGoods)

            uiThread {
                recyclerView.adapter = MyShopAdapter(mContext, listGoods)
            }
        }
    }


    inner class MyShopAdapter(mContext: Context, listGoods: ArrayList<GoodModel>) :
        BaseSuperAdapter<GoodModel>(mContext, listGoods, R.layout.buy_good_item) {

        override fun onBind(helper: SuperViewHolder, viewType: Int, layoutPosition: Int, item: GoodModel?) {
            var item_bg = helper.findViewById<View>(R.id.item_bg)
            if (item_bg != null) {
                ShapeBuilder.create().Radius(20f).Solid(Color.WHITE).build(item_bg)
            }
            var good_price = helper.findViewById<TextView>(R.id.good_price)
            item?.apply {
                helper.setText(R.id.good_name, name)
                var priceD = DoubleUtils.getPrice(realPrice)
                val spannableString = SpannableString("会员:¥$priceD")
                val foregroundColorSpan = ForegroundColorSpan(Color.parseColor("#fd545a"))
                spannableString.setSpan(
                    foregroundColorSpan,
                    3,
                    spannableString.length,
                    Spanned.SPAN_INCLUSIVE_EXCLUSIVE
                )

                val relativeSizeSpan = RelativeSizeSpan(2.0f)
                spannableString.setSpan(relativeSizeSpan, 4, spannableString.length, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)


                good_price.text = spannableString
                helper.setImageByUrl(R.id.good_pic, allImageUrl)

                helper.setViewVisible(
                    R.id.good_have_no, when (stock) {
                        0 -> View.VISIBLE
                        else -> View.GONE
                    }
                )
            }

            helper.itemView.setOnClickListener {
                if (item?.stock == 0) {
                    BaseToastUtils.toast("该商品已经售罄,请联系管理员及时添加")
                    return@setOnClickListener
                }
                showGoodDetail(item)
            }
        }
    }


    // 展示选中商品详情
    private fun showGoodDetail(item: GoodModel?) {
        clearGoodDetail()
        goodModel = item
        good_detail_bg.visibility = View.VISIBLE
        item?.apply {
            good_detail_good_name.text = name
            var priceD = DoubleUtils.getPrice(realPrice)
            val spannableString = SpannableString("会员:¥$priceD")
            val foregroundColorSpan = ForegroundColorSpan(Color.parseColor("#fd545a"))
            spannableString.setSpan(foregroundColorSpan, 3, spannableString.length, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)

            val relativeSizeSpan = RelativeSizeSpan(2.0f)
            spannableString.setSpan(relativeSizeSpan, 4, spannableString.length, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)


            good_detail_good_price.text = spannableString

            BaseImageUtils.create().load_http_image(good_detail_good_pic, allImageUrl)

            good_detail_btn.visibility = View.VISIBLE
            good_detail_qrcode.visibility = View.GONE
            good_detail_qrcode_text.visibility = View.GONE
            good_detail_btn.setOnClickListener {
                startBuy(this)
            }
        }
    }

    // 点击购买按钮
    var progressDialog: ProgressDialog?=null
    var bm:Bitmap?=null
    private fun startBuy(item: GoodModel) {
        progressDialog = ProgressDialog(mContext)//1.创建一个ProgressDialog的实例
        progressDialog!!.setTitle("")//2.设置标题
        progressDialog!!.setMessage("正在生成付款码,请稍后")//3.设置显示内容
        progressDialog!!.setCancelable(false)//4.设置可否用back键关闭对话框
        progressDialog!!.show()//5.将ProgessDialog显示出来

        doAsync {
            var orderModel = OrderModel(
                SysConfig.KEY_DEVICESN,
                SysConfig.KEY_MERCHANTID,
                arrayListOf(OrderGoodModel(item.goodsCode,item.name,item.imgUrl,item.realPrice))
            )
            Log.e(TAG,"params = $orderModel")
            var params = String(Base64.encode(orderModel.toString().toByteArray(Charsets.UTF_8),URL_SAFE),Charsets.UTF_8)
            var urls = AppConfig.URL_BUY_CODE_URL + params
            Log.e(TAG,"params = $params")
            bm = QrUtils.createQRCode(urls)
            runOnUiThread {
                good_detail_qrcode.setImageBitmap(bm)
                progressDialog!!.dismiss()
                good_detail_btn.visibility = View.GONE
                good_detail_qrcode.visibility = View.VISIBLE
                good_detail_qrcode_text.visibility = View.VISIBLE
                isStartBuy = true
            }
        }
    }

    // 清空之前的选中状态
    private fun clearGoodDetail() {
        good_detail_btn.visibility = View.VISIBLE
        good_detail_qrcode.visibility = View.GONE
        good_detail_qrcode_text.visibility = View.GONE

        good_result_bg.visibility = View.GONE
        isStartBuy = false

        bm?.recycle()
        bm = null

        goodModel = null
    }

    //  根据购买结果显示页面状态
    private fun buyByResule(isSuccess: Boolean, item: GoodModel) {
        if (isSuccess) {
            GoodUtils.getStockNums(item, "")
            item.stock = item.stock - 1
            recyclerView.adapter = MyShopAdapter(mContext, listGoods)

            // 购买成功
            good_detail_bg.visibility = View.GONE
            good_result_bg.visibility = View.VISIBLE
        } else {
            // 购买失败

        }
    }


    fun subBuy(){
        // 订阅状态用来检查购买是否成功
        // 订单下发状态
        RedisUtils.subscribeTopic(".robot.mqtt.order", messageOrderListener)
        // 设备执行出货
        RedisUtils.subscribeTopic(".robot.device.stockManager.schedule", messageStockListener)
    }

    var messageOrderListener = object: MessageListener<String>{
        override fun onMessage(channel: CharSequence?, msg: String?) {
            // 支付完成,下发了订单
            SpeechUtils.speech("支付已经完成,正在为您出货")
            buyByResule(true,goodModel!!)
        }
    }

    var messageStockListener = object: MessageListener<String>{
        override fun onMessage(channel: CharSequence?, msg: String?) {
            var scheduleModel:ScheduleModel = Gson().fromJson(msg,ScheduleModel::class.java)
            var sstates = scheduleModel.substates.toString()
            var substates = Substates.getByTypes(sstates.toInt())
            Log.e(TAG,"子状态为${substates}, desc = ${substates.desc}" )
            when(substates){
                Substates.no10006->{
                    BaseToastUtils.toast("出货失败")
                }
            }
        }
    }



    override fun finish() {
        super.finish()

        bm?.recycle()
        bm = null
        listGoods.clear()
        progressDialog?.dismiss()
        // 取消订阅
        RedisUtils.unSubscribeTopic(".robot.mqtt.order", messageOrderListener)
        RedisUtils.unSubscribeTopic(".robot.device.stockManager.schedule", messageStockListener)

    }
}
