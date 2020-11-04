package com.capgemini.service;

import java.util.*;
import java.util.stream.*;
import java.nio.file.*;
import java.time.LocalDate;
import java.io.*;
import com.opencsv.*;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import com.capgemini.databaseservice.AddressBookDBService;
import com.capgemini.fileioservice.AddressBookFileService;
import com.capgemini.models.AddressBook;
import com.capgemini.models.Contact;
import com.csvjson.CSVUser;
import com.google.gson.Gson;

public class AddressBookService {
	private static Scanner sc = new Scanner(System.in);
	private static HashMap<String, AddressBook> addressBookList = new HashMap<String, AddressBook>();
	private static AddressBook addressBookObj;
	private static ArrayList<Contact> contanctList;
	private static HashMap<String, ArrayList<String>> statemap = new HashMap<String, ArrayList<String>>();
	private static ArrayList<String> al;
	private static int count;
	private static String fname, state, bookName = "";
	private static boolean check;
	private static Contact c;

	public void setAddressBookList(HashMap<String, AddressBook> addressBookList) {
		this.addressBookList = addressBookList;
	}

	public void processWriteRequest() throws CsvDataTypeMismatchException, CsvRequiredFieldEmptyException, IOException {
		System.out.println("Enter the type of file you want to write to : TXT , CSV, JSON");
		String extension = sc.nextLine();
		AddressBookFileService fileService = new AddressBookFileService(addressBookList);
		if (extension.equals("TXT"))
			fileService.writeAddressBookToFile();
		else if (extension.equals("CSV"))
			fileService.writeAddressBookToCSVFile();
		else
			fileService.writeAddressBookToJSONFile();

	}

	public void processReadRequest() throws IOException {
		System.out.println("Enter the type of file you want to write to : TXT , CSV, JSON");
		String extension = sc.nextLine();
		AddressBookFileService fileService = new AddressBookFileService(addressBookList);
		if (extension.equals("TXT"))
			fileService.readAddressBookFromFile();
		else if (extension.equals("CSV"))
			fileService.readAddressBookFromCSVFile();
		else
			fileService.readAddressBookFromJSONFile();

	}

	public void createAddressBook() {
		bookName = getAddressBookName();
		addressBookList.put(bookName, new AddressBook());
		System.out.println("A new AddressBook with name " + bookName + " is created succesfully");
	}

	public void addAContactToAddressBook() {
		bookName = getAddressBookName();
		if (addressBookList.containsKey(bookName)) {
			addressBookObj = (AddressBook) addressBookList.get(bookName);
			c = consoleInput();
			check = addressBookObj.checkIfContactExists(c.getFirstName());
			if (check) {
				System.out.println("A person already exists with the same FirstName,Duplicate entry not allowed!");
			} else {
				if (!statemap.isEmpty()) {
					if (statemap.containsKey(c.getState())) {
						al = statemap.get(c.getState());
						al.add(c.getFirstName());
						statemap.replace(c.getState(), al);
					} else {
						ArrayList<String> ll = new ArrayList<String>();
						ll.add(c.getFirstName());
						statemap.put(c.getState(), ll);
					}
				} else {
					ArrayList<String> ll = new ArrayList<String>();
					ll.add(c.getFirstName());
					statemap.put(c.getState(), ll);
				}
				addressBookObj.addContact(c);
				System.out.println("Contact added succesfully to the AddressBook " + bookName);
			}
		} else {
			System.out.println("No AddressBook exists with the name " + bookName);
		}
	}

	public void editAContactInAddressBook() {
		bookName = getAddressBookName();
		if (addressBookList.containsKey(bookName)) {
			addressBookObj = (AddressBook) addressBookList.get(bookName);
			System.out.println("Enter the First Name of the Contact to be edited");
			fname = sc.nextLine();
			check = addressBookObj.checkIfContactExists(fname);
			if (check) {
				addressBookObj.editContact(consoleInput());
				addressBookList.replace(bookName, addressBookObj);
				System.out.println("Details Edited Succesfully");
			}

			else {
				System.out.println("No Contact Exists with that First Name");
			}
		} else {
			System.out.println("No AddressBook exists with the name " + bookName);
		}
	}

