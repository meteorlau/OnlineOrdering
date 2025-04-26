import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class UserService {
  private baseUrl = 'http://localhost:8080/auth'; // 后端服务基础路径

  constructor(private http: HttpClient) {}

  login(request: { username: string, password: string }): Observable<any> {
    return this.http.post(`${this.baseUrl}/login`, request);
  }

  register(user: any, role: string): Observable<any> {
    return this.http.post(`${this.baseUrl}/register?role=${role}`, user, { responseType: 'text' });
  }
}
