 # cs0320 Term Project
  
  **Team Members:**
Sol Zitter, Ben Doughty, Brendan Le, Henry Stone
  
  **Project Idea:**
Dominion Multiplayer Card Game
 
  **Mentor TA:**
Adam DeHovitz <adam_dehovitz@brown.edu>
  
  ## Project Requirements
https://docs.google.com/a/brown.edu/document/d/1S7Yn5-4c57bUifcHwQGHtDAIXlwFO_fYcHr3zk-i3bY
  
  ## Project Specs and Mockup
https://drive.google.com/drive/u/2/folders/0B9iSPjAB_b_pVnZJRmRHSElvRVU

  
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
      ?playeractions:[{
        urgent:boolean
        select:boolean
        handselect:[int]
        boardSelect:[int]
        buttons:[{id:string, name:string}]
        }]
      ?hand:[int],
      ?decksize:int,
      ?holding:boolean
      ?board:{piles:{[id:int->{size:int}]};
    }
