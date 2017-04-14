webpackJsonp([1,4],{

/***/ 100:
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__angular_core__ = __webpack_require__(1);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1__chat_model__ = __webpack_require__(58);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_2__shared_user_id_service__ = __webpack_require__(17);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_3__shared_chatsocket_service__ = __webpack_require__(63);
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "a", function() { return ChatComponent; });
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
var __metadata = (this && this.__metadata) || function (k, v) {
    if (typeof Reflect === "object" && typeof Reflect.metadata === "function") return Reflect.metadata(k, v);
};




var ChatComponent = (function () {
    function ChatComponent(_userIdService, _chatSocketService) {
        this._userIdService = _userIdService;
        this._chatSocketService = _chatSocketService;
        this.chatModel = new __WEBPACK_IMPORTED_MODULE_1__chat_model__["a" /* Chat */]();
        this.chatModel.addMessage({
            name: 'Another',
            color: '#333444',
            message: 'Another Message message'
        });
        this.chatModel.addMessage({
            name: 'Testing',
            color: '#333444',
            message: 'Testing message'
        });
    }
    ChatComponent.prototype.ngOnInit = function () {
        var _this = this;
        this._chatSocketService.addListener('chat', function (message) {
            _this.chatModel.addMessage({ name: 'Testing', color: 'Testing', message: message });
        });
    };
    ChatComponent.prototype.onEnter = function (value) {
        this._chatSocketService.send('chat', value);
        this.currentMessage = "";
    };
    return ChatComponent;
}());
ChatComponent = __decorate([
    __webpack_require__.i(__WEBPACK_IMPORTED_MODULE_0__angular_core__["_13" /* Component */])({
        selector: 'dmn-chat',
        template: __webpack_require__(166),
        styles: [__webpack_require__(160)]
    }),
    __metadata("design:paramtypes", [typeof (_a = typeof __WEBPACK_IMPORTED_MODULE_2__shared_user_id_service__["a" /* UserIdService */] !== "undefined" && __WEBPACK_IMPORTED_MODULE_2__shared_user_id_service__["a" /* UserIdService */]) === "function" && _a || Object, typeof (_b = typeof __WEBPACK_IMPORTED_MODULE_3__shared_chatsocket_service__["a" /* ChatSocketService */] !== "undefined" && __WEBPACK_IMPORTED_MODULE_3__shared_chatsocket_service__["a" /* ChatSocketService */]) === "function" && _b || Object])
], ChatComponent);

var _a, _b;
//# sourceMappingURL=chat.component.js.map

/***/ }),

/***/ 101:
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "a", function() { return ClientGame; });
var ClientGame = (function () {
    function ClientGame(players, pileArray, turnList, ownPlayerId) {
        this.players = players;
        this.pileArray = pileArray;
        this.turnList = turnList;
        this.ownPlayerId = ownPlayerId;
        this.turn = 0;
        this.gamePhase = "action";
        this.holding = false;
        this.toBuy = [];
        this.deck = 10;
        this.hand = [];
        this.discardPile = 0;
        this.toSelect = false;
        this.toSelectHand = [];
        this.toSelectBoard = [];
    }
    ClientGame.prototype.getSelf = function () {
        return this.players[this.ownPlayerId];
    };
    ClientGame.prototype.getCurrentPlayer = function () {
        return this.players[this.turnList[this.turn]];
    };
    return ClientGame;
}());

//# sourceMappingURL=client-game.model.js.map

/***/ }),

/***/ 102:
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "a", function() { return Pile; });
var Pile = (function () {
    function Pile(cardId, cost, count) {
        this.cardId = cardId;
        this.cost = cost;
        this.count = count;
    }
    return Pile;
}());

//# sourceMappingURL=pile.model.js.map

/***/ }),

/***/ 103:
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "a", function() { return Player; });
var Player = (function () {
    function Player(id, color, name) {
        this.id = id;
        this.color = color;
        this.name = name;
        this.victoryPoints = 3;
        this.buys = 0;
        this.actions = 0;
        this.gold = 0;
    }
    Player.prototype.drawHand = function () { };
    return Player;
}());

//# sourceMappingURL=player.model.js.map

/***/ }),

/***/ 104:
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__lobby_lobby_component__ = __webpack_require__(62);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1__game_game_component__ = __webpack_require__(60);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_2__creation_page_creation_page_component__ = __webpack_require__(59);
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "a", function() { return appRoutes; });



var appRoutes = [
    {
        path: 'lobby',
        component: __WEBPACK_IMPORTED_MODULE_0__lobby_lobby_component__["a" /* LobbyComponent */]
    },
    {
        path: 'game',
        component: __WEBPACK_IMPORTED_MODULE_1__game_game_component__["a" /* GameComponent */]
    },
    {
        path: 'create',
        component: __WEBPACK_IMPORTED_MODULE_2__creation_page_creation_page_component__["a" /* CreationComponent */]
    },
    { path: '',
        redirectTo: '/lobby',
        pathMatch: 'full'
    },
    {
        path: '**',
        component: __WEBPACK_IMPORTED_MODULE_0__lobby_lobby_component__["a" /* LobbyComponent */]
    }
];
//# sourceMappingURL=routes.js.map

