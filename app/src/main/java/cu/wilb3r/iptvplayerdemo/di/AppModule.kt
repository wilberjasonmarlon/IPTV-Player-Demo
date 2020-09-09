package cu.wilb3r.iptvplayerdemo.di

import cu.wilb3r.iptvplayerdemo.repos.PrincipalRepo
import cu.wilb3r.iptvplayerdemo.ui.vm.HomeViewModel
import cu.wilb3r.iptvplayerdemo.utils.initKtorClient
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

object AppModule {
    val myModule = module {
        single { PrincipalRepo() }
        single { initKtorClient() }
        viewModel { HomeViewModel(get()) }
    }
}