package com.leroy.oparetachallenge.ui

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.core.text.isDigitsOnly
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.leroy.oparetachallenge.databinding.FragmentMainBinding
import com.leroy.oparetachallenge.utils.ErrorHandler
import timber.log.Timber

class MainFragment : Fragment() {

    private lateinit var binding: FragmentMainBinding
    private lateinit var viewModel: MainViewModel
    protected lateinit var adapter: MainAdapter

    private val fiatCurrencies = mutableListOf("UGX", "USD", "EUR")
    private var selectedFiatCurrency = fiatCurrencies.first()
    private val convertHandler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.fiatCurrencyBtn.setOnClickListener { setSelectedFiatCurrency() }
        binding.fiatCurrencyBtn.text = selectedFiatCurrency
        binding.amountText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(s: Editable) {
                convertHandler.removeCallbacksAndMessages(convertRunnable)
                if (s.isBlank() || !s.isDigitsOnly()) return Timber.w("Query is empty or not digits only")
                convertHandler.postDelayed(convertRunnable, 2000)
            }

        })
        adapter = MainAdapter().also {
            binding.recyclerView.adapter = it
        }
    }

    private val convertRunnable = Runnable {
        Timber.d("Converting from runnable")
        convert()
    }

    private fun convert() {
        val amount = binding.amountText.text.toString()
        if (amount.isBlank() || !amount.isDigitsOnly()) return Timber.w("Amount is empty or not digits only")
        viewModel.convertPrice(amount.toInt(), this.selectedFiatCurrency, "BTC").observe(viewLifecycleOwner) {
            binding.fiatCurrencyBtn.isEnabled = it.loading().not()
            binding.amountText.isEnabled = it.loading().not()
            binding.progressBar.visibility = it.loadingToVisibility
            if (it.ex != null)
                ErrorHandler.showError(it.ex)
            if (it.data?.data != null) {
                adapter.quotes = it.data.data.quote
//                Timber.d(">>> Data: ${it.data.data}")
//                for (key in it.data.data.quote.keys) {
//                    Timber.d(">>> Key: $key")
//                }
            }
        }
    }

    private fun setSelectedFiatCurrency() {
        PopupMenu(requireContext(), binding.fiatCurrencyBtn).apply {
            fiatCurrencies.forEach { currency ->
                this.menu.add(currency)
            }
            this.setOnMenuItemClickListener {
                selectedFiatCurrency = it.title.toString()
                binding.fiatCurrencyBtn.text = selectedFiatCurrency
                convert()
                return@setOnMenuItemClickListener true
            }
        }.also {
            it.show()
        }
    }

    companion object {

        @JvmStatic
        fun newInstance() =
            MainFragment().apply {
                arguments = Bundle()
            }
    }
}