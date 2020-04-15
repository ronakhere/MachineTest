package com.ronaktest.myapp.ui.addContact


import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.addCallback
import androidx.core.widget.doAfterTextChanged
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.ronaktest.myapp.MainActivity

import com.ronaktest.myapp.R
import com.ronaktest.myapp.data.Contact
import com.ronaktest.myapp.databinding.FragmentAddContactBinding
import com.ronaktest.myapp.di.ViewModelProviderFactory
import com.ronaktest.myapp.util.OPTIONS
import dagger.android.support.DaggerFragment
import timber.log.Timber
import java.util.regex.Pattern
import javax.inject.Inject

class AddContactFragment : DaggerFragment() {
    val REQUEST_IMAGE = 100
    var profilePicturePath: String? = null
    val EMAIL_PATERN = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
    private lateinit var  binding: FragmentAddContactBinding

    @Inject
    lateinit var factory: ViewModelProviderFactory

    private lateinit var viewModel: AddContactViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {

        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_add_contact, container, false)

        binding.setLifecycleOwner(this.viewLifecycleOwner)

        viewModel = ViewModelProviders.of(this, factory).get(AddContactViewModel::class.java)

        val args = AddContactFragmentArgs.fromBundle(arguments!!)

        if (args.id != -1){
            val contact = viewModel.getContactById(args.id)
            contact.observe(this, Observer {
                binding.contact = it
            })
        }

        binding.firstNameEditText.doAfterTextChanged {
            viewModel.firstName = it.toString()
        }

        binding.lastNameEditText.doAfterTextChanged {
            viewModel.lastName = it.toString()
        }

        binding.phoneNumberEditText.doAfterTextChanged {
            viewModel.phoneNumber = it.toString()
        }

        binding.emailEditText.doAfterTextChanged {
            viewModel.email = it.toString()
        }

        binding.uploadProfilePictureImageView.setOnClickListener{
            selectProfilePicture()
        }

        binding.saveButton.setOnClickListener {
            if (isValid()) {
                if (args.id == -1) {
                    Timber.d("Hello this contact name is ${viewModel.firstName} ${viewModel.lastName}")
                    viewModel.saveContact(
                        Contact(
                            viewModel.firstName,
                            viewModel.lastName,
                            viewModel.phoneNumber,
                            viewModel.email,
                            profilePicturePath
                        )
                    )
                    it.findNavController().navigate(
                        AddContactFragmentDirections
                            .actionAddContactFragmentToContactsFragment()
                    )
                } else {
                    Timber.d("Hello this contact name is ${viewModel.firstName}")
                    viewModel.updateContact(
                        Contact(
                            viewModel.firstName,
                            viewModel.lastName,
                            viewModel.phoneNumber,
                            viewModel.email,
                            profilePicturePath,
                            args.id
                        )
                    )
                    Timber.d("Hello image is ${profilePicturePath}")

                    it.findNavController().navigate(
                        AddContactFragmentDirections
                            .actionAddContactFragmentToContactsFragment()
                    )
                }
            }
        }

        binding.cancelButton.setOnClickListener { view: View ->
            view.findNavController().navigate(R.id.action_addContactFragment_to_contactsFragment)
        }

        requireActivity().onBackPressedDispatcher.addCallback(this)
        {
            // handling back button
            findNavController().navigate(AddContactFragmentDirections.actionAddContactFragmentToContactsFragment(), OPTIONS)
        }

        return binding.root
    }

    fun isValid(): Boolean {
        if (viewModel.firstName.equals("")) {
            showShortToast(getString(R.string.enter_name))
            return false
        } else if (viewModel.lastName.equals("")) {
            showShortToast(getString(R.string.enter_last_name))
            return false
        } else if (!isValidMobile(viewModel.phoneNumber.trim())) {
            showShortToast(getString(R.string.plz_enter_valid_number))
            return false
        } else if (viewModel.email.trim().equals("")) {
            showShortToast(getString(R.string.enter_valid_email_address))
            return false
        } else if (!viewModel.email.trim().matches(EMAIL_PATERN.toRegex())) {
            showShortToast(getString(R.string.enter_valid_email_address))
            return false
        }
        return true
    }

    fun showShortToast(message: String) {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1) {
            val toast =
                Toast.makeText(activity, message, Toast.LENGTH_SHORT)
            toast.show()
        }else{
            Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
        }
    }

    private fun isValidMobile(phone: String): Boolean {
        if(!Pattern.matches("[a-zA-Z]+", phone)) {
            return phone.length > 6 && phone.length <= 10
        }
        return false
    }

    fun selectProfilePicture(){
        val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
            type = "image/*"
        }
        if (intent.resolveActivity(activity!!.packageManager) != null){
            startActivityForResult(intent, REQUEST_IMAGE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE && resultCode == Activity.RESULT_OK){
            val imageUri = data?.data
            Timber.d("URI ${imageUri}")

            profilePicturePath = imageUri.toString()
            Timber.d("image path ***** ${profilePicturePath}")
            binding.uploadProfilePictureImageView.setImageURI(data?.data)
        }
    }

    override fun onResume() {
        super.onResume()
        (activity as MainActivity).supportActionBar?.title = "Add New Contact"
    }
}
