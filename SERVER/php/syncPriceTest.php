<?php
include 'simple_html_dom.php';

ini_set('memory_limit', '256M');
header('Content-Type: text/html; charset=utf-8');

try {

	date_default_timezone_set("Hongkong");
	$today = date('Y-m-d', time());

print(">>> Start Synchronize Shop Price On Date '$today'<br>");

print(" >> Max Time : ".ini_get('max_execution_time')."<br>");

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
	$result = mysqli_query($con,"SELECT ID, ITEM_CODE FROM ITEM ORDER BY ID LIMIT 1"); 
	
	print("# of item to process : $result->num_rows<br>");
	
	if (!empty($result)) {
	while($row = mysqli_fetch_array($result)) {

		$id = $row["ID"];
		$itemCode = $row["ITEM_CODE"];

		print("<br> >> Start processing item id: [$id] Code: [$itemCode]<br>");

		// Create DOM from URL
		$html = file_get_html("http://www.consumer.org.hk/fc/txtver/b5_price_detail.php?itemcode=P000000325");

		// process for each pricing
		foreach($html->find('div.detail_table') as $shop) {
			$shopName = trim($shop->first_child()->plaintext);
			$date = trim($shop->find('table', 0)->last_child()->first_child()->plaintext);
			$price = trim($shop->find('table', 0)->last_child()->last_child()->innertext);
			$price = trim(str_replace('$', '', $price));
			$price = trim(str_replace('<br/>', ' ', $price));

			$priceArray = explode(' ', $price);
			$price = $priceArray[0];
				if (sizeof($priceArray) > 1) {
				$priceOther = $priceArray[1];
			}

			if ($price != "--") {

				// get the shop id
				mysqli_set_charset($con, 'utf8');
				$shopResult = mysqli_query($con,"SELECT ID FROM SHOP WHERE NAME_TC = '$shopName'");

				print("Find shop by 'SELECT ID FROM SHOP WHERE NAME_TC = '$shopName''<br>");

				if (mysqli_num_rows($shopResult)) {
					while($shopList = mysqli_fetch_array($shopResult)) {

						$shopId = $shopList["ID"];

						// get the previous price
						$prevPrice = "null";
					
						// print("Finding Previous Price for Id : $id Date : $date<br>");	

						$prevPriceResult = mysqli_query($con,"SELECT PRICE FROM SHOP_ITEM WHERE ID = $id AND EFF_DATE < '$date' ORDER BY EFF_DATE DESC LIMIT 1");

						$prevPrice = "null";
						if (!empty($prevPriceResult)) {
							while($prevPriceList = mysqli_fetch_array($prevPriceResult)) {
								$prevPrice = $prevPriceList["PRICE"];
							}
							mysqli_free_result($prevPriceResult);
						}

						$insert = "INSERT INTO SHOP_ITEM (SHOP_ID, ITEM_ID, PRICE, EFF_DATE, PREV_PRICE) VALUES ($shopId,$id,$price,'$date',$prevPrice)";
	
						// print("Insert statement : $insert<br>");
	
						mysqli_query($con, $insert) or mysqli_error($con)." Q=".$insert;
				
						print("Record inserted.<br>");
					}
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