	public void deleteAContactInAddressBook() {
		bookName = getAddressBookName();
		if (addressBookList.containsKey(bookName)) {
			addressBookObj = (AddressBook) addressBookList.get(bookName);
			System.out.println("Enter the First Name of the Contact to be edited");
			fname = sc.nextLine();
			check = addressBookObj.checkIfContactExists(fname);
			if (check) {
				addressBookObj.deleteContact(fname);
				addressBookList.replace(bookName, addressBookObj);
				System.out.println("Contact Deleted Succesfully");
			}

			else {
				System.out.println("No Contact Exists with that First Name");
			}
		} else {
			System.out.println("No AddressBook exists with the name " + bookName);
		}
	}

	public void addMultipleContactToAddressBook() {
		bookName = getAddressBookName();
		if (addressBookList.containsKey(bookName)) {
			addressBookObj = (AddressBook) addressBookList.get(bookName);
			System.out.println("Enter the No of Contacts to add");
			count = Integer.parseInt(sc.nextLine());
			for (int i = 0; i < count; i++) {
				c = consoleInput();
				check = addressBookObj.checkIfContactExists(c.getFirstName());
				if (check) {
					System.out.println("A person already exists with the same FirstName,Duplicate entry not allowed!");
				} else {
					if (!statemap.isEmpty()) {
						if (statemap.containsKey(c.getState())) {
							al = statemap.get(c.getState());
							al.add(c.getFirstName());
							statemap.replace(c.getState(), al);
						} else {
							ArrayList<String> ll = new ArrayList<String>();
							ll.add(c.getFirstName());
							statemap.put(c.getState(), ll);
						}
					} else {
						ArrayList<String> ll = new ArrayList<String>();
						ll.add(c.getFirstName());
						statemap.put(c.getState(), ll);
					}
					addressBookObj.addContact(c);
				}
			}
			System.out.println("All contacts added succesfully");
		} else {
			System.out.println("No AddressBook exists with the name " + bookName);
		}
	}

	public void searchPersonBasedOnState() {
		if (!addressBookList.isEmpty()) {
			System.out.println("Enter the name of the state");
			state = sc.nextLine();
			System.out.println("The list of people in the state " + state + " :");
			for (HashMap.Entry<String, AddressBook> entry : addressBookList.entrySet()) {
				AddressBook value = entry.getValue();
				contanctList = value.getAddressBook();
				for (Contact cc : contanctList) {
					if (cc.getState().equals(state)) {
						System.out.println(cc.getFirstName());
					}
				}
			}
		} else {
			System.out.println("No AddressBooks are added yet.");
		}
	}

	public void viewPersonBasedOnState() {
		System.out.println("Enter the name of the state");
		state = sc.nextLine();
		final String st = state;
		if (!statemap.isEmpty()) {
			statemap.entrySet().stream().filter(n -> n.getKey().equals(st)).flatMap(n -> n.getValue().stream())
					.forEach(n -> System.out.println(n));
		} else {
			System.out.println("No person is from the given state");
		}
	}

	public void viewCountOfPersosBasedOnState() {
		System.out.println("Enter the name of the state");
		state = sc.nextLine();
		final String st = state;
		if (!statemap.isEmpty()) {
			System.out.println(statemap.entrySet().stream().filter(n -> n.getKey().equals(st))
					.flatMap(n -> n.getValue().stream()).count() + " people are from the state " + state);
		} else {
			System.out.println("There are 0 persons form the given state");
		}
	}

	public void sortEntriesByName() {
		if (getContactList() != null) {
			ArrayList<Contact> sortedContactList = new ArrayList<Contact>(contanctList.stream()
					.sorted(Comparator.comparing(Contact::getFirstName)).collect(Collectors.toList()));
			addressBookObj.setAddressBook(sortedContactList);
			System.out.println("The Contacts in the address book are sorted succesfully.");
		} else {
			System.out.println("No AddressBook exists with the name " + bookName);
		}
	}

