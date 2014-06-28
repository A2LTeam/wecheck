<?php
include 'simple_html_dom.php';

class PricingList implements JsonSerializable {
	
	public
		$pricings = null;

	public function __construct($pricings) {
		$this->pricings = $pricings;
    }

    public function jsonSerialize() {
	$return = array('pricings' => $this->pricings);
	return $return;
    }
}

class Pricing implements JsonSerializable {
	
	public
		$shop = null,
		$date = null,
		$price = null;

	public function __construct($shop, $date, $price) {
		$this->shop = $shop;
		$this->date = $date;
        	$this->price = $price;
    }

    public function jsonSerialize() {
	$return = array('shop' => $this->shop, 'date' => $this->date, 'price' => $this->price);
	return $return;
    }
}

// Create DOM from URL
$html = file_get_html('http://www.consumer.org.hk/fc/txtver/b5_price_detail.php?itemcode='.$_REQUEST['itemCode']);

header('Content-Type: text/html; charset=utf-8');

$pricings = array();

// Find all pricings
foreach($html->find('div.detail_table') as $shop) {
	$pricings[] = new Pricing($shop->find('span', 0)->plaintext, 
		$shop->find('table', 0)->last_child()->first_child()->plaintext,
		$shop->find('table', 0)->last_child()->last_child()->plaintext);
}

echo json_encode(new PricingList($pricings), JSON_UNESCAPED_UNICODE);

exit();

?>
