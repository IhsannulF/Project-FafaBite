package com.example.fafabite.ui

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
        val switchStatus = findViewById<Switch>(R.id.switchStatus)
        val btnSimpan = findViewById<Button>(R.id.btnSimpanMakanan)

        btnBack.setOnClickListener { finish() }
        btnUploadLayout.setOnClickListener { pickImageLauncher.launch("image/*") }
        ivPreview.setOnClickListener { pickImageLauncher.launch("image/*") }

        // AKSI SAAT TOMBOL SIMPAN DIKLIK
        btnSimpan.setOnClickListener {
            val nama = etNama.text.toString().trim()
            val hargaAsli = etHargaAsli.text.toString().trim()
            val hargaDiskon = etHargaDiskon.text.toString().trim()
            val stok = etStok.text.toString().trim()
            val pickup = etPickup.text.toString().trim()
            val isTersedia = switchStatus.isChecked

            // 1. Validasi Input
            if (imageUri == null) {
                NotifHelper.showDialog(this, "Foto Wajib!", "Tolong masukkan foto makanannya ya.")
                return@setOnClickListener
            }
            if (nama.isEmpty() || hargaAsli.isEmpty() || hargaDiskon.isEmpty() || stok.isEmpty() || pickup.isEmpty()) {
                NotifHelper.showDialog(this, "Oops!", "Semua kolom teks harus diisi.")
                return@setOnClickListener
            }

            // Ubah teks tombol jadi loading
            btnSimpan.text = "Mengunggah..."
            btnSimpan.isEnabled = false

            // 2. Bungkus Data Teks menjadi RequestBody (Format yang didukung OkHttp 3)
            val textMediaType = MediaType.parse("text/plain")
            // Nanti "1" ini kita ambil dari data user yang sedang login
            val idTokoBody = RequestBody.create(textMediaType, "1")
            val namaBody = RequestBody.create(textMediaType, nama)
            val hargaAsliBody = RequestBody.create(textMediaType, hargaAsli)
            val hargaDiskonBody = RequestBody.create(textMediaType, hargaDiskon)
            val stokBody = RequestBody.create(textMediaType, stok)
            val pickupBody = RequestBody.create(textMediaType, pickup)

            val statusTeks = if (isTersedia) "tersedia" else "habis"
            val statusBody = RequestBody.create(textMediaType, statusTeks)

            // 3. Bungkus Foto (Uri) menjadi File Multipart
            val fileFoto = uriToFile(imageUri!!, this)
            val requestImageFile = RequestBody.create(MediaType.parse("image/jpeg"), fileFoto)
            val imageMultipart = MultipartBody.Part.createFormData("foto_makanan", fileFoto.name, requestImageFile)

            // 4. PANGGIL KURIR RETROFIT UNTUK BERANGKAT!
            ApiConfig.getApiService().uploadProduk(
                idTokoBody, namaBody, hargaAsliBody, hargaDiskonBody, stokBody, pickupBody, statusBody, imageMultipart
            ).enqueue(object : Callback<ResponseProduk> {
                override fun onResponse(call: Call<ResponseProduk>, response: Response<ResponseProduk>) {
                    // Kembalikan tombol seperti semula
                    btnSimpan.text = "Simpan"
                    btnSimpan.isEnabled = true

                    if (response.isSuccessful && response.body() != null) {
                        // Jika Laravel membalas "Sukses"
                        NotifHelper.showDialog(this@TambahMakananActivity, "Berhasil!", response.body()!!.pesan) {
                            finish() // Tutup halaman kalau sukses
                        }
                    } else {
                        NotifHelper.showDialog(this@TambahMakananActivity, "Gagal", "Maaf, terjadi kesalahan saat menyimpan data.")
                    }
                }

                override fun onFailure(call: Call<ResponseProduk>, t: Throwable) {
                    // Jika gagal nyambung ke server (IP salah / server mati)
                    btnSimpan.text = "Simpan"
                    btnSimpan.isEnabled = true
                    NotifHelper.showDialog(this@TambahMakananActivity, "Error Koneksi", "Server tidak merespon: ${t.message}")
                }
            })
        }
    }

    // --- FUNGSI BANTUAN: Mengubah Uri Foto dari Galeri menjadi File Fisik ---
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