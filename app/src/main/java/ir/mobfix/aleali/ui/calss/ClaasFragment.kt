package ir.mobfix.aleali.ui.calss

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.navigation.fragment.NavHostFragment.Companion.findNavController
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import ir.mobfix.aleali.R
import ir.mobfix.aleali.databinding.FragmentClaasBinding
import ir.mobfix.aleali.databinding.FragmentHomeBinding

@AndroidEntryPoint
class ClaasFragment : Fragment() {
    //Binding
    private var _binding: FragmentClaasBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentClaasBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            toolbar.toolbarOptionImg.isVisible =false
            toolbar.toolbarTitleTxt.text = getString(R.string.barnameh_c)
            toolbar.toolbarBackImg.setOnClickListener {findNavController().popBackStack()}
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}