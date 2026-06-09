package com.example.fafabite

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.fafabite.api.ApiConfig
import com.example.fafabite.api.ResponseProduk
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream


class EditMakananActivity : AppCompatActivity() {

    private lateinit var ivFotoPreview: ImageView
    private lateinit var btnUploadFoto: LinearLayout
    private var fileFotoBaru: File? = null
    private var idMakanan = 0

    // Launcher untuk buka galeri
    private val launcherGallery = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            val uri: Uri? = result.data?.data
            uri?.let {
                fileFotoBaru = uriToFile(it)
                ivFotoPreview.visibility = View.VISIBLE
                ivFotoPreview.setImageURI(it)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_makanan)

        val btnBack = findViewById<ImageView>(R.id.btnBackTambah)
        val etNama = findViewById<EditText>(R.id.etNamaMakanan)
        val etHargaAsli = findViewById<EditText>(R.id.etHargaAsli)
        val etHargaDiskon = findViewById<EditText>(R.id.etHargaDiskon)
        val etStok = findViewById<EditText>(R.id.etStokMakanan)
        val etWaktuPickup = findViewById<EditText>(R.id.etWaktuPickup)
        val btnSimpan = findViewById<Button>(R.id.btnSimpanMakanan)

        ivFotoPreview = findViewById(R.id.ivFotoPreview)
        btnUploadFoto = findViewById(R.id.btnUploadFoto)

        // 1. TANGKAP DATA LAMA DARI INTENT
        idMakanan = intent.getIntExtra("ID_MAKANAN", 0)
        val namaLama = intent.getStringExtra("NAMA_MAKANAN") ?: ""
        val hargaAsliLama = intent.getIntExtra("HARGA_ASLI", 0)
        val hargaDiskonLama = intent.getIntExtra("HARGA_DISKON", 0)
        val stokLama = intent.getIntExtra("STOK_MAKANAN", 0)
        val waktuPickupLama = intent.getStringExtra("WAKTU_PICKUP") ?: ""
        val fotoLama = intent.getStringExtra("FOTO_MAKANAN") ?: ""

        // 2. TAMPILKAN DATA LAMA KE LAYAR
        etNama.setText(namaLama)
        etHargaAsli.setText(hargaAsliLama.toString())
        etHargaDiskon.setText(hargaDiskonLama.toString())
        etStok.setText(stokLama.toString())
        etWaktuPickup.setText(waktuPickupLama)

        // Tampilkan foto lama menggunakan Glide
        if (fotoLama.isNotEmpty()) {
            ivFotoPreview.visibility = View.VISIBLE
            val urlFotoLama = ApiConfig.IMAGE_URL + fotoLama
            Glide.with(this).load(urlFotoLama).into(ivFotoPreview)
        }

        btnBack.setOnClickListener { finish() }

        btnUploadFoto.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            launcherGallery.launch(intent)
        }

        btnSimpan.setOnClickListener {
            val nama = etNama.text.toString()
            val hargaAsli = etHargaAsli.text.toString()
            val hargaDiskon = etHargaDiskon.text.toString()
            val stok = etStok.text.toString()
            val waktuPickup = etWaktuPickup.text.toString()

            if (nama.isEmpty() || hargaAsli.isEmpty() || hargaDiskon.isEmpty() || stok.isEmpty() || waktuPickup.isEmpty()) {
                Toast.makeText(this, "Mohon lengkapi semua data teks!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            btnSimpan.text = "Loading..."
            btnSimpan.isEnabled = false

            // Konversi teks ke RequestBody (Gaya Standar)
            val reqNama = RequestBody.create(okhttp3.MediaType.parse("text/plain"), nama)
            val reqHargaAsli = RequestBody.create(okhttp3.MediaType.parse("text/plain"), hargaAsli)
            val reqHargaDiskon = RequestBody.create(okhttp3.MediaType.parse("text/plain"), hargaDiskon)
            val reqStok = RequestBody.create(okhttp3.MediaType.parse("text/plain"), stok)
            val reqWaktuPickup = RequestBody.create(okhttp3.MediaType.parse("text/plain"), waktuPickup)
            val reqStatus = RequestBody.create(okhttp3.MediaType.parse("text/plain"), "Tersedia")

            // Konversi foto baru ke Multipart (Jika ada file baru yang dipilih)
            var fotoMultipart: MultipartBody.Part? = null
            if (fileFotoBaru != null) {
                val reqFile = RequestBody.create(okhttp3.MediaType.parse("image/jpeg"), fileFotoBaru!!)
                fotoMultipart = MultipartBody.Part.createFormData("foto_makanan", fileFotoBaru!!.name, reqFile)
            }
            // -------------------------

            // Panggil API Update
            ApiConfig.getApiService().updateProduk(
                idMakanan, reqNama, reqHargaAsli, reqHargaDiskon, reqStok, reqWaktuPickup, reqStatus, fotoMultipart
            ).enqueue(object : Callback<ResponseProduk> {
                override fun onResponse(call: Call<ResponseProduk>, response: Response<ResponseProduk>) {
                    btnSimpan.text = "Update Produk"
                    btnSimpan.isEnabled = true

                    if (response.isSuccessful && response.body() != null) {
                        Toast.makeText(this@EditMakananActivity, response.body()!!.pesan, Toast.LENGTH_SHORT).show()
                        finish() // Tutup halaman edit jika sukses
                    } else {
                        Toast.makeText(this@EditMakananActivity, "Gagal mengupdate data", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<ResponseProduk>, t: Throwable) {
                    btnSimpan.text = "Update Produk"
                    btnSimpan.isEnabled = true
                    Toast.makeText(this@EditMakananActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

    // Fungsi utilitas untuk mengubah URI gambar menjadi File fisik
    private fun uriToFile(selectedImg: Uri): File {
        val contentResolver = contentResolver
        val myFile = File(cacheDir, "temp_image_${System.currentTimeMillis()}.jpg")

        val inputStream = contentResolver.openInputStream(selectedImg)
        val outputStream = FileOutputStream(myFile)
        val buf = ByteArray(1024)
        var len: Int
        while (inputStream!!.read(buf).also { len = it } > 0) {
            outputStream.write(buf, 0, len)
        }
        outputStream.close()
        inputStream.close()

        return myFile
    }
}