<?php
include_once "koneksi.php";

$nimOld = $_POST['nimOld'];
$nim = $_POST['nim'];
$name = $_POST['name'];
$noHP = $_POST['nohp'];
$alamat = $_POST['alamat'];
$jk = $_POST['jk'];

class usr{}

$sql1 = "SELECT * FROM pengguna WHERE NIM = '$nimOld'";
$query1 = mysqli_query($con,$sql1);

if(mysqli_num_rows($query1)==0){
	$response = new usr();
	$response->success = 0;
	$response->message = "Profil tidak berhasil diperbarui";
	die(json_encode($response));
}else{
	$sql = "UPDATE pengguna SET NIM = '$nim', nama_pengguna = '$name',noHp_pengguna = '$noHP', jk_pengguna = '$jk', alamat_pengguna = '$alamat' WHERE NIM = '$nimOld'";
	$query = mysqli_query($con,$sql);

	$response = new usr();
	$response->success = 1;
	$response->message = "Profil berhasil diperbarui";
	die(json_encode($response));
}

mysqli_close($con);
?>