	public void sortEntriesByState() {
		if (getContactList() != null) {
			ArrayList<Contact> sortedContactList = new ArrayList<Contact>(
					contanctList.stream().sorted(Comparator.comparing(Contact::getState)).collect(Collectors.toList()));
			addressBookObj.setAddressBook(sortedContactList);
			System.out.println("The Contacts in the address book are sorted succesfully.");
		} else {
			System.out.println("No AddressBook exists with the name " + bookName);
		}
	}

	public void printAddressBook() {
		System.out.println("Enter the name of the address book");
		bookName = sc.nextLine();
		if (addressBookList.containsKey(bookName)) {
			addressBookObj = (AddressBook) addressBookList.get(bookName);
			contanctList = addressBookObj.getAddressBook();
			System.out.println("The contacts in the address book " + bookName + " are :");
			for (Contact cc : contanctList) {
				addressBookObj.printContact(cc.getFirstName());
			}
		} else {
			System.out.println("No AddressBook exists with the name " + bookName);
		}
	}

	private List<Contact> getContactList() {
		if (getAddressBookName() != null) {
			addressBookObj = (AddressBook) addressBookList.get(bookName);
			contanctList = addressBookObj.getAddressBook();
			return contanctList;
		}
		return null;
	}

	public String getAddressBookName() {
		System.out.println("Enter the name of the address book");
		bookName = sc.nextLine();
		return bookName;
	}

	public Contact consoleInput() {
		Scanner sc = new Scanner(System.in);
		System.out.println("Enter First Name");
		String fname = sc.nextLine();
		System.out.println("Enter Last Name");
		String lname = sc.nextLine();
		System.out.println("Enter Address");
		String city = sc.nextLine();
		System.out.println("Enter City");
		String address = sc.nextLine();
		System.out.println("Enter State");
		String state = sc.nextLine();
		System.out.println("Enter Zip");
		long zip = Long.parseLong(sc.nextLine());
		System.out.println("Enter Phone Number");
		long phno = Long.parseLong(sc.nextLine());
		System.out.println("Enter Email");
		String email = sc.nextLine();

		Contact c = new Contact(fname, lname, address, city, state, zip, phno, email);
		return c;
	}

	public HashMap<String, AddressBook> convertListToMap(List<Contact> contacts) {
		AddressBook contactList = new AddressBook();
		HashMap<String, AddressBook> addressBookList = new HashMap<String, AddressBook>();
		for (Contact contact : contacts) {
			String key = contact.getBookName() + "_" + contact.getBookType();
			if (addressBookList.containsKey(key)) {
				contactList = addressBookList.get(key);
				contactList.addContact(new Contact(contact.getFirstName(), contact.getLastName(), contact.getAddress(),
						contact.getCity(), contact.getState(), contact.getZip(), contact.getPhoneNumber(),
						contact.getEmail(), contact.getBookName(), contact.getBookType()));
				addressBookList.replace(key, contactList);
			} else {
				contactList = new AddressBook();
				contactList.addContact(new Contact(contact.getFirstName(), contact.getLastName(), contact.getAddress(),
						contact.getCity(), contact.getState(), contact.getZip(), contact.getPhoneNumber(),
						contact.getEmail(), contact.getBookName(), contact.getBookType()));
				addressBookList.put(key, contactList);
			}
		}
		return addressBookList;
	}

	public List<Contact> convertMapToList(HashMap<String, AddressBook> addressBooksList) {
		List<Contact> contactList = new ArrayList<Contact>();
		AddressBook adressbook;
		for (Map.Entry mapElement : addressBooksList.entrySet()) {
			adressbook = (AddressBook) mapElement.getValue();
			for (Contact c : adressbook.getAddressBook()) {
				contactList.add(c);
			}
		}
		return contactList;
	}

	public List<Contact> readData() {
		List<Contact> contactDataList = new ArrayList<Contact>();
		AddressBookDBService addressBookDBService = new AddressBookDBService();
		contactDataList = addressBookDBService.readData();
		addressBookList = convertListToMap(contactDataList);
		return contactDataList;
	}

