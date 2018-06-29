<?php
include_once "koneksi.php";

$nim = $_POST['nim'];
$id_gedung = $_POST['ID_Gedung'];
$keperluan = $_POST['Keperluan'];
$lama = $_POST['Lama_pinjam'];
$tanggal = $_POST['Tanggal_pinjam'];
$tambahan = $_POST['Tambahan'];
$fasArray = $_POST['array_fasilitas'];

class usr{}

$sql1 = "Insert Into peminjaman (NIM,ID_Gedung,Keperluan,Lama_pinjam,Tanggal_pinjam,Tambahan,Status) VALUES ('$nim','$id_gedung','$keperluan','$lama','$tanggal','$tambahan','diproses')";
$query1 = mysqli_query($con,$sql1);

if($query1){
	$sqlCount = "SELECT MAX(ID_Peminjam)as ID FROM peminjaman";
	$query2 = mysqli_query($con,$sqlCount);
	$last = mysqli_fetch_array($query2);
	$id_pinjam = $last['ID'];
	
	foreach($fasArray as $baris){
		$isi = json_decode($baris);
		$sql = "INSERT INTO detail_fasilitas(ID_Peminjam,ID_Fasilitas,Jumlah,Keterangan) VALUES ('$id_pinjam','$isi[0]','$isi[1]','')";
		$query3 = mysqli_query($con,$sql);
		if($query3){
			$berhasil = TRUE;
			$response = new usr();
			$response->success = 1;
			$response->message = "BERHASIL ".count($fasArray);
			die(json_encode($response));
		}else{
			$berhasil = FALSE;
			$response = new usr();
			$response->success = 0;
			$response->message = "GAGAL ".mysqli_error($con).count($fasArray);
			die(json_encode($response));
		}
	}
	
}else{

	$response = new usr();
	$response->success = 0;
	$response->message = "TIDAK BERHASIL".mysqli_error($con);
	die(json_encode($response));
}

mysqli_close($con);
?>