interface Message{
    color:string;
    message:string;
    name:string;
}

export class Chat {
  public messages:Message[] = [];

  constructor() { }

  addMessageFromComponents(color:string, message:string, name:string) {
    this.messages.push({
      color: color,
      message: message,
      name: name
    });
  }

  addMessage(chatItem : Message){
     this.messages.push(chatItem);
  }
}

