package ir.mobfix.aleali.ui.profile


import android.Manifest
import android.annotation.SuppressLint
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.findNavController
import coil.load
import com.app.imagepickerlibrary.ImagePicker
import com.app.imagepickerlibrary.ImagePicker.Companion.registerImagePicker
import com.app.imagepickerlibrary.listener.ImagePickerResultListener
import com.app.imagepickerlibrary.model.PickExtension
import com.app.imagepickerlibrary.model.PickerType

import dagger.hilt.android.AndroidEntryPoint
import ir.hamsaa.persiandatepicker.PersianDatePickerDialog
import ir.hamsaa.persiandatepicker.api.PersianPickerDate
import ir.hamsaa.persiandatepicker.api.PersianPickerListener
import ir.mobfix.aleali.R

import ir.mobfix.aleali.data.stored.SessionManager

import ir.mobfix.aleali.databinding.FragmentProfileBinding
import ir.mobfix.aleali.utils.*
import ir.mobfix.aleali.utils.extensions.getRealFileFromUri
import ir.mobfix.aleali.utils.extensions.loadImage
import ir.mobfix.aleali.utils.extensions.showSnackBar
import ir.mobfix.aleali.utils.network.NetworkChecker
import ir.mobfix.aleali.utils.network.NetworkRequest
import ir.mobfix.aleali.viewmodel.ProfileViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.http.PATCH
import java.io.File
import java.net.URLEncoder
import javax.inject.Inject


@AndroidEntryPoint
class ProfileFragment : Fragment(), ImagePickerResultListener {
    //Binding
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var networkChecker: NetworkChecker

    @Inject
    lateinit var sessionManager: SessionManager
    private var token : String =""
    private var user : String = ""

    private val profileViewModel: ProfileViewModel by viewModels()
    private var isNetworkAvailable = true
    private val imagePicker: ImagePicker by lazy { registerImagePicker(this) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentProfileBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            //Choose image
            avatarEditImg.setOnClickListener {
                openImagePicker()
            }
            //get token
            lifecycleScope.launch {token = sessionManager.getToken.first().toString()}

            //check net
            lifecycleScope.launch {
                networkChecker.checkNetwork().collect { isNetworkAvailable = it }
            }

            containerBot.apply {menuEditLay.setOnClickListener { findNavController().navigate(R.id.edtProfFragment) }}
            loadLoadAvatarData()
            getProfileData()
        }
    }

    private fun openImagePicker() {
            imagePicker
                .title(getString(R.string.galleryImages))
                .multipleSelection(false)
                .showFolder(true)
                .cameraIcon(true)
                .doneIcon(true)
                .allowCropping(true)
                .compressImage(false)
                .maxImageSize(2.5f)
                .extension(PickExtension.ALL)
            imagePicker.open(PickerType.GALLERY)

    }
    override fun onImagePick(uri: Uri?) {
        val imageFile = getRealFileFromUri(requireContext(), uri!!)?.let { path -> File(path) }

        val multipart = MultipartBody.Builder().setType(MultipartBody.FORM)
            .addFormDataPart(METHOD, PATCH)

        //Image body
        if (imageFile != null) {
            val fileName = URLEncoder.encode(imageFile.absolutePath, UTF_8)
            val reqFile = imageFile.asRequestBody(MULTIPART_FROM_DATA.toMediaTypeOrNull())
            multipart.addFormDataPart(AVATAR, fileName, reqFile)
        }
        //Call api
        val requestBody = multipart.build()
        if (isNetworkAvailable) {
            profileViewModel.callUploadAvatarApi("Token $token",user,requestBody)
        }
    }

    override fun onMultiImagePick(uris: List<Uri>?) {

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    @SuppressLint("SetTextI18n")
    private fun getProfileData() {
        profileViewModel.profileData(token)
            profileViewModel.profileData.observe(viewLifecycleOwner) { response ->
                when (response) {
                    is NetworkRequest.Loading -> {
                        binding.apply {
                            infoContainer.isVisible = false
                            constraintB.isVisible = false
                            loadingP.isVisible = true
                        }
                    }
                    is NetworkRequest.Success -> {
                        response.data?.let {
                           binding.apply {
                               nameTxt.text="@${it.username}"
                               infoLay.apply {
                                   firstNameTxt.text = it.firstName
                                   lastNameTxt.text = it.lastName
                                   dateTxt.text = it.birthday
                                   codeTxt.text = it.nationalCode
                               }
                               user = it.username.toString()
                               if (it.avatarUrl.isNullOrEmpty()){
                                   avatarImg.load(R.drawable.placeholder)
                               }else{
                                   avatarImg.loadImage("$BASE_URL_IMAGE${it.avatarUrl}")
                               }
                           }
                        }
                       binding. infoContainer.isVisible = true
                       binding. loadingP.isVisible = false
                        binding.constraintB.isVisible = true
                    }
                    is NetworkRequest.Error -> {
                        binding.apply {
                            infoContainer.isVisible = true
                            loadingP.isVisible = false
                            constraintB.isVisible = false
                        }
                        Toast.makeText(requireContext(), response.error, Toast.LENGTH_SHORT).show()
                    }
                }

            }
        }

    private fun loadLoadAvatarData() {
        binding.apply {
            profileViewModel.avatarData.observe(viewLifecycleOwner) { response ->
                when (response) {
                    is NetworkRequest.Loading -> {
                        avatarLoading.isVisible = true
                    }

                    is NetworkRequest.Success -> {
                        avatarLoading.isVisible = false
                        if (isNetworkAvailable)
                            profileViewModel.profileData(token)
                    }

                    is NetworkRequest.Error -> {
                        avatarLoading.isVisible = false
                        root.showSnackBar(response.error!!)
                    }
                }
            }
        }
    }
}