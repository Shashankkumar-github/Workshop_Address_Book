package com.bridgelabz.AddressBook_Workshop.service;

import com.bridgelabz.AddressBook_Workshop.model.AddressBookEntry;
import com.bridgelabz.AddressBook_Workshop.repository.AddressBookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AddressBookService {
    @Autowired
    private AddressBookRepository addressBookRepository;

    public List<AddressBookEntry> getAllContacts() {
        return addressBookRepository.findAll();
    }
}
