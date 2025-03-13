package com.bridgelabz.AddressBook_Workshop.repository;

import com.bridgelabz.AddressBook_Workshop.model.AddressBookEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressBookRepository extends JpaRepository<AddressBookEntry, Long> {
}
