import {Injectable, OnInit} from '@angular/core';

@Injectable()
export class UserIdService {
  private _id: number = -1;

  get id(): number {
    return this._id;
  }

  set id(id: number) {
    this._id = id;
  }
}
