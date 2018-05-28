<?php
	$server		= "localhost"; //sesuaikan dengan nama server
	$user		= "id5870773_root"; //sesuaikan username
	$password	= "12345"; //sesuaikan password
	$database	= "id5870773_peminjaman"; //sesuaikan target databese
	
	//$connect = mysql_connect($server, $user, $password) or die ("Koneksi gagal!");
//	mysql_select_db($database) or die ("Database belum siap!");

	/* ====== UNTUK MENGGUNAKAN MYSQLI DI UNREMARK YANG INI, YANG MYSQL_CONNECT DI REMARK ======= */
	$con = mysqli_connect($server, $user, $password, $database);
	 if (mysqli_connect_error()) {
		echo "Gagal terhubung MySQL: " . mysqli_connect_error();
	 }

?>