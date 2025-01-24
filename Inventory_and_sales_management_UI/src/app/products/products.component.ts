import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import {Router} from '@angular/router';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-products',
  templateUrl: './products.component.html',
  styleUrls: ['./products.component.css']
})
export class ProductsComponent {
  productForm!: FormGroup;
  categoryForm!: FormGroup;
  EditMode: boolean = false;
  existingProducts: any[] = [];
  categories: any[] = []; 
  editingProductId: string | null = null;

  productPage: number = 1; 


  constructor(private fb: FormBuilder,private http:HttpClient,private router:Router,private toastr:ToastrService) {}

  ngOnInit(): void {

    this.fetchCategories();
    this.fetchProducts();

   
    this.productForm = this.fb.group({

      name: ['', [Validators.required]],
      quantity: ['', [Validators.required,Validators.min(1),Validators.pattern('^[0-9]+$')]],
      price: ['', [Validators.required,Validators.min(1),Validators.pattern('^[0-9]+(\\.[0-9]{1,2})?$')]],
      category_Id: ['', [Validators.required]], 
    });

   
    this.categoryForm = this.fb.group({
      categoryName: ['', [Validators.required]],
    });

   
    this.existingProducts = [
     
    ];

  
  }

  
  // onFileSelected(event: any): void {
  //   this.selectedFile = event.target.files[0]; 
  // }

  fetchCategories(): void {
 
    this.http
      .get<any[]>('http://localhost:8080/api/getCategories', {
       
        withCredentials: true,
      })
      .subscribe(
        (response:any) => {
          console.log('Fetched categories:', response);
         
          this.categories = response; 
        },
        (error:any) => {
          console.error('Error fetching categories:', error);
        }
      );
  }
  fetchProducts():void{
    this.http.get<any[]>('http://localhost:8080/api/products/getProducts', { 
        withCredentials: true,
      })
      .subscribe(
        (response:any) => {
          console.log('Fetched Products:', response);
          // this.fetchProducts();
          this.existingProducts = response; 
        
        },
        (error:any) => {
          console.error('Error fetching Products:', error);
        }
      );
  }

  createProduct(): void {
    if (this.productForm.valid ) {

      this.http
        .post(`http://localhost:8080/api/products/create/${this.productForm.value.category_Id}`, this.productForm.value, {
          // headers: headers,
          withCredentials: true,
        })
        .subscribe(
          (response:any) => {
            console.log('Product created successfully:', response);
            this.toastr.success('Product created successfully')
            this.fetchProducts();
            this.existingProducts.push();
            this.productForm.reset(this.productForm.value);
            // alert("Product created successfully")
          },
          (error:any) => {
            console.error('Error creating product:', error);
            this.toastr.error('Error creating the Product.')
            // alert("Unable to create the Product")
          }
        );
    }
  }

  createCategory(): void {
    if (this.categoryForm.valid) {
      const newCategory = this.categoryForm.value.categoryName;

     
      const url = 'http://localhost:8080/api/categories';
      
      this.http.post(url, { name: newCategory }, { withCredentials: true}).subscribe(
        (response:any) => {
          console.log('Category created successfully:', response);
          if (!this.categories) {
            this.categories = []; 
        }
          if (!this.categories.includes(newCategory)) {
            this.categories.push(newCategory);
            console.log(newCategory)
            this.toastr.success('Category created successfully')
           
          }
          this.fetchCategories();
          this.categoryForm.reset();
        },
        (error:any) => {
          // console.error('Error creating category:', error);
          this.toastr.error("Category already Exists")
        }
      );
    }
  }

  // Edit(id: string, name: string, quantity: number, price: number, category: number): void {
   
    
    
 
  Edit(id: string, name: string, quantity: number, price: number, categoryId: number): void {
    this.EditMode = true;
    this.editingProductId = id;
    console.log(this.editingProductId)
  
    this.productForm.setValue({
      category_Id: categoryId, 
      name: name,
      quantity: quantity,
      price: price,
    });
  }
  

  submitForm(): void {
    if (this.productForm.valid && this.editingProductId) {
      const updatedData = this.productForm.value;
      console.log(this.productForm.value);

     
      this.http.put<boolean>(`http://localhost:8080/api/products/update/${this.editingProductId}/${this.productForm.value.category_Id}`, this.productForm.value).subscribe(
        (response: boolean) => {
          if (response) {
            this.fetchProducts();
            this.toastr.success('Products details updated successfully!')
           
          } else {
            this.toastr.error("Failed to update Products details. Please try again.")
            //alert('Failed to update Products details. Please try again.');
          }
        },
        (error: any) => {
          this.toastr.error("An error occurred while updating the Product details.")
          console.error('Error updating Product details:', error);
        //  alert('An error occurred while updating the Product details.');
        }
      );
    } else {
      this.toastr.info('Please fill in all required fields.')
      
    }

      this.EditMode = false;
      this.editingProductId = null;
      this.productForm.reset();
    }
  

  onCancel(): void {
    this.EditMode = false;
    this.editingProductId = null;
    this.productForm.reset();
  }

  Delete(id: string): void {
  
  
      this.http.delete<boolean>(`http://localhost:8080/api/products/${id}`, {withCredentials: true}).subscribe(
        (response: boolean) => {
          this.toastr.success('Product deleted sucessfully')
         // alert(""); 
          this.fetchProducts();
        },
        (error: any) => {
          console.error('Error deleting Product:', error);
          alert('An error occurred while trying to delete the Product. Please try again.');
        }
      );
    }
  

  // routeto(){
  //   this.router.navigate(['admin-dashboard/products/categories']);
  // }
}
