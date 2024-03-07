package ir.mobfix.aleali.ui.home

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import ir.mobfix.aleali.R
import ir.mobfix.aleali.data.models.ResponseMenu2
import ir.mobfix.aleali.data.stored.SessionManager
import ir.mobfix.aleali.databinding.DialogCalssBinding
import ir.mobfix.aleali.databinding.FragmentHomeBinding
import ir.mobfix.aleali.ui.home.adapters.Menu2Adapter
import ir.mobfix.aleali.ui.home.adapters.MenuAdapter
import ir.mobfix.aleali.ui.home.adapters.UserAdapter
import ir.mobfix.aleali.utils.extensions.transparentCorners
import ir.mobfix.aleali.utils.network.NetworkChecker
import ir.mobfix.aleali.utils.network.NetworkRequest
import ir.mobfix.aleali.viewmodel.HomeViewModel
import ir.mobfix.aleali.viewmodel.ProfileViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment() {
    //Binding
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val homeViewModel: HomeViewModel by viewModels()

    private val userViewModel: ProfileViewModel  by viewModels()

    @Inject
    lateinit var networkChecker: NetworkChecker
    @Inject
    lateinit var menuAdapter: MenuAdapter

    @Inject
    lateinit var menu2Adapter: Menu2Adapter

    @Inject
    lateinit var sessionManager: SessionManager

    private var token : String = ""

    private var isNetworkAvailable = true
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentHomeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {

            topBar.toolbarBackImg.isVisible = false
            topBar.toolbarOptionImg.isVisible =false
            //check net
            lifecycleScope.launch {
                networkChecker.checkNetwork().collect { isNetworkAvailable = it }
            }
            lifecycleScope.launch {
                delay(100)
                token = sessionManager.getToken.first() }
            if (isNetworkAvailable){
                lifecycleScope.launch {
                    delay(300)
                    homeViewModel.getMenu(token,0)
                    userViewModel.profileData(token)
                    loadUserMenu()
                    loadUserName()
                }
            }

            menuAdapter.setOnItemClickListener {
                showDialog(it)
            }

        }
    }

    private fun loadUserName() {
        homeViewModel.menuData.observe(viewLifecycleOwner){response->
            when (response) {
                is NetworkRequest.Loading -> {
                    binding.loading.visibility = View.VISIBLE
                    binding.constraintMain.visibility = View.GONE
                }
                is NetworkRequest.Success -> {
                    response.data?.let {
                        menuAdapter.setData(it)
                        binding.recycler1.apply {
                            layoutManager = GridLayoutManager(requireContext(),2)
                            adapter = menuAdapter
                        }

                    }
                    binding.loading.visibility = View.GONE
                    binding.constraintMain.visibility = View.VISIBLE
                }
                is NetworkRequest.Error -> {

                    binding.loading.visibility = View.GONE
                    binding.constraintMain.visibility = View.VISIBLE

                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun loadUserMenu() {
       userViewModel.profileData.observe(viewLifecycleOwner){response->
           when (response) {
               is NetworkRequest.Loading -> {}
               is NetworkRequest.Success -> {
                   response.data?.let {
                       binding.topBar.toolbarTitleTxt.text = "${getString(R.string.well)} ${it.firstName}"
                   }

               }
               is NetworkRequest.Error -> {

               }
           }
       }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun showDialog(item : ResponseMenu2.ResponseMenu2Item) {
        val dialog = Dialog(requireContext())
        val dialogBinding = DialogCalssBinding.inflate(layoutInflater)
        dialog.transparentCorners()
        dialog.setContentView(dialogBinding.root)
        dialogBinding.apply {
            close.setOnClickListener { dialog.dismiss() }
         //   menu2Adapter.setData(item)
            listCalss.apply {
                layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)

            }
        }
        dialog.show()
    }

}