<?php

use Illuminate\Support\Facades\Route;

Route::get('/', function () {
    return view('welcome');
});

// Pintu Belakang Khusus Android (Tanpa peduli shortcut Windows)
Route::get('/file-makanan/{folder}/{filename}', function ($folder, $filename) {
    // Langsung tembak ke lokasi asli file di dalam brankas
    $path = storage_path('app/public/' . $folder . '/' . $filename);
    
    if (file_exists($path)) {
        return response()->file($path);
    }
    
    abort(404);
});