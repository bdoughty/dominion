function has(object, key) {
    return object ? hasOwnProperty.call(object, key) : false;
}

updateWithMap = function(map) {
    //TODO FILL THIS OUT, BUT FIRST FILL OUT THE CLIENT GAME.
    if (has(map, 'actions')) {
        currentGame.actions = map.actions;
    }
}