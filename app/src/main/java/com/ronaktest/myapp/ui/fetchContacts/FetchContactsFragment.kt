package com.ronaktest.myapp.ui.fetchContacts

import android.Manifest
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import androidx.loader.app.LoaderManager
import androidx.loader.content.CursorLoader
import androidx.loader.content.Loader
import androidx.recyclerview.widget.RecyclerView
import com.ronaktest.myapp.R
import com.ronaktest.myapp.databinding.FragmentFetchContactsBinding
import com.ronaktest.myapp.di.ViewModelProviderFactory
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_fetch_contacts.*
import javax.inject.Inject

class FetchContactsFragment : DaggerFragment() {
    private val mSearchString = "9"
    private var mContactListView: RecyclerView? = null
    @Inject
    lateinit var factory: ViewModelProviderFactory
    private lateinit var viewModel: FetchContactsFragmentViewModel
    private var fetch_contacts_progress: ProgressBar? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentFetchContactsBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_fetch_contacts, container, false)
        binding.setLifecycleOwner(this.viewLifecycleOwner)
        binding.fetchContactsProgress.setVisibility(View.VISIBLE)

        fetch_contacts_progress=binding.fetchContactsProgress
        viewModel = ViewModelProviders.of(this, factory).get(FetchContactsFragmentViewModel::class.java)
        requestContacts()
        mContactListView= binding.rvContactList
        return binding.root
    }

    private fun requestContacts() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && activity!!.checkSelfPermission(
                Manifest.permission.READ_CONTACTS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(
                arrayOf(Manifest.permission.READ_CONTACTS),
                PERMISSIONS_REQUEST_READ_CONTACTS
            )
        } else {
            showContacts()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode == PERMISSIONS_REQUEST_READ_CONTACTS) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                showContacts()
            } else {
                Log.e("Permissions", "Access denied")
            }
        }
    }

    private fun showContacts() { // Initializes a loader for loading the contacts
        LoaderManager.getInstance(this).initLoader(
            0,
            null,
            object : LoaderManager.LoaderCallbacks<Cursor> {
                override fun onCreateLoader(
                    i: Int,
                    bundle: Bundle?
                ): Loader<Cursor> { /*
                 * Makes search string into pattern and
                 * stores it in the selection array
                 */
                    val contentUri = Uri.withAppendedPath(
                        ContactsContract.Contacts.CONTENT_FILTER_URI,
                        Uri.encode(mSearchString)
                    )
                    // Starts the query
                    return CursorLoader(
                        activity!!,
                        contentUri,
                        PROJECTION,
                        null,
                        null,
                        null
                    )
                }

                override fun onLoadFinished(
                    objectLoader: Loader<Cursor>,
                    c: Cursor
                ) { // Put the result Cursor in the adapter for the ListView
                    try {
                        fetch_contacts_progress!!.setVisibility(View.GONE)
                        //println("Value of c is: "+c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME_PRIMARY))
                        //if(!c.isLast)
                        mContactListView!!.adapter = ContactsAdapter(c)
                    } catch (e: Exception) {
                    }
                }

                override fun onLoaderReset(cursorLoader: Loader<Cursor>) { // TODO do I need to do anything here?
                }
            })
    }

    companion object {
        private val PROJECTION = arrayOf(
            ContactsContract.Contacts._ID,
            ContactsContract.Contacts.LOOKUP_KEY,
            ContactsContract.Contacts.DISPLAY_NAME_PRIMARY
        )
        private const val SELECTION =
            ContactsContract.Contacts.DISPLAY_NAME_PRIMARY + " LIKE ?"
        // Request code for READ_CONTACTS. It can be any number > 0.
        private const val PERMISSIONS_REQUEST_READ_CONTACTS = 100
    }
}