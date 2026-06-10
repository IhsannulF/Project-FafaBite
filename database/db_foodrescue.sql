-- --------------------------------------------------------
-- Host:                         127.0.0.1
-- Server version:               8.0.30 - MySQL Community Server - GPL
-- Server OS:                    Win64
-- HeidiSQL Version:             12.1.0.6537
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

-- Dumping structure for table db_foodrescue.cache
CREATE TABLE IF NOT EXISTS `cache` (
  `key` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `value` mediumtext COLLATE utf8mb4_unicode_ci NOT NULL,
  `expiration` bigint NOT NULL,
  PRIMARY KEY (`key`),
  KEY `cache_expiration_index` (`expiration`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Dumping data for table db_foodrescue.cache: ~0 rows (approximately)

-- Dumping structure for table db_foodrescue.cache_locks
CREATE TABLE IF NOT EXISTS `cache_locks` (
  `key` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `owner` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `expiration` bigint NOT NULL,
  PRIMARY KEY (`key`),
  KEY `cache_locks_expiration_index` (`expiration`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Dumping data for table db_foodrescue.cache_locks: ~0 rows (approximately)

-- Dumping structure for table db_foodrescue.failed_jobs
CREATE TABLE IF NOT EXISTS `failed_jobs` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `uuid` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `connection` text COLLATE utf8mb4_unicode_ci NOT NULL,
  `queue` text COLLATE utf8mb4_unicode_ci NOT NULL,
  `payload` longtext COLLATE utf8mb4_unicode_ci NOT NULL,
  `exception` longtext COLLATE utf8mb4_unicode_ci NOT NULL,
  `failed_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `failed_jobs_uuid_unique` (`uuid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Dumping data for table db_foodrescue.failed_jobs: ~0 rows (approximately)

-- Dumping structure for table db_foodrescue.jobs
CREATE TABLE IF NOT EXISTS `jobs` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `queue` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `payload` longtext COLLATE utf8mb4_unicode_ci NOT NULL,
  `attempts` smallint unsigned NOT NULL,
  `reserved_at` int unsigned DEFAULT NULL,
  `available_at` int unsigned NOT NULL,
  `created_at` int unsigned NOT NULL,
  PRIMARY KEY (`id`),
  KEY `jobs_queue_index` (`queue`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Dumping data for table db_foodrescue.jobs: ~0 rows (approximately)

-- Dumping structure for table db_foodrescue.job_batches
CREATE TABLE IF NOT EXISTS `job_batches` (
  `id` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `name` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `total_jobs` int NOT NULL,
  `pending_jobs` int NOT NULL,
  `failed_jobs` int NOT NULL,
  `failed_job_ids` longtext COLLATE utf8mb4_unicode_ci NOT NULL,
  `options` mediumtext COLLATE utf8mb4_unicode_ci,
  `cancelled_at` int DEFAULT NULL,
  `created_at` int NOT NULL,
  `finished_at` int DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Dumping data for table db_foodrescue.job_batches: ~0 rows (approximately)

-- Dumping structure for table db_foodrescue.migrations
CREATE TABLE IF NOT EXISTS `migrations` (
  `id` int unsigned NOT NULL AUTO_INCREMENT,
  `migration` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `batch` int NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Dumping data for table db_foodrescue.migrations: ~6 rows (approximately)
INSERT INTO `migrations` (`id`, `migration`, `batch`) VALUES
	(1, '0001_01_01_000000_create_users_table', 1),
	(2, '0001_01_01_000001_create_cache_table', 1),
	(3, '0001_01_01_000002_create_jobs_table', 1),
	(4, '2026_05_04_084004_create_personal_access_tokens_table', 1),
	(5, '2026_05_06_121638_create_produks_table', 1),
	(6, '2026_05_06_133347_create_tokos_table', 2),
	(7, '2026_05_07_131329_create_pesanans_table', 3),
	(8, '2026_05_09_105454_add_role_to_users_table', 4),
	(9, '2026_06_09_202313_add_foto_profil_to_users_table', 5);

-- Dumping structure for table db_foodrescue.password_reset_tokens
CREATE TABLE IF NOT EXISTS `password_reset_tokens` (
  `email` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `token` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `created_at` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Dumping data for table db_foodrescue.password_reset_tokens: ~0 rows (approximately)

-- Dumping structure for table db_foodrescue.personal_access_tokens
CREATE TABLE IF NOT EXISTS `personal_access_tokens` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `tokenable_type` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `tokenable_id` bigint unsigned NOT NULL,
  `name` text COLLATE utf8mb4_unicode_ci NOT NULL,
  `token` varchar(64) COLLATE utf8mb4_unicode_ci NOT NULL,
  `abilities` text COLLATE utf8mb4_unicode_ci,
  `last_used_at` timestamp NULL DEFAULT NULL,
  `expires_at` timestamp NULL DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `personal_access_tokens_token_unique` (`token`),
  KEY `personal_access_tokens_tokenable_type_tokenable_id_index` (`tokenable_type`,`tokenable_id`),
  KEY `personal_access_tokens_expires_at_index` (`expires_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Dumping data for table db_foodrescue.personal_access_tokens: ~0 rows (approximately)

-- Dumping structure for table db_foodrescue.pesanans
CREATE TABLE IF NOT EXISTS `pesanans` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `id_toko` bigint unsigned NOT NULL,
  `id_user` bigint unsigned NOT NULL,
  `id_produk` bigint unsigned NOT NULL,
  `nomor_order` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `jumlah_pesan` int NOT NULL,
  `total_harga` int NOT NULL,
  `status_pesanan` enum('menunggu','disiapkan','siap_diambil','selesai','batal') CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'menunggu',
  `created_at` timestamp NULL DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `pesanans_nomor_order_unique` (`nomor_order`),
  KEY `pesanans_id_toko_foreign` (`id_toko`),
  KEY `pesanans_id_user_foreign` (`id_user`),
  KEY `pesanans_id_produk_foreign` (`id_produk`),
  CONSTRAINT `pesanans_id_produk_foreign` FOREIGN KEY (`id_produk`) REFERENCES `produks` (`id`) ON DELETE CASCADE,
  CONSTRAINT `pesanans_id_toko_foreign` FOREIGN KEY (`id_toko`) REFERENCES `tokos` (`id_toko`) ON DELETE CASCADE,
  CONSTRAINT `pesanans_id_user_foreign` FOREIGN KEY (`id_user`) REFERENCES `users` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Dumping data for table db_foodrescue.pesanans: ~2 rows (approximately)
INSERT INTO `pesanans` (`id`, `id_toko`, `id_user`, `id_produk`, `nomor_order`, `jumlah_pesan`, `total_harga`, `status_pesanan`, `created_at`, `updated_at`) VALUES
	(12, 1, 1, 8, 'FAFA-7773', 2, 8000, 'selesai', '2026-06-09 04:18:18', '2026-06-09 04:30:28'),
	(13, 1, 1, 9, 'FAFA-9541', 1, 15000, 'selesai', '2026-06-09 04:30:52', '2026-06-09 04:37:17'),
	(15, 1, 4, 9, 'FAFA-5131', 2, 30000, 'selesai', '2026-06-09 12:12:41', '2026-06-09 12:13:53'),
	(16, 1, 4, 8, 'FAFA-1732', 1, 4000, 'selesai', '2026-06-09 12:14:16', '2026-06-09 12:15:17'),
	(17, 1, 4, 11, 'FAFA-8988', 1, 8000, 'selesai', '2026-06-09 12:15:43', '2026-06-09 12:19:58'),
	(18, 1, 4, 11, 'FAFA-2075', 1, 8000, 'selesai', '2026-06-09 20:05:30', '2026-06-09 20:13:18');

-- Dumping structure for table db_foodrescue.produks
CREATE TABLE IF NOT EXISTS `produks` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `id_toko` bigint unsigned NOT NULL,
  `nama_makanan` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `harga_asli` int NOT NULL,
  `harga_diskon` int NOT NULL,
  `stok` int NOT NULL,
  `waktu_pickup` datetime NOT NULL,
  `status` enum('tersedia','habis') COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'tersedia',
  `foto_makanan` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `created_at` timestamp NULL DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `produks_id_toko_foreign` (`id_toko`),
  CONSTRAINT `produks_id_toko_foreign` FOREIGN KEY (`id_toko`) REFERENCES `users` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Dumping data for table db_foodrescue.produks: ~4 rows (approximately)
INSERT INTO `produks` (`id`, `id_toko`, `nama_makanan`, `harga_asli`, `harga_diskon`, `stok`, `waktu_pickup`, `status`, `foto_makanan`, `created_at`, `updated_at`) VALUES
	(8, 1, 'Rambak', 10000, 4000, 12, '2026-06-15 13:00:00', 'tersedia', '1780992552.jpg', '2026-06-09 01:09:12', '2026-06-09 12:14:16'),
	(9, 1, 'Mentahan Udon', 38000, 15000, 12, '2026-06-12 15:10:00', 'tersedia', '1780992620.jpg', '2026-06-09 01:10:20', '2026-06-09 12:12:41'),
	(11, 1, 'Pisang goreng coklat', 18000, 8000, 0, '2026-06-11 15:45:00', 'tersedia', '1781031213.jpg', '2026-06-09 10:45:37', '2026-06-09 20:05:30');

-- Dumping structure for table db_foodrescue.sessions
CREATE TABLE IF NOT EXISTS `sessions` (
  `id` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `user_id` bigint unsigned DEFAULT NULL,
  `ip_address` varchar(45) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `user_agent` text COLLATE utf8mb4_unicode_ci,
  `payload` longtext COLLATE utf8mb4_unicode_ci NOT NULL,
  `last_activity` int NOT NULL,
  PRIMARY KEY (`id`),
  KEY `sessions_user_id_index` (`user_id`),
  KEY `sessions_last_activity_index` (`last_activity`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Dumping data for table db_foodrescue.sessions: ~73 rows (approximately)
INSERT INTO `sessions` (`id`, `user_id`, `ip_address`, `user_agent`, `payload`, `last_activity`) VALUES
	('02z25ER50zlD57d4CSXOjxxdLwaStKejjDa2MbSN', NULL, '192.168.1.62', 'Dalvik/2.1.0 (Linux; U; Android 16; CPH2687 Build/BP2A.250605.015)', 'eyJfdG9rZW4iOiIwZ2djZTBVM3lTTm5NbFJPWEt4em9wZERpQ2l3ZjUwcHkwbU1Tb2JEIiwiX3ByZXZpb3VzIjp7InVybCI6Imh0dHA6XC9cLzE5Mi4xNjguMS42MTo4MDAwXC9maWxlLW1ha2FuYW5cL2ZvdG9fcHJvZHVrXC9VbjRhSjNBVFVjVmhKTnBtM1RTN0RsY2haVW5sZUc3bHNIaWR3WDIwLmpwZyIsInJvdXRlIjpudWxsfSwiX2ZsYXNoIjp7Im9sZCI6W10sIm5ldyI6W119fQ==', 1778323779),
	('06Bo1mPMsvBNGMyaYQes7qwtMngqNZ1o0kqEanQC', NULL, '10.76.65.90', 'Dalvik/2.1.0 (Linux; U; Android 16; CPH2687 Build/BP2A.250605.015)', 'eyJfdG9rZW4iOiIwSzlvVWJYUVVxV25zeHY3ZFRsNmxKSUNFMzR4Q0FNSnpoeXN0V21VIiwiX3ByZXZpb3VzIjp7InVybCI6Imh0dHA6XC9cLzEwLjc2LjY1LjIzMjo4MDAwXC9maWxlLW1ha2FuYW5cL3Byb2R1a3NcLzE3NzgxNzIyOTUuanBnIiwicm91dGUiOm51bGx9LCJfZmxhc2giOnsib2xkIjpbXSwibmV3IjpbXX19', 1780840552),
	('1eukLm1BFi4mIRu7nei5zSGTkryztb6Q3kMFTSCZ', NULL, '10.76.65.90', 'Dalvik/2.1.0 (Linux; U; Android 16; CPH2687 Build/BP2A.250605.015)', 'eyJfdG9rZW4iOiJha2NZcFZkb2xLeEZlRVBTREdmT0pVWnR1TFAzb2FtM3Y2Um1MNTd3IiwiX3ByZXZpb3VzIjp7InVybCI6Imh0dHA6XC9cLzEwLjc2LjY1LjIzMjo4MDAwXC9maWxlLW1ha2FuYW5cL2ZvdG9fcHJvZHVrXC9VbjRhSjNBVFVjVmhKTnBtM1RTN0RsY2haVW5sZUc3bHNIaWR3WDIwLmpwZyIsInJvdXRlIjpudWxsfSwiX2ZsYXNoIjp7Im9sZCI6W10sIm5ldyI6W119fQ==', 1780839757),
	('2FkMhMmoKEXbiVvgGHK1jjzZ7Qi43VEJ4AtydLG7', NULL, '192.168.110.240', 'Dalvik/2.1.0 (Linux; U; Android 16; CPH2687 Build/BP2A.250605.015)', 'eyJfdG9rZW4iOiJ4bDA0ZDNVNkpWbHJzRTZjcExpbVBtM3BUV2FlUkpkWkNoMW5WWTdhIiwiX3ByZXZpb3VzIjp7InVybCI6Imh0dHA6XC9cLzE5Mi4xNjguMTEwLjE4OTo4MDAwXC9maWxlLW1ha2FuYW5cL3Byb2R1a3NcLzE3NzgxNzA1MTkuanBnIiwicm91dGUiOm51bGx9LCJfZmxhc2giOnsib2xkIjpbXSwibmV3IjpbXX19', 1780991866),
	('5cErc1kUvcKMtT85m93L2dd2PQQQqhOiadkCnHI6', NULL, '10.76.65.90', 'Dalvik/2.1.0 (Linux; U; Android 16; CPH2687 Build/BP2A.250605.015)', 'eyJfdG9rZW4iOiJmcW8wNmtkUlQzc0hzUnBUU3JUYkU1S3U2NVVkamlFdE12SzVHY3M1IiwiX3ByZXZpb3VzIjp7InVybCI6Imh0dHA6XC9cLzEwLjc2LjY1LjIzMjo4MDAwXC9maWxlLW1ha2FuYW5cL3Byb2R1a3NcLzE3NzgxNzIyOTUuanBnIiwicm91dGUiOm51bGx9LCJfZmxhc2giOnsib2xkIjpbXSwibmV3IjpbXX19', 1780840833),
	('5f6CLPD0kTdiUKlKNeo7gpsa1GQg8BvvHJmGfKZb', NULL, '192.168.1.11', 'Dalvik/2.1.0 (Linux; U; Android 16; CPH2687 Build/BP2A.250605.015)', 'eyJfdG9rZW4iOiJzaVgwOVVZTWo5am1NcHNPMzh4bUYwMUxtNWY2UFNGV2c0ZWFHUmxLIiwiX3ByZXZpb3VzIjp7InVybCI6Imh0dHA6XC9cLzE5Mi4xNjguMS42OjgwMDBcL2ZpbGUtbWFrYW5hblwvZm90b19wcm9kdWtcL1VuNGFKM0FUVWNWaEpOcG0zVFM3RGxjaFpVbmxlRzdsc0hpZHdYMjAuanBnIiwicm91dGUiOm51bGx9LCJfZmxhc2giOnsib2xkIjpbXSwibmV3IjpbXX19', 1778368879),
	('5hDrtMPb0l2QfUsvtzA0yhX5n2MOrKz93hXsm0qP', NULL, '192.168.1.62', 'Dalvik/2.1.0 (Linux; U; Android 16; CPH2687 Build/BP2A.250605.015)', 'eyJfdG9rZW4iOiJ4TFJVSUpRTUVaZnpLYWY5SHhZTTlrbFdkWW5MZkxvTmNWWjlsS25OIiwiX3ByZXZpb3VzIjp7InVybCI6Imh0dHA6XC9cLzE5Mi4xNjguMS42MTo4MDAwXC9maWxlLW1ha2FuYW5cL2ZvdG9fcHJvZHVrXC9VbjRhSjNBVFVjVmhKTnBtM1RTN0RsY2haVW5sZUc3bHNIaWR3WDIwLmpwZyIsInJvdXRlIjpudWxsfSwiX2ZsYXNoIjp7Im9sZCI6W10sIm5ldyI6W119fQ==', 1778331632),
	('60xFmQUvAaiHxJ7SlDuWT9PYasmH7FKsW8rjEzld', NULL, '192.168.1.11', 'Dalvik/2.1.0 (Linux; U; Android 16; CPH2687 Build/BP2A.250605.015)', 'eyJfdG9rZW4iOiJFbkdsY0NJcDlsMENmMm55eWJVa05XT0VZUkdXRDhrVWNaQ1hUZVRHIiwiX3ByZXZpb3VzIjp7InVybCI6Imh0dHA6XC9cLzE5Mi4xNjguMS42OjgwMDBcL2ZpbGUtbWFrYW5hblwvZm90b19wcm9kdWtcL1VuNGFKM0FUVWNWaEpOcG0zVFM3RGxjaFpVbmxlRzdsc0hpZHdYMjAuanBnIiwicm91dGUiOm51bGx9LCJfZmxhc2giOnsib2xkIjpbXSwibmV3IjpbXX19', 1778368842),
	('7gvUpixNNeU4N2GfHrTlQCBQUlnTd8sOJYsdrSls', NULL, '192.168.1.11', 'Dalvik/2.1.0 (Linux; U; Android 16; CPH2687 Build/BP2A.250605.015)', 'eyJfdG9rZW4iOiJkZkpGeUtlSk8xcGsyNUVLRFE5NzJQM2R1U2FvNE5zdGdhMzN2Nkx6IiwiX3ByZXZpb3VzIjp7InVybCI6Imh0dHA6XC9cLzE5Mi4xNjguMS42OjgwMDBcL2ZpbGUtbWFrYW5hblwvZm90b19wcm9kdWtcL1VuNGFKM0FUVWNWaEpOcG0zVFM3RGxjaFpVbmxlRzdsc0hpZHdYMjAuanBnIiwicm91dGUiOm51bGx9LCJfZmxhc2giOnsib2xkIjpbXSwibmV3IjpbXX19', 1778368876),
	('8ufxB4yyo0BQ087SDNfNZvWkDdDH2l5nQrRmoif0', NULL, '192.168.110.240', 'Dalvik/2.1.0 (Linux; U; Android 16; CPH2687 Build/BP2A.250605.015)', 'eyJfdG9rZW4iOiI4VUxFbngwOUlUWXJLRlMxOFc5VWp2dWt2NU9rOTNhNTYyVkxRN0pZIiwiX3ByZXZpb3VzIjp7InVybCI6Imh0dHA6XC9cLzE5Mi4xNjguMTEwLjE4OTo4MDAwXC9maWxlLW1ha2FuYW5cL3Byb2R1a3NcLzE3NzgxNzIyOTUuanBnIiwicm91dGUiOm51bGx9LCJfZmxhc2giOnsib2xkIjpbXSwibmV3IjpbXX19', 1780991866),
	('9RoLazWT2kOdoaV7A7eeauzDIeC4Bf0zR4V3CSRz', NULL, '10.76.65.90', 'Dalvik/2.1.0 (Linux; U; Android 16; CPH2687 Build/BP2A.250605.015)', 'eyJfdG9rZW4iOiJhUjd5bzRVSGM4dXY0NVhQRkVQeXZ0SHlrZjNDYmNBT2I0STJKYWN2IiwiX3ByZXZpb3VzIjp7InVybCI6Imh0dHA6XC9cLzEwLjc2LjY1LjIzMjo4MDAwXC9maWxlLW1ha2FuYW5cL3Byb2R1a3NcLzE3NzgxNzA1MTkuanBnIiwicm91dGUiOm51bGx9LCJfZmxhc2giOnsib2xkIjpbXSwibmV3IjpbXX19', 1780841355),
	('AaE1ib8lQqh2WqwM6NZ0h96Jb1mqbEZOIUSc7SYl', NULL, '10.76.65.90', 'Dalvik/2.1.0 (Linux; U; Android 16; CPH2687 Build/BP2A.250605.015)', 'eyJfdG9rZW4iOiJWa0tBMk84b2p5VGlPZlp5R2ZUSVFYc1VBeG85bVFwUk9jR1phZVZPIiwiX3ByZXZpb3VzIjp7InVybCI6Imh0dHA6XC9cLzEwLjc2LjY1LjIzMjo4MDAwXC9maWxlLW1ha2FuYW5cL3Byb2R1a3NcLzE3NzgxNzIyOTUuanBnIiwicm91dGUiOm51bGx9LCJfZmxhc2giOnsib2xkIjpbXSwibmV3IjpbXX19', 1780841330),
	('aGwcaGRmhBt6cAwX8c3BOp4F6D9hSssP7SqJ1Zy4', NULL, '192.168.110.189', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/148.0.0.0 Safari/537.36', 'eyJfdG9rZW4iOiJaVU1hb3U5S3Mxak9xNjJtcno2ZG1SbktNZE4xTVVqVHVES2ZFWWJZIiwiX3ByZXZpb3VzIjp7InVybCI6Imh0dHA6XC9cLzE5Mi4xNjguMTEwLjE4OTo4MDAwIiwicm91dGUiOm51bGx9LCJfZmxhc2giOnsib2xkIjpbXSwibmV3IjpbXX19', 1780837968),
	('AtyBMCtYsr7Ky7dkfmRpjoyWnBK3SCKCSAABCrpg', NULL, '192.168.1.62', 'Dalvik/2.1.0 (Linux; U; Android 16; CPH2687 Build/BP2A.250605.015)', 'eyJfdG9rZW4iOiJQTkJ5VHJtWHhkcFRnT05zR3V2dTJRQTFxMmdseDFlcVFZTmlLWGFOIiwiX3ByZXZpb3VzIjp7InVybCI6Imh0dHA6XC9cLzE5Mi4xNjguMS42MTo4MDAwXC9maWxlLW1ha2FuYW5cL3Byb2R1a3NcLzE3NzgxNzA1MTkuanBnIiwicm91dGUiOm51bGx9LCJfZmxhc2giOnsib2xkIjpbXSwibmV3IjpbXX19', 1778331632),
	('BVUuTALtdOHnADiYtLqp7g7xVL0ipuRewVyvPKEd', NULL, '192.168.1.62', 'Dalvik/2.1.0 (Linux; U; Android 16; CPH2687 Build/BP2A.250605.015)', 'eyJfdG9rZW4iOiJWd3lGb0Fzd01XaFpKaDlra3k3UDRWRlE2MTNKTjF3dllFUmJaR3I3IiwiX3ByZXZpb3VzIjp7InVybCI6Imh0dHA6XC9cLzE5Mi4xNjguMS42MTo4MDAwXC9maWxlLW1ha2FuYW5cL2ZvdG9fcHJvZHVrXC9VbjRhSjNBVFVjVmhKTnBtM1RTN0RsY2haVW5sZUc3bHNIaWR3WDIwLmpwZyIsInJvdXRlIjpudWxsfSwiX2ZsYXNoIjp7Im9sZCI6W10sIm5ldyI6W119fQ==', 1778326002),
	('BY7JZoINBGhi7XHdXwAU5nb1wAuwzpl3iThZOVCL', NULL, '10.76.65.90', 'Dalvik/2.1.0 (Linux; U; Android 16; CPH2687 Build/BP2A.250605.015)', 'eyJfdG9rZW4iOiJOU1dUQ1pFYWZKRUNtQ1Y1Nk8wZGFGRlNoWDZYMWZLQUJCRUdQWFBnIiwiX3ByZXZpb3VzIjp7InVybCI6Imh0dHA6XC9cLzEwLjc2LjY1LjIzMjo4MDAwXC9maWxlLW1ha2FuYW5cL3Byb2R1a3NcLzE3NzgxNzIyOTUuanBnIiwicm91dGUiOm51bGx9LCJfZmxhc2giOnsib2xkIjpbXSwibmV3IjpbXX19', 1780840568),
	('ceGR3JDyOHI7wNJSdeYqdQbNTIltqAJ7pmQMplFB', NULL, '192.168.1.62', 'Dalvik/2.1.0 (Linux; U; Android 16; CPH2687 Build/BP2A.250605.015)', 'eyJfdG9rZW4iOiJHV2hqUDhzNmRzV1dRR3k4OEk1R0d0SjlKa09GVFZTUkFmckd2WHZPIiwiX3ByZXZpb3VzIjp7InVybCI6Imh0dHA6XC9cLzE5Mi4xNjguMS42MTo4MDAwXC9maWxlLW1ha2FuYW5cL3Byb2R1a3NcLzE3NzgxNzIyOTUuanBnIiwicm91dGUiOm51bGx9LCJfZmxhc2giOnsib2xkIjpbXSwibmV3IjpbXX19', 1778330381),
	('cLZveMaTAyXAdsnTMpZnljDjjU0THuq4oYnfXdAs', NULL, '192.168.1.11', 'Dalvik/2.1.0 (Linux; U; Android 16; CPH2687 Build/BP2A.250605.015)', 'eyJfdG9rZW4iOiJha2NHeEROaDJma0VwU3FGQUpBa2N2OHVuc0N1RGJFTGhwb0gyTWYzIiwiX3ByZXZpb3VzIjp7InVybCI6Imh0dHA6XC9cLzE5Mi4xNjguMS42OjgwMDBcL2ZpbGUtbWFrYW5hblwvZm90b19wcm9kdWtcL1VuNGFKM0FUVWNWaEpOcG0zVFM3RGxjaFpVbmxlRzdsc0hpZHdYMjAuanBnIiwicm91dGUiOm51bGx9LCJfZmxhc2giOnsib2xkIjpbXSwibmV3IjpbXX19', 1778368417),
	('DhOnNM0QOPxNMg8QXV6Osdj0tcirpKUixVapCnbz', NULL, '10.76.65.90', 'Dalvik/2.1.0 (Linux; U; Android 16; CPH2687 Build/BP2A.250605.015)', 'eyJfdG9rZW4iOiJDQlJkcGNvVWczdk52VFR6QkxZbWlLZnk2cFRGNEtQU1BjR212UmgzIiwiX3ByZXZpb3VzIjp7InVybCI6Imh0dHA6XC9cLzEwLjc2LjY1LjIzMjo4MDAwXC9maWxlLW1ha2FuYW5cL3Byb2R1a3NcLzE3NzgxNzA1MTkuanBnIiwicm91dGUiOm51bGx9LCJfZmxhc2giOnsib2xkIjpbXSwibmV3IjpbXX19', 1780840552),
	('EBof34T3PoRYMmcR805Kh4I0wpap7PUzXmUXtMaa', NULL, '192.168.1.11', 'Dalvik/2.1.0 (Linux; U; Android 16; CPH2687 Build/BP2A.250605.015)', 'eyJfdG9rZW4iOiJOMnhMblBmQ0ViR0ZOWG1ncDJtMEZ5TkVETDZUOU5jN2ZNY1BXY2F2IiwiX3ByZXZpb3VzIjp7InVybCI6Imh0dHA6XC9cLzE5Mi4xNjguMS42OjgwMDBcL2ZpbGUtbWFrYW5hblwvZm90b19wcm9kdWtcL1VuNGFKM0FUVWNWaEpOcG0zVFM3RGxjaFpVbmxlRzdsc0hpZHdYMjAuanBnIiwicm91dGUiOm51bGx9LCJfZmxhc2giOnsib2xkIjpbXSwibmV3IjpbXX19', 1778368857),
	('ec6SypkCPmOKdgoZL7Jc7Wh4y7ZLQTejijiWliUL', NULL, '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/148.0.0.0 Safari/537.36', 'eyJfdG9rZW4iOiJrRVlYa1AzN01ZaUJUTzd0OGdqTTh6MkFuWEN3VlhLUElkVWJqR09kIiwiX3ByZXZpb3VzIjp7InVybCI6Imh0dHA6XC9cLzEyNy4wLjAuMTo4MDAwIiwicm91dGUiOm51bGx9LCJfZmxhc2giOnsib2xkIjpbXSwibmV3IjpbXX19', 1781008308),
	('ejQB8H3EJZAS0decwpzB20BpVK6X3g9G2Chtkjw3', NULL, '10.76.65.90', 'Dalvik/2.1.0 (Linux; U; Android 16; CPH2687 Build/BP2A.250605.015)', 'eyJfdG9rZW4iOiI2TFBLN2NLUkpMbWkwVndYTWdtT05OeEFSNTZvZVd6dzlmVWl3SlNHIiwiX3ByZXZpb3VzIjp7InVybCI6Imh0dHA6XC9cLzEwLjc2LjY1LjIzMjo4MDAwXC9maWxlLW1ha2FuYW5cL3Byb2R1a3NcLzE3NzgxNzA1MTkuanBnIiwicm91dGUiOm51bGx9LCJfZmxhc2giOnsib2xkIjpbXSwibmV3IjpbXX19', 1780839758),
	('ESx5IBiw0cF9nQGX2xKn7Kuzx9NoH7YbuhdtaYz1', NULL, '192.168.110.189', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/148.0.0.0 Safari/537.36', 'eyJfdG9rZW4iOiI0YjJobmJkblpxRk1OUmlaWlMyMFhsSTcyTVllWEh5MUkyVUdNMkJRIiwiX3ByZXZpb3VzIjp7InVybCI6Imh0dHA6XC9cLzE5Mi4xNjguMTEwLjE4OTo4MDAwIiwicm91dGUiOm51bGx9LCJfZmxhc2giOnsib2xkIjpbXSwibmV3IjpbXX19', 1780837971),
	('EsXavA82cIYbAeKKcp0yHl3DsYnJJbAciSTukpJU', NULL, '10.76.65.90', 'Dalvik/2.1.0 (Linux; U; Android 16; CPH2687 Build/BP2A.250605.015)', 'eyJfdG9rZW4iOiJjcUo5YjFRUWFObU1MN2hxQlJmNzN6R204WVRzSzE4OGJtYVd6SnZEIiwiX3ByZXZpb3VzIjp7InVybCI6Imh0dHA6XC9cLzEwLjc2LjY1LjIzMjo4MDAwXC9maWxlLW1ha2FuYW5cL2ZvdG9fcHJvZHVrXC9VbjRhSjNBVFVjVmhKTnBtM1RTN0RsY2haVW5sZUc3bHNIaWR3WDIwLmpwZyIsInJvdXRlIjpudWxsfSwiX2ZsYXNoIjp7Im9sZCI6W10sIm5ldyI6W119fQ==', 1780840623),
	('eTdzn5R7nvoBckqxdALPecAmNIiOf2VDIrxxdsaD', NULL, '192.168.1.62', 'Dalvik/2.1.0 (Linux; U; Android 16; CPH2687 Build/BP2A.250605.015)', 'eyJfdG9rZW4iOiI4enFaZ2VReEp4QjhBU3ZaTUw2YnNCUXRZU2NtSHhPejNRTGQ4V29jIiwiX3ByZXZpb3VzIjp7InVybCI6Imh0dHA6XC9cLzE5Mi4xNjguMS42MTo4MDAwXC9maWxlLW1ha2FuYW5cL2ZvdG9fcHJvZHVrXC9VbjRhSjNBVFVjVmhKTnBtM1RTN0RsY2haVW5sZUc3bHNIaWR3WDIwLmpwZyIsInJvdXRlIjpudWxsfSwiX2ZsYXNoIjp7Im9sZCI6W10sIm5ldyI6W119fQ==', 1778331436),
	('exnfoqgulhi4kcCKkP1BGWHrMw0r0QuVfCEFMUpI', NULL, '10.76.65.90', 'Dalvik/2.1.0 (Linux; U; Android 16; CPH2687 Build/BP2A.250605.015)', 'eyJfdG9rZW4iOiJlU240b0ltRDhBY3FkcENRbWJrbUZuUFBxdGFQNFJVZGNobFplcXBjIiwiX3ByZXZpb3VzIjp7InVybCI6Imh0dHA6XC9cLzEwLjc2LjY1LjIzMjo4MDAwXC9maWxlLW1ha2FuYW5cL3Byb2R1a3NcLzE3NzgxNzIyOTUuanBnIiwicm91dGUiOm51bGx9LCJfZmxhc2giOnsib2xkIjpbXSwibmV3IjpbXX19', 1780840781),
	('fNcovOmUxCSLWr2Qbxjkgg03q4HqHaAFZPTGjGq4', NULL, '192.168.1.62', 'Dalvik/2.1.0 (Linux; U; Android 16; CPH2687 Build/BP2A.250605.015)', 'eyJfdG9rZW4iOiJGOUdqOEJMYWNvSVpybGdSYUdBNXBZZEdMczczM2JYMjZXdlNWUTMyIiwiX3ByZXZpb3VzIjp7InVybCI6Imh0dHA6XC9cLzE5Mi4xNjguMS42MTo4MDAwXC9maWxlLW1ha2FuYW5cL2ZvdG9fcHJvZHVrXC9VbjRhSjNBVFVjVmhKTnBtM1RTN0RsY2haVW5sZUc3bHNIaWR3WDIwLmpwZyIsInJvdXRlIjpudWxsfSwiX2ZsYXNoIjp7Im9sZCI6W10sIm5ldyI6W119fQ==', 1778327426),
	('FNSujIacNcIkj0hLMC81s7k11bqWsz4LeamKXfAv', NULL, '10.76.65.90', 'Dalvik/2.1.0 (Linux; U; Android 16; CPH2687 Build/BP2A.250605.015)', 'eyJfdG9rZW4iOiJlQUk1dXZmTkZpbXZuWHMxNjdscjlCM293NDdWYTBPaEtaVW54YmFHIiwiX3ByZXZpb3VzIjp7InVybCI6Imh0dHA6XC9cLzEwLjc2LjY1LjIzMjo4MDAwXC9maWxlLW1ha2FuYW5cL2ZvdG9fcHJvZHVrXC9VbjRhSjNBVFVjVmhKTnBtM1RTN0RsY2haVW5sZUc3bHNIaWR3WDIwLmpwZyIsInJvdXRlIjpudWxsfSwiX2ZsYXNoIjp7Im9sZCI6W10sIm5ldyI6W119fQ==', 1780840543),
	('FS7ksHA7eZ357HNfNCkKIKn26mSmLAeLVKsolpbM', NULL, '192.168.1.62', 'Dalvik/2.1.0 (Linux; U; Android 16; CPH2687 Build/BP2A.250605.015)', 'eyJfdG9rZW4iOiJ5VHpDTlFQZmZsR3p5STlDUDRvamY0bllNSXpLcXR6ZXZ2cW5lRFJrIiwiX3ByZXZpb3VzIjp7InVybCI6Imh0dHA6XC9cLzE5Mi4xNjguMS42MTo4MDAwXC9maWxlLW1ha2FuYW5cL3Byb2R1a3NcLzE3NzgxNzA1MTkuanBnIiwicm91dGUiOm51bGx9LCJfZmxhc2giOnsib2xkIjpbXSwibmV3IjpbXX19', 1778327426),
	('fxqamPsuw25Ko5L2s2N4zzS4Bhw9jArc84OsW4xZ', NULL, '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/148.0.0.0 Safari/537.36', 'eyJfdG9rZW4iOiJvYVdkZU5uUmJUUEtRNWp6N25ONEFtcTFQVmFnUkJQdVRXNEFFajhaIiwiX3ByZXZpb3VzIjp7InVybCI6Imh0dHA6XC9cL2ZhZmFiaXRlLWFwaS50ZXN0Iiwicm91dGUiOm51bGx9LCJfZmxhc2giOnsib2xkIjpbXSwibmV3IjpbXX19', 1781006109),
	('hnb1afXGJiKd75YUq0EkqbtcUMocnP80N0IMjfVN', NULL, '10.76.65.90', 'Dalvik/2.1.0 (Linux; U; Android 16; CPH2687 Build/BP2A.250605.015)', 'eyJfdG9rZW4iOiI5c2xDQk1mRVJPUXRQaU5ucE5qM29mS1c1YW9yNGhHMXBxYkdxZWVSIiwiX3ByZXZpb3VzIjp7InVybCI6Imh0dHA6XC9cLzEwLjc2LjY1LjIzMjo4MDAwXC9maWxlLW1ha2FuYW5cL2ZvdG9fcHJvZHVrXC9VbjRhSjNBVFVjVmhKTnBtM1RTN0RsY2haVW5sZUc3bHNIaWR3WDIwLmpwZyIsInJvdXRlIjpudWxsfSwiX2ZsYXNoIjp7Im9sZCI6W10sIm5ldyI6W119fQ==', 1780841330),
	('hqVVOvXyUp0XtZBh8DFee1OcKSdfaV9H5MOCi05c', NULL, '192.168.1.62', 'Dalvik/2.1.0 (Linux; U; Android 16; CPH2687 Build/BP2A.250605.015)', 'eyJfdG9rZW4iOiJqQTVNUkhEQnBLdU9iSXh5N3VOc0k0MDVBR0xveWF3dVZDRTYzSWZOIiwiX3ByZXZpb3VzIjp7InVybCI6Imh0dHA6XC9cLzE5Mi4xNjguMS42MTo4MDAwXC9maWxlLW1ha2FuYW5cL3Byb2R1a3NcLzE3NzgxNzA1MTkuanBnIiwicm91dGUiOm51bGx9LCJfZmxhc2giOnsib2xkIjpbXSwibmV3IjpbXX19', 1778323779),
	('InAaRBuSqFTASeFODYGZv1PTfHLIiRCDCihf78uJ', NULL, '10.76.65.90', 'Dalvik/2.1.0 (Linux; U; Android 16; CPH2687 Build/BP2A.250605.015)', 'eyJfdG9rZW4iOiJMajJFZWpTc1A0NE5KZXlDMDhiUWYwTGU2NmN4T2FUNmt5NVMxUUgxIiwiX3ByZXZpb3VzIjp7InVybCI6Imh0dHA6XC9cLzEwLjc2LjY1LjIzMjo4MDAwXC9maWxlLW1ha2FuYW5cL2ZvdG9fcHJvZHVrXC9VbjRhSjNBVFVjVmhKTnBtM1RTN0RsY2haVW5sZUc3bHNIaWR3WDIwLmpwZyIsInJvdXRlIjpudWxsfSwiX2ZsYXNoIjp7Im9sZCI6W10sIm5ldyI6W119fQ==', 1780840833),
	('JizXLTFuae8f6LX322lBhG52QKZ4RQsWzaLkM3Z7', NULL, '10.76.65.90', 'Dalvik/2.1.0 (Linux; U; Android 16; CPH2687 Build/BP2A.250605.015)', 'eyJfdG9rZW4iOiJvZzRHUFA4Wk5aTm5pTnRVbHB6YVNuNjVydjhGRlE0RVFrYUZBTThXIiwiX3ByZXZpb3VzIjp7InVybCI6Imh0dHA6XC9cLzEwLjc2LjY1LjIzMjo4MDAwXC9maWxlLW1ha2FuYW5cL3Byb2R1a3NcLzE3NzgxNzIyOTUuanBnIiwicm91dGUiOm51bGx9LCJfZmxhc2giOnsib2xkIjpbXSwibmV3IjpbXX19', 1780841172),
	('jlpdfu7ryGVbiHdqwzcjveySQ99SCrzM1uTN97K1', NULL, '192.168.110.240', 'Dalvik/2.1.0 (Linux; U; Android 16; CPH2687 Build/BP2A.250605.015)', 'eyJfdG9rZW4iOiJKSUFvajdPZ3JvSklzcWJZR0puZ1FLYnlUaENzdUdEcDVTRjBQYW81IiwiX3ByZXZpb3VzIjp7InVybCI6Imh0dHA6XC9cLzE5Mi4xNjguMTEwLjE4OTo4MDAwXC9maWxlLW1ha2FuYW5cL2ZvdG9fcHJvZHVrXC9VbjRhSjNBVFVjVmhKTnBtM1RTN0RsY2haVW5sZUc3bHNIaWR3WDIwLmpwZyIsInJvdXRlIjpudWxsfSwiX2ZsYXNoIjp7Im9sZCI6W10sIm5ldyI6W119fQ==', 1780991866),
	('k9KKjvoVEVGQbMyqlbbT13JaXYCOhCLqpn3EslXt', NULL, '192.168.1.11', 'Dalvik/2.1.0 (Linux; U; Android 16; CPH2687 Build/BP2A.250605.015)', 'eyJfdG9rZW4iOiJPcmxLcDhEa1ZxeWwyaUozamtQMlhIUkpoWXJSNmtQSmt0UVdsVHBUIiwiX3ByZXZpb3VzIjp7InVybCI6Imh0dHA6XC9cLzE5Mi4xNjguMS42OjgwMDBcL2ZpbGUtbWFrYW5hblwvZm90b19wcm9kdWtcL1VuNGFKM0FUVWNWaEpOcG0zVFM3RGxjaFpVbmxlRzdsc0hpZHdYMjAuanBnIiwicm91dGUiOm51bGx9LCJfZmxhc2giOnsib2xkIjpbXSwibmV3IjpbXX19', 1778368788),
	('lPgEekgpIaq3J9r0VjtQPtNwqgEvaWmjjJ6Ex17t', NULL, '10.76.65.90', 'Dalvik/2.1.0 (Linux; U; Android 16; CPH2687 Build/BP2A.250605.015)', 'eyJfdG9rZW4iOiIwaGpKSkZ5dmtWSTVIZlM3S2d6TVM3NFFiakhROEJSMlhGdkpJMFlUIiwiX3ByZXZpb3VzIjp7InVybCI6Imh0dHA6XC9cLzEwLjc2LjY1LjIzMjo4MDAwXC9maWxlLW1ha2FuYW5cL3Byb2R1a3NcLzE3NzgxNzIyOTUuanBnIiwicm91dGUiOm51bGx9LCJfZmxhc2giOnsib2xkIjpbXSwibmV3IjpbXX19', 1780840552),
	('LRlzFiEknfPRPzwr8mUORNcazyifF8o6E2Sut8o5', NULL, '10.76.65.90', 'Dalvik/2.1.0 (Linux; U; Android 16; CPH2687 Build/BP2A.250605.015)', 'eyJfdG9rZW4iOiJhT244RDRsajMwTlVpREQ4MWN0NHhmR0tLSDltOXdxNjh6WThlcGJEIiwiX3ByZXZpb3VzIjp7InVybCI6Imh0dHA6XC9cLzEwLjc2LjY1LjIzMjo4MDAwXC9maWxlLW1ha2FuYW5cL3Byb2R1a3NcLzE3NzgxNzA1MTkuanBnIiwicm91dGUiOm51bGx9LCJfZmxhc2giOnsib2xkIjpbXSwibmV3IjpbXX19', 1780841330),
	('m9UO1WAuHh5g8sFXHqGSEjT1SXAlc36mQEPOMRYT', NULL, '192.168.1.62', 'Dalvik/2.1.0 (Linux; U; Android 16; CPH2687 Build/BP2A.250605.015)', 'eyJfdG9rZW4iOiJhcXVCVEJOaFZKVU9iOENGUlZYbk03MVFRT0xCejZab1VoRHQxQVEyIiwiX3ByZXZpb3VzIjp7InVybCI6Imh0dHA6XC9cLzE5Mi4xNjguMS42MTo4MDAwXC9maWxlLW1ha2FuYW5cL2ZvdG9fcHJvZHVrXC9VbjRhSjNBVFVjVmhKTnBtM1RTN0RsY2haVW5sZUc3bHNIaWR3WDIwLmpwZyIsInJvdXRlIjpudWxsfSwiX2ZsYXNoIjp7Im9sZCI6W10sIm5ldyI6W119fQ==', 1778331632),
	('mNs1CCBBTTFGhZI5QrXX8gyx2KsoYJCAR4Huafdl', NULL, '192.168.110.240', 'Dalvik/2.1.0 (Linux; U; Android 16; CPH2687 Build/BP2A.250605.015)', 'eyJfdG9rZW4iOiI3dVlzc1lHaHd6N01OMGpVWFZjUzdqd2kwUFIwbDhBR0NLb1E4cFlzIiwiX3ByZXZpb3VzIjp7InVybCI6Imh0dHA6XC9cLzE5Mi4xNjguMTEwLjE4OTo4MDAwXC9maWxlLW1ha2FuYW5cL3Byb2R1a3NcLzE3NzgxNzIyOTUuanBnIiwicm91dGUiOm51bGx9LCJfZmxhc2giOnsib2xkIjpbXSwibmV3IjpbXX19', 1780991866),
	('nAEugH1nlH7X0K5c1MHF8B9LHMU9EFHD0oykcd4V', NULL, '192.168.1.62', 'Dalvik/2.1.0 (Linux; U; Android 16; CPH2687 Build/BP2A.250605.015)', 'eyJfdG9rZW4iOiJwTk9VdkduRE5YUUtXRkFLMGVwdklMZVhMZkpETVp2OVJJdkZlVmc5IiwiX3ByZXZpb3VzIjp7InVybCI6Imh0dHA6XC9cLzE5Mi4xNjguMS42MTo4MDAwXC9maWxlLW1ha2FuYW5cL2ZvdG9fcHJvZHVrXC9VbjRhSjNBVFVjVmhKTnBtM1RTN0RsY2haVW5sZUc3bHNIaWR3WDIwLmpwZyIsInJvdXRlIjpudWxsfSwiX2ZsYXNoIjp7Im9sZCI6W10sIm5ldyI6W119fQ==', 1778332647),
	('ocVJ1MX3HzCRCTCmaSbuOg4OzrI9tnrkligiQBHb', NULL, '192.168.110.240', 'Dalvik/2.1.0 (Linux; U; Android 16; CPH2687 Build/BP2A.250605.015)', 'eyJfdG9rZW4iOiJBeUpGM1B0bFZmOVNXYUd1cllPc0ZSSWhxSEQwQ1FiT1JvZ1VMWk9QIiwiX3ByZXZpb3VzIjp7InVybCI6Imh0dHA6XC9cLzE5Mi4xNjguMTEwLjE4OTo4MDAwXC9maWxlLW1ha2FuYW5cL3Byb2R1a3NcLzE3NzgxNzA1MTkuanBnIiwicm91dGUiOm51bGx9LCJfZmxhc2giOnsib2xkIjpbXSwibmV3IjpbXX19', 1780991862),
	('oGCv0hHyMZCEYLjxohRPWDcsrs1lllrtO3DSBTGy', NULL, '192.168.1.62', 'Dalvik/2.1.0 (Linux; U; Android 16; CPH2687 Build/BP2A.250605.015)', 'eyJfdG9rZW4iOiJXQ3F3a0pYYzhMWUEzNk5UV0V0NDFOekpQOVRaTVFYWFJ6eGljR2hwIiwiX3ByZXZpb3VzIjp7InVybCI6Imh0dHA6XC9cLzE5Mi4xNjguMS42MTo4MDAwXC9maWxlLW1ha2FuYW5cL3Byb2R1a3NcLzE3NzgxNzIyOTUuanBnIiwicm91dGUiOm51bGx9LCJfZmxhc2giOnsib2xkIjpbXSwibmV3IjpbXX19', 1778323779),
	('oGe42Dbw8PxOCSq3yYuzwyeAxmvkEpQjCAoMLMqc', NULL, '10.76.65.90', 'Dalvik/2.1.0 (Linux; U; Android 16; CPH2687 Build/BP2A.250605.015)', 'eyJfdG9rZW4iOiJQZG1YdkNNTjR6blpGWDE4SmNERXRaZ3lkZ1ZmUEF6VXFBUHlycEMzIiwiX3ByZXZpb3VzIjp7InVybCI6Imh0dHA6XC9cLzEwLjc2LjY1LjIzMjo4MDAwXC9maWxlLW1ha2FuYW5cL3Byb2R1a3NcLzE3NzgxNzIyOTUuanBnIiwicm91dGUiOm51bGx9LCJfZmxhc2giOnsib2xkIjpbXSwibmV3IjpbXX19', 1780840624),
	('Opf5R8QuZy2gbnRmcJasuNIamDeuK9Yhkl3L0oHE', NULL, '192.168.1.11', 'Dalvik/2.1.0 (Linux; U; Android 16; CPH2687 Build/BP2A.250605.015)', 'eyJfdG9rZW4iOiJseTFuYzRudDQ2UUxtTHZNRFJkSDdHY2tENkNCMTJRWFB6cE9hQnlGIiwiX3ByZXZpb3VzIjp7InVybCI6Imh0dHA6XC9cLzE5Mi4xNjguMS42OjgwMDBcL2ZpbGUtbWFrYW5hblwvZm90b19wcm9kdWtcL1VuNGFKM0FUVWNWaEpOcG0zVFM3RGxjaFpVbmxlRzdsc0hpZHdYMjAuanBnIiwicm91dGUiOm51bGx9LCJfZmxhc2giOnsib2xkIjpbXSwibmV3IjpbXX19', 1778368416),
	('opVACb5LrCVn8NWsYi5PhFXuKUzzr5TFRWCIat3G', NULL, '192.168.1.62', 'Dalvik/2.1.0 (Linux; U; Android 16; CPH2687 Build/BP2A.250605.015)', 'eyJfdG9rZW4iOiJrM3pUT05rckNHTUJwNTg3azJSWG9LSmVaMnVVUmJQeDU0ZFpyMGNlIiwiX3ByZXZpb3VzIjp7InVybCI6Imh0dHA6XC9cLzE5Mi4xNjguMS42MTo4MDAwXC9maWxlLW1ha2FuYW5cL3Byb2R1a3NcLzE3NzgxNzA1MTkuanBnIiwicm91dGUiOm51bGx9LCJfZmxhc2giOnsib2xkIjpbXSwibmV3IjpbXX19', 1778332646),
	('oRPAVwuhYf2F6cOw13emvO6IAbgYSZD8W9BI98Mm', NULL, '10.76.65.90', 'Dalvik/2.1.0 (Linux; U; Android 16; CPH2687 Build/BP2A.250605.015)', 'eyJfdG9rZW4iOiJReWNwc3Rzb1gxNW4yTjVEb21TekV2Z3FDODNxWjJ2dk1SQ3NDS3ZhIiwiX3ByZXZpb3VzIjp7InVybCI6Imh0dHA6XC9cLzEwLjc2LjY1LjIzMjo4MDAwXC9maWxlLW1ha2FuYW5cL3Byb2R1a3NcLzE3NzgxNzIyOTUuanBnIiwicm91dGUiOm51bGx9LCJfZmxhc2giOnsib2xkIjpbXSwibmV3IjpbXX19', 1780840543),
	('oVRfm5sTsJPLRXLvLYvK4vMYXHY2IKhzsdOre0wC', NULL, '10.76.65.90', 'Dalvik/2.1.0 (Linux; U; Android 16; CPH2687 Build/BP2A.250605.015)', 'eyJfdG9rZW4iOiJvNzJvTXhTYU0xR3lDN3NJMkFxY0doWk1LNG13UnA5Q2k2S2J4WmV2IiwiX3ByZXZpb3VzIjp7InVybCI6Imh0dHA6XC9cLzEwLjc2LjY1LjIzMjo4MDAwXC9maWxlLW1ha2FuYW5cL3Byb2R1a3NcLzE3NzgxNzA1MTkuanBnIiwicm91dGUiOm51bGx9LCJfZmxhc2giOnsib2xkIjpbXSwibmV3IjpbXX19', 1780840568),
	('OzLmc5YPA22l9qFq8Pvz2vlyZ9PCfGz6vnycN8KC', NULL, '192.168.1.62', 'Dalvik/2.1.0 (Linux; U; Android 16; CPH2687 Build/BP2A.250605.015)', 'eyJfdG9rZW4iOiIxWk5DWlZURnFHSTNmM2FlN0s1OFZtYVFoQnhsNFlldUpHSVhodXVQIiwiX3ByZXZpb3VzIjp7InVybCI6Imh0dHA6XC9cLzE5Mi4xNjguMS42MTo4MDAwXC9maWxlLW1ha2FuYW5cL3Byb2R1a3NcLzE3NzgxNzA1MTkuanBnIiwicm91dGUiOm51bGx9LCJfZmxhc2giOnsib2xkIjpbXSwibmV3IjpbXX19', 1778326002),
	('Pk5jCbs2IT7GPODFoaCR6iTgHc7jRke9OASe07oC', NULL, '192.168.1.62', 'Dalvik/2.1.0 (Linux; U; Android 16; CPH2687 Build/BP2A.250605.015)', 'eyJfdG9rZW4iOiJaNHJ5ckV1ZFlDMUp5U1dmeUV0NlNPRkdOb0FNTjRiVEY0d0t0aVRCIiwiX3ByZXZpb3VzIjp7InVybCI6Imh0dHA6XC9cLzE5Mi4xNjguMS42MTo4MDAwXC9maWxlLW1ha2FuYW5cL3Byb2R1a3NcLzE3NzgxNzA1MTkuanBnIiwicm91dGUiOm51bGx9LCJfZmxhc2giOnsib2xkIjpbXSwibmV3IjpbXX19', 1778331436),
	('pl9BNBFYQLlk2pdwVkmvVPGd1inhxeJV7VIG7gPM', NULL, '10.76.65.90', 'Dalvik/2.1.0 (Linux; U; Android 16; CPH2687 Build/BP2A.250605.015)', 'eyJfdG9rZW4iOiJaSmtSc3d3andESWlnU1M1cVhFUU1YNDZKSjVKdUpPM1FRSjRqNXNNIiwiX3ByZXZpb3VzIjp7InVybCI6Imh0dHA6XC9cLzEwLjc2LjY1LjIzMjo4MDAwXC9maWxlLW1ha2FuYW5cL2ZvdG9fcHJvZHVrXC9VbjRhSjNBVFVjVmhKTnBtM1RTN0RsY2haVW5sZUc3bHNIaWR3WDIwLmpwZyIsInJvdXRlIjpudWxsfSwiX2ZsYXNoIjp7Im9sZCI6W10sIm5ldyI6W119fQ==', 1780841138),
	('PY3bB4HqHpE9JlynMrtDUFuogzN5GajvuJWeSdgn', NULL, '10.76.65.90', 'Dalvik/2.1.0 (Linux; U; Android 16; CPH2687 Build/BP2A.250605.015)', 'eyJfdG9rZW4iOiJ4REZwMEJPMGplT1JVOENscHkzcWM0eVpDWFVmUjlvdGdMcEYzQXVtIiwiX3ByZXZpb3VzIjp7InVybCI6Imh0dHA6XC9cLzEwLjc2LjY1LjIzMjo4MDAwXC9maWxlLW1ha2FuYW5cL3Byb2R1a3NcLzE3NzgxNzA1MTkuanBnIiwicm91dGUiOm51bGx9LCJfZmxhc2giOnsib2xkIjpbXSwibmV3IjpbXX19', 1780840543),
	('QoB7jjipccZ0lIHhNadNcW5egFdnahQY9CLaYA8h', NULL, '192.168.1.62', 'Dalvik/2.1.0 (Linux; U; Android 16; CPH2687 Build/BP2A.250605.015)', 'eyJfdG9rZW4iOiJrUnBDYlMxZWY5cTdXaG9tQ3NwNThLbFIyUHR1NHlXSE9yeGlJR0Y3IiwiX3ByZXZpb3VzIjp7InVybCI6Imh0dHA6XC9cLzE5Mi4xNjguMS42MTo4MDAwXC9maWxlLW1ha2FuYW5cL3Byb2R1a3NcLzE3NzgxNzA1MTkuanBnIiwicm91dGUiOm51bGx9LCJfZmxhc2giOnsib2xkIjpbXSwibmV3IjpbXX19', 1778331404),
	('qwBIs1bCb9YMPy3Or9MFI5VfZtdxTmrIJM1uphbD', NULL, '10.76.65.90', 'Dalvik/2.1.0 (Linux; U; Android 16; CPH2687 Build/BP2A.250605.015)', 'eyJfdG9rZW4iOiJVN09RU3pCUEQ3YzJPQkt2cFhiQ09xY3J5ZkRhZHIxUFp6UDVUQjdqIiwiX3ByZXZpb3VzIjp7InVybCI6Imh0dHA6XC9cLzEwLjc2LjY1LjIzMjo4MDAwXC9maWxlLW1ha2FuYW5cL3Byb2R1a3NcLzE3NzgxNzA1MTkuanBnIiwicm91dGUiOm51bGx9LCJfZmxhc2giOnsib2xkIjpbXSwibmV3IjpbXX19', 1780840623),
	('rcGkP6Ra5rOwl3uSfHsc9Oz9UbqHDPFJCehb38WU', NULL, '192.168.110.240', 'Dalvik/2.1.0 (Linux; U; Android 16; CPH2687 Build/BP2A.250605.015)', 'eyJfdG9rZW4iOiIyem5FZU1hOVQ2OEdVazg3WGdLM2VBZ1RETXlsa3pXblN6aGpGZ0ZOIiwiX3ByZXZpb3VzIjp7InVybCI6Imh0dHA6XC9cLzE5Mi4xNjguMTEwLjE4OTo4MDAwXC9maWxlLW1ha2FuYW5cL3Byb2R1a3NcLzE3NzgxNzIyOTUuanBnIiwicm91dGUiOm51bGx9LCJfZmxhc2giOnsib2xkIjpbXSwibmV3IjpbXX19', 1780991861),
	('RgCXrwm1lEXoj3bVq9Jb5NjkWwpOyfKnCpnf1zfk', NULL, '10.76.65.90', 'Dalvik/2.1.0 (Linux; U; Android 16; CPH2687 Build/BP2A.250605.015)', 'eyJfdG9rZW4iOiJpQXRSTTV5b2QzRElSdXg2UU9vemZLMkZtTEpNRVpMM0Z0anQyTkt1IiwiX3ByZXZpb3VzIjp7InVybCI6Imh0dHA6XC9cLzEwLjc2LjY1LjIzMjo4MDAwXC9maWxlLW1ha2FuYW5cL2ZvdG9fcHJvZHVrXC9VbjRhSjNBVFVjVmhKTnBtM1RTN0RsY2haVW5sZUc3bHNIaWR3WDIwLmpwZyIsInJvdXRlIjpudWxsfSwiX2ZsYXNoIjp7Im9sZCI6W10sIm5ldyI6W119fQ==', 1780841355),
	('RhU2pcM4mth4CJmAf9ajidpIe0NsDVaD9j3qaDy8', NULL, '192.168.1.62', 'Dalvik/2.1.0 (Linux; U; Android 16; CPH2687 Build/BP2A.250605.015)', 'eyJfdG9rZW4iOiJURlJ0RFdmaEcwMDM3OXZxYkV6ZmpCdkk0bENlS2pBcENoZnVqZmRPIiwiX3ByZXZpb3VzIjp7InVybCI6Imh0dHA6XC9cLzE5Mi4xNjguMS42MTo4MDAwXC9maWxlLW1ha2FuYW5cL2ZvdG9fcHJvZHVrXC9VbjRhSjNBVFVjVmhKTnBtM1RTN0RsY2haVW5sZUc3bHNIaWR3WDIwLmpwZyIsInJvdXRlIjpudWxsfSwiX2ZsYXNoIjp7Im9sZCI6W10sIm5ldyI6W119fQ==', 1778331403),
	('RlbOjRSBxSJ8QqwmgK134FlZCnIMbn8v2P6LdAIS', NULL, '10.76.65.90', 'Dalvik/2.1.0 (Linux; U; Android 16; CPH2687 Build/BP2A.250605.015)', 'eyJfdG9rZW4iOiJ0QjdjdWhsMnFGMUkyNldvTjJNVERKMWhpWjdNUGFmM0NKa1l1Q2l0IiwiX3ByZXZpb3VzIjp7InVybCI6Imh0dHA6XC9cLzEwLjc2LjY1LjIzMjo4MDAwXC9maWxlLW1ha2FuYW5cL3Byb2R1a3NcLzE3NzgxNzIyOTUuanBnIiwicm91dGUiOm51bGx9LCJfZmxhc2giOnsib2xkIjpbXSwibmV3IjpbXX19', 1780841330),
	('Rr2f42nVjKnE9UNRfBmw5LLLwreGSOdypltYZcZF', NULL, '10.76.65.90', 'Dalvik/2.1.0 (Linux; U; Android 16; CPH2687 Build/BP2A.250605.015)', 'eyJfdG9rZW4iOiI1V3lzckRKYUNrRHFrb1dsTE91YTZyNUZEZU95ZTNnQWMxUFYxR3hqIiwiX3ByZXZpb3VzIjp7InVybCI6Imh0dHA6XC9cLzEwLjc2LjY1LjIzMjo4MDAwXC9maWxlLW1ha2FuYW5cL2ZvdG9fcHJvZHVrXC9VbjRhSjNBVFVjVmhKTnBtM1RTN0RsY2haVW5sZUc3bHNIaWR3WDIwLmpwZyIsInJvdXRlIjpudWxsfSwiX2ZsYXNoIjp7Im9sZCI6W10sIm5ldyI6W119fQ==', 1780841172),
	('rXm4fu5hohNuPmb4BrcG4Xu8hn6qpOrXuUCdFWbT', NULL, '10.76.65.90', 'Dalvik/2.1.0 (Linux; U; Android 16; CPH2687 Build/BP2A.250605.015)', 'eyJfdG9rZW4iOiI3aHN3WnREVmJSbHkzTFpZZjhBVUNNTk9HcVVEM3FJWVFMTHZWNnpVIiwiX3ByZXZpb3VzIjp7InVybCI6Imh0dHA6XC9cLzEwLjc2LjY1LjIzMjo4MDAwXC9maWxlLW1ha2FuYW5cL3Byb2R1a3NcLzE3NzgxNzIyOTUuanBnIiwicm91dGUiOm51bGx9LCJfZmxhc2giOnsib2xkIjpbXSwibmV3IjpbXX19', 1780839758),
	('SlHrdiL2DUu3gew5ZLlpRY9f5R2jNAwaK7LwT10r', NULL, '192.168.1.62', 'Dalvik/2.1.0 (Linux; U; Android 16; CPH2687 Build/BP2A.250605.015)', 'eyJfdG9rZW4iOiJaWVNrNDREZnVnUDc1Vm5lNUF5SUdGb2ZGRXpieU92VG04SVVuR1RoIiwiX3ByZXZpb3VzIjp7InVybCI6Imh0dHA6XC9cLzE5Mi4xNjguMS42MTo4MDAwXC9maWxlLW1ha2FuYW5cL3Byb2R1a3NcLzE3NzgxNzA1MTkuanBnIiwicm91dGUiOm51bGx9LCJfZmxhc2giOnsib2xkIjpbXSwibmV3IjpbXX19', 1778330381),
	('TUx8sKRrb1hb1X9KsIEWoC5ogXDl7Qr8NWJA2zgi', NULL, '192.168.110.240', 'Dalvik/2.1.0 (Linux; U; Android 16; CPH2687 Build/BP2A.250605.015)', 'eyJfdG9rZW4iOiJlbTlOZVZidVhoc0hxUlA1aGpOZ3pmeERKN0RUVFJ3NDQ1ckdIdXlqIiwiX3ByZXZpb3VzIjp7InVybCI6Imh0dHA6XC9cLzE5Mi4xNjguMTEwLjE4OTo4MDAwXC9maWxlLW1ha2FuYW5cL2ZvdG9fcHJvZHVrXC9VbjRhSjNBVFVjVmhKTnBtM1RTN0RsY2haVW5sZUc3bHNIaWR3WDIwLmpwZyIsInJvdXRlIjpudWxsfSwiX2ZsYXNoIjp7Im9sZCI6W10sIm5ldyI6W119fQ==', 1780991862),
	('u0TcD3xj5fEyccTL0NZN1SiLHApnZZa0wyzqPRrc', NULL, '10.76.65.90', 'Dalvik/2.1.0 (Linux; U; Android 16; CPH2687 Build/BP2A.250605.015)', 'eyJfdG9rZW4iOiJUUm92ejZ5NkVKSkhjcGQwY0NHQ3VIRDFzOWI3bExOODJjaVltN2N6IiwiX3ByZXZpb3VzIjp7InVybCI6Imh0dHA6XC9cLzEwLjc2LjY1LjIzMjo4MDAwXC9maWxlLW1ha2FuYW5cL3Byb2R1a3NcLzE3NzgxNzIyOTUuanBnIiwicm91dGUiOm51bGx9LCJfZmxhc2giOnsib2xkIjpbXSwibmV3IjpbXX19', 1780841355),
	('UEL3C0evBm0Ku4jfhSoxl4u7K83MmxPPhuLxstJS', NULL, '192.168.1.62', 'Dalvik/2.1.0 (Linux; U; Android 16; CPH2687 Build/BP2A.250605.015)', 'eyJfdG9rZW4iOiIwMzJWdTZFaEJiRXdVbmNaTlc1SEJOYmhMb0V3Z3FWUVVNaVlaUno4IiwiX3ByZXZpb3VzIjp7InVybCI6Imh0dHA6XC9cLzE5Mi4xNjguMS42MTo4MDAwXC9maWxlLW1ha2FuYW5cL3Byb2R1a3NcLzE3NzgxNzIyOTUuanBnIiwicm91dGUiOm51bGx9LCJfZmxhc2giOnsib2xkIjpbXSwibmV3IjpbXX19', 1778326002),
	('uiONH2WyiSDO2zE2Jq4jr2yQVkDOjlhaOtXQAOOV', NULL, '192.168.1.62', 'Dalvik/2.1.0 (Linux; U; Android 16; CPH2687 Build/BP2A.250605.015)', 'eyJfdG9rZW4iOiJjNFNmZlpjb1d1M013SDMxVEVpanVUMlhxc1E5TUJzdXN0VEtwa0J3IiwiX3ByZXZpb3VzIjp7InVybCI6Imh0dHA6XC9cLzE5Mi4xNjguMS42MTo4MDAwXC9maWxlLW1ha2FuYW5cL3Byb2R1a3NcLzE3NzgxNzIyOTUuanBnIiwicm91dGUiOm51bGx9LCJfZmxhc2giOnsib2xkIjpbXSwibmV3IjpbXX19', 1778327426),
	('viqo2gqZHvVvz6nLfk0k8oFu4XlV45JtLhs15G5u', NULL, '10.76.65.90', 'Dalvik/2.1.0 (Linux; U; Android 16; CPH2687 Build/BP2A.250605.015)', 'eyJfdG9rZW4iOiIwS3FNS2kwU1NvT2wySFBqMEhNellXc1BaWWIxWWc3WmRGSHV3cFNRIiwiX3ByZXZpb3VzIjp7InVybCI6Imh0dHA6XC9cLzEwLjc2LjY1LjIzMjo4MDAwXC9maWxlLW1ha2FuYW5cL3Byb2R1a3NcLzE3NzgxNzIyOTUuanBnIiwicm91dGUiOm51bGx9LCJfZmxhc2giOnsib2xkIjpbXSwibmV3IjpbXX19', 1780840623),
	('vQFbRAOj8RTX4HSGAiYq7KSXD5BHTBllTt5iL54f', NULL, '10.76.65.90', 'Dalvik/2.1.0 (Linux; U; Android 16; CPH2687 Build/BP2A.250605.015)', 'eyJfdG9rZW4iOiJhZ1RlaGJOMnRKSGpnSWduaVJaNW1DZVdEV2dRUjFyR0RrNVFvR1VnIiwiX3ByZXZpb3VzIjp7InVybCI6Imh0dHA6XC9cLzEwLjc2LjY1LjIzMjo4MDAwXC9maWxlLW1ha2FuYW5cL2ZvdG9fcHJvZHVrXC9VbjRhSjNBVFVjVmhKTnBtM1RTN0RsY2haVW5sZUc3bHNIaWR3WDIwLmpwZyIsInJvdXRlIjpudWxsfSwiX2ZsYXNoIjp7Im9sZCI6W10sIm5ldyI6W119fQ==', 1780840552),
	('W3GMnqQ76vhrfqw3DXzTjhZP0zXBTU969yaSIXlH', NULL, '10.76.65.90', 'Dalvik/2.1.0 (Linux; U; Android 16; CPH2687 Build/BP2A.250605.015)', 'eyJfdG9rZW4iOiI0U3NGb1oxeG41WHQxdEl3a3NDbmk4VXV4Qm8zMVFqWDlzUXlTb3VTIiwiX3ByZXZpb3VzIjp7InVybCI6Imh0dHA6XC9cLzEwLjc2LjY1LjIzMjo4MDAwXC9maWxlLW1ha2FuYW5cL3Byb2R1a3NcLzE3NzgxNzA1MTkuanBnIiwicm91dGUiOm51bGx9LCJfZmxhc2giOnsib2xkIjpbXSwibmV3IjpbXX19', 1780841138),
	('W57dBwLc02X9wesdq39PC09T1eE6mEeRU16iTpdT', NULL, '192.168.1.11', 'Dalvik/2.1.0 (Linux; U; Android 16; CPH2687 Build/BP2A.250605.015)', 'eyJfdG9rZW4iOiJVM05jb25CcVRCSXlBT1laTHR2YWVaYVZlTGVvNHVVZTluYzRpVHFtIiwiX3ByZXZpb3VzIjp7InVybCI6Imh0dHA6XC9cLzE5Mi4xNjguMS42OjgwMDBcL2ZpbGUtbWFrYW5hblwvZm90b19wcm9kdWtcL1VuNGFKM0FUVWNWaEpOcG0zVFM3RGxjaFpVbmxlRzdsc0hpZHdYMjAuanBnIiwicm91dGUiOm51bGx9LCJfZmxhc2giOnsib2xkIjpbXSwibmV3IjpbXX19', 1778368853),
	('wCTuIqnLLpZYw6Gd4KV3Xgo55Ly52S7tpJPBKDlN', NULL, '192.168.1.62', 'Dalvik/2.1.0 (Linux; U; Android 16; CPH2687 Build/BP2A.250605.015)', 'eyJfdG9rZW4iOiJXdEhrQ09CSFp4TW5RQ0J1VkpGV0hWbTlkSkZWWnpVeHlSVzczSmRyIiwiX3ByZXZpb3VzIjp7InVybCI6Imh0dHA6XC9cLzE5Mi4xNjguMS42MTo4MDAwXC9maWxlLW1ha2FuYW5cL2ZvdG9fcHJvZHVrXC9VbjRhSjNBVFVjVmhKTnBtM1RTN0RsY2haVW5sZUc3bHNIaWR3WDIwLmpwZyIsInJvdXRlIjpudWxsfSwiX2ZsYXNoIjp7Im9sZCI6W10sIm5ldyI6W119fQ==', 1778330385),
	('wnrS5jyOn3Xb9jCW1LUZpR89Grn6Btmgng5M1XEC', NULL, '192.168.1.62', 'Dalvik/2.1.0 (Linux; U; Android 16; CPH2687 Build/BP2A.250605.015)', 'eyJfdG9rZW4iOiJ6MkV2Y3V2SUprSGQ2SzJ3RndpU3JFQjdNYU1seVVhbnhPT2tjVkoxIiwiX3ByZXZpb3VzIjp7InVybCI6Imh0dHA6XC9cLzE5Mi4xNjguMS42MTo4MDAwXC9maWxlLW1ha2FuYW5cL3Byb2R1a3NcLzE3NzgxNzA1MTkuanBnIiwicm91dGUiOm51bGx9LCJfZmxhc2giOnsib2xkIjpbXSwibmV3IjpbXX19', 1778331404),
	('xLk0159korcqy1OBoP97BgHp9FeHEvReMJqYyJej', NULL, '10.76.65.90', 'Dalvik/2.1.0 (Linux; U; Android 16; CPH2687 Build/BP2A.250605.015)', 'eyJfdG9rZW4iOiJ1YzVld0V6dW9JNEp2TW5YV3AwR2RyalBsbVJFeWtua0ZKem9BNWxyIiwiX3ByZXZpb3VzIjp7InVybCI6Imh0dHA6XC9cLzEwLjc2LjY1LjIzMjo4MDAwXC9maWxlLW1ha2FuYW5cL3Byb2R1a3NcLzE3NzgxNzIyOTUuanBnIiwicm91dGUiOm51bGx9LCJfZmxhc2giOnsib2xkIjpbXSwibmV3IjpbXX19', 1780841138),
	('xXiMBrhrWjOWDspLtXaC0CslLVVGZ5YQnnuHcfGg', NULL, '10.76.65.90', 'Dalvik/2.1.0 (Linux; U; Android 16; CPH2687 Build/BP2A.250605.015)', 'eyJfdG9rZW4iOiJGcUVOcTJEdjZET0ZYTWhlOWRkTm92SjdyY08zTVVQaU9tQ0RYOXdpIiwiX3ByZXZpb3VzIjp7InVybCI6Imh0dHA6XC9cLzEwLjc2LjY1LjIzMjo4MDAwXC9maWxlLW1ha2FuYW5cL2ZvdG9fcHJvZHVrXC9VbjRhSjNBVFVjVmhKTnBtM1RTN0RsY2haVW5sZUc3bHNIaWR3WDIwLmpwZyIsInJvdXRlIjpudWxsfSwiX2ZsYXNoIjp7Im9sZCI6W10sIm5ldyI6W119fQ==', 1780840568),
	('xXt2UHte90BTypYpTKAzAzjXXtTCFe0XmiEswbLh', NULL, '192.168.1.11', 'Dalvik/2.1.0 (Linux; U; Android 16; CPH2687 Build/BP2A.250605.015)', 'eyJfdG9rZW4iOiJIVFAzeldOYnBOa3ZqOWJ2aDNQdTVRREFOOVBjSWF5d3NRU1loWFJsIiwiX3ByZXZpb3VzIjp7InVybCI6Imh0dHA6XC9cLzE5Mi4xNjguMS42OjgwMDBcL2ZpbGUtbWFrYW5hblwvZm90b19wcm9kdWtcL1VuNGFKM0FUVWNWaEpOcG0zVFM3RGxjaFpVbmxlRzdsc0hpZHdYMjAuanBnIiwicm91dGUiOm51bGx9LCJfZmxhc2giOnsib2xkIjpbXSwibmV3IjpbXX19', 1778369075),
	('YBBVKXFbF5Q9Oh2Mmo4BBz9NRrn3j2ZkOcIHUnDw', NULL, '192.168.1.62', 'Dalvik/2.1.0 (Linux; U; Android 16; CPH2687 Build/BP2A.250605.015)', 'eyJfdG9rZW4iOiJCYXpnNDZ2emdQSjJTSEw3dm9ES2JaR2ozcnR5bkVrTDZiQjg5bDVMIiwiX3ByZXZpb3VzIjp7InVybCI6Imh0dHA6XC9cLzE5Mi4xNjguMS42MTo4MDAwXC9maWxlLW1ha2FuYW5cL3Byb2R1a3NcLzE3NzgxNzIyOTUuanBnIiwicm91dGUiOm51bGx9LCJfZmxhc2giOnsib2xkIjpbXSwibmV3IjpbXX19', 1778331404),
	('yZjKT3bTvJydcaSFy7qbR0rAc5NCc8iCjZDXbCED', NULL, '10.76.65.90', 'Dalvik/2.1.0 (Linux; U; Android 16; CPH2687 Build/BP2A.250605.015)', 'eyJfdG9rZW4iOiJSY0Q4cHRrQVpMQUtpN2Y4OHRnd29ocGRMY21md3Bka1dDbkMwV3VRIiwiX3ByZXZpb3VzIjp7InVybCI6Imh0dHA6XC9cLzEwLjc2LjY1LjIzMjo4MDAwXC9maWxlLW1ha2FuYW5cL3Byb2R1a3NcLzE3NzgxNzA1MTkuanBnIiwicm91dGUiOm51bGx9LCJfZmxhc2giOnsib2xkIjpbXSwibmV3IjpbXX19', 1780840833),
	('zcFL84fDGF9gOHyJu32sYOdz0OWZLAgryzPdnE83', NULL, '10.76.65.90', 'Dalvik/2.1.0 (Linux; U; Android 16; CPH2687 Build/BP2A.250605.015)', 'eyJfdG9rZW4iOiI3YmJmZTR0aVVHTFlKM0hYMjlaOUxwdlRkWFRjYWdEOVR6RjMyZUY5IiwiX3ByZXZpb3VzIjp7InVybCI6Imh0dHA6XC9cLzEwLjc2LjY1LjIzMjo4MDAwXC9maWxlLW1ha2FuYW5cL2ZvdG9fcHJvZHVrXC9VbjRhSjNBVFVjVmhKTnBtM1RTN0RsY2haVW5sZUc3bHNIaWR3WDIwLmpwZyIsInJvdXRlIjpudWxsfSwiX2ZsYXNoIjp7Im9sZCI6W10sIm5ldyI6W119fQ==', 1780840781),
	('zMHySaX22Mq7B4dmKxdguGf4sVCM88OJ942yKKFP', NULL, '10.76.65.90', 'Dalvik/2.1.0 (Linux; U; Android 16; CPH2687 Build/BP2A.250605.015)', 'eyJfdG9rZW4iOiJhdktkRFdnbTJpY0ppd3NTS3lNUUt2dDc2ZHM3TUpheFJDNWNmemNpIiwiX3ByZXZpb3VzIjp7InVybCI6Imh0dHA6XC9cLzEwLjc2LjY1LjIzMjo4MDAwXC9maWxlLW1ha2FuYW5cL3Byb2R1a3NcLzE3NzgxNzA1MTkuanBnIiwicm91dGUiOm51bGx9LCJfZmxhc2giOnsib2xkIjpbXSwibmV3IjpbXX19', 1780841172),
	('ZuNQ02n06iyKYIy84UdVVjP2nHcJYkDviiCU0Ywg', NULL, '10.76.65.90', 'Dalvik/2.1.0 (Linux; U; Android 16; CPH2687 Build/BP2A.250605.015)', 'eyJfdG9rZW4iOiJjcU9KZGNaQ0VDcVBrMXFuUlhLdjNFQ3lNR29QV0xldTRrM0JjQmVrIiwiX3ByZXZpb3VzIjp7InVybCI6Imh0dHA6XC9cLzEwLjc2LjY1LjIzMjo4MDAwXC9maWxlLW1ha2FuYW5cL3Byb2R1a3NcLzE3NzgxNzA1MTkuanBnIiwicm91dGUiOm51bGx9LCJfZmxhc2giOnsib2xkIjpbXSwibmV3IjpbXX19', 1780840781),
	('zvT64kPqIvuFPfiauzv0AiArlIvgsypiHI9z4Qg9', NULL, '192.168.1.11', 'Dalvik/2.1.0 (Linux; U; Android 16; CPH2687 Build/BP2A.250605.015)', 'eyJfdG9rZW4iOiI4enlTdFJSZEl1WFQwR1M4MVlFSmlzRjhpMGVDbnc4ODQ5V2ZzRFAxIiwiX3ByZXZpb3VzIjp7InVybCI6Imh0dHA6XC9cLzE5Mi4xNjguMS42OjgwMDBcL2ZpbGUtbWFrYW5hblwvZm90b19wcm9kdWtcL1VuNGFKM0FUVWNWaEpOcG0zVFM3RGxjaFpVbmxlRzdsc0hpZHdYMjAuanBnIiwicm91dGUiOm51bGx9LCJfZmxhc2giOnsib2xkIjpbXSwibmV3IjpbXX19', 1778368850);

-- Dumping structure for table db_foodrescue.tokos
CREATE TABLE IF NOT EXISTS `tokos` (
  `id_toko` bigint unsigned NOT NULL AUTO_INCREMENT,
  `id_user` bigint unsigned NOT NULL,
  `nama_toko` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `alamat` text COLLATE utf8mb4_unicode_ci,
  `latitude` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `longitude` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `jam_tutup` time DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT NULL,
  `saldo` int DEFAULT NULL,
  PRIMARY KEY (`id_toko`),
  KEY `tokos_id_user_foreign` (`id_user`),
  CONSTRAINT `tokos_id_user_foreign` FOREIGN KEY (`id_user`) REFERENCES `users` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Dumping data for table db_foodrescue.tokos: ~1 rows (approximately)
INSERT INTO `tokos` (`id_toko`, `id_user`, `nama_toko`, `alamat`, `latitude`, `longitude`, `jam_tutup`, `created_at`, `updated_at`, `saldo`) VALUES
	(1, 1, 'Yangsio Cafe and Resto', 'Medokan Ayu no. 24 Surabaya', NULL, NULL, NULL, '2026-05-06 06:53:45', '2026-06-09 19:48:16', NULL);

-- Dumping structure for table db_foodrescue.users
CREATE TABLE IF NOT EXISTS `users` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `email` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `foto_profil` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `role` enum('pembeli','penjual') COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'pembeli',
  `email_verified_at` timestamp NULL DEFAULT NULL,
  `password` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `remember_token` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT NULL,
  `saldo` int NOT NULL DEFAULT '100000',
  PRIMARY KEY (`id`),
  UNIQUE KEY `users_email_unique` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Dumping data for table db_foodrescue.users: ~2 rows (approximately)
INSERT INTO `users` (`id`, `name`, `email`, `foto_profil`, `role`, `email_verified_at`, `password`, `remember_token`, `created_at`, `updated_at`, `saldo`) VALUES
	(1, 'Yangsio Cafe and Resto', 'yangsiocafe@gmail.com', '1781039454_profil_terkompresi.jpg', 'penjual', NULL, '12345678', NULL, '2026-05-06 06:53:45', '2026-06-09 20:13:18', 103000),
	(4, 'Ihsanul Fikri', 'ihsanul@fafabite.com', '1781038416_profil_terkompresi.jpg', 'pembeli', NULL, '12345678', NULL, '2026-05-09 04:51:47', '2026-06-09 20:05:30', 150000);

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
