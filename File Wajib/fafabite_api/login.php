<?php
// Mengizinkan akses API dan mengatur format balasan ke JSON
header("Access-Control-Allow-Origin: *");
header("Content-Type: application/json; charset=UTF-8");

include 'koneksi.php';

// Memastikan data dikirim menggunakan metode POST (standar keamanan form login)
if ($_SERVER['REQUEST_METHOD'] == 'POST') {
    
    // Menangkap data yang dikirim dari Android
    $email = $_POST['email'] ?? '';
    $password = $_POST['password'] ?? '';

    // Mencari data user di database
    $query = "SELECT * FROM users WHERE email = '$email' AND password = '$password'";
    $result = mysqli_query($koneksi, $query);

    if (mysqli_num_rows($result) > 0) {
        $user = mysqli_fetch_assoc($result);
        
        // Jika cocok, kirim balasan sukses beserta data user
        echo json_encode([
            "sukses" => true,
            "pesan" => "Login berhasil!",
            "data" => [
                "id_user" => $user['id_user'],
                "nama_lengkap" => $user['nama_lengkap'],
                "role" => $user['role']
            ]
        ]);
    } else {
        // Jika tidak cocok, kirim pesan error
        echo json_encode([
            "sukses" => false,
            "pesan" => "Email atau password salah!"
        ]);
    }
} else {
    echo json_encode(["sukses" => false, "pesan" => "Metode request tidak valid."]);
}
?>