package com.example.fafabite.ui

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.fafabite.R
import com.example.fafabite.api.ApiConfig
import com.example.fafabite.api.ResponseProduk
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.util.*

class TambahMakananActivity : AppCompatActivity() {

    private var imageUri: Uri? = null

    // PINTU AJAIB KE GALERI
    private val pickImageLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        if (uri != null) {
            imageUri = uri
            val ivPreview = findViewById<ImageView>(R.id.ivFotoPreview)
            val btnUploadLayout = findViewById<LinearLayout>(R.id.btnUploadFoto)
            ivPreview.setImageURI(uri)
            ivPreview.visibility = View.VISIBLE
            btnUploadLayout.visibility = View.GONE
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tambah_makanan)

        val btnBack = findViewById<ImageView>(R.id.btnBackTambah)
        val btnUploadLayout = findViewById<LinearLayout>(R.id.btnUploadFoto)
        val ivPreview = findViewById<ImageView>(R.id.ivFotoPreview)
        val etNama = findViewById<EditText>(R.id.etNamaMakanan)
        val etHargaAsli = findViewById<EditText>(R.id.etHargaAsli)
        val etHargaDiskon = findViewById<EditText>(R.id.etHargaDiskon)
        val etStok = findViewById<EditText>(R.id.etStokMakanan)
        val etPickup = findViewById<EditText>(R.id.etWaktuPickup)
        val btnSimpan = findViewById<Button>(R.id.btnSimpanMakanan)

        btnBack.setOnClickListener { finish() }
        btnUploadLayout.setOnClickListener { pickImageLauncher.launch("image/*") }
        ivPreview.setOnClickListener { pickImageLauncher.launch("image/*") }

        // MUNCULKAN KALENDER DAN JAM OTOMATIS
        etPickup.isFocusable = false
        etPickup.isClickable = true
        etPickup.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
                val hour = calendar.get(Calendar.HOUR_OF_DAY)
                val minute = calendar.get(Calendar.MINUTE)

                TimePickerDialog(this, { _, selectedHour, selectedMinute ->
                    val formattedDate = String.format(Locale.getDefault(), "%04d-%02d-%02d %02d:%02d:00",
                        selectedYear, selectedMonth + 1, selectedDay, selectedHour, selectedMinute)

                    etPickup.setText(formattedDate)
                }, hour, minute, true).show()

            }, year, month, day).show()
        }

        // AKSI SAAT TOMBOL SIMPAN DIKLIK
        btnSimpan.setOnClickListener {
            val nama = etNama.text.toString().trim()
            val hargaAsli = etHargaAsli.text.toString().trim()
            val hargaDiskon = etHargaDiskon.text.toString().trim()
            val stok = etStok.text.toString().trim()
            val pickup = etPickup.text.toString().trim()

            // 1. Validasi Input
            if (imageUri == null) {
                NotifHelper.showDialog(this, "Foto Wajib!", "Tolong masukkan foto makanannya ya.")
                return@setOnClickListener
            }
            if (nama.isEmpty() || hargaAsli.isEmpty() || hargaDiskon.isEmpty() || stok.isEmpty() || pickup.isEmpty()) {
                NotifHelper.showDialog(this, "Oops!", "Semua kolom teks harus diisi.")
                return@setOnClickListener
            }

            btnSimpan.text = "Mengunggah..."
            btnSimpan.isEnabled = false

            // 2. Bungkus Data Teks menjadi RequestBody
            val textMediaType = MediaType.parse("text/plain")
            val idTokoBody = RequestBody.create(textMediaType, "1")
            val namaBody = RequestBody.create(textMediaType, nama)
            val hargaAsliBody = RequestBody.create(textMediaType, hargaAsli)
            val hargaDiskonBody = RequestBody.create(textMediaType, hargaDiskon)
            val stokBody = RequestBody.create(textMediaType, stok)
            val pickupBody = RequestBody.create(textMediaType, pickup)

            // Mengatur default status langsung ke "tersedia" karena ini produk baru
            val statusBody = RequestBody.create(textMediaType, "tersedia")

            // 3. Bungkus Foto (Uri) menjadi File Multipart
            val fileFoto = uriToFile(imageUri!!, this)
            val requestImageFile = RequestBody.create(MediaType.parse("image/jpeg"), fileFoto)
            val imageMultipart = MultipartBody.Part.createFormData("foto_makanan", fileFoto.name, requestImageFile)

            // 4. PANGGIL KURIR RETROFIT
            ApiConfig.getApiService().uploadProduk(
                idTokoBody, namaBody, hargaAsliBody, hargaDiskonBody, stokBody, pickupBody, statusBody, imageMultipart
            ).enqueue(object : Callback<ResponseProduk> {

                override fun onResponse(call: Call<ResponseProduk>, response: Response<ResponseProduk>) {
                    btnSimpan.text = "Simpan Produk"
                    btnSimpan.isEnabled = true

                    if (response.isSuccessful && response.body() != null) {
                        val dataServer = response.body()!!

                        // Cek apakah PHP benar-benar bilang sukses = true
                        if (dataServer.sukses) {
                            NotifHelper.showDialog(this@TambahMakananActivity, "Berhasil!", dataServer.pesan) {
                                finish()
                            }
                        } else {
                            // Jika PHP membalas 200 OK tapi datanya ditolak (misal validasi gagal)
                            NotifHelper.showDialog(this@TambahMakananActivity, "Ditolak PHP", dataServer.pesan)
                        }
                    } else {
                        // INI YANG PALING PENTING! Tangkap error asli dari server (Misal 500 atau 413)
                        val pesanErrorAsli = response.errorBody()?.string() ?: "Error tidak terbaca"
                        NotifHelper.showDialog(this@TambahMakananActivity, "Error Server (${response.code()})", pesanErrorAsli)
                    }
                }

                override fun onFailure(call: Call<ResponseProduk>, t: Throwable) {
                    btnSimpan.text = "Simpan Produk"
                    btnSimpan.isEnabled = true
                    NotifHelper.showDialog(this@TambahMakananActivity, "Error Koneksi", "Server tidak merespon: ${t.message}")
                }
            })
        }
    }

    // --- FUNGSI BANTUAN: Mengubah Uri Foto ---
    private fun uriToFile(selectedImg: Uri, context: Context): File {
        val contentResolver = context.contentResolver
        val myFile = File.createTempFile("temp_img", ".jpg", context.cacheDir)

        val inputStream = contentResolver.openInputStream(selectedImg) as InputStream
        val outputStream = FileOutputStream(myFile)
        val buf = ByteArray(1024)
        var len: Int
        while (inputStream.read(buf).also { len = it } > 0) outputStream.write(buf, 0, len)
        outputStream.close()
        inputStream.close()

        return myFile
    }
}