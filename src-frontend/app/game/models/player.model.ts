export class Player {
  public victoryPoints: number = 3;
  public numCards: number = 0;

  constructor(
    public id: number,
    public color: string,
    public name: string) {
  }
}
