var stompClient = null;

function setConnected(connected) {
    $("#connect").prop("disabled", connected);
    $("#disconnect").prop("disabled", !connected);
    if (connected) {
        $("#conversation").show();
    }
    else {
        $("#conversation").hide();
    }
    $("#greetings").html("");
}

function connect() {
    var socket = new SockJS('/gs-guide-websocket');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        setConnected(true);
        console.log('Connected: ' + frame);
        stompClient.subscribe('/topic/move/1', function (greeting) {
            showGreeting(greeting.body)
        });
    });
}
function connect1() {
    var socket = new SockJS('/gs-guide-websocket');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        setConnected(true);
        console.log('Connected: ' + frame);
        stompClient.subscribe('/topic/move/2', function (greeting) {
            showGreeting(greeting.body)
        });
    });
}

function disconnect() {
    if (stompClient !== null) {
        stompClient.disconnect();
    }
    setConnected(false);
    console.log("Disconnected");
}

function sendName() {
    stompClient.send("/app/move/1", {}, JSON.stringify({'from': $("#name").val(), 'to' : 'to', 'color': 'black', 'gameId':'asdfa'}));
}
function sendName1() {
    stompClient.send("/app/move/2", {}, JSON.stringify({'from': $("#name").val(), 'to' : 'to', 'color': 'black', 'gameId':'asdfa'}));
}

function showGreeting(message) {

    $("#greetings").append("<tr><td>"+ message + "</td></tr>");
}

$(function () {
    $("form").on('submit', function (e) {
        e.preventDefault();
    });
    $( "#connect" ).click(function() { connect(); });
    $( "#connect1" ).click(function() { connect1(); });
    $( "#disconnect" ).click(function() { disconnect(); });
    $( "#send" ).click(function() { sendName(); });
    $( "#send1" ).click(function() { sendName1(); });
});