package com.leroy.oparetachallenge.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.leroy.oparetachallenge.databinding.ActivityMainBinding

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