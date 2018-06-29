<?php
	include_once "koneksi.php";

	class usr{}
	
	$query = mysqli_query($con, "SELECT * FROM gedung");
	$query2 = mysqli_query($con, "SELECT * FROM fasilitas");
	
	$row = mysqli_fetch_all($query,MYSQLI_ASSOC);
	$row2 = mysqli_fetch_all($query2,MYSQLI_ASSOC);
	
	if (!empty($row)){
	$response = new usr();
	$response->success = 1;
	$response->message = "Data Berhasil";
	$response->data_gedung = $row;
	$response->data_fasilitas = $row2;
	die(json_encode($response));
		
	} else { 
	$response = new usr();
	$response->success = 0;
	$response->message = "Data Tidak Berhasil";
	die(json_encode($response));
	}
	
	mysqli_close($con);

?>