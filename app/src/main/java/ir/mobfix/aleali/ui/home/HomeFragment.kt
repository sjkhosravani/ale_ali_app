package ir.mobfix.aleali.ui.home

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import ir.mobfix.aleali.R
import ir.mobfix.aleali.data.models.ModelsStudent
import ir.mobfix.aleali.databinding.DialogCalssBinding
import ir.mobfix.aleali.databinding.FragmentHomeBinding
import ir.mobfix.aleali.databinding.FragmentLoginBinding
import ir.mobfix.aleali.ui.home.adapters.ClassAdapter
import ir.mobfix.aleali.ui.home.adapters.StudentAdapter
import ir.mobfix.aleali.ui.home.adapters.TeacherAdapter
import ir.mobfix.aleali.utils.extensions.showSnackBar
import ir.mobfix.aleali.utils.extensions.transparentCorners
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


    @Inject
    lateinit var studentAdapter:StudentAdapter

    @Inject
    lateinit var teacherAdapter:TeacherAdapter

    @Inject
    lateinit var classAdapter : ClassAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentHomeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setMenuStudents()
        setMenuTeacher()

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            //Init Views
            studentAdapter.setData(studentList)
            recycler1.apply {
                layoutManager = GridLayoutManager(requireContext(),2)
                adapter = studentAdapter
            }
            teacherAdapter.setData(teacherList)

            studentAdapter.setOnItemClickListener {
                showDialog()
            }

        }
    }
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    fun setMenuStudents(){
        classList.clear()
        studentList.add(ModelsStudent(1,getString(R.string.barnameh_c),R.drawable.classs,R.color.lightTurquoise))
        studentList.add(ModelsStudent(2,getString(R.string.barnameh_e),R.drawable.examlist,R.color.lightSalmon))
        studentList.add(ModelsStudent(3,getString(R.string.karnameh),R.drawable.certificate,R.color.caribbeanGreen))
        studentList.add(ModelsStudent(4,getString(R.string.ertebat),R.drawable.logo,R.color.crayola))
    }

    fun setMenuTeacher(){
        classList.clear()
        teacherList.add(ModelsStudent(1,getString(R.string.clas),R.drawable.classs,R.color.red))
        teacherList.add(ModelsStudent(2,getString(R.string.emtehan),R.drawable.examlist,R.color.darkTurquoise))
        teacherList.add(ModelsStudent(3,getString(R.string.karnameh),R.drawable.certificate,R.color.chineseYellow))
        teacherList.add(ModelsStudent(4,getString(R.string.ertebat),R.drawable.logo,R.color.gray))
    }

    fun setMenuClass(){
        classList.clear()
        classList.add(ModelsStudent(1,getString(R.string.aval),R.drawable.classs,R.color.red))
        classList.add(ModelsStudent(2,getString(R.string.dovom),R.drawable.examlist,R.color.darkTurquoise))
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
                dialog.dismiss()
                findNavController().navigate(R.id.programFragment)
            }
        }
        dialog.show()
    }
}