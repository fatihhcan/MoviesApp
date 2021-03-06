package com.mustafa.movieguideapp.view.ui.main

import android.content.Intent
import android.os.Bundle
import android.speech.RecognizerIntent
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.NavDestination
import androidx.navigation.fragment.NavHostFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.mustafa.movieguideapp.R
import com.mustafa.movieguideapp.extension.*
import com.mustafa.movieguideapp.utils.setupWithNavController
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import javax.inject.Inject


/**
 * An activity that inflates a layout that has a [BottomNavigationView].
 */
class MainActivity : AppCompatActivity(), HasAndroidInjector {
    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Any>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        setSplashy()
        if (savedInstanceState == null) {
            setupBottomNavigationBar()
        } // Else, need to wait for onRestoreInstanceState
        setOnNavigationItemReselected()

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_container) as NavHostFragment


        navHostFragment.navController.addOnDestinationChangedListener { _, destination, _ ->
            if (isMainFragment(destination)) {
                bottom_navigation.visible()
            } else bottom_navigation.gone()

        }
    }


    override fun onBackPressed() {
//    val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_container)
//    val backStackEntryCount = navHostFragment?.childFragmentManager?.backStackEntryCount
//    for (x in 0 until backStackEntryCount!!) {
//      val entry = navHostFragment.childFragmentManager.getBackStackEntryAt(x)
//      Timber.d("BackStack ${entry.name}")
//    }
        val currentFragment: Fragment? = supportFragmentManager.getCurrentNavigationFragment()
        val currentFragmentName = (currentFragment as Fragment).javaClass.simpleName

        if (currentFragmentName == MOVIE_LIST_FRAGMENT) {
            if (!currentFragment.isRecyclerViewScrollPositionZero(R.id.recyclerView_list_movies)!!) {
                currentFragment.setSmoothScrollToZero(R.id.recyclerView_list_movies)
            } else {
                super.onBackPressed()
            }
        } else if (currentFragmentName == TV_LIST_FRAGMENT) {
            if (!currentFragment.isRecyclerViewScrollPositionZero(R.id.recyclerView_list_tvs)!!) {
                currentFragment.setSmoothScrollToZero(R.id.recyclerView_list_tvs)
            } else {
                super.onBackPressed()
            }
        } else if (currentFragmentName == CELEBRITY_LIST_FRAGMENT) {
            if (!currentFragment.isRecyclerViewScrollPositionZero(R.id.recyclerView_list_celebrities)!!) {
                currentFragment.setSmoothScrollToZero(R.id.recyclerView_list_celebrities)
            } else {
                super.onBackPressed()
            }
        } else super.onBackPressed()
    }


    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
        super.onRestoreInstanceState(savedInstanceState)
        // Now that BottomNavigationBar has restored its instance state
        // and its selectedItemId, we can proceed with setting up the
        // BottomNavigationBar with Navigation
        setupBottomNavigationBar()
    }

    /**
     * Called on first creation and when restoring state.
     */
    private fun setupBottomNavigationBar() {
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)

        val navGraphIds = listOf(R.navigation.movie, R.navigation.tv, R.navigation.star)

        // Setup the bottom navigation view with a list of navigation graphs
        bottomNavigationView.setupWithNavController(
            navGraphIds = navGraphIds,
            fragmentManager = supportFragmentManager,
            containerId = R.id.nav_host_container,
            intent = intent
        )

        //Whenever the selected controller changes, setup the action bar.
//    controller.observe(this, Observer { navController ->
//      setupActionBarWithNavController(navController)
//    })
    }

//  override fun onSupportNavigateUp(): Boolean {
//    return currentNavController?.value?.navigateUp() ?: false
//  }

    private fun setOnNavigationItemReselected() {
        bottom_navigation.setOnNavigationItemReselectedListener {
            when (it.itemId) {
                R.id.movie -> supportFragmentManager.getCurrentNavigationFragment()
                    ?.setSmoothScrollToZero(R.id.recyclerView_list_movies)
                R.id.tv -> supportFragmentManager.getCurrentNavigationFragment()
                    ?.setSmoothScrollToZero(R.id.recyclerView_list_tvs)
                R.id.star -> supportFragmentManager.getCurrentNavigationFragment()
                    ?.setSmoothScrollToZero(R.id.recyclerView_list_celebrities)
            }
        }
    }


    private fun isMainFragment(destination: NavDestination): Boolean =
        destination.id == R.id.moviesFragment || destination.id == R.id.tvsFragment || destination.id == R.id.celebritiesFragment


    override fun androidInjector(): AndroidInjector<Any> = dispatchingAndroidInjector

//    private fun setSplashy() {
//        Splashy(this)
//            .setLogo(R.mipmap.ic_launcher_foreground)
//            .setTitle("MovieGuide")
//            .setTitleColor(R.color.colorAccent)
//            .showProgress(true)
//            .setProgressColor(R.color.colorAccent)
//            .setSubTitle("Eng. Mustafa Shahoud")
//            .setProgressColor(R.color.colorAccent)
//            .setBackgroundResource(R.color.backgroundDarker)
//            .setFullScreen(true)
//            .setDuration(3000)
//            .show()
//    }

    companion object {
        const val MOVIE_LIST_FRAGMENT = "MovieListFragment"
        const val TV_LIST_FRAGMENT = "TvListFragment"
        const val CELEBRITY_LIST_FRAGMENT = "CelebritiesListFragment"
    }

    fun getVoiceRecognitionIntent(): Intent? {
        val voiceIntent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        with(voiceIntent) {
            putExtra(
                RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
            )
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
        }
        return if (this.packageManager?.let { packageManager ->
                voiceIntent.resolveActivity(
                    packageManager
                )
            } != null) {
            voiceIntent
        } else {
            Toast.makeText(
                this,
                "your device does not support Speech Input",
                Toast.LENGTH_SHORT
            ).show()
            null
        }
    }
}




