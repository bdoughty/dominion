const testSocket = new WebSocket("ws://" + location.hostname + ":" + location.port + "/socket");
let userId;
let messageFuntions = [];

const send = function(type, message) {
    testSocket.send(type + ":" + userId + ":" + message);
}

messageFuntions.userid = function (id) {
    document.cookie = "id = " + id;
    console.log("User ID Received: " + id);
};

const getCookie = function(name) {
    const value = "; " + document.cookie;
    const parts = value.split("; " + name + "=");
    if (parts.length == 2) return parts.pop().split(";").shift();
};

testSocket.onopen = function () {
    if (getCookie("id") == null) {
        testSocket.send("newid:");
    } else {
        userId = getCookie("id");
        testSocket.send("oldid:" + userId);
        console.log("User ID Remembered: " + userId);
    }
};

testSocket.onmessage = function (event) {
    const str = event.data;
    const semi = str.indexOf(':');
    const type = str.substring(0, semi);
    if(str.length > semi + 1) {
        const message = str.substring(semi + 1);
        messageFuntions[type](message);
    } else {
        messageFuntions[type]();
    }
};