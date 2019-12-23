package com.myrobot.org.base

import android.content.Context
import android.os.Bundle
import android.os.PersistableBundle
import android.support.v7.app.AppCompatActivity

/**
 *
 * @author Lixingxing
 */
open class BaseActivity :AppCompatActivity(){

    protected lateinit var mContext: Context

    override fun onCreate(savedInstanceState: Bundle?) {
        mContext = this
        super.onCreate(savedInstanceState)
    }

}