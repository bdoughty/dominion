import {Button} from "./button-interface";
export class PlayerAction {
  constructor(
    public urgent: boolean,
    public requiresSelect: boolean,
    public handSelect: number[],
    public boardSelect: number[],
    public buttons: Button[],
    public cancel: boolean,
    public id: number
  ) {}
}
