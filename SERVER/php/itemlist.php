<?php
header('Content-Type: text/html; charset=utf-8');

class ItemList implements JsonSerializable {
	
	public
		$items = null;

	public function __construct($items) {
		$this->items = $items;
    }

    public function jsonSerialize() {
	$return = array('items' => $this->items);
	return $return;
    }
}

class Item implements JsonSerializable {
	
	public
		$id = null,
		$itemCode = null,
		$brandEn = null,
		$nameEn = null,
		$brandTc = null,
		$nameTc = null,
		$brandSc = null,
		$nameSc = null;

    public function jsonSerialize() {
	    $return = array('id' => $this->id, 'itemCode' => $this->itemCode, 
		    'brandEn' => $this->brandEn, 'nameEn' => $this->nameEn, 
		    'brandTc' => $this->brandTc, 'nameTc' => $this->nameTc,
		    'brandSc' => $this->brandSc, 'nameSc' => $this->nameSc);
	return $return;
    }
}

// Create connection
$con=mysqli_connect("mysql.serversfree.com","u363963258_test","password","u363963258_test");

// Check connection
if (mysqli_connect_errno()) {
	echo "Failed to connect to MySQL: " . mysqli_connect_error();
	exit();
}
else
{
	$result = mysqli_query($con,"SELECT * FROM ITEM");
	while($row = mysqli_fetch_array($result)) {
		$item = new Item();
		$item->id = $row["ID"];
		$item->itemCode = $row["ITEM_CODE"];
		$item->brandEn = $row["BRAND_EN"];
	       	$item->nameEn = $row["NAME_EN"];
		$item->brandTc = $row["BRAND_TC"];
		$item->nameTc = $row["NAME_TC"];
		$item->brandSc = $row["BRAND_SC"];
		$item->nameSc = $row["NAME_SC"];
		$items[] = $item;
	}
	mysqli_free_result($result);
	
	echo json_encode(new ItemList($items), JSON_UNESCAPED_UNICODE);

}

mysqli_close($con);

exit();
?>
