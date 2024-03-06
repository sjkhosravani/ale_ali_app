package ir.mobfix.aleali.ui.tools

import android.app.Activity
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import ir.mobfix.aleali.R
import ir.mobfix.aleali.data.stored.LanguageLocal
import ir.mobfix.aleali.databinding.FragmentToolsBinding
import ir.mobfix.aleali.ui.main.MainActivity
import ir.mobfix.aleali.utils.ENGLISH
import ir.mobfix.aleali.utils.FARSI
import ir.mobfix.aleali.utils.extensions.getIndexFromList
import ir.mobfix.aleali.utils.extensions.setAppLocale
import ir.mobfix.aleali.utils.extensions.setupListWithAdapter
import ir.mobfix.aleali.viewmodel.SettingsViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.system.exitProcess

@AndroidEntryPoint
class ToolsFragment : Fragment() {

    //Binding
    private var _binding: FragmentToolsBinding? = null
    private val binding get() = _binding!!

    private val languageList: MutableList<String> = mutableListOf()
    private var language = ""

    @Inject
    lateinit var languageLocal: LanguageLocal
    private var lang = ""
    private var index = 0

    private val viewModel: SettingsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentToolsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            viewModel.loadLang()
            viewModel.languageSelected.observe(viewLifecycleOwner) {
                languageList.addAll(it)
                categoriesSpinner.setupListWithAdapter(it) { itItem -> language = itItem }
            }
            lifecycleScope.launch {lang = languageLocal.getLang().first()}
            lifecycleScope.launch {
                delay(100)
                when(lang){
                    FARSI->{
                        categoriesSpinner.setSelection(0)
                        Toast.makeText(requireContext(), FARSI, Toast.LENGTH_SHORT).show()}
                    ENGLISH->{
                        categoriesSpinner.setSelection(1)
                        Toast.makeText(requireContext(), ENGLISH, Toast.LENGTH_SHORT).show()}
                }
            }

            btnSave.setOnClickListener {
                val select = categoriesSpinner.selectedItemPosition
                when (select){
                    0->{
                        lifecycleScope.launch { languageLocal.saveLang(FARSI) }
                        setAppLocale(requireContext(), "fa")
                    }
                    1->{
                        lifecycleScope.launch { languageLocal.saveLang(ENGLISH) }
                        setAppLocale(requireContext(), "en")
                    }
                }
                requireActivity().recreate()
               // triggerRestart(requireActivity())
            }

        }
    }

    private fun triggerRestart(context: Activity) {
        val intent = Intent(context, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
        (context as Activity).finish()
        Runtime.getRuntime().exit(0)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}