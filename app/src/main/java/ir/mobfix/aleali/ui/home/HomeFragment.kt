package ir.mobfix.aleali.ui.home

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import ir.mobfix.aleali.R
import ir.mobfix.aleali.data.models.ModelsStudent
import ir.mobfix.aleali.data.stored.GroupManager
import ir.mobfix.aleali.data.stored.SessionManager
import ir.mobfix.aleali.databinding.DialogCalssBinding
import ir.mobfix.aleali.databinding.FragmentHomeBinding

import ir.mobfix.aleali.ui.home.adapters.ClassAdapter
import ir.mobfix.aleali.ui.home.adapters.StudentAdapter
import ir.mobfix.aleali.ui.home.adapters.TeacherAdapter
import ir.mobfix.aleali.utils.extensions.transparentCorners
import ir.mobfix.aleali.utils.network.NetworkChecker
import ir.mobfix.aleali.utils.network.NetworkRequest
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
    //Other
    private val studentList :MutableList<ModelsStudent> = mutableListOf()
    private val teacherList :MutableList<ModelsStudent> = mutableListOf()
    private val classList :MutableList<ModelsStudent> = mutableListOf()

    private val profileViewModel: ProfileViewModel by viewModels()
    @Inject
    lateinit var networkChecker: NetworkChecker
    @Inject
    lateinit var studentAdapter:StudentAdapter
    @Inject
    lateinit var teacherAdapter:TeacherAdapter
    @Inject
    lateinit var classAdapter : ClassAdapter
    @Inject
    lateinit var groupManager: GroupManager
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
                    profileViewModel.profileData(token)
                    loadG()
                    setMenuStudents()
                    setMenuTeacher()
                }
            }

        }
    }

    @SuppressLint("SetTextI18n")
    private fun loadG() {
       profileViewModel.profileData.observe(viewLifecycleOwner){response->
           when (response) {
               is NetworkRequest.Loading -> {}
               is NetworkRequest.Success -> {
                   response.data?.let {res->
                       res.userGroups.let {
                           if (it!!.isEmpty()){
                               Toast.makeText(requireContext(), "دسترسی ندارید", Toast.LENGTH_SHORT).show()
                           }else{
                               val userGroup =it[0]
                               when(userGroup!!.id){
                                   1 ->{
                                       studentList()
                                       binding.topBar.toolbarTitleTxt.text = "${res.firstName} ${userGroup.name} ${getString(R.string.well)}"
                                   }
                                   2->{
                                       listTeachers()
                                       binding.topBar.toolbarTitleTxt.text = "${res.firstName} ${userGroup.name} ${getString(R.string.well)}"
                                   }
                                   3 ->{
                                       binding.topBar.toolbarTitleTxt.text = "${res.firstName} ${userGroup.name} ${getString(R.string.well)}"
                                   }
                               }
                           }


                       }


                   }
               }
               is NetworkRequest.Error -> {}
           }
       }
    }
    private fun studentList(){
        studentAdapter.setData(studentList)
        binding.recycler1.apply {
            layoutManager =GridLayoutManager(requireContext(),2)
            adapter = studentAdapter
        }
        studentAdapter.setOnItemClickListener {
            when(it.id){
                1->{ findNavController().navigate(R.id.claasFragment) }
                2->{showDialog()}
                3->{}
                4->{}
            }
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
    fun setMenuStudents(){
        studentList.clear()
        studentList.add(ModelsStudent(1,getString(R.string.barnameh_c),R.drawable.classs,R.color.lightTurquoise))
        studentList.add(ModelsStudent(2,getString(R.string.barnameh_e),R.drawable.examlist,R.color.lightSalmon))

    }
    fun setMenuTeacher(){
        teacherList.clear()
        teacherList.add(ModelsStudent(1,getString(R.string.clas),R.drawable.classs,R.color.red))
        teacherList.add(ModelsStudent(2,getString(R.string.emtehan),R.drawable.examlist,R.color.darkTurquoise))
        teacherList.add(ModelsStudent(3,getString(R.string.karnameh),R.drawable.certificate,R.color.chineseYellow))
        teacherList.add(ModelsStudent(4,getString(R.string.ertebat),R.drawable.logo,R.color.gray))
    }
    fun setMenuClass(){
        classList.clear()
        classList.add(ModelsStudent(1,getString(R.string.aval),R.drawable.classs,R.color.red))
        classList.add(ModelsStudent(2,getString(R.string.dovom),R.drawable.examlist,R.color.darkTurquoise))
        classList.add(ModelsStudent(3,getString(R.string.sevom),R.drawable.examlist,R.color.darkTurquoise))
        classList.add(ModelsStudent(4,getString(R.string.charom),R.drawable.examlist,R.color.darkTurquoise))
        classList.add(ModelsStudent(5,getString(R.string.panjom),R.drawable.examlist,R.color.darkTurquoise))
        classList.add(ModelsStudent(6,getString(R.string.sheshom),R.drawable.examlist,R.color.darkTurquoise))
    }
    private fun showDialog() {
        val dialog = Dialog(requireContext())
        val dialogBinding = DialogCalssBinding.inflate(layoutInflater)
        dialog.transparentCorners()
        dialog.setContentView(dialogBinding.root)
        dialogBinding.apply {
            setMenuClass()
            classAdapter.setData(classList)
            close.setOnClickListener { dialog.dismiss() }
            listCalss.apply {
                layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
                adapter = classAdapter
            }
            classAdapter.setOnItemClickListener {
                val direction = HomeFragmentDirections.actionToExamFragment(it.text)
                findNavController().navigate(direction)
                dialog.dismiss()
            }
        }
        dialog.show()
    }
    private fun listTeachers(){
        teacherAdapter.setData(teacherList)
        binding.recycler1.apply {
            layoutManager =GridLayoutManager(requireContext(),2)
            adapter = teacherAdapter
        }
        teacherAdapter.setOnItemClickListener {

        }
    }

}