	public void updateContact(String name, long phoneNumber) {
		List<Contact> contactDataList = new ArrayList<Contact>();
		AddressBookDBService addressBookDBService = new AddressBookDBService();
		contactDataList = addressBookDBService.updateContactPhoneNumber(name, phoneNumber);
		addressBookList = convertListToMap(contactDataList);
	}

	public boolean checkDBInSyncWithList(String name) {
		AddressBookDBService addressBookDBService = new AddressBookDBService();
		Contact contactDB = addressBookDBService.getContact(name);
		AddressBook contactList = null;

		for (Map.Entry mapElement : addressBookList.entrySet()) {
			contactList = (AddressBook) mapElement.getValue();
			if (contactList.checkIfContactExists(contactDB.getFirstName())) {
				for (Contact c : contactList.getAddressBook()) {
					if (c.equals(contactDB)) {
						return true;
					}
				}
			}
		}
		return false;
	}

	public List<Contact> readDataInDateRange(String startDate, String endDate) {
		AddressBookDBService addressBookDBService = new AddressBookDBService();
		List<Contact> contactDataList = addressBookDBService.readDataInDateRange(startDate, endDate);
		addressBookList = convertListToMap(contactDataList);
		return contactDataList;
	}

	public int getNoOfContactsByCity(String city) {
		AddressBookDBService addressBookDBService = new AddressBookDBService();
		return addressBookDBService.getNoOfContactsByCity(city);
	}

	public List<Contact> addNewContact(String firstName, String lastName, String address, String city, String state,
			long zip, long phoneNumber, String email, LocalDate dateAdded, String bookName, String bookType) {
		AddressBookDBService addressBookDBService = new AddressBookDBService();
		List<Contact> contactDataList = new ArrayList<Contact>();
		addressBookDBService.addNewContact(firstName, lastName, address, city, state, zip, phoneNumber, email,
				dateAdded, bookName, bookType);
		contactDataList = addressBookDBService.readData();
		addressBookList = convertListToMap(contactDataList);
		return contactDataList;
	}

	public List<Contact> addMultipleContacts(List<Contact> contacts) {
		HashMap<Integer, Boolean> additionStatus = new HashMap<Integer, Boolean>();

		contacts.forEach(c -> {
			additionStatus.put(c.hashCode(), false);
			Runnable task = () -> {
				System.out.println("Employee adding : " + Thread.currentThread().getName());
				addNewContact(c.firstName, c.lastName, c.address, c.city, c.state, c.zip, c.phoneNumber, c.email,
						c.dateAdded, c.bookName, c.bookType);
				System.out.println("Employee added : " + Thread.currentThread().getName());
				additionStatus.put(c.hashCode(), true);
			};
			Thread thread = new Thread(task, c.firstName);
			thread.start();
		});

		while (additionStatus.containsValue(false)) {
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		return convertMapToList(addressBookList);
	}

	public void addAContactToAddressBookList(Contact contact, String bookName, String bookType) {
		String key = bookName + "_" + bookType;
		AddressBook addressbook = new AddressBook();
		if (!addressBookList.containsKey(key)) {
			addressbook.setAddressBook(new ArrayList<Contact>(Arrays.asList(contact)));
			addressBookList.put(key, addressbook);
		} else {
			addressbook = addressBookList.get(key);
			addressbook.addContact(contact);
			addressBookList.replace(key, addressbook);
		}
	}

	public int getContactsCount() {
		int i[] = { 0 };
		addressBookList.forEach((k, v) -> {
			i[0] = i[0] + v.getAddressBook().size();
		});
		return i[0];
	}
	
	public void printAddressBookList()
	{
		addressBookList.forEach((k, v) -> {
			System.out.println(k);
			for(Contact c : v.getAddressBook())
			{
				System.out.println(c.firstName);
			}
		});
	}

	public HashMap<String, AddressBook> convertContactListToAddressBookInMap(
			HashMap<String, List<Contact>> addressBooks) {
		HashMap<String, AddressBook> addressBookList = new HashMap<String, AddressBook>();
		addressBooks.forEach((k, v) -> {
			AddressBook addressbook = new AddressBook();
			addressbook.setAddressBook((ArrayList<Contact>) v);
			addressBookList.put(k, addressbook);
		});
		return addressBookList;
	}

}
