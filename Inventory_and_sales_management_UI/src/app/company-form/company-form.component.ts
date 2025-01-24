import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
// import { CompanyService } from '../services/company.service';
import { Company } from '../Models/company.model';
import { Router } from '@angular/router';
import { ActivatedRoute } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { HttpClient} from '@angular/common/http';
@Component({
  selector: 'app-company-form',
  templateUrl: './company-form.component.html',
  styleUrls: ['./company-form.component.css'],
})
export class CompanyFormComponent implements OnInit {
  companyForm: FormGroup;
  company: Company | null = null;
  isEditMode = false;

  constructor(private fb: FormBuilder, private route: ActivatedRoute,private router:Router,private tostr:ToastrService,private http:HttpClient) {
    this.companyForm = this.fb.group({
      name: ['', [Validators.required]],
      address: ['', [Validators.required]],
      email: [{ value: '', disabled: true }, [Validators.required, Validators.email]],
      phone: ['', [Validators.required, Validators.pattern('[0-9]{10}')]],
      // taxDetails: ['', [Validators.required]],
    });
  }

  
  ngOnInit(): void {
    this.route.queryParams.subscribe((params:any) => {
      const email = params['email']; // Extract email from query params
      console.log('Extracted Email:', email);
  
      if (email) {
        this.isEditMode = true;
        this.companyForm.patchValue({ email: email });
      } else {
        console.error('Email query parameter is missing or undefined.');
      }
    });
  

 
  }

  
  saveCompany(): void {
    if (this.companyForm.valid) {
      const companyData: Company = this.companyForm.getRawValue();
  
      if (this.isEditMode && this.company) {
        companyData.id = this.company.id;
      }
  
    
      this.http.post<Company>('http://localhost:8080/api/company', companyData).subscribe({
        next: (savedCompany: Company) => {
          this.company = savedCompany;
          this.tostr.success("Company details saved successfully");
          this.router.navigate(['/admin-dashboard']);
        },
        error: () => {
          console.error('Error saving company details.');
          alert('An error occurred while saving the company details.');
        },
      });
    } else {
      alert('Please fill in all required fields correctly.');
    }
  }
  // saveCompany(): void {
  //   if (this.companyForm.valid) {
  //     const companyData: Company = this.companyForm.getRawValue();

      

  //     if (this.isEditMode && this.company) {
  //       companyData.id = this.company.id; // Include ID for updates
  //     }

  //     this.companyService.saveCompany(companyData).subscribe({
  //       next: (savedCompany: Company) => {
  //         this.company = savedCompany;
  //         this.tostr.success("Company details saved successfully")
         
  //         this.router.navigate(['/admin-dashboard'])
  //       },
  //       error: () => {
  //         console.error('Error saving company details.');
  //         alert('An error occurred while saving the company details.');
  //       },
  //     });
  //   } else {
  //     alert('Please fill in all required fields correctly.');
  //   }
  // }
}
