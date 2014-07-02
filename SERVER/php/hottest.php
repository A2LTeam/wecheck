<?php
header('Content-Type: text/html; charset=utf-8');

class HotItemList implements JsonSerializable {
	
	public
		$hotItems = null;

	public function __construct($hotItems) {
		$this->hotItems = $hotItems;
    }

    public function jsonSerialize() {
	$return = array('hotItems' => $this->hotItems);
	return $return;
    }
}

class HotItem implements JsonSerializable {
	
	public
		$itemId = null,
		$shopId = null,
		$date = null,
	       	$price = null;

	public function __construct($itemId, $shopId, $date, $price) {
		$this->itemId = $itemId;
		$this->shopId = $shopId;
		$this->date = $date; 
		$this->price = $price;
    }

    public function jsonSerialize() {
	$return = array('itemId' => $this->itemId, 'shopId' => $this->shopId, 'date' => $this->date, 'price' => $this->price);
	return $return;
    }
}

$hotItems = array();

// Create connection
$con=mysqli_connect("mysql.serversfree.com","u363963258_test","password","u363963258_test");

// Check connection
if (mysqli_connect_errno()) {
	echo "Failed to connect to MySQL: " . mysqli_connect_error();
	exit();
}
else
{
	$result = mysqli_query($con,"SELECT * FROM SHOP_ITEM LIMIT 20");
	while($row = mysqli_fetch_array($result)) {
		$hotitem = new HotItem($row["ITEM_ID"], $row["SHOP_ID"], $row["EFF_DATE"], $row["PRICE"]);
		$hotitems[] = $hotitem; 
	} 

	mysqli_free_result($result);

}

mysqli_close($con);

echo json_encode(new HotItemList($hotitems), JSON_UNESCAPED_UNICODE);

exit();

?>
