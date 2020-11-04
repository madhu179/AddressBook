package com.capgemini.utility;

import java.io.IOException;
import java.util.Scanner;

import com.capgemini.service.AddressBookService;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;

public class AddressBookMain {

	private static int option;
	private static Scanner sc = new Scanner(System.in);
	private static AddressBookService addressBookService;

	public AddressBookMain() {
		addressBookService = new AddressBookService();
	}

	public static void main(String args[])
			throws CsvDataTypeMismatchException, CsvRequiredFieldEmptyException, IOException {
		do {
			System.out.println("Choose one of the following : ");
			System.out.println("1. Create a AddressBook");
			System.out.println("2. Add a contact to a particular AddressBook");
			System.out.println("3. Edit a contact to a particular AddressBook");
			System.out.println("4. Delete a Contact in a particular AddressBook");
			System.out.println("5. Add Multiple Contacts to a particular AddressBook");
			System.out.println("6. Search person based on state across all AddressBooks");
			System.out.println("7. View person based on state across all AddressBooks");
			System.out.println("8. View No of contact persons from a state across all AddressBooks");
			System.out.println("9. Sort the entries in a particular address book by person name");
			System.out.println("10. Sort the entries in a particular address book by state");
			System.out.println("11. Write Details of an AddressBook to a File");
			System.out.println("12. Read Details of an AddressBook from a File");
			System.out.println("13. Print Details of a AddressBook");
			System.out.println("14. Exit");

			option = Integer.parseInt(sc.nextLine());

			switch (option) {
			case 1:
				addressBookService.createAddressBook();
				break;
			case 2:
				addressBookService.addAContactToAddressBook();
				break;
			case 3:
				addressBookService.editAContactInAddressBook();
				break;
			case 4:
				addressBookService.deleteAContactInAddressBook();
				break;
			case 5:
				addressBookService.addMultipleContactToAddressBook();
				break;
			case 6:
				addressBookService.searchPersonBasedOnState();
				break;
			case 7:
				addressBookService.viewPersonBasedOnState();
				break;
			case 8:
				addressBookService.viewCountOfPersosBasedOnState();
				break;
			case 9:
				addressBookService.sortEntriesByName();
				break;
			case 10:
				addressBookService.sortEntriesByState();
				break;
			case 11:
				addressBookService.processWriteRequest();
				break;
			case 12:
				addressBookService.processReadRequest();
				break;
			case 13:
				addressBookService.printAddressBook();
				break;
			case 14:
				break;
			default:
				System.out.println("Choose from the given options");
			}

		} while (option != 14);

	}

}
