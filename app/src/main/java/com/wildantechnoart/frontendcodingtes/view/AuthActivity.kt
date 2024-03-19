package com.wildantechnoart.frontendcodingtes.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.wildantechnoart.frontendcodingtes.MyApp
import com.wildantechnoart.frontendcodingtes.R
import com.wildantechnoart.frontendcodingtes.databinding.ActivityAuthBinding
import com.wildantechnoart.frontendcodingtes.utils.Constant
import com.wildantechnoart.frontendcodingtes.utils.ViewBindingExt.viewBinding
import kotlinx.coroutines.launch

class AuthActivity : AppCompatActivity() {

    private val binding by viewBinding(ActivityAuthBinding::inflate)
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launch {
            val token =
                MyApp.getInstance().readStringDataStore(this@AuthActivity, Constant.TOKEN_KEY_ACCESS)
            if(token != null){
                startActivity(Intent(this@AuthActivity, MainActivity::class.java))
                finish()
            }
        }

        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        val navController = getNavController()
        navController?.let { controller ->
            appBarConfiguration = AppBarConfiguration(controller.graph)
            setupActionBarWithNavController(controller, appBarConfiguration)
        }
    }

    private fun getNavController(): NavController? {
        val fragment: Fragment? =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity)
        if (fragment !is NavHostFragment) {
            throw IllegalStateException(
                "Activity " + this
                        + " does not have a NavHostFragment"
            )
        }
        return (fragment as NavHostFragment?)?.navController
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_activity)
        return appBarConfiguration.let { navController.navigateUp(it) } || super.onSupportNavigateUp()
    }
}