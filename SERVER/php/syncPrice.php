<?php
include 'simple_html_dom.php';

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
    global $config;
    
    if ( $config["debug"] == true )
    {
        print "<div style='text-align: center;'>";
        print "<h2 style='color: rgb(190, 50, 50);'>Exception Occured:</h2>";
        print "<table style='width: 800px; display: inline-block;'>";
        print "<tr style='background-color:rgb(230,230,230);'><th style='width: 80px;'>Type</th><td>" . get_class( $e ) . "</td></tr>";
        print "<tr style='background-color:rgb(240,240,240);'><th>Message</th><td>{$e->getMessage()}</td></tr>";
        print "<tr style='background-color:rgb(230,230,230);'><th>File</th><td>{$e->getFile()}</td></tr>";
        print "<tr style='background-color:rgb(240,240,240);'><th>Line</th><td>{$e->getLine()}</td></tr>";
        print "</table></div>";
    }
    else
    {
        $message = "Type: " . get_class( $e ) . "; Message: {$e->getMessage()}; File: {$e->getFile()}; Line: {$e->getLine()};";
        file_put_contents( $config["app_dir"] . "/tmp/logs/exceptions.log", $message . PHP_EOL, FILE_APPEND );
        header( "Location: {$config["error_page"]}" );
    }
    
    exit();
}

/**
 * Checks for a fatal error, work around for set_error_handler not working on fatal errors.
 */
function check_for_fatal()
{
    print("Connection status :".connection_status()."<br>");
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

print(">>> Start Synchronize Shop Price On Date '$today'<br>");

print(" >> Max Time : ".ini_get('max_execution_time')."<br>");

// Create connection
$con=mysqli_connect("mysql.serversfree.com","u363963258_test","password","u363963258_test");

// Check connection
if (mysqli_connect_errno()) {
	print("Failed to connect to MySQL: " . mysqli_connect_error());
	exit();
}
else
{
	// query unprocessed item
	$result = mysqli_query($con,"SELECT ID, ITEM_CODE FROM ITEM WHERE ID > IFNULL((SELECT MAX(ITEM_ID) FROM SHOP_ITEM WHERE EFF_DATE = '$today'), 0) ORDER BY ID LIMIT 100"); 
	
	print("# of item to process : $result->num_rows<br>");
	
	if (!empty($result)) {
	while($row = mysqli_fetch_array($result)) {

		$id = $row["ID"];
		$itemCode = $row["ITEM_CODE"];

		print("<br> >> Start processing item id: [$id] Code: [$itemCode]<br>");

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
//			$priceOther = $priceArray[1];

			if ($price != "--") {
				// get the shop id
//				print("Finding shop : $shopName<br>");
			
				$shopResult = mysqli_query($con,"SELECT ID FROM SHOP WHERE NAME_TC = '$shopName'");

				if (!empty($shopResult)) {
					while($shopList = mysqli_fetch_array($shopResult)) {
						$shopId = $shopList["ID"];
					}	

					// get the previous price
					$prevPrice = "null";
					
//					print("Finding Previous Price for Id : $id Date : $date<br>");	

					$prevPriceResult = mysqli_query($con,"SELECT PRICE FROM SHOP_ITEM WHERE ID = $id AND EFF_DATE < '$date' ORDER BY EFF_DATE DESC LIMIT 1");

					$prevPrice = "null";
					if (!empty($prevPriceResult)) {
						while($prevPriceList = mysqli_fetch_array($prevPriceResult)) {
							$prevPrice = $prevPriceList["PRICE"];
						}
						mysqli_free_result($prevPriceResult);
					}

					$insert = "INSERT INTO SHOP_ITEM (SHOP_ID, ITEM_ID, PRICE, EFF_DATE, PREV_PRICE) VALUES ($shopId,$id,$price,'$date',$prevPrice)";
	
//					print("Insert statement : $insert<br>");
	
					mysqli_query($con, $insert) or mysqli_error($con)." Q=".$insert;
				
					print("Statement executed.<br>");

				} else {
					print(" > Cannot find shop : $shopName<br>");
				}

				mysqli_free_result($shopResult);

			} else {
//				print("Price not found for shop : $shopName<br>");
			}
		}

//		print("Clean up memory.<br>");

		// clean up memory
		$html->clear(); 
		unset($html);
		
		print(" >> Completed processing item : $itemCode<br>");
	}
	}

	mysqli_free_result($result);
}

print(">>> Completed Synchronization");

} catch (Exception $e) {
    print 'Caught exception: '.  $e->getMessage(). "<br>";
}

mysqli_close($con);

?>