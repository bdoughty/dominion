export class Player {
  public victoryPoints: number = 3;
  public numCards: number = 5;

  constructor(
    public id: number,
    public color: string,
    public name: string) {
  }
}
