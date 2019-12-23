package com.myrobot.org.base.superadapter

import android.content.Context
import com.myrobot.org.base.superadapter.IMulItemViewType
import com.myrobot.org.base.superadapter.BaseSuperAdapter
import com.myrobot.org.base.superadapter.SuperConfig
import com.myrobot.org.base.superadapter.SuperViewHolder

/**
 * 如果有空数据默认补上一条空数据以显示内容的适配器
 * Created by lixingxing on 2019/5/29.
 */
abstract class BaseEmptyAdapter<T>(context: Context, items: List<T>, defaultItemLayout:Int,defaultEmyptyItemLayout:Int) :
    BaseSuperAdapter<T>(context, items, EmptyMulItemViewType(defaultItemLayout,defaultEmyptyItemLayout)) {
    override fun getItemCount(): Int {
        return if(mData.isEmpty()) 1 else super.getItemCount()
    }

    override fun getCount(): Int {
        return if(mData.isEmpty()) 1 else super.getCount()
    }
    override fun onBind(helper: SuperViewHolder, viewType: Int, layoutPosition: Int, item: T) {
        if(viewType == SuperConfig.CODE_EMPTY){
            return
        }
        onBinds(helper,viewType,layoutPosition,item)
    }

    abstract fun onBinds(helper: SuperViewHolder, viewType: Int, layoutPosition: Int, item: T)
}
