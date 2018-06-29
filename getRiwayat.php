<?php
	include_once "koneksi.php";
	
	$id = $_POST['id'];

	class usr{}
	
	$query = mysqli_query($con, "SELECT * FROM (SELECT peminjaman.ID_Peminjam,pengguna.nama_pengguna,gedung.Nama_Gedung,peminjaman.Keperluan,peminjaman.Lama_pinjam,peminjaman.Tanggal_pinjam,peminjaman.Tambahan,peminjaman.Status,peminjaman.NIM FROM peminjaman,pengguna,gedung WHERE peminjaman.NIM = pengguna.NIM AND peminjaman.ID_Gedung = gedung.ID_Gedung) as a WHERE a.NIM = '$id'");
	
	$row = mysqli_fetch_all($query,MYSQLI_ASSOC);
	
	if (!empty($row)){
	$response = new usr();
	$response->success = 1;
	$response->message = "Data Berhasil";
	$response->data_riwayat = $row;
	die(json_encode($response));
		
	} else { 
	$response = new usr();
	$response->success = 0;
	$response->message = "Data Tidak Berhasil";
	die(json_encode($response));
	}
	
	mysqli_close($con);

?>