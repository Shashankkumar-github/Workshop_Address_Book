package com.bridgelabz.AddressBook_Workshop.controller;

import com.bridgelabz.AddressBook_Workshop.dto.AddressBookDTO;
import com.bridgelabz.AddressBook_Workshop.service.IAddressBookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AddressBookControllerTest {

    @Mock
    private IAddressBookService addressBookService;

    @InjectMocks
    private AddressBookController addressBookController;

    private AddressBookDTO validContact;
    private AddressBookDTO updatedContact;
    private AddressBookDTO invalidContact;
    private Long validId;
    private Long invalidId;

    @BeforeEach
    void setUp() {
        validContact = new AddressBookDTO("John Doe", "9876543210", "john@example.com", "New York");
        updatedContact = new AddressBookDTO("Jane Doe", "9123456780", "jane@example.com", "California");
        invalidContact = null;
        validId = 1L;
        invalidId = 99L;
    }

    // ✅ Test Get All Contacts (Valid)
    @Test
    void getAllContactsTest() {
        when(addressBookService.getAllContacts()).thenReturn(List.of(validContact));

        ResponseEntity<List<AddressBookDTO>> response = addressBookController.getAllContacts();

        assertFalse(response.getBody().isEmpty());
        assertEquals(1, response.getBody().size());
        assertTrue(response.getBody().contains(validContact));
    }

    // ✅ Test Get Contact By ID (Valid & Invalid Combined)
    @Test
    void getContactByIdTest() {
        // Valid contact scenario
        when(addressBookService.getContactById(validId)).thenReturn(Optional.of(validContact));

        ResponseEntity<AddressBookDTO> validResponse = addressBookController.getContactById(validId);
        assertTrue(validResponse.getBody().getName().equals("John Doe"));

        // Invalid contact scenario
        when(addressBookService.getContactById(invalidId)).thenReturn(Optional.empty());

        ResponseEntity<AddressBookDTO> invalidResponse = addressBookController.getContactById(invalidId);
        assertFalse(invalidResponse.hasBody());
        assertEquals(404, invalidResponse.getStatusCode().value());
    }

    // ✅ Test Add Contact (Valid & Exception Combined)
    @Test
    void addContactTest() {
        // Valid contact scenario
        when(addressBookService.addContact(validContact)).thenReturn(validContact);

        ResponseEntity<AddressBookDTO> validResponse = addressBookController.addContact(validContact);
        assertTrue(validResponse.getBody().getName().equals("John Doe"));

        // Exception scenario (Simulating DB failure)
        when(addressBookService.addContact(any())).thenThrow(new RuntimeException("Database error"));
        Exception exception = assertThrows(RuntimeException.class, () ->
                addressBookController.addContact(validContact)
        );
        assertEquals("Database error", exception.getMessage());
    }

    // ✅ Test Update Contact (Valid, Invalid, and Exception Combined)
    @Test
    void updateContactTest() {
        // Valid update scenario
        when(addressBookService.updateContact(validId, updatedContact)).thenReturn(updatedContact);

        ResponseEntity<AddressBookDTO> validResponse = addressBookController.updateContact(validId, updatedContact);
        assertTrue(validResponse.getBody().getName().equals("Jane Doe"));

        // Invalid update scenario
        when(addressBookService.updateContact(invalidId, updatedContact)).thenThrow(new RuntimeException("Contact not found"));

        Exception invalidException = assertThrows(RuntimeException.class, () ->
                addressBookController.updateContact(invalidId, updatedContact)
        );
        assertEquals("Contact not found", invalidException.getMessage());
    }

    // ✅ Test Delete Contact (Valid & Exception Combined)
    @Test
    void deleteContactTest() {
        // Valid delete scenario
        doNothing().when(addressBookService).deleteContact(validId);
        ResponseEntity<Void> validResponse = addressBookController.deleteContact(validId);
        assertEquals(204, validResponse.getStatusCode().value());

        // Exception scenario (Simulating contact not found)
        doThrow(new RuntimeException("Contact not found")).when(addressBookService).deleteContact(invalidId);

        Exception invalidException = assertThrows(RuntimeException.class, () ->
                addressBookController.deleteContact(invalidId)
        );
        assertEquals("Contact not found", invalidException.getMessage());
    }
}
