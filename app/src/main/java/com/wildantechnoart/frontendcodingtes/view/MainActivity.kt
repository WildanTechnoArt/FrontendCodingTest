package com.wildantechnoart.frontendcodingtes.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.wildantechnoart.frontendcodingtes.MyApp
import com.wildantechnoart.frontendcodingtes.R
import com.wildantechnoart.frontendcodingtes.databinding.ActivityMainBinding
import com.wildantechnoart.frontendcodingtes.utils.ViewBindingExt.viewBinding
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private val binding by viewBinding(ActivityMainBinding::inflate)
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        val navController = getNavController()
        navController?.let { controller ->
            appBarConfiguration = AppBarConfiguration(controller.graph)
            setupActionBarWithNavController(controller, appBarConfiguration)
        }
        addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu_main, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.menu_logout -> {
                        val builder = MaterialAlertDialogBuilder(this@MainActivity).apply {
                            setTitle("Konfirmasi")
                            setMessage("Kamu yakin ingin keluar?")
                            setPositiveButton("Iya") { dialog, _ ->
                                lifecycleScope.launch {
                                    MyApp.getInstance().clearDataStore(this@MainActivity)
                                    startActivity(Intent(this@MainActivity, AuthActivity::class.java))
                                    finish()
                                }
                                dialog.dismiss()
                            }
                            setNegativeButton("Tidak") { dialog, _ ->
                                dialog.dismiss()
                            }
                        }

                        builder.show()
                        true
                    }
                    else -> false
                }
            }
        })
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