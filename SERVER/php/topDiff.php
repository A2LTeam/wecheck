<?php
include 'simple_html_dom.php';

ini_set('memory_limit', '256M');
header('Content-Type: text/html; charset=utf-8');

set_error_handler("myErrorHandler");
function myErrorHandler($errno, $errstr, $errfile, $errline)
{
    return $errno;
}

function getPrice($str)
{
    $result = str_replace("$ ", "",$str);
    $result = str_replace("&nbsp;", "",$result);
    $result = str_replace("--", "",$result);
    return trim($result);
}

function getPriceDiff($str)
{
    $result = str_replace("&nbsp;", "",$str);
    return trim($result);
}

function getPercentage($str)
{
    $result = str_replace("&nbsp;", "",$str);
    return trim($result);
}

class TopDiffList implements JsonSerializable {

    public
        $topDiff = null;

    public function __construct($topDiffs) {
        $this->topDiff = $topDiffs;
    }

    public function jsonSerialize() {
        $return = array('topDiff' => $this->topDiff);
        return $return;
    }
}

class TopDiff implements JsonSerializable {

    public
        $itemCode = null,
        $priceWell = null,
        $pricePark = null,
        $priceMark = null,
        $priceAeon = null,
        $priceDchf = null,
        $priceDiff = null,
        $pricePercent = null,
        $lstUpdDt = null;

    public function __construct() {

    }

    public function jsonSerialize() {
        $return = array('itemCode' => $this->itemCode, 'priceWell' => $this->priceWell, 'pricePark' => $this->pricePark,
            'priceMark' => $this->priceMark, 'priceAeon' => $this->priceAeon, 'priceDchf' => $this->priceDchf,
            'priceDiff' => $this->priceDiff, 'pricePercent' => $this->pricePercent, 'lstUpdDt' => $this->lstUpdDt);
        return $return;
    }
}

// Create DOM from URL
$html = file_get_html("http://www3.consumer.org.hk/pricewatch/supermarket/topdiff.php?lang=en");

//$lastUpdate = str_replace("Last updated: ", "", $html->find('span[class=comment]', 0)->plaintext);

$table = $html->find('table[width=1005]', 0);
// process for each pricing
foreach ($table->find('tr') as $rows) {
    $tableheader = $rows->first_child()->class;
    if ($tableheader != 'tableheader') {

        $topDiff = new TopDiff();
        $itemCode = str_replace("detail.php?itemcode=", "", $rows->childNodes(3)->first_child()->href);
        $topDiff->itemCode = $itemCode;
        $topDiff->priceWell = getPrice($rows->childNodes(4)->plaintext);
        $topDiff->pricePark = getPrice($rows->childNodes(5)->plaintext);
        $topDiff->priceMark = getPrice($rows->childNodes(6)->plaintext);
        $topDiff->priceAeon = getPrice($rows->childNodes(7)->plaintext);
        $topDiff->priceDchf = getPrice($rows->childNodes(8)->plaintext);
        $topDiff->priceDiff = getPriceDiff($rows->childNodes(9)->plaintext);
        $topDiff->pricePercent = getPercentage($rows->childNodes(10)->plaintext);
        $topDiff->lstUpdDt = $rows->childNodes(11)->plaintext;

        $topDiffs[] = $topDiff;
    }
}

echo json_encode(new TopDiffList($topDiffs), JSON_UNESCAPED_UNICODE);

?>
