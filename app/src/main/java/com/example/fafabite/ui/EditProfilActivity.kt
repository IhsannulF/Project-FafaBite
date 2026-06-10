package com.example.fafabite.ui

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.fafabite.R
import com.example.fafabite.api.ApiConfig
import com.example.fafabite.api.ResponseProfilToko
import okhttp3.MultipartBody
import okhttp3.MediaType
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

class EditProfilActivity : AppCompatActivity() {

    private var uriFotoTerpilih: Uri? = null
    private lateinit var ivEditFoto: ImageView
    private lateinit var btnSimpan: Button

    // Peluncur untuk membuka Galeri
    private val peluncurGaleri = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK && result.data != null) {
            uriFotoTerpilih = result.data?.data

            uriFotoTerpilih?.let {
                Glide.with(this)
                    .load(it)
                    .circleCrop()
                    .into(ivEditFoto)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profil)

        val btnBack = findViewById<ImageView>(R.id.btnBackEdit)
        val btnUbahFoto = findViewById<TextView>(R.id.btnUbahFoto)
        btnSimpan = findViewById(R.id.btnSimpanProfil) // Diinisialisasi di tingkat class

        ivEditFoto = findViewById(R.id.ivEditFoto)
        val etNama = findViewById<EditText>(R.id.etEditNama)
        val etEmail = findViewById<EditText>(R.id.etEditEmail)
        val etAlamat = findViewById<EditText>(R.id.etEditAlamat)
        val layoutAlamat = findViewById<LinearLayout>(R.id.layoutEditAlamat)

        btnBack.setOnClickListener { finish() }

        // ========================================================
        // 1. CEK ROLE USER & ISI DATA AWAL
        // ========================================================
        val sharedPref = getSharedPreferences("FafaBitePrefs", Context.MODE_PRIVATE)
        val roleUser = sharedPref.getString("ROLE_USER", "") ?: ""

        val namaLokal = sharedPref.getString("NAMA_USER", "")
        val emailLokal = sharedPref.getString("EMAIL_USER", "")

        etNama.setText(namaLokal)
        etEmail.setText(emailLokal)

        if (roleUser.equals("pembeli", ignoreCase = true)) {
            layoutAlamat.visibility = View.GONE
        } else {
            layoutAlamat.visibility = View.VISIBLE
        }

        // ========================================================
        // 2. LOGIKA UBAH FOTO
        // ========================================================
        btnUbahFoto.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            peluncurGaleri.launch(intent)
        }

        // ========================================================
        // 3. LOGIKA KLIK TOMBOL SIMPAN (Validasi -> Tampilkan Dialog)
        // ========================================================
        btnSimpan.setOnClickListener {
            val namaBaru = etNama.text.toString().trim()
            val emailBaru = etEmail.text.toString().trim()
            val alamatBaru = etAlamat.text.toString().trim()

            if (namaBaru.isEmpty() || emailBaru.isEmpty()) {
                Toast.makeText(this, "Nama dan Email tidak boleh kosong", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Panggil pop-up konfirmasi terlebih dahulu
            tampilkanDialogKonfirmasi(namaBaru, emailBaru, alamatBaru)
        }
    }

    // ========================================================
    // 4. FUNGSI MENAMPILKAN DIALOG KONFIRMASI SIMPAN
    // ========================================================
    private fun tampilkanDialogKonfirmasi(namaBaru: String, emailBaru: String, alamatBaru: String) {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.layout_dialog_notif)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        val ivIcon = dialog.findViewById<ImageView>(R.id.ivDialogIcon)
        val tvTitle = dialog.findViewById<TextView>(R.id.tvDialogTitle)
        val tvMessage = dialog.findViewById<TextView>(R.id.tvDialogMessage)

        val btnOkBiasa = dialog.findViewById<Button>(R.id.btnDialogOk)
        val layoutDuaTombol = dialog.findViewById<LinearLayout>(R.id.layoutDialogDuaTombol)
        val btnBatal = dialog.findViewById<Button>(R.id.btnDialogBatal)
        val btnAksi = dialog.findViewById<Button>(R.id.btnDialogKeluar) // Kita pinjam ID ini

        // Sembunyikan 1 tombol, Munculkan 2 tombol
        btnOkBiasa.visibility = View.GONE
        layoutDuaTombol.visibility = View.VISIBLE

        // Modifikasi teks dan warna agar sesuai dengan tema "Simpan"
        tvTitle.text = "Simpan Perubahan"
        tvMessage.text = "Apakah kamu yakin ingin memperbarui data profil ini?"

        ivIcon.setImageResource(android.R.drawable.ic_menu_save)
        ivIcon.setColorFilter(Color.parseColor("#4CAF50")) // Warna Hijau
        ivIcon.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#E8F5E9"))

        btnAksi.text = "Simpan" // Ubah teks tombol jadi Simpan
        btnAksi.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#4CAF50")) // Tombol jadi hijau

        btnBatal.setOnClickListener {
            dialog.dismiss()
        }

        btnAksi.setOnClickListener {
            dialog.dismiss()
            // Lanjut ke proses upload API
            prosesSimpanKeServer(namaBaru, emailBaru, alamatBaru)
        }

        dialog.show()
    }

    // ========================================================
    // 5. FUNGSI EKSEKUSI API UPDATE PROFIL (RETROFIT)
    // ========================================================
    private fun prosesSimpanKeServer(namaBaru: String, emailBaru: String, alamatBaru: String) {
        btnSimpan.text = "Mengunggah..."
        btnSimpan.isEnabled = false

        val sharedPref = getSharedPreferences("FafaBitePrefs", Context.MODE_PRIVATE)
        val idUser = sharedPref.getInt("ID_USER", 0)

        // 1. Siapkan data teks dengan sintaks klasik (mendukung semua versi OkHttp)
        val reqId = RequestBody.create(MediaType.parse("text/plain"), idUser.toString())
        val reqNama = RequestBody.create(MediaType.parse("text/plain"), namaBaru)
        val reqEmail = RequestBody.create(MediaType.parse("text/plain"), emailBaru)

        val reqAlamat = if (alamatBaru.isNotEmpty()) {
            RequestBody.create(MediaType.parse("text/plain"), alamatBaru)
        } else {
            null
        }

        // 2. Siapkan file foto (jika ada perubahan)
        var bodyFoto: MultipartBody.Part? = null
        if (uriFotoTerpilih != null) {
            val fileFotoTerkompresi = kompresFoto(uriFotoTerpilih!!, this)
            val requestImageFile = RequestBody.create(MediaType.parse("image/jpeg"), fileFotoTerkompresi)
            bodyFoto = MultipartBody.Part.createFormData("foto", fileFotoTerkompresi.name, requestImageFile)
        }

        // 3. Tembak API
        ApiConfig.getApiService().updateProfilUser(reqId, reqNama, reqEmail, reqAlamat, bodyFoto)
            .enqueue(object : Callback<ResponseProfilToko> {
                override fun onResponse(call: Call<ResponseProfilToko>, response: Response<ResponseProfilToko>) {
                    btnSimpan.text = "Simpan Perubahan"
                    btnSimpan.isEnabled = true

                    if (response.isSuccessful) {
                        Toast.makeText(this@EditProfilActivity, "Profil berhasil diperbarui!", Toast.LENGTH_SHORT).show()

                        val editor = sharedPref.edit()
                        editor.putString("NAMA_USER", namaBaru)
                        editor.putString("EMAIL_USER", emailBaru)
                        editor.apply()

                        finish()
                    } else {
                        Toast.makeText(this@EditProfilActivity, "Gagal menyimpan profil", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<ResponseProfilToko>, t: Throwable) {
                    btnSimpan.text = "Simpan Perubahan"
                    btnSimpan.isEnabled = true
                    Toast.makeText(this@EditProfilActivity, "Error jaringan: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
    }

    // ========================================================
    // 6. FUNGSI KOMPRESI FOTO
    // ========================================================
    private fun kompresFoto(uri: Uri, context: Context): File {
        val fileTemp = File(context.cacheDir, "profil_terkompresi.jpg")
        val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
        val bitmapAsli = BitmapFactory.decodeStream(inputStream)
        inputStream?.close()

        val maxResolusi = 800
        var lebar = bitmapAsli.width
        var tinggi = bitmapAsli.height
        val rasio = lebar.toFloat() / tinggi.toFloat()

        if (lebar > maxResolusi || tinggi > maxResolusi) {
            if (rasio > 1) {
                lebar = maxResolusi
                tinggi = (lebar / rasio).toInt()
            } else {
                tinggi = maxResolusi
                lebar = (tinggi * rasio).toInt()
            }
        }

        val bitmapKecil = Bitmap.createScaledBitmap(bitmapAsli, lebar, tinggi, true)
        val outputStream = FileOutputStream(fileTemp)
        bitmapKecil.compress(Bitmap.CompressFormat.JPEG, 80, outputStream)
        outputStream.flush()
        outputStream.close()

        return fileTemp
    }
}