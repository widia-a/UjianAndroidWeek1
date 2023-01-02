package com.bcaf.ujianandroidweek1

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.core.graphics.drawable.toBitmap
import kotlinx.android.synthetic.main.activity_foto.*
import kotlinx.android.synthetic.main.fragment_menu.*
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*

class FotoActivity : AppCompatActivity() {

    companion object{
        private val REQUEST_CODE_PERMISSIONS = 999
        private val CAMERA_REQUEST_CAPTURE = 100
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_foto)

        takeImg.setOnClickListener(View.OnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED && checkSelfPermission(
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED ) {
                    val permissions = arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    requestPermissions(permissions, REQUEST_CODE_PERMISSIONS)
                } else {
                    captureCamera()
                }
            }
        })

        btnLoginSelfie.setOnClickListener(View.OnClickListener {
            if (resultInfo.text.toString().length == 0 || resultInfo.text.toString().contentEquals("Login Foto Selfie Gagal")){
                takeImg.setImageResource(R.drawable.ic_baseline_cancel_presentation_24)
                resultInfo.setText("Login Foto Selfie Gagal")
                Toast.makeText(applicationContext, "Ambil selfie foto terlebih dahulu", Toast.LENGTH_SHORT).show()
            }
            else
            {
                Toast.makeText(applicationContext, "Login Foto Selfie Berhasil", Toast.LENGTH_SHORT).show()
                finish()

            }
        })
    }

    private fun requestPermission(permissions: Array<String>, requestCodePermissions: Int) {

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode){
            REQUEST_CODE_PERMISSIONS -> {
                if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    captureCamera()
                }
                else
                {
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    fun captureCamera(){
        val takeCamera = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(takeCamera, CAMERA_REQUEST_CAPTURE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == CAMERA_REQUEST_CAPTURE && resultCode == RESULT_OK){
            val bitmapImage = data?.extras?.get("data") as Bitmap
            val resized = Bitmap.createScaledBitmap(bitmapImage, 600, 600, true)
            takeImg.setImageBitmap(resized)
            saveImage(bitmapImage)
            resultInfo.setText("Login Foto Selfie Berhasil")
        }
        else {
            resultInfo.setText("Login Foto Selfie Gagal")
            takeImg.setImageResource(R.drawable.ic_baseline_cancel_presentation_24)

        }
    }

    fun saveImage(bitmap: Bitmap){
        try {
            val tanggal = SimpleDateFormat("HHmmss_yyyyMMdd").format(Date())
            val extStorage = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).toString()
            val namaFile = extStorage + "/BCAF_" + tanggal + ".png"

            var file : File? = null
            file = File(namaFile)
            file.createNewFile()

            val bos = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, bos)
            val bitmapData = bos.toByteArray()

            val fos = FileOutputStream(file)
            fos.write(bitmapData)
            fos.flush()
            fos.close()
        }
        catch (e: java.lang.Exception){
            e.printStackTrace()
        }

    }
}