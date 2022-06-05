package com.shakhee.pdfgeneratesample

import android.Manifest
import android.annotation.SuppressLint
import android.app.*
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.media.AudioAttributes
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.os.StrictMode
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.itextpdf.text.*
import com.itextpdf.text.pdf.PdfWriter
import com.shakhee.pdfgenerateusingjson.EmployeeListAdapter
import com.shakhee.pdfgenerateusingjson.EmployeeModel
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.nio.charset.StandardCharsets
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {
    private val STORAGE_CODE: Int = 100;
    var PERMISSION_ALL = 1
    var PERMISSIONS = arrayOf<String>(
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.READ_EXTERNAL_STORAGE
    )
    var file_name_path = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        recyclerview = findViewById(R.id.recyclerView)

        // this creates a vertical layout Manager
        recyclerview.layoutManager = LinearLayoutManager(this)
        gettingStudentDetails()
        val builder = StrictMode.VmPolicy.Builder()
        StrictMode.setVmPolicy(builder.build())

        if (!hasPermissions(this@MainActivity, *PERMISSIONS)) {
            ActivityCompat.requestPermissions(
                this@MainActivity,
                PERMISSIONS,
                PERMISSION_ALL
            )
        }

        val file = File(
            Objects.requireNonNull(getExternalFilesDir(null))!!.absolutePath, "pdfsdcard_location"
        )
        if (!file.exists()) {
            file.mkdir()
        }

    }




    companion object {
        @SuppressLint("StaticFieldLeak")
        var createdDate: String? = null

        @SuppressLint("StaticFieldLeak")
        var employeeListJson: String? = ""

        @SuppressLint("StaticFieldLeak")
        var employeeList = ArrayList<EmployeeModel>()


        @SuppressLint("StaticFieldLeak")
        lateinit var recyclerview: RecyclerView


        private var mProgressDialog: ProgressDialog? = null

        fun removeSimpleProgressDialog() {
            try {
                if (mProgressDialog != null) {
                    if (mProgressDialog!!.isShowing) {
                        mProgressDialog!!.dismiss()
                        mProgressDialog = null
                    }
                }
            } catch (ie: IllegalArgumentException) {
                ie.printStackTrace()

            } catch (re: RuntimeException) {
                re.printStackTrace()
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }

        fun showSimpleProgressDialog(
            context: Context, title: String,
            msg: String, isCancelable: Boolean
        ) {
            try {
                if (mProgressDialog == null) {
                    mProgressDialog = ProgressDialog.show(context, title, msg)
                    mProgressDialog!!.setCancelable(isCancelable)
                }

                if (!mProgressDialog!!.isShowing) {
                    mProgressDialog!!.show()
                }

            } catch (ie: IllegalArgumentException) {
                ie.printStackTrace()
            } catch (re: RuntimeException) {
                re.printStackTrace()
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
    }
    fun generateStaticPDF(view: View) {


    }



    fun drawableToBitmap(drawable: Drawable): Bitmap? {
        if (drawable is BitmapDrawable) {
            return drawable.bitmap
        }
        val bitmap = Bitmap.createBitmap(drawable.intrinsicWidth, drawable.intrinsicHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)
        return bitmap
    }
    @RequiresApi(Build.VERSION_CODES.O)
    fun generateDynamicPDF(view: View) {
        createNdispay()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNdispay() {
        //create object of Document class
        val mDoc = Document()
        //pdf file name
        val mFileName = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(System.currentTimeMillis())
        //pdf file path
        file_name_path =  "/" + "pdf_sample"+mFileName +".pdf"

        val pdfPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            .toString()
        val file = File(pdfPath, file_name_path)

        Log.d("createPdf", "PDF Path: " + pdfPath)

        try {
            //create instance of PdfWriter class
            PdfWriter.getInstance(mDoc, FileOutputStream(file))

            mDoc.open()
            val printLength = 40

            val f = Font(Font.FontFamily.TIMES_ROMAN, 25.0f, Font.BOLD, BaseColor.WHITE)

            val st = "Total number of  Space"
            if (st.length > 0) {
                val len: Int = printLength - st.length
                val space = StringBuilder()
                for (v in 0 until len) {
                    space.append(" ")
                }

                val p = Paragraph("User Id" + space + "Name" + space + "Department" + space + "Designation" + space + "Salary")
                mDoc.add(p)
                mDoc.add(Chunk. NEWLINE)
            }

            if (employeeList != null) {
                if (employeeList.size > 0) {
                    val i = 0
                    val printLength = 40
                    for (i in i..employeeList.size step 1 ){
                        try {
                            if (employeeList!=null){
                                val st = "Total number Space"
                                if (st.length > 0) {
                                    val len: Int = printLength - st.length
                                    val space = StringBuilder()
                                    for (v in 0 until len) {
                                        space.append(" ")
                                    }
                                    val  p = Paragraph(employeeList.get(i).userId + space + employeeList.get(i).name+ space + employeeList.get(i).department+ space + employeeList.get(i).designation+ space + employeeList.get(i).salary)
                                    mDoc.add(p)
                                    mDoc.add(Chunk.NEWLINE )
                                }


                            }

                        }catch (e:Exception){

                        }

                    }


                }

            }


            //close document
            mDoc.close()

            //show file saved message with file name and path
            Toast.makeText(this, "$mFileName.pdf\nis saved to\n$file_name_path", Toast.LENGTH_SHORT).show()
            addNotification()
            viewPdfFile()
        }
        catch (e: Exception){
            println("error............"+e.message)

            //if anything goes wrong causing exception, get and show exception message
            Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
        }

    }

    private fun gettingStudentDetails() {
        var json: String
        try {
            val inputStream: InputStream = this@MainActivity.getAssets().open("employeeDetails.json")
            val size = inputStream.available()
            val buffer = ByteArray(size)
            inputStream.read(buffer)
            inputStream.close()
            json= String(buffer, StandardCharsets.UTF_8)

            try {
                val jsonObject = JSONObject(json)
                println("sOrder..............jsonObject................."+jsonObject)
                val json1: JSONObject = jsonObject.getJSONObject("data")
                val jsonArray: JSONArray = json1.getJSONArray("employees")
                println("sOrder..............jsonArray................."+jsonArray)
                employeeListJson = jsonArray.toString()
                callingStudentList()
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(this@MainActivity, "Information Getting Null", Toast.LENGTH_SHORT)
                    .show()
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun callingStudentList() {

        try {
            if (employeeListJson == null || employeeListJson!!.trim { it <= ' ' }.length == 0 || employeeListJson.equals(
                    "null",
                    ignoreCase = true
                )
            ) {
                return
            }
            employeeList = ArrayList<EmployeeModel>()
            val jsonArray = JSONArray(employeeListJson)
            if (jsonArray.length() > 0) {
                for (i in 0 until jsonArray.length()) {
                    val jsonObject: JSONObject = jsonArray.getJSONObject(i)
                    println("sOrder..............jsonObject..........00......."+jsonObject)

                    employeeList.add(
                        EmployeeModel(
                            jsonObject.getString("userId"),
                            jsonObject.getString("name"),
                            jsonObject.getString("department"),
                            jsonObject.getString("role"),
                            jsonObject.getString("designation"),
                            jsonObject.getString("salary")
                        )
                    )
                }
                removeSimpleProgressDialog()
                val employeeListAdapter = EmployeeListAdapter(employeeList)
                recyclerview.setHasFixedSize(true);
                recyclerview.setAdapter(employeeListAdapter)
                employeeListAdapter.notifyDataSetChanged()


            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode){
            STORAGE_CODE -> {
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    //permission from popup was granted, call savePdf() method
                    createNdispay()
                }
                else{
                    println(".error........grantResults......"+grantResults)
                    //permission from popup was denied, show error message
                    Toast.makeText(this, "Permission denied...!", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
    fun hasPermissions(context: Context?, vararg permissions: String?): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (permission in permissions) {
                if (ActivityCompat.checkSelfPermission(
                        context,
                        permission!!
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    return false
                }
            }
        }
        return true
    }
    fun viewPdfFile() {
        try {
            val pdfPath =
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString()
            val file = File("$pdfPath/$file_name_path")
            val intent = Intent(Intent.ACTION_VIEW)
            intent.setDataAndType(Uri.fromFile(file), "application/pdf")
            intent.flags = Intent.FLAG_ACTIVITY_NO_HISTORY
            val intent1 = Intent.createChooser(intent, "Open File")
            startActivity(intent1)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun addNotification() {
        val pdfPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            .toString()
        val file = File("$pdfPath/$file_name_path")

        val intent = Intent(Intent.ACTION_VIEW)
        intent.setDataAndType(Uri.fromFile(file), "application/pdf")
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        val pendingIntent = PendingIntent.getActivity(applicationContext, 0, intent, 0)
        val viewPendingIntent = PendingIntent.getActivity(
            applicationContext,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        val channel = NotificationChannel(
            "baishakhee",
            "shakhee",
            NotificationManager.IMPORTANCE_HIGH
        )
        val manager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        manager.createNotificationChannel(channel)

        //Creating the notification object
        val notification = NotificationCompat.Builder(this, "exceloid")
        //notification.setAutoCancel(true);
        notification.setContentTitle(file_name_path)
        notification.setContentText("Download in progress")
        notification.setSmallIcon(R.drawable.ic_baseline_download_for_offline_24)
        notification.priority = NotificationCompat.PRIORITY_HIGH
        notification.setAutoCancel(true)
        notification.color = ContextCompat.getColor(applicationContext, R.color.purple_200)
        notification.setCategory(Notification.CATEGORY_REMINDER)
        notification.setGroup(file_name_path)
        notification.setContentIntent(pendingIntent)
        val PROGRESS_MAX = 100
        val PROGRESS_CURRENT = 0
        notification.setProgress(PROGRESS_MAX, PROGRESS_CURRENT, false)
        notification.addAction(R.drawable.ic_baseline_download_for_offline_24, "VIEW", viewPendingIntent) // #1
        notification.setContentText("Download complete")
            .setProgress(0, 0, false)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val audioAttributes = AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .setUsage(AudioAttributes.USAGE_ALARM)
                .build()
            val importance = NotificationManager.IMPORTANCE_HIGH
            val notificationChannel = NotificationChannel("exceloid", "fmcg", importance)
            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.RED
            notificationChannel.enableVibration(true)
            notificationChannel.vibrationPattern =
                longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400)
            notification.setChannelId("exceloid")
            assert(manager != null)
            manager.createNotificationChannel(notificationChannel)
        }
        assert(manager != null)
        manager.notify(
            System.currentTimeMillis().toInt(),
            notification.build()
        )
    }
}