/***/ }),

/***/ 105:
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__environments_environment__ = __webpack_require__(64);
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "a", function() { return AbstractSocketService; });

var AbstractSocketService = (function () {
    function AbstractSocketService(_userIdService, endpoint) {
        var _this = this;
        this._userIdService = _userIdService;
        this.endpoint = endpoint;
        this.listeners = {};
        this.sock = new WebSocket("ws://" + location.hostname + ":" + __WEBPACK_IMPORTED_MODULE_0__environments_environment__["a" /* environment */].port + endpoint);
        this.addListener('userid', function (userId) {
            _userIdService.id = userId;
            console.log("Loaded id " + userId);
        });
        this.sock.onopen = function () {
            if (_this.getCookie("id") != null) {
                _this.userId = _this.getCookie("id");
                _this.sock.send("oldid:" + _this.userId);
            }
            else {
                _this.sock.send("newid:");
            }
        };
        this.sock.onmessage = function (event) {
            var str = event.data;
            var semi = str.indexOf(':');
            var type = str.substring(0, semi);
            if (_this.listeners[type]) {
                if (str.length > semi + 1) {
                    var message_1 = str.substring(semi + 1);
                    _this.listeners[type].forEach(function (func) { return func(message_1); });
                }
                else {
                    _this.listeners[type].forEach(function (func) { return func(); });
                }
            }
        };
    }
    AbstractSocketService.prototype.addListener = function (name, func) {
        if (!this.listeners[name]) {
            this.listeners[name] = [func];
        }
        else {
            this.listeners[name].push(func);
        }
    };
    AbstractSocketService.prototype.send = function (type, message) {
        this.sock.send(type + ":" + this._userIdService.id + ":" + message);
    };
    AbstractSocketService.prototype.getCookie = function (name) {
        var value = "; " + document.cookie;
        var parts = value.split("; " + name + "=");
        if (parts.length == 2)
            return parts.pop().split(";").shift();
    };
    return AbstractSocketService;
}());

// messageFuntions.userid = function (id) {
//   document.cookie = "id = " + id;
//   console.log("User ID Received: " + id);
// };
//# sourceMappingURL=socket.service.js.map

/***/ }),

/***/ 159:
/***/ (function(module, exports, __webpack_require__) {

exports = module.exports = __webpack_require__(14)();
// imports


// module
exports.push([module.i, "", ""]);

// exports


/*** EXPORTS FROM exports-loader ***/
module.exports = module.exports.toString();

/***/ }),

/***/ 160:
/***/ (function(module, exports, __webpack_require__) {

exports = module.exports = __webpack_require__(14)();
// imports


// module
exports.push([module.i, ".messages {\n  height: 100%;\n  display: -webkit-box;\n  display: -ms-flexbox;\n  display: flex;\n  -webkit-box-orient: vertical;\n  -webkit-box-direction: reverse;\n      -ms-flex-direction: column-reverse;\n          flex-direction: column-reverse;\n  padding: 0 14px 50px 14px;\n}\n\n.message-container {\n  overflow: autok;\n  overflow-x: hidden;\n}\n\n.message {\n  padding: 4px;\n  display: -webkit-box;\n  display: -ms-flexbox;\n  display: flex;\n}\n\n.name {\n  padding-right: 12px;\n  line-height: 20px;\n}\n\n.message-content {\n  line-height: 20px;\n  font-weight: 300;\n}\n.input-container {\n  position: absolute;\n  bottom: 0;\n  right: 0;\n  left: 0;\n}\n.input-container input {\n  width: 100%;\n  border: none;\n  font-size: 0.95em;\n  font-weight: 300;\n  background: rgba(50, 50, 50, 0.8);\n  color: white;\n  border-top: 1px solid #999;\n}\n", ""]);

// exports


/*** EXPORTS FROM exports-loader ***/
module.exports = module.exports.toString();

/***/ }),

/***/ 161:
/***/ (function(module, exports, __webpack_require__) {

exports = module.exports = __webpack_require__(14)();
// imports


// module
exports.push([module.i, "", ""]);

// exports


/*** EXPORTS FROM exports-loader ***/
module.exports = module.exports.toString();

/***/ }),

/***/ 162:
/***/ (function(module, exports, __webpack_require__) {

exports = module.exports = __webpack_require__(14)();
// imports


// module
exports.push([module.i, "html {\n  background: #338833;\n}\n", ""]);

// exports


/*** EXPORTS FROM exports-loader ***/
module.exports = module.exports.toString();

/***/ }),

