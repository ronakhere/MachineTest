package com.ronaktest.myapp.di.updateProfile

import androidx.lifecycle.ViewModel
import com.ronaktest.myapp.di.ViewModelKey
import com.ronaktest.myapp.ui.updateProfile.UpdateProfileFragmentViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class UpdateProfileViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(UpdateProfileFragmentViewModel::class)
    abstract fun bindContactsListViewModel(updateProfileFragmentViewModel: UpdateProfileFragmentViewModel):
            ViewModel
}