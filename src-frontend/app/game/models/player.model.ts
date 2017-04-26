export class Player {
  public victoryPoints: number = 3;
  public actions: number = 0;
  public buys: number = 0;
  public gold: number = 0;
  public numCards: number = 0;

  constructor(
    public id: number,
    public color: string,
    public name: string) {
  }
}
