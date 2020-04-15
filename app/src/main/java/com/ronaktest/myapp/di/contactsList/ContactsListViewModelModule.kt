package com.ronaktest.myapp.di.contactsList

import androidx.lifecycle.ViewModel
import com.ronaktest.myapp.di.ViewModelKey
import com.ronaktest.myapp.ui.contactsList.ContactsListViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ContactsListViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(ContactsListViewModel::class)
    abstract fun bindContactsListViewModel(contactsListViewModel: ContactsListViewModel): ViewModel

}