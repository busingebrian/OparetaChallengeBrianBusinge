package com.leroy.oparetachallenge.ui

import android.os.Bundle
import android.os.Environment
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.BuildConfig
import com.leroy.oparetachallenge.databinding.ActivityMainBinding
import java.io.File
import java.lang.reflect.Method


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportFragmentManager.beginTransaction().replace(
            binding.fragmentContainer.id,
            MainFragment.newInstance()
        ).commit()
    }
}