/***/ 163:
/***/ (function(module, exports, __webpack_require__) {

exports = module.exports = __webpack_require__(14)();
// imports


// module
exports.push([module.i, ".lobby-panes {\n  position: relative;\n  z-index: 2;\n  display: -webkit-box;\n  display: -ms-flexbox;\n  display: flex;\n  height: 100%;\n  -webkit-box-orient: vertical;\n  -webkit-box-direction: normal;\n      -ms-flex-direction: column;\n          flex-direction: column;\n}\n.pane-row {\n  display: -webkit-box;\n  display: -ms-flexbox;\n  display: flex;\n  width: 100%;\n  height: 50%;\n}\n.pane {\n  width: 50%;\n  height: 100%;\n  padding: 10px;\n  position: relative;\n}\n.pane-content {\n  height: 100%;\n  box-shadow: 0 1px 2px rgba(0, 0, 0, 0.12), 0 1px 3px rgba(0, 0, 0, 0.24);\n  /*background: rgba(255, 255, 255, 0.9);*/\n  background: #FAFAFA;\n}\n\n.pane#header .pane-content {\n}\n.pane#header h1 {\n  color: white;\n  font-weight: 100;\n  font-size: 5em;\n  text-shadow: 0px 0px 10px rgba(0, 0, 0, 1);\n  text-align: center;\n  text-transform: uppercase;\n}\n\n.pane#game-listing .pane-content {\n\n}\n.header {\n  height: 70px;\n  padding: 10px;\n  -webkit-box-align: center;\n      -ms-flex-align: center;\n          align-items: center;\n  display: -webkit-box;\n  display: -ms-flexbox;\n  display: flex;\n}\n.header input {\n  -webkit-box-flex: 6;\n      -ms-flex-positive: 6;\n          flex-grow: 6;\n  margin-left: 30px;\n  -webkit-transition: background 0.15s;\n  transition: background 0.15s;\n  font-size: 1em;\n  /*border: none;*/\n  /*box-shadow: 0 1px 2px rgba(0, 0, 0, 0.12), 0 1px 3px rgba(0, 0, 0, 0.24);*/\n}\n.header button {\n  font-size: 0.75em;\n}\n\n.header .button-container {\n  -webkit-box-flex: 1;\n      -ms-flex-positive: 1;\n          flex-grow: 1;\n  text-align: center;\n}\n.header .button-container button {\n  margin: 0 10px;\n  display: inline-block;\n}\n\n.games {\n  margin: 0;\n  padding: 0;\n  font-size: 0.95em;\n}\n.game {\n  font-weight: 300;\n  min-height: 50px;\n  display: -webkit-box;\n  display: -ms-flexbox;\n  display: flex;\n  -webkit-box-align: center;\n      -ms-flex-align: center;\n          align-items: center;\n  -webkit-box-pack: justify;\n      -ms-flex-pack: justify;\n          justify-content: space-between;\n  padding: 0 20px;\n  border-bottom: 1px solid #CCC;\n  background: white;\n  -webkit-transition: background 0.15s;\n  transition: background 0.15s;\n}\n.game:first-child {\n  border-top: 1px solid #CCC;\n}\n.game:hover {\n  background: #FAFAFA;\n  cursor: pointer;\n}\n\n.game-name {\n\n}\n\n.players {\n\n}\n\n.game .cards {\n  display: -webkit-box;\n  display: -ms-flexbox;\n  display: flex;\n}\n.game .card {\n  /* TODO: Replace with real image */\n  width: 17px;\n  height: 24px;\n  background: #222;\n  border-radius: 2px;\n  margin: 0 2px;\n}\n\n.pane#chat .pane-content {\n  background: rgba(30, 30, 30, 0.7);\n  border: 1px solid #BBB;\n  color: white;\n  position: relative;\n}\n\n\n.pane#game-info .pane-content {\n  display: -webkit-box;\n  display: -ms-flexbox;\n  display: flex;\n  padding: 32px;\n  -webkit-box-orient: vertical;\n  -webkit-box-direction: normal;\n      -ms-flex-direction: column;\n          flex-direction: column;\n  -ms-flex-pack: distribute;\n      justify-content: space-around;\n}\n#game-info .game-info-header {\n  display: -webkit-box;\n  display: -ms-flexbox;\n  display: flex;\n  padding: 0 12px;\n  -webkit-box-pack: justify;\n      -ms-flex-pack: justify;\n          justify-content: space-between;\n  -webkit-box-align: center;\n      -ms-flex-align: center;\n          align-items: center;\n}\n#game-info .game-name {\n  font-size: 1.4em;\n}\n#game-info .join-status {\n  font-size: 0.9em;\n}\n\n#game-info .players-list {\n  display: -webkit-box;\n  display: -ms-flexbox;\n  display: flex;\n  padding: 0 20px;\n}\n#game-info .players-list-title {\n  padding-right: 20px;\n  text-transform: uppercase;\n  line-height: 28px;\n  font-size: 0.95em;\n}\n#game-info .players {\n  font-weight: 300;\n}\n#game-info .player {\n  line-height: 28px;\n}\n\n#game-info .join-game {\n  padding-left: 40px;\n}\n#game-info .join-game button {\n  font-size: 0.8em;\n  min-width: 150px;\n  height: 40px;\n}\n#game-info .cards {\n  display: -webkit-box;\n  display: -ms-flexbox;\n  display: flex;\n  -webkit-box-pack: justify;\n      -ms-flex-pack: justify;\n          justify-content: space-between;\n}\n#game-info .card {\n  width: 50px;\n  height: 80px;\n  margin: 4px;\n  background: #222;\n}\n#game-info .card:hover {\n  cursor: pointer;\n}\n", ""]);

// exports


/*** EXPORTS FROM exports-loader ***/
module.exports = module.exports.toString();

/***/ }),

