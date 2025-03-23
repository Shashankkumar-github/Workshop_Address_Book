package com.bridgelabz.AddressBook_Workshop.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class AddressBookDTO {

    @NotBlank(message = "Full Name cannot be empty")
    private String fullName;

    @NotBlank(message = "Address cannot be empty")
    private String address;

    @NotBlank(message = "City cannot be empty")
    private String city;

    @NotBlank(message = "State cannot be empty")
    private String state;

    @NotBlank(message = "Zip Code cannot be empty")
    @Pattern(regexp = "\\d{5,6}", message = "Zipcode must be 5 or 6 digits")
    private String zipcode;

    @Pattern(regexp = "\\d{10}", message = "Phone number must be 10 digits")
    private String phoneNumber;

    public AddressBookDTO(String fullName, String address, String city, String state, String zipcode, String phoneNumber) {
        this.fullName = fullName;
        this.address = address;
        this.city = city;
        this.state = state;
        this.zipcode = zipcode;
        this.phoneNumber = phoneNumber;
    }
}
