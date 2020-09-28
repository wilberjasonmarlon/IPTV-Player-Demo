package cu.wilb3r.iptvplayerdemo.ui.fragments

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import cu.wilb3r.iptvplayerdemo.ui.adapters.ItemAdapter
import javax.inject.Inject

class ItemsFragmentFactory @Inject constructor(
    private val adapter: ItemAdapter
) : FragmentFactory() {

    override fun instantiate(classLoader: ClassLoader, className: String): Fragment {
        return when (className) {
            HomeFragment::class.java.name -> HomeFragment(adapter)
            FavFragment::class.java.name -> FavFragment()
            ProfileFragment::class.java.name -> ProfileFragment()
            else -> super.instantiate(classLoader, className)
        }
    }
}