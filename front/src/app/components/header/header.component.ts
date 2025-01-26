import { Component, HostListener, OnInit } from '@angular/core';

import { HeaderDesktopComponent } from './header-desktop/header-desktop.component';
import { HeaderMobileComponent } from './header-mobile/header-mobile.component';
import {NgIf} from '@angular/common';
import {RouterLink} from '@angular/router';

@Component({
  selector: 'app-header',
  imports: [
    HeaderDesktopComponent,
    HeaderMobileComponent,
    NgIf,
    RouterLink
  ],
  templateUrl: './header.component.html',
  styleUrl: './header.component.css'
})
export class HeaderComponent implements OnInit {
  isDesktop: boolean = true;

  ngOnInit() {
    this.checkScreenSize();
  }

  @HostListener('window:resize', [])
  checkScreenSize(): void {
    this.isDesktop = window.innerWidth >= 1024; // 768px est le seuil pour passer en mode mobile
    console.log(this.isDesktop);
    console.log(window.innerWidth);
  }
}
