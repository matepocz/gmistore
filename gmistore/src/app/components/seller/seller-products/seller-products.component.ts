import {ChangeDetectorRef, Component, OnDestroy, OnInit, ViewChild} from '@angular/core';
import {Subscription} from "rxjs";
import {SellerService} from "../../../service/seller/seller.service";
import {ProductTableModel} from "../../../models/product/productTableModel";
import {MatTableDataSource} from "@angular/material/table";
import {MatPaginator, PageEvent} from "@angular/material/paginator";
import {MatDialogRef} from "@angular/material/dialog";
import {LoadingSpinnerComponent} from "../../loading-spinner/loading-spinner.component";
import {PopupSnackbar} from "../../../utils/popup-snackbar";
import {ActivatedRoute, ParamMap, Router} from "@angular/router";
import {SpinnerService} from "../../../service/spinner-service.service";
import {MatSort} from "@angular/material/sort";
import {MediaChange, MediaObserver} from "@angular/flex-layout";

@Component({
  selector: 'app-seller-products',
  templateUrl: './seller-products.component.html',
  styleUrls: ['./seller-products.component.css']
})
export class SellerProductsComponent implements OnInit, OnDestroy {
  @ViewChild(MatPaginator) paginator: MatPaginator;
  @ViewChild(MatSort, {static: true}) sort: MatSort;

  private productSub: Subscription = new Subscription();
  productTableData: Array<ProductTableModel>;
  dataSource: MatTableDataSource<ProductTableModel>;
  currentScreenWidth: string = '';
  flexMediaWatcher: Subscription;
  displayedColumns: string[] = ['picture', 'name', 'category', 'price', 'active', 'modify'];

  numberOfProducts = 0;
  pageIndex: number = 0;
  pageSize: number = 10;
  pageSizeOptions: Array<number> = [10, 20, 50];

  spinner: MatDialogRef<LoadingSpinnerComponent> = this.spinnerService.start();

  constructor(private sellerService: SellerService,
              private router: Router,
              private snackBar: PopupSnackbar,
              private activatedRoute: ActivatedRoute,
              private spinnerService: SpinnerService,
              private cdRef: ChangeDetectorRef,
              private mediaObserver: MediaObserver) {
  }

  ngOnInit(): void {
    this.productSub.add(
      this.activatedRoute.queryParamMap.subscribe(
        (param: ParamMap) => {
          this.spinnerService.stop(this.spinner);
          this.pageIndex = Number(param.get('pageIndex'));
          this.pageSize = Number(param.get('pageSize'));
          this.getSellerProducts();
        }
      )
    );

    this.flexMediaWatcher = this.mediaObserver.media$.subscribe((change: MediaChange) => {
      if (change.mqAlias !== this.currentScreenWidth) {
        this.currentScreenWidth = change.mqAlias;
        this.setupTable();
      }
    });
  }

  setupTable = () => {
    if (this.currentScreenWidth === 'xs' || this.currentScreenWidth === 'sm') {
      let displayedColumns = this.displayedColumns;
      this.displayedColumns = displayedColumns.filter(str => !str.match(/^(category|price)$/)); // remove
      console.log(this.displayedColumns)
    } else if (!this.displayedColumns.includes("category" || "price")) {
      this.displayedColumns = ['picture', 'name', 'category', 'price', 'active', 'modify'];
    }
  };


  private getSellerProducts() {
    this.spinner = this.spinnerService.start();
    this.productSub.add(
      this.sellerService.getOwnProducts(this.pageSize, this.pageIndex).subscribe(
        response => {
          console.log(response.products);
          this.productTableData = response.products;
          this.dataSource = new MatTableDataSource<ProductTableModel>(this.productTableData);
          this.dataSource.paginator = this.paginator;
          this.dataSource.sort = this.sort;
          this.spinnerService.stop(this.spinner);
        },
        error => {
          this.spinnerService.stop(this.spinner);
          console.log(error);
        },
        () => {
        }
      ));
  }

  ngOnDestroy(): void {
    this.productSub.unsubscribe();
  }

  detectChanges() {
    this.cdRef.detectChanges();
  }


  editProduct(slug: any) {
    this.router.navigate(['/edit-product', slug]);
  }

  paginationEventHandler($event: PageEvent) {
    this.pageSize = $event.pageSize;
    this.pageIndex = $event.pageIndex;
    this.router.navigate(['.'], {
      relativeTo: this.activatedRoute,
      queryParams: {
        pageIndex: this.pageIndex,
        pageSize: this.pageSize,
      }
    });
  }
}
