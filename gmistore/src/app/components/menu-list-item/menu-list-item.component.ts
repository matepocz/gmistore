import {Component, HostBinding, Input, OnInit} from '@angular/core';
import {MainCategoryModel} from "../../models/main-category.model";
import {Router} from "@angular/router";
import {SideNavComponent} from "../side-nav/side-nav.component";
import {animate, state, style, transition, trigger} from "@angular/animations";

@Component({
  selector: 'app-menu-list-item',
  templateUrl: './menu-list-item.component.html',
  styleUrls: ['./menu-list-item.component.css'],
  animations: [
    trigger('indicatorRotate', [
      state('collapsed', style({transform: 'rotate(0deg)'})),
      state('expanded', style({transform: 'rotate(180deg)'})),
      transition('expanded <=> collapsed',
        animate('500ms cubic-bezier(0.4,0.0,0.2,1)')
      ),
    ])
  ]
})
export class MenuListItemComponent implements OnInit {

  expanded: boolean;
  @HostBinding('attr.aria-expanded') ariaExpanded = this.expanded;
  @Input() item: MainCategoryModel;
  @Input() depth: number;

  constructor(public sideNav: SideNavComponent, public router: Router) {
    if (this.depth === undefined) {
      this.depth = 0;
    }
  }


  onItemSelected(item: MainCategoryModel) {
    if (!item.subCategories || !item.subCategories.length) {
      this.router.navigate(['/product-list'],
        {
          queryParams: {
            category: item.key,
            pageSize: 10,
            pageIndex: 0
          }
        });
      this.sideNav.closeNav();
    }
    if (item.subCategories && item.subCategories.length) {
      this.expanded = !this.expanded;
    }
  }

  ngOnInit(): void {
  }

}
