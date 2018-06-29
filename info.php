<?php
include_once 'koneksi.php';
$no = 1;

if(isset($_POST['submit'])){
	$tahun = $_POST['tahun'];
	$bulan = $_POST['bulan'];
	//var_dump($_POST);
	
	$sql = "SELECT * FROM (SELECT pengguna.nama_pengguna,peminjaman.Keperluan,peminjaman.Lama_pinjam,peminjaman.Tanggal_pinjam,gedung.Nama_Gedung FROM peminjaman,gedung,pengguna WHERE peminjaman.NIM = pengguna.NIM AND peminjaman.ID_Gedung = gedung.ID_Gedung) as kolom WHERE kolom.Tanggal_pinjam LIKE '$tahun-$bulan-%' ";
	//var_dump($sql);
$query = mysqli_query($con, $sql);
$result = mysqli_fetch_all($query,MYSQLI_ASSOC);

}else{
	$sql = "SELECT * FROM (SELECT pengguna.nama_pengguna,peminjaman.Keperluan,peminjaman.Lama_pinjam,peminjaman.Tanggal_pinjam,gedung.Nama_Gedung FROM peminjaman,gedung,pengguna WHERE peminjaman.NIM = pengguna.NIM AND peminjaman.ID_Gedung = gedung.ID_Gedung) as kolom WHERE kolom.Tanggal_pinjam LIKE '2018-06-%' ORDER BY kolom.Tanggal_pinjam ASC ";
	$query = mysqli_query($con, $sql);
$result = mysqli_fetch_all($query,MYSQLI_ASSOC);	
}
//var_dump($result);

?>
<html>
	<head>
		<style>
			table {
				width: 500px;
				margin-left: 10px;
				margin-right: 10px
			}
			th {
				background: #107aef;
				color: white;
			}
			table,th, td {
				border: 1px solid black;
				border-collapse: collapse;
			}
			th,td {
				padding : 5px;
			}
		</style>
	</head>
	<body>
		<form action="" method="post">
		<div style="float: right; margin-bottom: 20px">
			<font>Tahun : </font>
			<select name="tahun">
				<option value="2015">2015</option>
				<option value="2016">2016</option>
				<option value="2017">2017</option>
				<option value="2018" selected="">2018</option>
			</select>
			<font>Bulan :</font>
			<select name="bulan">
				<option value="01">Januari</option>
				<option value="02">Februari</option>
				<option value="03">Maret</option>
				<option value="04">April</option>
				<option value="05">Mei</option>
				<option value="06" selected="">Juni</option>
				<option value="07">Juli</option>
				<option value="08">Agustus</option>
				<option value="09">September</option>
				<option value="10">Oktober</option>
				<option value="11">November</option>
				<option value="12">Desember</option>
			</select>
			<button type="submit" name="submit" >Lihat</button>
		</div>
			
		</form>
		
		
		<?php 
		if(count($result)==0){ ?>
		<h1>
			Data tidak ditemukan
		</h1>
		<?php }else{ ?>
		<table>
			<tr>
				<th>No.</th>
				<th>Tanggal</th>
				<th>Gedung</th>
				<th>Keperluan</th>
				<th>Lama Pinjam</th>
				<th>Nama Peminjam</th>
			</tr>
		
		
		<?php foreach ($result as $row){ ?>
		
			<tr>
				<td align="center"><?php echo $no?></td>
				<td><?php echo $row['Tanggal_pinjam']; ?></td>
				<td><?php echo $row['Nama_Gedung']; ?></td>
				<td><?php echo $row['Keperluan']; ?></td>
				<td><?php echo $row['Lama_pinjam']." Jam"; ?></td>
				<td><?php echo $row['nama_pengguna']; ?></td>
			</tr>	
		<?php $no++; }} ?>
		</table>
	</body>
</html>