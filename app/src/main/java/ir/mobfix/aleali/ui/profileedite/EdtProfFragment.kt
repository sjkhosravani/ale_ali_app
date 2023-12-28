package ir.mobfix.aleali.ui.profileedite

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import ir.hamsaa.persiandatepicker.PersianDatePickerDialog
import ir.hamsaa.persiandatepicker.api.PersianPickerDate
import ir.hamsaa.persiandatepicker.api.PersianPickerListener
import ir.mobfix.aleali.R
import ir.mobfix.aleali.data.models.profile.BodyUpdateProfile
import ir.mobfix.aleali.data.stored.SessionManager
import ir.mobfix.aleali.databinding.FragmentEdtProfBinding
import ir.mobfix.aleali.utils.extensions.showSnackBar
import ir.mobfix.aleali.utils.network.NetworkChecker
import ir.mobfix.aleali.utils.network.NetworkRequest
import ir.mobfix.aleali.viewmodel.ProfileViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class EdtProfFragment : BottomSheetDialogFragment() {
    //Binding
    private var _binding: FragmentEdtProfBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var body: BodyUpdateProfile

    @Inject
    lateinit var networkChecker: NetworkChecker

    @Inject
    lateinit var sessionManager: SessionManager

    private var token : String =""

    private val profileViewModel: ProfileViewModel by viewModels()

    private var user : String = ""


    //Theme
    override fun getTheme() = R.style.RemoveDialogBackground

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentEdtProfBinding.inflate(layoutInflater)
        return binding.root
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launch { token = sessionManager.getToken.first()!!}
        binding.apply {
            profileViewModel.profileData(token)
            //Open date picker
            birthDateEdt.setOnTouchListener { _, event ->
                if (event.action == MotionEvent.ACTION_DOWN) {
                    openDatePicker()
                }
                false
            }

            //Submit data
            submitBtn.setOnClickListener {
                val firsName = nameEdt.text.toString()
                val lastName = lastEdt.text.toString()
                val birthDate = birthDateEdt.text.toString()
                val id = idEdt.text.toString()

                body.first_name =nameEdt.text.toString()
                body.last_name = lastEdt.text.toString()
                body.birthday = birthDateEdt.text.toString()
                body.national_code = idEdt.text.toString()
                if (firsName.isEmpty()||lastName.isEmpty()||birthDate.isEmpty()||id.isEmpty()){
                    root.showSnackBar(getString(R.string.error))
                }
                else
                {
                    //Call api
                    profileViewModel.profileDataUpdate(token,user,body)
                }

            }
            loadProfileData()
            loadUpdateProfileData()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
    private fun openDatePicker() {
        PersianDatePickerDialog(requireContext())
            .setTodayButtonVisible(true)
            .setMinYear(1300)
            .setMaxYear(1400)
            .setInitDate(1370, 3, 13)
            .setTitleType(PersianDatePickerDialog.DAY_MONTH_YEAR)
            .setShowInBottomSheet(true)
            .setListener(object : PersianPickerListener {
                override fun onDateSelected(pDate: PersianPickerDate) {
                    val birthDate = "${pDate.gregorianYear}-${pDate.gregorianMonth}-${pDate.gregorianDay}"
                    val birthDatePersian = "${pDate.persianYear}-${pDate.persianMonth}-${pDate.persianDay}"
                    body.birthday = birthDate
                    binding.birthDateEdt.setText(birthDatePersian)
                }

                override fun onDismissed() {}
            }).show()
    }

    @SuppressLint("SetTextI18n")
    private fun loadProfileData() {
        binding.apply {
            profileViewModel.profileData.observe(viewLifecycleOwner) { response ->
                when (response) {
                    is NetworkRequest.Loading -> {
                        loading.isVisible = true
                    }
                    is NetworkRequest.Success -> {
                        loading.isVisible = false
                        response.data?.let { data ->
                            user = data.username!!
                            if (data.firstName.isNullOrEmpty().not())
                                nameEdt.setText(data.firstName)
                            if (data.lastName.isNullOrEmpty().not())
                                lastEdt.setText(data.lastName)
                            if (data.birthday.isNullOrEmpty().not())
                                birthDateEdt.setText(data.birthday)

                        }
                    }

                    is NetworkRequest.Error -> {
                        loading.isVisible = false
                        root.showSnackBar(response.error!!)
                    }
                }
            }
        }
    }
    private fun loadUpdateProfileData() {
        binding.apply {
           profileViewModel.profileDataUpdate.observe(viewLifecycleOwner) { response ->
                when (response) {
                    is NetworkRequest.Loading -> {

                    }

                    is NetworkRequest.Success -> {
                        response.data?.let {
                            Toast.makeText(requireContext(), "Sucsses", Toast.LENGTH_SHORT).show()
                        }
                        findNavController().popBackStack()
                    }

                    is NetworkRequest.Error -> {

                        root.showSnackBar(response.error!!)
                    }
                }
            }
        }
    }
}