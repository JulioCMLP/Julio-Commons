package com.julioapps.commons.samples.activities

import android.os.Bundle
import com.julioapps.commons.extensions.appLaunched
import com.julioapps.commons.samples.R

class MainActivity : com.julioapps.commons.activities.BaseSimpleActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        appLaunched()
    }
}
