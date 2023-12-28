package ir.mobfix.aleali.ui.main

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.isVisible
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import dagger.hilt.android.AndroidEntryPoint
import io.github.inflationx.viewpump.ViewPumpContextWrapper
import ir.mobfix.aleali.R
import ir.mobfix.aleali.databinding.ActivityMainBinding


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    //Binding
    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!


    //Other
    private lateinit var navHost: NavHostFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //Init nav host
        navHost = supportFragmentManager.findFragmentById(R.id.navHost) as NavHostFragment
        //Bottom nav menu
        binding.bottomNav.apply {
            setupWithNavController(navHost.navController)
            //Disable double click on items
            setOnNavigationItemReselectedListener {}
        }
        //Gone bottom menu
        navHost.navController.addOnDestinationChangedListener { _, destination, _ ->
            binding.apply {
                when (destination.id) {
                    R.id.homeFragment -> bottomNav.isVisible = true
                    R.id.profileFragment -> bottomNav.isVisible = true
                    R.id.settingsFragment -> bottomNav.isVisible =true
                    else -> bottomNav.isVisible = false
                }
            }
        }

    }

    override fun onNavigateUp(): Boolean {
        return navHost.navController.navigateUp() || super.onNavigateUp()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    //Calligraphy
    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase))
    }
}