/***/ 165:
/***/ (function(module, exports) {

module.exports = "<router-outlet></router-outlet>\n\n"

/***/ }),

/***/ 166:
/***/ (function(module, exports) {

module.exports = "<div class=\"messages\">\n  <div class=\"message-container\">\n    <div class=\"message\" *ngFor=\"let message of chatModel.messages\">\n      <div class=\"name\" [style.color]=\"message.color\">\n        {{ message.name }}\n      </div>\n      <div class=\"message-content\">\n        {{ message.message }}\n      </div>\n    </div>\n  </div>\n</div>\n<div class=\"input-container\">\n  <input #chatInput\n         type=\"text\"\n         (keyup.enter)=\"onEnter(chatInput.value)\"\n         [(ngModel)]=\"currentMessage\"/>\n</div>\n"

/***/ }),

/***/ 167:
/***/ (function(module, exports) {

module.exports = "Creation page\n"

/***/ }),

/***/ 168:
/***/ (function(module, exports) {

module.exports = "<div id=\"game\">\n  This is where the game will be\n</div>\n\n"

/***/ }),

/***/ 169:
/***/ (function(module, exports) {

module.exports = "<div class=\"lobby-panes\">\n  <div class=\"pane-row\">\n    <div class=\"pane\" id=\"header\"><h1>Dominion</h1></div>\n    <div class=\"pane\" id=\"game-listing\">\n      <div class=\"pane-content\">\n        <div class=\"header\">\n          <input type=\"text\"/>\n          <div class=\"button-container\">\n            <button>Create Game</button>\n          </div>\n        </div>\n        <ul class=\"games\">\n          <li class=\"game\">\n            <div class=\"game-name\">Awesome_Gam32</div>\n            <div class=\"players\">3/4 Players</div>\n            <div class=\"cards\">\n              <div class=\"card\"></div>\n              <div class=\"card\"></div>\n              <div class=\"card\"></div>\n              <div class=\"card\"></div>\n              <div class=\"card\"></div>\n              <div class=\"card\"></div>\n              <div class=\"card\"></div>\n              <div class=\"card\"></div>\n              <div class=\"card\"></div>\n              <div class=\"card\"></div>\n            </div>\n          </li>\n          <li class=\"game\">\n            <div class=\"game-name\">Awesome_Gam32</div>\n            <div class=\"players\">3/4 Players</div>\n            <div class=\"cards\">\n              <div class=\"card\"></div>\n              <div class=\"card\"></div>\n              <div class=\"card\"></div>\n              <div class=\"card\"></div>\n              <div class=\"card\"></div>\n              <div class=\"card\"></div>\n              <div class=\"card\"></div>\n              <div class=\"card\"></div>\n              <div class=\"card\"></div>\n              <div class=\"card\"></div>\n            </div>\n          </li>\n          <li class=\"game\">\n            <div class=\"game-name\">Awesome_Gam32</div>\n            <div class=\"players\">3/4 Players</div>\n            <div class=\"cards\">\n              <div class=\"card\"></div>\n              <div class=\"card\"></div>\n              <div class=\"card\"></div>\n              <div class=\"card\"></div>\n              <div class=\"card\"></div>\n              <div class=\"card\"></div>\n              <div class=\"card\"></div>\n              <div class=\"card\"></div>\n              <div class=\"card\"></div>\n              <div class=\"card\"></div>\n            </div>\n          </li>\n          <li class=\"game\">\n            <div class=\"game-name\">Awesome_Gam32</div>\n            <div class=\"players\">3/4 Players</div>\n            <div class=\"cards\">\n              <div class=\"card\"></div>\n              <div class=\"card\"></div>\n              <div class=\"card\"></div>\n              <div class=\"card\"></div>\n              <div class=\"card\"></div>\n              <div class=\"card\"></div>\n              <div class=\"card\"></div>\n              <div class=\"card\"></div>\n              <div class=\"card\"></div>\n              <div class=\"card\"></div>\n            </div>\n          </li>\n        </ul>\n      </div>\n    </div>\n  </div>\n  <div class=\"pane-row\">\n    <div class=\"pane\" id=\"chat\">\n      <div class=\"pane-content\">\n        <dmn-chat></dmn-chat>\n      </div>\n    </div>\n\n    <!--<div class=\"pane\" id=\"chat\">-->\n      <!--<div class=\"pane-content\">-->\n        <!--<div class=\"messages\">-->\n          <!--<div class=\"message\">-->\n            <!--<div class=\"name\">-->\n              <!--Guest-->\n            <!--</div>-->\n            <!--<div class=\"message-content\">-->\n              <!--Hola-->\n            <!--</div>-->\n          <!--</div>-->\n          <!--<div class=\"message\">-->\n            <!--<div class=\"name\">-->\n              <!--Guest-->\n            <!--</div>-->\n            <!--<div class=\"message-content\">-->\n              <!--Ut non bibendum velit. Phasellus non vulputate ipsum, sit amet pellentesque nulla.-->\n              <!--Maecenas at turpis volutpat, mattis nunc eu, gravida purus. In sit amet dui leo.-->\n            <!--</div>-->\n          <!--</div>-->\n          <!--<div class=\"message\">-->\n            <!--<div class=\"name\">-->\n              <!--xXxB3n_tha_GodxXx-->\n            <!--</div>-->\n            <!--<div class=\"message-content\">-->\n              <!--Hey-->\n            <!--</div>-->\n          <!--</div>-->\n        <!--</div>-->\n        <!--<div class=\"input-container\">-->\n          <!--<input type=\"text\"/>-->\n        <!--</div>-->\n      <!--</div>-->\n    <!--</div>-->\n    <div class=\"pane\" id=\"game-info\">\n      <div class=\"pane-content\">\n        <div class=\"game-info-header\">\n          <div class=\"game-name\">\n            Awesome_Gam32\n          </div>\n          <div class=\"join-status\">\n            Joined, waiting for users...\n          </div>\n        </div>\n        <div class=\"players-list\">\n          <div class=\"players-list-title\">\n            Players\n          </div>\n          <div class=\"players\">\n            <div class=\"player\">\n              xXxB3n_tha_GodxXx\n            </div>\n            <div class=\"player\">\n              x.X.D.a.|.G.3.n.3.n.3.r.4.a.l.X.x\n            </div>\n            <div class=\"player\">\n              x.X.D.a.|.G.3.n.3.n.3.r.4.a.l.X.x\n            </div>\n            <div class=\"player\">\n              &quot;); DROP TABLE users; --\n            </div>\n          </div>\n        </div>\n        <div class=\"join-game\">\n          <button>Join Game</button>\n        </div>\n        <div class=\"cards\">\n          <div class=\"card\"></div>\n          <div class=\"card\"></div>\n          <div class=\"card\"></div>\n          <div class=\"card\"></div>\n          <div class=\"card\"></div>\n          <div class=\"card\"></div>\n          <div class=\"card\"></div>\n          <div class=\"card\"></div>\n          <div class=\"card\"></div>\n          <div class=\"card\"></div>\n        </div>\n      </div>\n    </div>\n  </div>\n</div>\n"

/***/ }),

