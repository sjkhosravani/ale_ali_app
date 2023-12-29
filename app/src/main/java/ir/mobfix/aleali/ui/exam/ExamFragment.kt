package ir.mobfix.aleali.ui.exam

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import dagger.hilt.android.AndroidEntryPoint
import ir.mobfix.aleali.R
import ir.mobfix.aleali.databinding.FragmentExamBinding
import ir.mobfix.aleali.databinding.FragmentHomeBinding

@AndroidEntryPoint
class ExamFragment : Fragment() {
    //Binding
    private var _binding: FragmentExamBinding? = null
    private val binding get() = _binding!!

    //Other
    private val args: ExamFragmentArgs by navArgs()

    private var userNameExam = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentExamBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userNameExam = args.userName
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            toolbar.toolbarOptionImg.isVisible = false
            toolbar.toolbarTitleTxt.text = userNameExam
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