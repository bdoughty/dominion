class Pile {
    constructor(cardId, cost, count) {
        this.count = count;
        this.cardId = cardId;
        this.cost = cost;
    }
}

class ClientGame {
    constructor(playerMap, pileArray, turnList, ownPlayerId) {
        this.players = playerMap;
        this.turnList = turnList;
        this.ownPlayerId = ownPlayerId;

        this.turn = 0;
        this.gamePhase = "action";
        this.holding = false;

        //Note this pile array is loaded [y][x]
        this.pileArray = pileArray;
        this.toBuy = [];
        this.deck = 10;
        this.hand = [];
        this.discardPile = 0;

        this.toSelect = false;
        this.toSelectHand = [];
        this.toSelectBoard = [];
    }

    getSelf() {
        return this.players[this.ownPlayerId];
    }

    getCurrentPlayer() {
        return this.players[this.turnList[this.turn]];
    }
}

class Player {
    constructor(id, color, name) {
        this.id = id;
        this.color = color;
        this.name = name;
        this.victoryPoints = 3;

        this.actions = 0;
        this.gold = 0;
        this.buys = 0;
    }

    drawHand() {}
}

class Chat {
    constructor() {
        this.chat = [];
    }

    addMessage(color, message, name) {
        this.chat.push({
            color: color,
            message: message,
            name: name
        });
    }

    addMessage(chatItem) {
        this.chat.push(chatItem);
    }
}
