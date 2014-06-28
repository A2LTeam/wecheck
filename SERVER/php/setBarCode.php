<?php

header('Content-Type: text/html; charset=utf-8');

// Create connection
$con=mysqli_connect("mysql.serversfree.com","u363963258_test","password","u363963258_test");

// Check connection
if (mysqli_connect_errno()) {
	echo "Failed to connect to MySQL: " . mysqli_connect_error();
	exit();
}
else
{
	if (!mysqli_query($con,
		"UPDATE ITEM SET BARCODE = '".$_REQUEST['barCode']."' WHERE ITEM_CODE = '".$_REQUEST['itemCode']."'"))
	{
		echo("Error: ". mysqli_error($con));
	}	
	else
	{
		if (mysqli_affected_rows($con) == 0)
		{
			echo("No record affected");
		}
		else
		{
			echo("Success");
		}
	}
}

mysqli_close($con);

exit();
?>
