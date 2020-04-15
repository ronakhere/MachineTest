package com.ronaktest.myapp.di.contactDetails

import androidx.lifecycle.ViewModel
import com.ronaktest.myapp.di.ViewModelKey
import com.ronaktest.myapp.ui.fetchContacts.FetchContactsFragmentViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class FetchContactsFragmentViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(FetchContactsFragmentViewModel::class)
    abstract fun bindContactDetailsViewModel(FetchContactsFragmentViewModel: FetchContactsFragmentViewModel): ViewModel
}