/***/ 17:
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__angular_core__ = __webpack_require__(1);
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "a", function() { return UserIdService; });
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};

var UserIdService = (function () {
    function UserIdService() {
        this._id = "";
    }
    Object.defineProperty(UserIdService.prototype, "id", {
        get: function () {
            return this._id;
        },
        set: function (id) {
            this._id = id;
        },
        enumerable: true,
        configurable: true
    });
    return UserIdService;
}());
UserIdService = __decorate([
    __webpack_require__.i(__WEBPACK_IMPORTED_MODULE_0__angular_core__["c" /* Injectable */])()
], UserIdService);

//# sourceMappingURL=user-id.service.js.map

/***/ }),

/***/ 200:
/***/ (function(module, exports, __webpack_require__) {

module.exports = __webpack_require__(90);


/***/ }),

/***/ 58:
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "a", function() { return Chat; });
var Chat = (function () {
    function Chat() {
        this.messages = [];
    }
    Chat.prototype.addMessageFromComponents = function (color, message, name) {
        this.messages.push({
            color: color,
            message: message,
            name: name
        });
    };
    Chat.prototype.addMessage = function (chatItem) {
        this.messages.push(chatItem);
    };
    return Chat;
}());

//# sourceMappingURL=chat.model.js.map

/***/ }),

/***/ 59:
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__angular_core__ = __webpack_require__(1);
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "a", function() { return CreationComponent; });
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};

var CreationComponent = (function () {
    function CreationComponent() {
        this.title = 'app works!';
    }
    return CreationComponent;
}());
CreationComponent = __decorate([
    __webpack_require__.i(__WEBPACK_IMPORTED_MODULE_0__angular_core__["_13" /* Component */])({
        selector: 'dmn-creation',
        template: __webpack_require__(167),
        styles: [__webpack_require__(161)]
    })
], CreationComponent);

//# sourceMappingURL=creation-page.component.js.map

/***/ }),

