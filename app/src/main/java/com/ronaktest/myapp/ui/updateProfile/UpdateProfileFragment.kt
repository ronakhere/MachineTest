package com.ronaktest.myapp.ui.updateProfile

import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.ronaktest.myapp.R
import com.ronaktest.myapp.databinding.FragmentUpdateProfileBinding
import com.ronaktest.myapp.di.ViewModelProviderFactory
import com.ronaktest.myapp.util.AppPreference
import com.ronaktest.myapp.util.PathUtil
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_update_profile.*
import java.io.*
import java.util.*
import java.util.concurrent.TimeUnit
import java.util.regex.Pattern
import javax.inject.Inject


class UpdateProfileFragment : DaggerFragment() {
    @Inject
    lateinit var factory: ViewModelProviderFactory
    private lateinit var viewModel: UpdateProfileFragmentViewModel
    private val CAMERA_PERMISSION_REQUEST_CODE = 88
    val OPEN_CAMERA = 2
    val OPEN_GALLARY = 3
    private var selectedImage: String? = null
    private var storedVerificationId: String? = null
    private lateinit var resendToken: PhoneAuthProvider.ForceResendingToken

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentUpdateProfileBinding =
            DataBindingUtil.inflate(inflater,R.layout.fragment_update_profile,container,false)
        binding.setLifecycleOwner(this.viewLifecycleOwner)
        viewModel = ViewModelProviders.of(this, factory).get(UpdateProfileFragmentViewModel::class.java)



        var name= AppPreference.getInstance(activity).getString("NAME")
        binding.edUserName.setText(name)



        var email= AppPreference.getInstance(activity).getString("EMAIL")
        binding.edEmail.setText(email)

        binding.imagePick.setOnClickListener {
            if (checkPermission())
                openCameraAndGalley()
        }

        binding.btnUpdate.setOnClickListener {
            if (isValidMobile(binding.edMobile.text.toString())){
                checkFirebaseOTP(binding.edMobile.text.toString())
            }else{
                Toast.makeText(activity,"Mobile no. is not valid", Toast.LENGTH_SHORT).show()
            }
        }

