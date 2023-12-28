package ir.mobfix.aleali.ui.splash

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import ir.mobfix.aleali.BuildConfig
import ir.mobfix.aleali.R
import ir.mobfix.aleali.data.stored.SessionManager
import ir.mobfix.aleali.databinding.FragmentSplashBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class SplashFragment : Fragment() {
    //Binding
    private var _binding: FragmentSplashBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var sessionManager: SessionManager

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentSplashBinding.inflate(layoutInflater)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //Init views
        binding.apply {
            //Version
            versionTxt.text = "${getString(R.string.version)} : ${BuildConfig.VERSION_NAME}"
        }
        //Check user
        lifecycleScope.launch {
            delay(2000)
            val token = sessionManager.getToken.first()
            Log.e("userToken", token.toString())
            findNavController().popBackStack(R.id.splashFragment, true)
            if (token == null) {
                findNavController().navigate(R.id.loginFragment)
            } else {
                findNavController().navigate(R.id.homeFragment)
            }
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


}