<?php
header("Access-Control-Allow-Origin: *");
header("Content-Type: application/json; charset=UTF-8");

include 'koneksi.php';

if ($_SERVER['REQUEST_METHOD'] == 'POST') {
    $nama = $_POST['nama_lengkap'] ?? '';
    $email = $_POST['email'] ?? '';
    $no_hp = $_POST['no_hp'] ?? '';
    $password = $_POST['password'] ?? '';
    $role = 'pembeli'; // Default untuk pengguna baru

    // Cek apakah email sudah terdaftar
    $cek_email = mysqli_query($koneksi, "SELECT * FROM users WHERE email = '$email'");
    if (mysqli_num_rows($cek_email) > 0) {
        echo json_encode(["sukses" => false, "pesan" => "Email sudah terdaftar!"]);
        exit;
    }

    // Masukkan data baru ke database
    $query = "INSERT INTO users (nama_lengkap, email, no_hp, password, role) 
              VALUES ('$nama', '$email', '$no_hp', '$password', '$role')";

    if (mysqli_query($koneksi, $query)) {
        echo json_encode(["sukses" => true, "pesan" => "Pendaftaran berhasil! Silakan Login."]);
    } else {
        echo json_encode(["sukses" => false, "pesan" => "Gagal mendaftar: " . mysqli_error($koneksi)]);
    }
} else {
    echo json_encode(["sukses" => false, "pesan" => "Metode tidak diizinkan."]);
}
?>