package cu.wilb3r.iptvplayerdemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import cu.wilb3r.iptvplayerdemo.databinding.ActivityMainBinding
import cu.wilb3r.iptvplayerdemo.ui.fragments.FavFragment
import cu.wilb3r.iptvplayerdemo.ui.fragments.HomeFragment
import cu.wilb3r.iptvplayerdemo.ui.fragments.ItemsFragmentFactory
import cu.wilb3r.iptvplayerdemo.ui.fragments.ProfileFragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    @Inject
    lateinit var fragmentFactory: ItemsFragmentFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportFragmentManager.fragmentFactory = fragmentFactory
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setFragment(
            supportFragmentManager.fragmentFactory.instantiate(
                classLoader,
                HomeFragment::class.java.name
            ), "Home"
        )

        binding.bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.menu_home -> {
                    setFragment(
                        supportFragmentManager.fragmentFactory.instantiate(
                            classLoader,
                            HomeFragment::class.java.name
                        ), "Home"
                    )
                }
                R.id.menu_fav -> {
                    setFragment(
                        supportFragmentManager.fragmentFactory.instantiate(
                            classLoader,
                            FavFragment::class.java.name
                        ), "Fav"
                    )
                }
                R.id.menu_profile -> {
                    setFragment(
                        supportFragmentManager.fragmentFactory.instantiate(
                            classLoader,
                            ProfileFragment::class.java.name
                        ), "profile"
                    )
                }
            }
            true
        }
    }

    private fun setFragment(fragment: Fragment, tag: String) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.frgContainer, fragment, tag)
            commit()
        }
    }

}