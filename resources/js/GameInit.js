let game = null;
let gameChat = new Chat();

messageFuntions.initgame = function (gameInitString) {
    const gameStateFromServer = JSON.parse(gameInitString);
    const players = gameStateFromServer.players;
    const playerMap = {};
    players.forEach(function(p) {
        playerMap[p.id] = new Player(p.id, p.color, p.name);
    });
    const turnList = players.map(function(p){return p.id;});
    const cards = gameStateFromServer.cards;
    const pileArray = cards.map(function(row){return row.map(function(p){
        return new Pile(p.card.id, p.card.cost, p.count);
    })});

    game = new ClientGame(playerMap, pileArray, turnList, userId);
};

messageFuntions.updatemap = function (updateMap) {
    const update = JSON.parse(updateMap);
    if (game != null) {
        if (!typeof update.actions == "undefined") {
            game.getSelf().actions = update.actions;
        }
        if (!typeof update.buys == "undefined") {
            game.getSelf().buys = update.buys;
        }
        if (!typeof update.gold == "undefined") {
            game.getSelf().gold = update.gold;
        }
        if (!typeof update.select == "undefined") {
            if (update.select) {
                game.toSelect = true;
                game.toSelectHand = update.handSelect;
                game.toSelectBoard = update.boardSelect;
            } else {
                game.toSelect = false;
            }
        }
        if (!typeof update.hand == "undefined") {
            game.hand = update.hand;
        }
        if (!typeof update.decksize == "undefined") {
            game.deck = update.decksize;
        }
        if (!typeof update.discardsize == "undefined") {
            game.discardPile = update.discardsize;
        }
        if (!typeof update.holding == "undefined") {
            game.holding = update.holding;
        }

    }
};

messageFuntions.stophold = function(msg){
    game.holding = false;
};

messageFuntions.chat = function(msg){
    gameChat.addMessage(JSON.parse(msg));
};