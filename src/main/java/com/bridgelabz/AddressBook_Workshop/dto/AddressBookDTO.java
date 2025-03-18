package com.bridgelabz.AddressBook_Workshop.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddressBookDTO {

    @NotBlank(message = "Name cannot be empty")
    private String name;

    @Email(message = "Invalid email format")
    private String email;

    @Pattern(regexp = "\\d{10}", message = "Phone number must be 10 digits")
    private String phoneNumber;

    @NotBlank(message = "Address cannot be empty")
    private String address;

//    public AddressBookDTO(String name, String email, String phoneNumber, String address) {
//        this.name = name;
//        this.email = email;
//        this.phoneNumber = phoneNumber;
//        this.address = address;
//    }

}

