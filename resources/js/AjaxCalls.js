//TODO, maybe not the best way to store / load this method
const updateMapFromJson = messageFuntions.updatemap;

const updateMapPost = function(url, params){
    $.post("/" + url, params, response => {
        updateMapFromJson(response);
    });
};

const sendBuys = function(buys){
    const postParams = {userId:userId, boughtCards:buys};
    updateMapPost("buys", postParams);
};

const sendAction = function(actionCardId){
    const postParams = {userId:userId, cardId:actionCardId};
    updateMapPost("action", postParams);
};