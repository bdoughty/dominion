 # cs0320 Term Project
  
  **Team Members:**
Sol Zitter, Ben Doughty, Brendan Le, Henry Stone
  
  **Project Idea:** _Fill this in!_
  
 +Dominion Multiplayer Card Game
 +
  **Mentor TA:** _Put your mentor TA's name and email here once you're assigned one!_
  
  ## Project Requirements
https://docs.google.com/a/brown.edu/document/d/1S7Yn5-4c57bUifcHwQGHtDAIXlwFO_fYcHr3zk-i3bY
  
  ## Project Specs and Mockup
  _A link to your specifications document and your mockup will go here!_

  
API DOCUMENTATION:

Do Action "doaction": Client -> Server
  data = {handid:int}

End Action Phase "endaction": Client -> Server
  data = {}

Make Selection "select": Client -> Server
  data = {inhand:boolean, loc:int}
  
End Buy Phase "endbuy": Client -> Server
  data = [int]
  
Register New ID "newid": Client -> Server
  data = {}
  
Register Old ID "oldid": Client -> Server
  data = int
  
Leave Game "leave": Client -> Server
  data = {}
  
Create Game "/creategame": AJAX
  data = {tbd}
  
  
Join Game "join": Client -> Server
  data = {gameid:int}
  
Pending Games "pending": Server -> Client
  data = [
            id:int
            name:string, 
            maxusers:int, 
            actioncardids:[int], 
            users:[
              {id:int,
               name:string,
               color:String}
            ]
          ]
          
Join Game Response "joinresponse": Server -> Client
  data = [didjoin:boolean, gameid:int]
  
Leave Game Response "leaveresponse": Server -> Client
  data = {}
  
  
  
  
Game Init "init": Server -> Client
  data = {
    gameid:int,
    users:[{id:int, name:string, color:String}],
    cardids:[int],
  }
  
Global Update "globalmap" ->
  data = {
    ?turn:int
    ?winner:[{id:int, name:string, color:String}]
  }

Update Map "updatemap": Server -> Client
  data = {
    ?actions:int,
    ?buys:int,
    ?gold:int,
    ?select:boolean true ->
      handSelect:[int],
      boardSelect:[int],
    ?hand:[int],
    ?decksize:int,
    ?holding:boolean
    ?board:{piles:{[id:int->{size:int}]}}
  }
