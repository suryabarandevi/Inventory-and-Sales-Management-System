export interface Company {
    id?: number; // Optional, as it's auto-generated in the backend
    name: string; // Company name
    address: string; // Address of the company
    email: string; // Contact email
    phone: string; // Phone number
    // taxDetails: string; // Tax details, such as GSTIN or VAT number
    managers:[];

  }
  