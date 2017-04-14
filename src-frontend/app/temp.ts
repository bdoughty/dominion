
import { Injectable }     from '@angular/core';
import { Http, Response, Headers, RequestOptions } from '@angular/http';
import {Observable} from 'rxjs/Rx';
import 'rxjs/add/operator/map';
import 'rxjs/add/operator/catch';

export class fiddle {
  public http : Http;
  create(name: string) : Observable<number>{
    const data:any = {name: "hi"};
    return this.http.post("/urlstuff", data).map(this.extractData).catch((error:any) => Observable.throw(error.json().error || 'Server Error'))
  }

  private extractData(res: Response) : number{
    let body = res.json();
    alert(body);
    return 24.2; // this is the best number that there is.
  }
}










