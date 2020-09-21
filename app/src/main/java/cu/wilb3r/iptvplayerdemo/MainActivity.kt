 package cu.wilb3r.iptvplayerdemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import cu.wilb3r.iptvplayerdemo.databinding.ActivityMainBinding
import cu.wilb3r.iptvplayerdemo.ui.fragments.FavFragment
import cu.wilb3r.iptvplayerdemo.ui.fragments.HomeFragment
import cu.wilb3r.iptvplayerdemo.ui.fragments.ProfileFragment
import dagger.hilt.android.AndroidEntryPoint

 @AndroidEntryPoint
 class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setFragment(HomeFragment(), "home")
        binding.bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.menu_home -> {
                    setFragment(HomeFragment(), "home")
                }
                R.id.menu_fav -> {
                    setFragment(FavFragment(), "Fav")
                }
                R.id.menu_profile -> {
                    setFragment(ProfileFragment(), "profile")
                }
            }
            true
        }

        //setBudge(R.id.menu_fav, 9)

    }

     private fun setFragment(fragment: Fragment, tag: String) {
         supportFragmentManager.beginTransaction().apply {
             replace(R.id.frgContainer, fragment, tag)
             commit()
         }
     }

     fun setBudge(resourse: Int, count: Int) =
         binding.bottomNavigationView.getOrCreateBadge(resourse).apply {
             number = count
             isVisible = true
         }
}