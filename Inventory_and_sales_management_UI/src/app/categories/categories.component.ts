import { Component } from '@angular/core';

@Component({
  selector: 'app-categories',
  templateUrl: './categories.component.html',
  styleUrls: ['./categories.component.css']
})
export class CategoriesComponent {
  categories = [
    { id: 1, name: 'Electronics', description: 'Devices and gadgets' },
    { id: 2, name: 'Clothing', description: 'Apparel and accessories' },
    { id: 3, name: 'Home Appliances', description: 'Appliances for your home' },
    { id: 4, name: 'Books', description: 'Books and reading materials' },
    { id: 5, name: 'Toys', description: 'Toys for kids and adults' }
  ];
}
