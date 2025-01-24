import { Component, OnInit } from '@angular/core';

import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { map } from 'rxjs/operators';

import { HttpClient, HttpHeaders } from '@angular/common/http';
import { ToastrService } from 'ngx-toastr';
// import { FormGroup } from '@angular/forms';
@Component({
  selector: 'app-about',
  templateUrl: './about.component.html',
  styleUrls: ['./about.component.css']
})

export class AboutComponent implements OnInit {
  currentUserEmail!: string;
  companyForm!: FormGroup;
  company: any;
  
 

  constructor(
    
    private http: HttpClient,
    private fb: FormBuilder,
    private toastr:ToastrService
  
  ) {
    this.companyForm = this.fb.group({
      name: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      address: ['', Validators.required],
      phone: [
        '',
        [Validators.required, Validators.pattern('^[0-9]{10}$')]
      ],
    });

  }
  


  
  editMode: boolean = false;
  ngOnInit(): void {
    // this.fetchCurrentUserEmail();
    this.getCompanyDetails();
   // console.log(this.toastr.info('This is a test notification.', 'Info'))
    
    
    
  }
  //  companyId:Number=-1;
  

  onCancel(){
    this.editMode=false;
  }

  getCompanyDetails(): void {
  
    const url = `http://localhost:8080/api/email`; 
  
    this.http.get<{ company: any }>(url, { withCredentials: true }).subscribe(
      (response: any) => {
        console.log(response);
        this.company = response.company; 
        this.companyForm.patchValue(this.company); 
      },
      (error: any) => {
        this.company = null;
        console.error('Error fetching company details:', error);
      }
    );
  }
  

    onEdit(companyId:Number){
      this.editMode = true;
      if (this.company) {
        this.companyForm.patchValue({
          name: this.company.name,
          address: this.company.address,
          email: this.company.email,
          phone: this.company.phone,
        });
      }


    }
   
    submitForm(): void {
     
      if (this.companyForm.valid) {
        const updatedData = this.companyForm.value;
    
        this.http.put<boolean>(`http://localhost:8080/api/update`, updatedData, { withCredentials: true}).subscribe(
          (response: boolean) => {
            if (response) {
              this.getCompanyDetails();
              this.toastr.success('Company Details updated successfully');
              // this.showSuccess();
              // alert('!');
              this.editMode = false;
            } else {

              this.toastr.error('Failed to update company details. Please try again.')

             
            }
          },
          (error: any) => {
            this.toastr.error('Error Updating Company Details.');
            console.error('Error updating company details:', error);
           // alert('An error occurred while updating the company details.');
          }
        );
      } else {
        this.toastr.error('Please fill in all required fields.')
        //alert('Please fill in all required fields.');
      }
    }
   
  }
  
 