/***/ 60:
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__angular_core__ = __webpack_require__(1);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1__shared_user_id_service__ = __webpack_require__(17);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_2__chat_chat_model__ = __webpack_require__(58);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_3__models_player_model__ = __webpack_require__(103);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_4__models_pile_model__ = __webpack_require__(102);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_5__models_client_game_model__ = __webpack_require__(101);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_6__game_service__ = __webpack_require__(61);
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "a", function() { return GameComponent; });
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
var __metadata = (this && this.__metadata) || function (k, v) {
    if (typeof Reflect === "object" && typeof Reflect.metadata === "function") return Reflect.metadata(k, v);
};







var GameComponent = (function () {
    function GameComponent(_userIdService, _gameService) {
        this._userIdService = _userIdService;
        this._gameService = _gameService;
        this.title = 'Dominion';
        this.game = null;
        this.gameChat = new __WEBPACK_IMPORTED_MODULE_2__chat_chat_model__["a" /* Chat */]();
    }
    GameComponent.prototype.ngOnInit = function () {
        var pile1 = new __WEBPACK_IMPORTED_MODULE_4__models_pile_model__["a" /* Pile */](123, 3, 3);
        var player = new __WEBPACK_IMPORTED_MODULE_3__models_player_model__["a" /* Player */](4234, "name1", "#123123");
        var state = {
            pile: [
                [pile1, pile1, pile1, pile1],
                [pile1, pile1, pile1, pile1],
                [pile1, pile1, pile1, pile1],
                [pile1, pile1, pile1, pile1]
            ],
            players: [player, player, player]
        };
        this.initGameFromState(state);
    };
    GameComponent.prototype.initGameFromState = function (state) {
        var gameStateFromServer = state;
        var players = gameStateFromServer.players;
        var playerMap = {};
        players.forEach(function (p) {
            playerMap[p.id] = new __WEBPACK_IMPORTED_MODULE_3__models_player_model__["a" /* Player */](p.id, p.color, p.name);
        });
        var turnList = players.map(function (p) {
            return p.id;
        });
        var cards = gameStateFromServer.cards;
        var pileArray = cards.map(function (row) {
            return row.map(function (p) {
                return new __WEBPACK_IMPORTED_MODULE_4__models_pile_model__["a" /* Pile */](p.card.id, p.card.cost, p.count);
            });
        });
        this.game = new __WEBPACK_IMPORTED_MODULE_5__models_client_game_model__["a" /* ClientGame */](playerMap, pileArray, turnList, this._userIdService.id);
    };
    GameComponent.prototype.updateMap = function (updateMap) {
        var update = JSON.parse(updateMap);
        if (this.game != null) {
            if (update.actions === "undefined") {
                this.game.getSelf().actions = update.actions;
            }
            if (update.buys === "undefined") {
                this.game.getSelf().buys = update.buys;
            }
            if (update.gold === "undefined") {
                this.game.getSelf().gold = update.gold;
            }
            if (update.select === "undefined") {
                if (update.select) {
                    this.game.toSelect = true;
                    this.game.toSelectHand = update.handSelect;
                    this.game.toSelectBoard = update.boardSelect;
                }
                else {
                    this.game.toSelect = false;
                }
            }
            if (update.hand === "undefined") {
                this.game.hand = update.hand;
            }
            if (update.decksize === "undefined") {
                this.game.deck = update.decksize;
            }
            if (update.discardsize === "undefined") {
                this.game.discardPile = update.discardsize;
            }
            if (update.holding === "undefined") {
                this.game.holding = update.holding;
            }
        }
    };
    GameComponent.prototype.stophold = function (msg) {
        this.game.holding = false;
    };
    GameComponent.prototype.chat = function (msg) {
        this.gameChat.addMessage(JSON.parse(msg));
    };
    return GameComponent;
}());
GameComponent = __decorate([
    __webpack_require__.i(__WEBPACK_IMPORTED_MODULE_0__angular_core__["_13" /* Component */])({
        selector: 'dmn-game',
        template: __webpack_require__(168),
        styles: [__webpack_require__(162)]
    }),
    __metadata("design:paramtypes", [typeof (_a = typeof __WEBPACK_IMPORTED_MODULE_1__shared_user_id_service__["a" /* UserIdService */] !== "undefined" && __WEBPACK_IMPORTED_MODULE_1__shared_user_id_service__["a" /* UserIdService */]) === "function" && _a || Object, typeof (_b = typeof __WEBPACK_IMPORTED_MODULE_6__game_service__["a" /* GameService */] !== "undefined" && __WEBPACK_IMPORTED_MODULE_6__game_service__["a" /* GameService */]) === "function" && _b || Object])
], GameComponent);

var _a, _b;
//# sourceMappingURL=game.component.js.map

/***/ }),

/***/ 61:
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__angular_core__ = __webpack_require__(1);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1__shared_user_id_service__ = __webpack_require__(17);
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "a", function() { return GameService; });
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
var __metadata = (this && this.__metadata) || function (k, v) {
    if (typeof Reflect === "object" && typeof Reflect.metadata === "function") return Reflect.metadata(k, v);
};


