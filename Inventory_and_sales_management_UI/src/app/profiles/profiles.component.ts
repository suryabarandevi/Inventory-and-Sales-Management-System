import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { HttpClient, HttpHeaders } from '@angular/common/http';
// import { CurrentUserService } from '../services/currentUserService';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-profiles',
  templateUrl: './profiles.component.html',
  styleUrls: ['./profiles.component.css'],
})
export class ProfilesComponent implements OnInit {
  profileForm: FormGroup;
  roleType = ['Manager'];
  existingProfiles: any[] = []; 
  currentUserEmail: any;
  EditMode:boolean=false;
  companyForm!: FormGroup;
  // id:Number=-1;

  constructor(private fb: FormBuilder, private http: HttpClient,private toastr:ToastrService) {

    this.profileForm = this.fb.group({
      role: ['', [Validators.required]],
      name: ['', [
        Validators.required,
        Validators.minLength(2),
        Validators.pattern(/^[a-zA-Z\s]*$/)
      ]],
      email: ['', [
        Validators.required,
        Validators.email,
        Validators.pattern(/^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/)
      ]],
      phone: ['', [
        Validators.required,
        Validators.pattern(/^[0-9]{10}$/),
      ]]
    });
  }


  id: string=""
  // name:string= "";
  // email:string= "";
  // phone: string="";
  // address: string="";
  ngOnInit(): void {
   // this.fetchCurrentUserEmail();
    // this.getCompanyId();   http://localhost:8080/api/current-user/email
    this.fetchProfiles();
  }
 
 
  
  
  fetchProfiles(): void {
 
    this.http.get<any[]>(`http://localhost:8080/api/managers/company`,{withCredentials: true}).subscribe(
      (data:any) => {
        console.log(data);
        this.existingProfiles = data;
        console.log('Existing Profiles:', this.existingProfiles);
      },
      (error:any) => {
        console.error('Failed to fetch profiles:', error);
      }
    );
  }

  
  createProfile(): void {
    if (this.profileForm.valid) {
     
  
      this.http.post(`http://localhost:8080/api/managers/create`, this.profileForm.value, {withCredentials: true}).subscribe(
        (response: any) => {
          console.log(response);
          this.toastr.success('Profile created successfully!')
         
         // alert('Profile created successfully!');
          this.fetchProfiles();
         
        },
        (error: any) => {
          this.toastr.error("The User you're trying to make manager is already a manager in other company,choose another Email.")
          console.error('Failed to create profile:', error);
        }
      );
    } else {
      alert('Please fill in all required fields correctly.');
    }
  }

  Edit(id:string,name:string,email:string,phone:string){
    this.EditMode=true;
    this.id=id;
    // // this.name=name;
    // this.email=email;
    // this.phone=phone;
    this.profileForm.patchValue({
     role: 'Manager', 
      name: name,
      email: email, 
      phone: phone,
    });
    console.log(this.profileForm)
    this.profileForm.controls['email'].disable();

  }

 submitForm(){
  if (this.profileForm.valid) {
    const updatedData = this.profileForm.value;

   
    this.http.put<boolean>(`http://localhost:8080/api/managers/${this.id}`, updatedData).subscribe(
      (response: boolean) => {
        if (response) {
          this.profileForm.reset();
          this.profileForm.controls['email'].enable();
          this.toastr.success('Manager details updated successfully!')
          this.EditMode=false;
          this.fetchProfiles();
         
         // alert('');
        } else {
          alert('Failed to update Manager details. Please try again.');
        }
      },
      (error: any) => {
        console.error('Error updating Manager details:', error);
        alert('An error occurred while updating the Manager details.');
      }
    );
  } else {
    console.log(this.companyForm);
    alert('Please fill in all required fields.');
  }
 }

 Delete(id: string): void {
   console.log(id)
   
    this.http.delete<boolean>(`http://localhost:8080/api/managers/${id}`, {withCredentials: true}).subscribe(
      (response: boolean) => {
     
        this.toastr.success('Manager Deleted Sucessfully')
        // this.toastr.warning("are")
        this.fetchProfiles(); 
      },
      (error: any) => {
        console.error('Error deleting manager:', error);
        this.toastr.error('An error occurred while trying to delete the manager. Please try again.')
        //alert('');
      }
    );
  
}

  onCancel(){
    this.EditMode=false;
    this.profileForm.reset();
    this.profileForm.controls['email'].enable();
  }
}
