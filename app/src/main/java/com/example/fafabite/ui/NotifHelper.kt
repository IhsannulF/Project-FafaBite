package com.example.fafabite.ui

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.example.fafabite.R


object NotifHelper {

    // Fungsi untuk memunculkan pop-up sukses/informasi
    fun showDialog(
        context: Context,
        judul: String,
        pesan: String,
        onOkClick: (() -> Unit)? = null // Aksi yang terjadi setelah tombol Oke diklik
    ) {
        val dialog = Dialog(context)
        dialog.setContentView(R.layout.layout_dialog_notif)

        // Membuat latar belakang bawaan dialog menjadi transparan agar rounded corner kita terlihat
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCancelable(false) // Tidak bisa ditutup dengan klik luar kotak

        val tvTitle = dialog.findViewById<TextView>(R.id.tvDialogTitle)
        val tvMessage = dialog.findViewById<TextView>(R.id.tvDialogMessage)
        val btnOk = dialog.findViewById<Button>(R.id.btnDialogOk)

        tvTitle.text = judul
        tvMessage.text = pesan

        btnOk.setOnClickListener {
            dialog.dismiss() // Tutup pop-up
            onOkClick?.invoke() // Jalankan perintah selanjutnya (pindah halaman, dll)
        }

        dialog.show()
    }
}