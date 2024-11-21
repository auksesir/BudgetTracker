package main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import uk.ac.bbk.dcs.budgettracker.R
import uk.ac.bbk.dcs.budgettracker.databinding.ActivityMainBinding
import androidx.activity.viewModels
import viewmodel.ExpenseViewModel

class MainActivity : AppCompatActivity() {
    //For managing expenses related data
    private val viewModel: ExpenseViewModel by viewModels()
    //for accessing views in the activity
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //using binding to inflate the layout
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //actionbar for the AppBar defined in the layout
        setSupportActionBar(binding.appBarMain)

        //initializing navigation components
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_content_main) as NavHostFragment
        val navController = navHostFragment.navController

        //AppBarConfiguration with DrawerLayout and top level destinations
        val drawerLayout = binding.drawerLayout
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.ExpenseListFragment, R.id.AddExpenseFragment, R.id.CategoryTotalsFragment
            ), drawerLayout
        )

        //connecting NAvController and ActionBar
        setupActionBarWithNavController(navController, appBarConfiguration)
        //connecting NavigationView to the NavController
        binding.navView.setupWithNavController(navController)

        // initial set up
        viewModel.checkAndUpdateCategoryTotals()


    }

    //when the ActionBar Up button is pressed
    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}
