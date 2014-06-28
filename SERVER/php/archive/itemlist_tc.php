<?php
include 'simple_html_dom.php';

ini_set('memory_limit', '256M');

header('Content-Type: text/html; charset=utf-8');

$base = 'http://www3.consumer.org.hk/pricewatch/supermarket/?lang=tc';

$curl = curl_init();
curl_setopt($curl, CURLOPT_SSL_VERIFYPEER, FALSE);
curl_setopt($curl, CURLOPT_HEADER, false);
//curl_setopt($curl, CURLOPT_FOLLOWLOCATION, true);
curl_setopt($curl, CURLOPT_URL, $base);
curl_setopt($curl, CURLOPT_REFERER, $base);
curl_setopt($curl, CURLOPT_RETURNTRANSFER, TRUE);
$str = curl_exec($curl);
curl_close($curl);

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
		$code = null,
		$catagory = null,
		$brand = null,
		$name = null;

	public function __construct($code, $catagory, $brand, $name) {
		$this->code = $code;
		$this->catagory = $catagory;
		$this->brand = $brand;
		$this->name = $name;
    }

    public function jsonSerialize() {
	$return = array('code' => $this->code, 'category' => $this->category, 'brand' => $this->brand, 'name' => $this->name);
	return $return;
    }
}

// Create a DOM object
$html = new simple_html_dom();
// Load HTML from a string
$html->load($str);

$items = array();

// Find all items
$table = $html->find('table[width=945]', 0);

foreach($table->find('tr[!bgcolor]') as $row) {
	$code = $row->first_child()->first_child()->value;
	$catagory = $row->children(1)->plaintext;
	$brand = $row->children(2)->plaintext;
	$name = $row->children(3)->first_child()->plaintext;
	$item = new Item($code, $catagory, $brand, $name);
	$items[] = $item;
}

echo json_encode(new ItemList($items), JSON_UNESCAPED_UNICODE);

exit();

?>

