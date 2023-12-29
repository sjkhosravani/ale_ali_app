package ir.mobfix.aleali.ui.login

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import coil.load
import dagger.hilt.android.AndroidEntryPoint
import ir.mobfix.aleali.R
import ir.mobfix.aleali.data.models.login.BodyLogin
import ir.mobfix.aleali.data.stored.GroupManager
import ir.mobfix.aleali.data.stored.SessionManager
import ir.mobfix.aleali.databinding.FragmentLoginBinding
import ir.mobfix.aleali.utils.BASE_URL
import ir.mobfix.aleali.utils.BASE_URL_DOC
import ir.mobfix.aleali.utils.extensions.hideKeyboard
import ir.mobfix.aleali.utils.extensions.showSnackBar
import ir.mobfix.aleali.utils.network.NetworkChecker
import ir.mobfix.aleali.utils.network.NetworkRequest
import ir.mobfix.aleali.viewmodel.LoginViewModel

import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class LoginFragment : Fragment() {

    //Binding
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var sessionManager: SessionManager

    @Inject
    lateinit var groupSessionManager: GroupManager

    @Inject
    lateinit var body: BodyLogin

    @Inject
    lateinit var networkChecker: NetworkChecker

    private val viewModel: LoginViewModel by viewModels()
    private var isNetworkAvailable = true


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(layoutInflater)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            //Init img help
            imgHelp.load(R.drawable.twotone_help_24) {
                crossfade(true)
                crossfade(500)
            }
            imgLogo.load(R.drawable.logo) {
                crossfade(true)
                crossfade(800)
            }
            //Check network
            lifecycleScope.launch {
                networkChecker.checkNetwork().collect { isNetworkAvailable = it }
            }
            //click
            loginBtn.setOnClickListener {
                val user = userEdt.text.toString()
                val pass = passEdt.text.toString()
                if (user.isEmpty() || pass.isEmpty()) {
                    it.showSnackBar(getString(R.string.error))
                } else {
                    body.username = user
                    body.password = pass
                    viewModel.userLogin(body)
                    loginWithUserPass()
                }
                it.hideKeyboard()
            }

            //click
            constraintHelp.setOnClickListener{
            /*    var url = BASE_URL_DOC
                if (!url.startsWith("http://") && !url.startsWith("https://")) {
                    url = "http://$url"
                }
                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                startActivity(browserIntent)*/

                findNavController().navigate(R.id.webViewFragment)
            }
        }
    }

    private fun loginWithUserPass() {
        if (!isNetworkAvailable) {
            Toast.makeText(requireContext(), R.string.checkYourNetwork, Toast.LENGTH_SHORT).show()
        } else {
            viewModel.loginData.observe(viewLifecycleOwner) { response ->
                when (response) {
                    is NetworkRequest.Loading -> {
                        binding.apply {
                            loading.isVisible = true
                            constraintLoginBox.alpha = 0.5f
                            constraintLoginBox.isEnabled = false
                            passEdt.isEnabled = false
                            userEdt.isEnabled = false
                        }
                    }
                    is NetworkRequest.Success -> {
                        response.data?.let {
                            lifecycleScope.launch {
                                sessionManager.saveToken(response.data.token)
                            }
                            lifecycleScope.launch {
                                groupSessionManager.saveType(response.data.token)
                            }
                            findNavController().popBackStack(R.id.loginFragment, true)
                            findNavController().navigate(R.id.homeFragment)
                        }
                    }
                    is NetworkRequest.Error -> {
                        binding.apply {
                            loading.isVisible = false
                            constraintLoginBox.alpha = 1f
                            constraintLoginBox.isEnabled = true
                            passEdt.isEnabled = true
                            userEdt.isEnabled = true
                            passEdt.setText("")
                            userEdt.setText("")
                        }
                        Toast.makeText(requireContext(), response.error, Toast.LENGTH_SHORT).show()
                    }
                }

            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}