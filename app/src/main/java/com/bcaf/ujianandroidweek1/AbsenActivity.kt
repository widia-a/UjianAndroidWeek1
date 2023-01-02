package com.bcaf.ujianandroidweek1

import android.Manifest
import android.app.DatePickerDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import android.widget.DatePicker
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_absen.*
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*

class AbsenActivity : AppCompatActivity() {

    companion object{
        private val REQUEST_CODE_PERMISSIONS = 999
        private val CAMERA_REQUEST_CAPTURE = 100
    }
    var btnFromStat: Boolean = false
    var btnToStat: Boolean = false
    var fda = Calendar.getInstance().getTime()

    var lampir1: Boolean = false
    var lampir2: Boolean = false
    var lampir3: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_absen)

        btnFromDate.setOnClickListener(
            View.OnClickListener {
                btnFromStat = true
                pickDate() }
        )

        btnToDate.setOnClickListener(View.OnClickListener {
            btnToStat = true
            pickDate()
        })

        btnKirim.setOnClickListener(View.OnClickListener {
            if(fromDate.text.toString().length == 0 || toDate.text.toString().length == 0 || txtPerihal.text.toString().length == 0 || txtKet.text.toString().length == 0 ){
                Toast.makeText(applicationContext, "Data tidak boleh kosong", Toast.LENGTH_SHORT).show()
            }
            else{
                Toast.makeText(applicationContext, "Data Berhasil Dikirim", Toast.LENGTH_SHORT).show()
            }
        })

        lampiran1.setOnClickListener(View.OnClickListener {
            lampir1 = true
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED && checkSelfPermission(
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED ) {
                    val permissions = arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    requestPermissions(permissions, AbsenActivity.REQUEST_CODE_PERMISSIONS)
                } else {
                    captureCamera()
                }
            }
        })

        lampiran2.setOnClickListener(View.OnClickListener {
            lampir2 = true
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED && checkSelfPermission(
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED ) {
                    val permissions = arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    requestPermissions(permissions, AbsenActivity.REQUEST_CODE_PERMISSIONS)
                } else {
                    captureCamera()
                }
            }
        })

        lampiran3.setOnClickListener(View.OnClickListener {
            lampir3 = true
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED && checkSelfPermission(
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED ) {
                    val permissions = arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    requestPermissions(permissions, AbsenActivity.REQUEST_CODE_PERMISSIONS)
                } else {
                    captureCamera()
                }
            }
        })
    }

    fun pickDate(){
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        val dateSetListener = object : DatePickerDialog.OnDateSetListener{
            override fun onDateSet(
                view: DatePicker, year: Int, monthOfYear: Int, dayOfMonth: Int
            )
            {
                c.set(Calendar.YEAR, year)
                c.set(Calendar.MONTH, monthOfYear)
                c.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                val myFormat = "dd MMMM yyyy"
                val sdf = SimpleDateFormat(myFormat, Locale.ENGLISH)

                if(btnFromStat == true){
                    fda = c.getTime()
                    fromDate.setText(sdf.format(c.getTime()))
                    btnFromStat = false
                }
                else if(btnToStat == true){
                    val tda = c.getTime()
                    if (tda.compareTo(fda) < 0){
                        val fdas = fda
                        Toast.makeText(applicationContext, "Pilih tanggal yang lebih besar dari $fda", Toast.LENGTH_SHORT).show()
                    }
                    else if (tda.compareTo(fda) > 0)
                    {
                        toDate.setText(sdf.format(c.getTime()))
                    }
                    btnToStat = false
                }
            }
        }

        DatePickerDialog(this,
            dateSetListener,
            c.get(Calendar.YEAR),
            c.get(Calendar.MONTH),
            c.get(Calendar.DAY_OF_MONTH)
        ).show()
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
            AbsenActivity.REQUEST_CODE_PERMISSIONS -> {
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
        startActivityForResult(takeCamera, AbsenActivity.CAMERA_REQUEST_CAPTURE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == AbsenActivity.CAMERA_REQUEST_CAPTURE && resultCode == RESULT_OK){
            if (lampir1 == true){
                val bitmapImage = data?.extras?.get("data") as Bitmap
                lampiran1.setImageBitmap(bitmapImage)
//                saveImage(bitmapImage)
            }
            else if(lampir2 == true){
                val bitmapImage = data?.extras?.get("data") as Bitmap
                lampiran2.setImageBitmap(bitmapImage)
            }

            else if(lampir3 == true){
                val bitmapImage = data?.extras?.get("data") as Bitmap
                lampiran3.setImageBitmap(bitmapImage)
            }

        }
    }

    fun saveImage(bitmap: Bitmap){
        try {
            val tanggal = SimpleDateFormat("ddMMyyyy_HHmmss").format(Date())
            val extStorage = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).toString()
            val namaFile = extStorage + "/BCAF_" + tanggal + ".png"

            var file : File? = null
            file = File(namaFile)
            file.createNewFile()

            val bos = ByteArrayOutputStream()
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