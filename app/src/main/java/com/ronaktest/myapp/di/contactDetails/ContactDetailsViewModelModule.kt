package com.ronaktest.myapp.di.contactDetails

import androidx.lifecycle.ViewModel
import com.ronaktest.myapp.di.ViewModelKey
import com.ronaktest.myapp.ui.contactDetails.ContactDetailsViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ContactDetailsViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(ContactDetailsViewModel::class)
    abstract fun bindContactDetailsViewModel(contactDetailsViewModel: ContactDetailsViewModel): ViewModel
}