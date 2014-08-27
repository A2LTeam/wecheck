<?php
include 'simple_html_dom.php';

//place this before any script you want to calculate time
$time_start = microtime(true);

ini_set('memory_limit', '256M');

// Ignore user aborts and allow the script
// to run forever
ignore_user_abort(true);

header('Content-Type: text/html; charset=utf-8');

/**
 * Error handler, passes flow over the exception logger with new ErrorException.
 */
function log_error( $num, $str, $file, $line, $context = null )
{
    log_exception( new ErrorException( $str, 0, $num, $file, $line ) );
}

/**
 * Uncaught exception handler.
 */
function log_exception( Exception $e )
{
        print "<div style='text-align: center;'>";
        print "<h2 style='color: rgb(190, 50, 50);'>Exception Occured:</h2>";
        print "<table style='width: 800px; display: inline-block;'>";
        print "<tr style='background-color:rgb(230,230,230);'><th style='width: 80px;'>Type</th><td>" . get_class( $e ) . "</td></tr>";
        print "<tr style='background-color:rgb(240,240,240);'><th>Message</th><td>{$e->getMessage()}</td></tr>";
        print "<tr style='background-color:rgb(230,230,230);'><th>File</th><td>{$e->getFile()}</td></tr>";
        print "<tr style='background-color:rgb(240,240,240);'><th>Line</th><td>{$e->getLine()}</td></tr>";
        print "</table></div>";
    
    exit();
}

/**
 * Checks for a fatal error, work around for set_error_handler not working on fatal errors.
 */
function check_for_fatal()
{
    print("Connection status :".connection_status()."\r\n");
    $error = error_get_last();
    if ( $error["type"] == E_ERROR )
        log_error( $error["type"], $error["message"], $error["file"], $error["line"] );
}

register_shutdown_function( "check_for_fatal" );
set_error_handler( "log_error" );
set_exception_handler( "log_exception" );
ini_set( "display_errors", "on" );
error_reporting( E_ALL ); 

try {

	date_default_timezone_set("Hongkong");
	$today = date('Y-m-d', time());

print(">>> Start Synchronize Shop Price On Date '$today'\r\n");

print(" >> Max Time : ".ini_get('max_execution_time')."\r\n");

// Create connection
$con=mysqli_connect("localhost","wecheck","password","wecheck");

// Check connection
if (mysqli_connect_errno()) {
	print("Failed to connect to MySQL: " . mysqli_connect_error());
	exit();
}
else
{
	// query unprocessed item
	$result = mysqli_query($con,"SELECT ID, ITEM_CODE FROM ITEM WHERE ID > IFNULL((SELECT MAX(ITEM_ID) FROM SHOP_ITEM WHERE EFF_DATE = '$today'), 0) ORDER BY ID"); 
	
	print("# of item to process : $result->num_rows\r\n");
	
	if (!empty($result)) {
	while($row = mysqli_fetch_array($result)) {

		$id = $row["ID"];
		$itemCode = $row["ITEM_CODE"];

		print("\r\n >> Start processing item id: [$id] Code: [$itemCode]\r\n");

		// Create DOM from URL
		$html = file_get_html('http://www.consumer.org.hk/fc/txtver/b5_price_detail.php?itemcode='.$itemCode);

		// process for each pricing
		foreach($html->find('div.detail_table') as $shop) {
			$shopName = $shop->first_child()->plaintext;
			$date = $shop->find('table', 0)->last_child()->first_child()->plaintext;
			$price = $shop->find('table', 0)->last_child()->last_child()->innertext;
			$price = trim(str_replace('$', '', $price));
			$price = trim(str_replace('<br/>', ' ', $price));

			$priceArray = explode(' ', $price);
			$price = $priceArray[0];

			if ($price != "--") {
				// get the shop id
				// print("Finding shop : $shopName\r\n");
		
				mysqli_set_charset($con, 'utf8');	
				$shopResult = mysqli_query($con,"SELECT ID FROM SHOP WHERE NAME_TC = '$shopName'");

				if (!empty($shopResult)) {
					while($shopList = mysqli_fetch_array($shopResult)) {
						$shopId = $shopList["ID"];
					}	

					// get the previous price
					$prevPrice = "null";
					
//					print("Finding Previous Price for Id : $id Date : $date\r\n");	

					$prevPriceResult = mysqli_query($con,"SELECT PRICE FROM SHOP_ITEM WHERE ID = $id AND EFF_DATE < '$date' ORDER BY EFF_DATE DESC LIMIT 1");

					$prevPrice = "null";
					if (!empty($prevPriceResult)) {
						while($prevPriceList = mysqli_fetch_array($prevPriceResult)) {
							$prevPrice = $prevPriceList["PRICE"];
						}
						mysqli_free_result($prevPriceResult);
					}

					$priceOther = "null";
					if (sizeof($priceArray) > 1) {
						$priceOther = $priceArray[1];
					}

					$insert = "INSERT INTO SHOP_ITEM (SHOP_ID, ITEM_ID, PRICE, EFF_DATE, PREV_PRICE, PROMOTE) VALUES ($shopId,$id,$price,'$date',$prevPrice, $priceOther)";
	
//					print("Insert statement : $insert\r\n");
	
					mysqli_query($con, $insert) or mysqli_error($con)." Q=".$insert;
				
			//		print("Statement executed.\r\n");

				} else {
					print(" > ERROR ! Cannot find shop : [$shopName]\r\n");
				}

				mysqli_free_result($shopResult);

			} else {
//				print("Price not found for shop : $shopName\r\n");
			}
		}

//		print("Clean up memory.\r\n");

		// clean up memory
		$html->clear(); 
		unset($html);
		
		print(" >> Completed processing item : [$itemCode]\r\n");
	}
	}

	mysqli_free_result($result);
}

print(">>> Completed Synchronization\r\n");

} catch (Exception $e) {
    print 'Caught exception: '.  $e->getMessage(). "\r\n";
}

mysqli_close($con);

$time_end = microtime(true);
//dividing with 60 will give the execution time in minutes other wise seconds
$execution_time = ($time_end - $time_start);

//execution time of the script
echo '<b>Total Execution Time:</b> '.$execution_time.' sec';

?>
