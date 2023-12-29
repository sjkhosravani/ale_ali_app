package ir.mobfix.aleali.ui.webview

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import ir.mobfix.aleali.R
import ir.mobfix.aleali.databinding.FragmentSplashBinding
import ir.mobfix.aleali.databinding.FragmentWebViewBinding
import ir.mobfix.aleali.utils.BASE_URL_DOC

@AndroidEntryPoint
class WebViewFragment : Fragment() {
    //Binding
    private var _binding: FragmentWebViewBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentWebViewBinding.inflate(layoutInflater)
        return binding.root
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            webView.webViewClient = WebViewClient()
            webView.loadUrl(BASE_URL_DOC)
            webView.settings.javaScriptEnabled = true
            webView.settings.setSupportZoom(true)
            toolbar.toolbarOptionImg.isVisible = false
            toolbar.toolbarTitleTxt.text = getString(R.string.help)
            toolbar.toolbarBackImg.setOnClickListener {
                findNavController().popBackStack()
            }
        }

    }
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}