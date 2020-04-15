package com.ronaktest.myapp.di.addContact

import androidx.lifecycle.ViewModel
import com.ronaktest.myapp.di.ViewModelKey
import com.ronaktest.myapp.ui.addContact.AddContactViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class AddContactViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(AddContactViewModel::class)
    abstract fun bindAddContactViewModel(addContactViewModel: AddContactViewModel): ViewModel
}