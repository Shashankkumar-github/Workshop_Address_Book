package com.bridgelabz.AddressBook_Workshop.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "address_book")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class AddressBookEntry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fullName;
    private String address;
    private String city;
    private String state;
    private String zipcode;
    private String phoneNumber;

    // Constructor to map DTO to Entity
    public AddressBookEntry(String fullName, String address, String city, String state, String zipcode, String phoneNumber) {
        this.fullName = fullName;
        this.address = address;
        this.city = city;
        this.state = state;
        this.zipcode = zipcode;
        this.phoneNumber = phoneNumber;
    }
}
