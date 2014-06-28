<?php
class Message implements JsonSerializable {
	
	public
		$id = null,
		$subject = null,
		$content = null;

	public function __construct($id, $subject, $content) {
		$this->id = $id;
		$this->subject = $subject;
        	$this->content = $content;
    }

    public function jsonSerialize() {
	$return = array('id' => $this->id, 'subject' => $this->subject, 'content' => $this->content);
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
	$result = mysqli_query($con,"SELECT * FROM message");
	while($row = mysqli_fetch_array($result)) {
		$message = new Message($row["id"], $row["subject"], $row["content"]);
		echo json_encode($message, JSON_PRETTY_PRINT);
	}
	mysqli_free_result($result);
}

mysqli_close($con);

?>
