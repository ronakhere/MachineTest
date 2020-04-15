package com.ronaktest.myapp.di

import com.ronaktest.myapp.di.addContact.AddContactViewModelModule
import com.ronaktest.myapp.di.contactDetails.ContactDetailsViewModelModule
import com.ronaktest.myapp.di.contactDetails.FetchContactsFragmentViewModelModule
import com.ronaktest.myapp.di.contactsList.ContactsListViewModelModule
import com.ronaktest.myapp.di.updateProfile.UpdateProfileViewModelModule
import com.ronaktest.myapp.ui.addContact.AddContactFragment
import com.ronaktest.myapp.ui.contactDetails.ContactDetailsFragment
import com.ronaktest.myapp.ui.contactsList.ContactsListFragment
import com.ronaktest.myapp.ui.fetchContacts.FetchContactsFragment
import com.ronaktest.myapp.ui.updateProfile.UpdateProfileFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FragmentBuilderModule {
    @ContributesAndroidInjector(modules = [ContactsListViewModelModule::class])
    abstract fun contributeContactsFragment() : ContactsListFragment

    @ContributesAndroidInjector(modules = [AddContactViewModelModule::class])
    abstract fun contributeAddContactFragment() : AddContactFragment

    @ContributesAndroidInjector(modules = [ContactDetailsViewModelModule::class])
    abstract fun contributeContactDetailsFragment() : ContactDetailsFragment

    @ContributesAndroidInjector(modules = [FetchContactsFragmentViewModelModule::class])
    abstract fun contributeFetchContactsFragment() : FetchContactsFragment

    @ContributesAndroidInjector(modules = [UpdateProfileViewModelModule::class])
    abstract fun contributeUpdateProfileFragment() : UpdateProfileFragment
}