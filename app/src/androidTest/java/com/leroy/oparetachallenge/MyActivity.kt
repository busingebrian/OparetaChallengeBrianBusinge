package com.leroy.oparetachallenge
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import io.sentry.Sentry

class MyActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Sentry.captureMessage("testing SDK setup")
    }
}
