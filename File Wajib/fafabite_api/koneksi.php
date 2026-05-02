<?php
$host = "localhost";
$user = "root";
$pass = ""; // Laragon default tidak menggunakan password
$db   = "db_foodrescue";

$koneksi = mysqli_connect($host, $user, $pass, $db);

if (!$koneksi) {
    die("Koneksi database gagal: " . mysqli_connect_error());
}
?>