const testSocket = new WebSocket("ws://localhost:4567/socket");
let userId;

const getCookie = function(name) {
    const value = "; " + document.cookie;
    const parts = value.split("; " + name + "=");
    if (parts.length == 2) return parts.pop().split(";").shift();
};

testSocket.onopen = function () {
    if (getCookie("id") == null) {
        testSocket.send("register:newid");
    } else {
        userId = getCookie("id");
        testSocket.send("register:" + userId);
        alert("User ID Remembered: " + userId);
    }
};

testSocket.onmessage = function (event) {
    const str = event.data;
    if (str.startsWith("userid")) {
        const userId = str.substring(7);
        document.cookie = "id" + userId;
        alert("User ID Received: " + userId);
    }
};