        return binding.root
    }

    private fun checkFirebaseOTP(phoneNo: String) {
       // val phoneCallbacks = PhoneCallbacks(this)

     /*   PhoneAuthProvider.getInstance().verifyPhoneNumber(phoneNo, 60,
            TimeUnit.SECONDS, this, phoneCallbacks)*/

      /*  var auth = FirebaseAuth.getInstance()
        auth.setLanguageCode(Locale.getDefault().language)*/
        Toast.makeText(activity,"Firebase code..", Toast.LENGTH_SHORT).show()

        /*val intent = Intent(this@MainActivity, PhoneNumberVerification::class.java)
        startActivity(intent)*/
    }

    private fun isValidMobile(phone: String): Boolean {
    if(!Pattern.matches("[a-zA-Z]+", phone)) {
        return phone.length > 6 && phone.length <= 10
    }
    return false;
    }


    private fun checkPermission(): Boolean {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true
        }
        // request camera permission if it has not been granted.
        if (ActivityCompat.checkSelfPermission(
                context!!,
                Manifest.permission.CAMERA
            ) !== android.content.pm.PackageManager.PERMISSION_GRANTED ||
            ActivityCompat.checkSelfPermission(
                context!!,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) !== android.content.pm.PackageManager.PERMISSION_GRANTED ||
            ActivityCompat.checkSelfPermission(
                context!!,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) !== android.content.pm.PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(
                arrayOf(
                    Manifest.permission.CAMERA,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ), CAMERA_PERMISSION_REQUEST_CODE
            )
            return false
        } else
            return true
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            CAMERA_PERMISSION_REQUEST_CODE -> if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED
                && grantResults[2] == PackageManager.PERMISSION_GRANTED
            ) {
                Toast.makeText(activity, "Permission has been granted.", Toast.LENGTH_SHORT).show()
            }
        }
    }


    private fun openCameraAndGalley() {
        val dialog = Dialog(activity!!)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.select_option_new)

        val select_from_gallery = dialog.findViewById(R.id.select_from_gallery) as Button
        val select_from_camera = dialog.findViewById(R.id.select_from_camera) as Button
        val cancel = dialog.findViewById(R.id.cancel) as Button

        select_from_gallery.setOnClickListener(View.OnClickListener {
            val pickPhoto = Intent(
                Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            )
            startActivityForResult(
                pickPhoto,
                OPEN_GALLARY
            )//one can be replaced with any action code
            dialog.dismiss()
        })

        select_from_camera.setOnClickListener(View.OnClickListener {
            val takePicture = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(takePicture, OPEN_CAMERA)
            dialog.dismiss()
        })

        cancel.setOnClickListener(View.OnClickListener { dialog.dismiss() })
        dialog.show()
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                OPEN_GALLARY -> {
                    selectedImage = PathUtil.getPath(activity, data?.data)
                    if (selectedImage == null) {
                        Toast.makeText(activity, getString(R.string.cannot_use),Toast.LENGTH_SHORT).show()
                    } else {
                        val requestOptions = RequestOptions()
                        requestOptions.placeholder(R.drawable.ic_person_black_24dp)
                        requestOptions.error(R.drawable.ic_person_black_24dp)
                        Glide.with(activity!!)
                            .setDefaultRequestOptions(requestOptions)
                            .load(data?.data)
                            .into(img)
                    }
                }
                OPEN_CAMERA -> {
                    captureImageResult(/*selectedImage, */data)
                }
            }
        }
    }


    private fun captureImageResult(data: Intent?) {
        val thumbnail = data?.extras!!.get("data") as Bitmap
        val bytes = ByteArrayOutputStream()
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes)
        val destination = File(
            Environment.getExternalStorageDirectory(),
            System.currentTimeMillis().toString() + ".jpg"
        )
        val fo: FileOutputStream
        try {
            destination.createNewFile()
            fo = FileOutputStream(destination)
            fo.write(bytes.toByteArray())
            fo.close()
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        var uri = Uri.fromFile(destination);

        val requestOptions = RequestOptions()
        requestOptions.placeholder(R.mipmap.my_profile)
        requestOptions.error(R.mipmap.my_profile)
        Glide.with(view!!.context)
            .load(uri)
            .placeholder(R.drawable.ic_image_placeholder)
            .into(img)
        selectedImage = uri.path
    }


   /* class PhoneCallbacks(private val listener : PhoneCallbacksListener) :
        PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        interface PhoneCallbacksListener {
            fun onVerificationCompleted(credential: PhoneAuthCredential?)
            fun onVerificationFailed(exception: FirebaseException?)
            fun onCodeSent(
                verificationId: String?,
                token: PhoneAuthProvider.ForceResendingToken?
            )
        }

      *//*  override fun onCodeSent(verificationId: String?, token: PhoneAuthProvider.ForceResendingToken?) {
            listener.onCodeSent(verificationId,token)
        }

        override fun onVerificationCompleted(phoneAuthCredential: PhoneAuthCredential) {
            listener.onVerificationCompleted(phoneAuthCredential)
        }

        override fun onVerificationFailed(exception: FirebaseException) {
            listener.onVerificationFailed(exception)
        }*//*

        override fun onVerificationCompleted(credential: PhoneAuthCredential?) {
            if (credential != null && storedVerificationId != null)
                signInWithPhoneAuthCredential(credential)
            else {
                Toast.makeText(
                    this,
                    "Something went wrong, please try later.",
                    Toast.LENGTH_LONG).show()
                finish()
            }
        }


        override fun onVerificationFailed(exception: FirebaseException?) {
            Toast.makeText(
                activity, "Something went wrong, try again later please.",
                Toast.LENGTH_LONG).show()
            finish()
        }

        override fun onCodeSent(
            verificationId: String?,
            token: PhoneAuthProvider.ForceResendingToken?
        ) {
            storedVerificationId = verificationId!!
            resendToken = token!!
        }
        e
    }*/
}