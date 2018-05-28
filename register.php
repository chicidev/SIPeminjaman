<?php

	include_once "koneksi.php";

	class usr{}
	
	$nim = $_POST["nim"];
	$username = $_POST["username"];
	$password = $_POST["password"];
	$confirm_password = $_POST["confirm_password"];
	$noHP = $_POST["noHP"];
	$alamat = $_POST["alamat"];
	$jk = $_POST["jk"];
	

	if ((empty($nim))) {
	$response = new usr();
	$response->success = 0;
	$response->message = "Kolom nim tidak boleh kosong";
	die(json_encode($response));
	} 
	else if ((empty($username))) {
	$response = new usr();
	$response->success = 0;
	$response->message = "Kolom username tidak boleh kosong";
	die(json_encode($response));
	}
	else if ((empty($password))) {
	$response = new usr();
	$response->success = 0;
	$response->message = "Kolom password tidak boleh kosong";
	die(json_encode($response));
	} 
	else if ((empty($confirm_password)) || $password != $confirm_password) {
	$response = new usr();
	$response->success = 0;
	$response->message = "Konfirmasi password tidak sama";
	die(json_encode($response));
	} 
	else if ((empty($noHP))) {
	$response = new usr();
	$response->success = 0;
	$response->message = "Kolom no HP tidak boleh kosong";
	die(json_encode($response));
	}
	else if ((empty($alamat))) {
	$response = new usr();
	$response->success = 0;
	$response->message = "Kolom alamat tidak boleh kosong";
	die(json_encode($response));
	}
	else if ((empty($jk))) {
	$response = new usr();
	$response->success = 0;
	$response->message = "Kolom jenis kelamin tidak boleh kosong";
	die(json_encode($response));
	}
	else {
		if (!empty($nim) && $password == $confirm_password){
		$num_rows = mysqli_num_rows(mysqli_query($con, "SELECT * FROM pengguna WHERE NIM='".$nim."'"));

		if ($num_rows == 0){
			$query = mysqli_query($con, "INSERT INTO pengguna (NIM, nama_pengguna, noHp_pengguna, jk_pengguna, password,alamat_pengguna) VALUES('".$nim."','".$username."','".$noHP."','".$jk."','".$password."','".$alamat."')");

		 		if ($query){
		 			$response = new usr();
		 			$response->success = 1;
		 			$response->message = "Register berhasil, silahkan login.";
		 			die(json_encode($response));

		 		} else {
		 			$response = new usr();
		 			$response->success = 0;
		 			$response->message = "Username sudah ada";
		 			die(json_encode($response));
		 		}
		 	} else {
		 		$response = new usr();
		 		$response->success = 0;
		 		$response->message = "Username sudah ada";
		 		die(json_encode($response));
		 	}
		 }
	 }

	 mysqli_close($con);
?>	