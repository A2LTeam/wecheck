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

// Create a DOM object
$html = new simple_html_dom();
// Load HTML from a string
$html->load($str);

$items = array();

// Find all items
$table = $html->find('table[width=945]', 0);

foreach($table->find('tr[!bgcolor]') as $row) {
	$code = $row->first_child()->first_child()->value;
	$category = $row->children(1)->plaintext;
	$brand = $row->children(2)->plaintext;
	$name = $row->children(3)->first_child()->plaintext;
	echo($code."#".$category."#".$brand."#".$name.";");
}

exit();

?>

