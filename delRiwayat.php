<?php
include_once "koneksi.php";

$tanggal = $_POST['Tanggal_pinjam'];
$nim = $_POST['nim'];

class usr{}

$sql1 = "DELETE FROM peminjaman WHERE NIM = '$nim' AND Tanggal_pinjam = '$tanggal'";
$query1 = mysqli_query($con,$sql1);

if($query1){
	$response = new usr();
	$response->success = 1;
	$response->message = "Riwayat berhasil dihapus";
	die(json_encode($response));
}else{
	$response = new usr();
	$response->success = 0;
	$response->message = "Hapus Riwayat Gagal";
	die(json_encode($response));
}

mysqli_close($con);
?>