<?php
//place this before any script you want to calculate time
$time_start = microtime(true);

ini_set('memory_limit', '256M');

// Ignore user aborts and allow the script
// to run forever
ignore_user_abort(true);

header('Content-Type: text/html; charset=utf-8');

$commands = file_get_contents("statement.sql");

// Create connection
$con=mysqli_connect("mysql.serversfree.com","u504115118_db","password","u504115118_db");

// Check connection
if (mysqli_connect_errno()) {
	print("Failed to connect to MySQL: " . mysqli_connect_error());
	exit();
}
else
{
	//convert to array
	$commands = explode(";", $commands);

	foreach($commands as $command){
		if(trim($command)){
			print("insert statement : $command<br>");
			mysqli_set_charset($con, 'utf8');
			mysqli_query($con, $command);
		}
	}

	mysqli_close($con);
}

rename("statement.sql", "statement-$time_start.sql" );

$time_end = microtime(true);
//dividing with 60 will give the execution time in minutes other wise seconds
$execution_time = ($time_end - $time_start);

//execution time of the script
echo '<b>Total Execution Time:</b> '.$execution_time.' sec';

?>
