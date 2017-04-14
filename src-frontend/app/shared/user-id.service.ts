import {Injectable, OnInit} from '@angular/core';

@Injectable()
export class UserIdService {
  private _id: string = "";

  get id(): string {
    return this._id;
  }

  set id(id: string) {
    this._id = id;
  }
}