var GameService = (function () {
    function GameService(_userIdService) {
        this._userIdService = _userIdService;
    }
    //TODO, maybe not the best way to store / load this method
    GameService.prototype.updateMapFromJson = function (response) {
        // this._messageService.updateMap(response);
    };
    GameService.prototype.updateMapPost = function (url, params) {
        // $.post("/" + url, params, response => {
        //   this.updateMapFromJson(response);
        // });
    };
    GameService.prototype.sendBuys = function (buys) {
        var postParams = { userId: this._userIdService.id, boughtCards: buys };
        this.updateMapPost("buys", postParams);
    };
    GameService.prototype.sendAction = function (actionCardId) {
        var postParams = { userId: this._userIdService.id, cardId: actionCardId };
        this.updateMapPost("action", postParams);
    };
    return GameService;
}());
GameService = __decorate([
    __webpack_require__.i(__WEBPACK_IMPORTED_MODULE_0__angular_core__["c" /* Injectable */])(),
    __metadata("design:paramtypes", [typeof (_a = typeof __WEBPACK_IMPORTED_MODULE_1__shared_user_id_service__["a" /* UserIdService */] !== "undefined" && __WEBPACK_IMPORTED_MODULE_1__shared_user_id_service__["a" /* UserIdService */]) === "function" && _a || Object])
], GameService);

var _a;
//# sourceMappingURL=game.service.js.map

/***/ }),

/***/ 62:
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__angular_core__ = __webpack_require__(1);
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "a", function() { return LobbyComponent; });
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
var __metadata = (this && this.__metadata) || function (k, v) {
    if (typeof Reflect === "object" && typeof Reflect.metadata === "function") return Reflect.metadata(k, v);
};

var LobbyComponent = (function () {
    function LobbyComponent() {
    }
    LobbyComponent.prototype.ngOnInit = function () {
    };
    return LobbyComponent;
}());
LobbyComponent = __decorate([
    __webpack_require__.i(__WEBPACK_IMPORTED_MODULE_0__angular_core__["_13" /* Component */])({
        selector: 'dmn-lobby',
        template: __webpack_require__(169),
        styles: [__webpack_require__(163)]
    }),
    __metadata("design:paramtypes", [])
], LobbyComponent);

//# sourceMappingURL=lobby.component.js.map

/***/ }),

/***/ 63:
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__socket_service__ = __webpack_require__(105);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1__angular_core__ = __webpack_require__(1);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_2__user_id_service__ = __webpack_require__(17);
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "a", function() { return ChatSocketService; });
var __extends = (this && this.__extends) || (function () {
    var extendStatics = Object.setPrototypeOf ||
        ({ __proto__: [] } instanceof Array && function (d, b) { d.__proto__ = b; }) ||
        function (d, b) { for (var p in b) if (b.hasOwnProperty(p)) d[p] = b[p]; };
    return function (d, b) {
        extendStatics(d, b);
        function __() { this.constructor = d; }
        d.prototype = b === null ? Object.create(b) : (__.prototype = b.prototype, new __());
    };
})();
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
var __metadata = (this && this.__metadata) || function (k, v) {
    if (typeof Reflect === "object" && typeof Reflect.metadata === "function") return Reflect.metadata(k, v);
};



var ChatSocketService = (function (_super) {
    __extends(ChatSocketService, _super);
    function ChatSocketService(_userIdService) {
        var _this = _super.call(this, _userIdService, "/homechat") || this;
        _this._userIdService = _userIdService;
        return _this;
    }
    return ChatSocketService;
}(__WEBPACK_IMPORTED_MODULE_0__socket_service__["a" /* AbstractSocketService */]));
ChatSocketService = __decorate([
    __webpack_require__.i(__WEBPACK_IMPORTED_MODULE_1__angular_core__["c" /* Injectable */])(),
    __metadata("design:paramtypes", [typeof (_a = typeof __WEBPACK_IMPORTED_MODULE_2__user_id_service__["a" /* UserIdService */] !== "undefined" && __WEBPACK_IMPORTED_MODULE_2__user_id_service__["a" /* UserIdService */]) === "function" && _a || Object])
], ChatSocketService);

var _a;
//# sourceMappingURL=chatsocket.service.js.map

/***/ }),

/***/ 64:
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "a", function() { return environment; });
// The file contents for the current environment will overwrite these during build.
// The build system defaults to the dev environment which uses `environment.ts`, but if you do
// `ng build --env=prod` then `environment.prod.ts` will be used instead.
// The list of which env maps to which file can be found in `.angular-cli.json`.
// The file contents for the current environment will overwrite these during build.
var environment = {
    production: false,
    port: 4567
};
//# sourceMappingURL=environment.js.map

/***/ }),

