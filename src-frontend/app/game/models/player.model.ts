export class Player {
  public victoryPoints: number;
  public actions: number;
  public buys: number;
  public gold: number;

  constructor(
    public id: number,
    public color: string,
    public name: string) {

    this.victoryPoints = 3;
    this.buys = 0;
    this.actions = 0;
    this.gold = 0;
  }

  drawHand() {}
}
