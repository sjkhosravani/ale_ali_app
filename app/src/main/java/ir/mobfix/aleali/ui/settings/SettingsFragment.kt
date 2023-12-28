package ir.mobfix.aleali.ui.settings

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import ir.mobfix.aleali.R
import ir.mobfix.aleali.data.models.ModelsFragment2
import ir.mobfix.aleali.data.models.ModelsStudent
import ir.mobfix.aleali.databinding.DialogCalssBinding
import ir.mobfix.aleali.databinding.FragmentHomeBinding
import ir.mobfix.aleali.databinding.FragmentSettingsBinding
import ir.mobfix.aleali.ui.settings.adapter.ListAdapter
import ir.mobfix.aleali.utils.extensions.transparentCorners
import javax.inject.Inject

@AndroidEntryPoint
class SettingsFragment : Fragment() {
    //Binding
    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    private val listMenu :MutableList<ModelsFragment2> = mutableListOf()

    @Inject
    lateinit var listAdapter : ListAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentSettingsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            setMenuStudents()
            topTop.toolbarOptionImg.isVisible =false
            topTop.toolbarBackImg.isVisible = false
            topTop.toolbarTitleTxt.text = getString(R.string.setting)
            listAdapter.setData(listMenu)
            list.apply {
                layoutManager = GridLayoutManager(requireContext(),2)
                adapter = listAdapter
            }
            listAdapter.setOnItemClickListener {
                AlertDialog.Builder(requireContext()).setMessage(it.desc)
                    .setTitle(getString(R.string.inf))
                    .setPositiveButton(getString(R.string.bashe)) { _, _ ->

                    }
                    .show()
            }

        }
    }

    fun setMenuStudents(){
        listMenu.clear()
        listMenu.add(ModelsFragment2(1,getString(R.string.exam),R.drawable.icon1,"این بخش مربوط به امتحانات دانش آموزان می باشد",R.color.red))
        listMenu.add(ModelsFragment2(2,getString(R.string.question),R.drawable.icon2,"این بخش مربوط به طراحی سوالات از طرف معلمان می باشد",R.color.gray))
        listMenu.add(ModelsFragment2(3,getString(R.string.workbook),R.drawable.icon3,"این بخش مربوط به تکالیف و تمرینات می باشد",R.color.black))
        listMenu.add(ModelsFragment2(4,getString(R.string.ranking),R.drawable.icon4,"در این بخش رتبه بندی دانش آموزان در هر آیتم جداگانه بررسی می شود",R.color.pacificBlue))
        listMenu.add(ModelsFragment2(5,getString(R.string.curriclum),R.drawable.icon5,"در این بخش کارنامه نهایی دانش آموز ارائه می شود",R.color.caribbeanGreen))
        listMenu.add(ModelsFragment2(6,getString(R.string.attendance),R.drawable.icon6,"در این بخش حضور و غیاب دانش آموز در کلاس ها و امتحانات ارائه می شود",R.color.lavender))
    }

}