/***/ 89:
/***/ (function(module, exports) {

function webpackEmptyContext(req) {
	throw new Error("Cannot find module '" + req + "'.");
}
webpackEmptyContext.keys = function() { return []; };
webpackEmptyContext.resolve = webpackEmptyContext;
module.exports = webpackEmptyContext;
webpackEmptyContext.id = 89;


/***/ }),

/***/ 90:
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
Object.defineProperty(__webpack_exports__, "__esModule", { value: true });
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__angular_core__ = __webpack_require__(1);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1__angular_platform_browser_dynamic__ = __webpack_require__(96);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_2__app_app_module__ = __webpack_require__(99);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_3__environments_environment__ = __webpack_require__(64);




if (__WEBPACK_IMPORTED_MODULE_3__environments_environment__["a" /* environment */].production) {
    __webpack_require__.i(__WEBPACK_IMPORTED_MODULE_0__angular_core__["a" /* enableProdMode */])();
}
__webpack_require__.i(__WEBPACK_IMPORTED_MODULE_1__angular_platform_browser_dynamic__["a" /* platformBrowserDynamic */])().bootstrapModule(__WEBPACK_IMPORTED_MODULE_2__app_app_module__["a" /* AppModule */]);
//# sourceMappingURL=main.js.map

/***/ }),

/***/ 98:
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__angular_core__ = __webpack_require__(1);
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "a", function() { return AppComponent; });
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};

var AppComponent = (function () {
    function AppComponent() {
        this.title = 'app works!';
    }
    return AppComponent;
}());
AppComponent = __decorate([
    __webpack_require__.i(__WEBPACK_IMPORTED_MODULE_0__angular_core__["_13" /* Component */])({
        selector: 'app-root',
        template: __webpack_require__(165),
        styles: [__webpack_require__(159)]
    })
], AppComponent);

//# sourceMappingURL=app.component.js.map

/***/ }),

/***/ 99:
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__angular_platform_browser__ = __webpack_require__(16);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1__angular_core__ = __webpack_require__(1);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_2__angular_forms__ = __webpack_require__(94);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_3__angular_http__ = __webpack_require__(95);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_4__angular_router__ = __webpack_require__(97);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_5__app_component__ = __webpack_require__(98);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_6__lobby_lobby_component__ = __webpack_require__(62);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_7__creation_page_creation_page_component__ = __webpack_require__(59);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_8__game_game_component__ = __webpack_require__(60);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_9__shared_user_id_service__ = __webpack_require__(17);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_10__game_game_service__ = __webpack_require__(61);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_11__routes__ = __webpack_require__(104);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_12__chat_chat_component__ = __webpack_require__(100);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_13__shared_chatsocket_service__ = __webpack_require__(63);
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "a", function() { return AppModule; });
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};














var AppModule = (function () {
    function AppModule() {
    }
    return AppModule;
}());
AppModule = __decorate([
    __webpack_require__.i(__WEBPACK_IMPORTED_MODULE_1__angular_core__["b" /* NgModule */])({
        declarations: [
            __WEBPACK_IMPORTED_MODULE_5__app_component__["a" /* AppComponent */],
            __WEBPACK_IMPORTED_MODULE_6__lobby_lobby_component__["a" /* LobbyComponent */],
            __WEBPACK_IMPORTED_MODULE_7__creation_page_creation_page_component__["a" /* CreationComponent */],
            __WEBPACK_IMPORTED_MODULE_8__game_game_component__["a" /* GameComponent */],
            __WEBPACK_IMPORTED_MODULE_6__lobby_lobby_component__["a" /* LobbyComponent */],
            __WEBPACK_IMPORTED_MODULE_7__creation_page_creation_page_component__["a" /* CreationComponent */],
            __WEBPACK_IMPORTED_MODULE_12__chat_chat_component__["a" /* ChatComponent */]
        ],
        imports: [
            __WEBPACK_IMPORTED_MODULE_0__angular_platform_browser__["a" /* BrowserModule */],
            __WEBPACK_IMPORTED_MODULE_2__angular_forms__["a" /* FormsModule */],
            __WEBPACK_IMPORTED_MODULE_3__angular_http__["a" /* HttpModule */],
            __WEBPACK_IMPORTED_MODULE_4__angular_router__["a" /* RouterModule */].forRoot(__WEBPACK_IMPORTED_MODULE_11__routes__["a" /* appRoutes */])
        ],
        providers: [
            __WEBPACK_IMPORTED_MODULE_9__shared_user_id_service__["a" /* UserIdService */],
            __WEBPACK_IMPORTED_MODULE_10__game_game_service__["a" /* GameService */],
            __WEBPACK_IMPORTED_MODULE_13__shared_chatsocket_service__["a" /* ChatSocketService */]
            // SocketService
        ],
        bootstrap: [__WEBPACK_IMPORTED_MODULE_5__app_component__["a" /* AppComponent */]]
    })
], AppModule);

//# sourceMappingURL=app.module.js.map

/***/ })

},[200]);
//# sourceMappingURL=main.bundle.js.map