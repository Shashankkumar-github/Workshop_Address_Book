package com.bridgelabz.AddressBook_Workshop.controller;

import com.bridgelabz.AddressBook_Workshop.dto.AddressBookDTO;
import com.bridgelabz.AddressBook_Workshop.service.IAddressBookService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.CrossOrigin;


import java.util.List;

@RestController
@RequestMapping("/api/addressbook")
@CrossOrigin(origins = "http://localhost:4200")
public class AddressBookController {
    @Autowired
    private IAddressBookService addressBookService;

    // Get all contacts
    @GetMapping
    public ResponseEntity<List<AddressBookDTO>> getAllContacts() {
        List<AddressBookDTO> contacts = addressBookService.getAllContacts();
        return ResponseEntity.ok(contacts);
    }

    // Get a contact by ID
    @GetMapping("/{id}")
    public ResponseEntity<AddressBookDTO> getContactById(@PathVariable Long id) {
        return addressBookService.getContactById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Add a new contact
    @PostMapping
    public ResponseEntity<AddressBookDTO> addContact(@Valid @RequestBody AddressBookDTO contactDTO) {
        AddressBookDTO savedContact = addressBookService.addContact(contactDTO);
        return ResponseEntity.ok(savedContact);
    }

    // Update a contact
    @PutMapping("/{id}")
    public ResponseEntity<AddressBookDTO> updateContact(@PathVariable Long id, @Valid @RequestBody AddressBookDTO updatedContact) {
        return ResponseEntity.ok(addressBookService.updateContact(id, updatedContact));
    }

    // Delete a contact
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteContact(@PathVariable Long id) {
        addressBookService.deleteContact(id);
        return ResponseEntity.noContent().build();
    }
}
