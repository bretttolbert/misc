var n; //n of puzzle, n+1 = number of tiles, sqrt(n+1) = number of rows
var moveCount; //number of moves made by user
var startTime; //used to record time user started solving the puzzle
var emptyTileId; //id of empty space, initially = n
var normalColor = "#6699cc"; //normal tile color
var hoverColor = "#88bbee"; //when mouse over movable tile
var emptyColor = "#ffffff"; //color of empty space

//removes onmouseover and onmouseout events for all table cells
function removeHover() {
	for (var i=0; i<n+1; i++) {
		document.getElementById(i).onmouseover = function() { };
		document.getElementById(i).onmouseout = function() { };
	}
}

//adds onmouseover and onmouseout events to table cells which are valid moves
function addHover() {
	for (var i=0; i<n+1; i++) {
		if (isValidMove(i)) {
			document.getElementById(i).onmouseover = 
				function() { this.style.backgroundColor = hoverColor; };
			document.getElementById(i).onmouseout = 
				function() { this.style.backgroundColor = normalColor; };
		}
	}
}

//returns true if a tile can immediately be moved to the empty square
function isValidMove(tileId) {
	tileId = parseInt(tileId);
	var row = Math.sqrt(n+1); //number of cells in a row
	return (tileId + row == emptyTileId ||
	tileId - row == emptyTileId ||
	(tileId + 1 == emptyTileId && emptyTileId % row != 0) ||
	tileId - 1 == emptyTileId && tileId % row != 0);
}

//moves a tile (only if the move is valid)
//caller: 0 = shuffle, 1 = player
function moveTile(tileId, caller) {
	tileId = parseInt(tileId);
	var tile = document.getElementById(tileId);
	var emptyTile = document.getElementById(emptyTileId);
	var tileValue = tile.innerHTML;
	if (isValidMove(tileId)) {
		emptyTile.innerHTML = tileValue;
		emptyTileId = parseInt(tileId);
		tile.innerHTML = "";
		//now make empty tile white
		tile.style.backgroundColor = emptyColor;
		emptyTile.style.backgroundColor = normalColor;
		if (caller == 1)
			moveCount++; //if caller was user and move made
	}
	removeHover();
	addHover();
	//only show complete msg if caller was player
	if (isComplete() && caller == 1)
		alert("Puzzle solved!\nTime: " + getElapsedTimeString()
			 + "\n" + moveCount + " moves made");
}

function getElapsedTimeString()
{
	var now = new Date();
	var s = Math.floor((now.getTime() - startTime.getTime()) / 1000);
	var h = Math.floor(s / 3600);
	s -= (h * 3600);
	var m = Math.floor(s / 60);
	s -= (m * 60);
	startTime = now;
	return pad(h) + ":" + pad(m) + ":" + pad(s);
}

function pad(n)
{
	n = n.toString();
	return (n.length == 1) ? "0" + n : n;
}

//returns true if the puzzle is complete
function isComplete() {
	var fail = false;
	for (var i=0; i<n; i++) {
		var tile = document.getElementById(i);
		if (parseInt(tile.innerHTML) != i+1)
			fail = true;
	}
	return !fail;
}

//makes a number (provided by the user) of random, valid moves
function shuffle() {
	var moves = prompt("How many random moves to make?" +
			+"\n(more moves typically means more difficult puzzle)","100");
	for (var i=0; i<moves; i++) {
		var tile = Math.floor(Math.random() * (n+1));
		if (isValidMove(tile, 0))
			moveTile(tile, 0);
		else
			i--; //no move made, so un-increment i
		moveCount = 0; //reset move count
		startTime = new Date(); //record start time
	}
}

//generates the table
function init() {
    //$(window).resize();
	var nselect = document.getElementById("nselect");
	n = parseInt(nselect.options[nselect.selectedIndex].text);
	startTime = new Date();
	moveCount = 0;
	emptyTileId = n;
	var str = "<table>";
	var row = Math.sqrt(n+1);
	for (var i=0; i<row; i++) {
		str += "<tr>";
		for (var j=0; j<row; j++) {
			str += "<td id='" + ((i*row)+j) + "'>" + ((i*row)+j+1) + "</td>";
		}
		str += "</tr>";
	}
	str += "</table>";
	document.getElementById("puzzle").innerHTML = str;
	var fontSize = Math.round((4/n)*100) + "px"; //higher n means smaller font
	for (var i=0; i<n+1; i++) {
		var tile = document.getElementById(i);
		tile.style.backgroundColor = normalColor;
		tile.style.fontSize = fontSize;
		tile.onclick = function() { moveTile(this.id, 1); };
	}
	//fix empty tile
	document.getElementById(n).innerHTML = "";
	document.getElementById(n).style.backgroundColor = emptyColor;
	addHover();
}