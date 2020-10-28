package AddressBook;

import java.util.*;
import java.util.stream.*;
import java.nio.file.*;
import java.io.*;
import com.opencsv.*;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import com.csvjson.CSVUser;
import com.google.gson.Gson;

public class AddressBookMain {
	private static Scanner sc = new Scanner(System.in);
	private static final String HOME = System.getProperty("user.home");
	private static final String FOLDER = "AddressBookFolder";
	private static HashMap<String, AddressBook> AddressBookList = new HashMap<String, AddressBook>();
	private static AddressBook addressBookObj;
	private static ArrayList<Contact> contanctList;
	private static HashMap<String, ArrayList<String>> statemap = new HashMap<String, ArrayList<String>>();
	private static ArrayList<String> al;
	private static int option, count;
	private static String fname, lname, address, city, state, email, bookName = "";
	private static long zip, phno;
	private static boolean check;
	private static Contact c;

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
				createAddressBook();
				break;
			case 2:
				addAContactToAddressBook();
				break;
			case 3:
				editAContactInAddressBook();
				break;
			case 4:
				deleteAContactInAddressBook();
				break;
			case 5:
				addMultipleContactToAddressBook();
				break;
			case 6:
				searchPersonBasedOnState();
				break;
			case 7:
				viewPersonBasedOnState();
				break;
			case 8:
				viewCountOfPersosBasedOnState();
				break;
			case 9:
				sortEntriesByName();
				break;
			case 10:
				sortEntriesByState();
				break;
			case 11:
				processWriteRequest();
				break;
			case 12:
				processReadRequest();
				break;
			case 13:
				printAddressBook();
				break;
			case 14:
				break;
			default:
				System.out.println("Choose from the given options");
			}

		} while (option != 14);

	}

	public static void processWriteRequest()
			throws CsvDataTypeMismatchException, CsvRequiredFieldEmptyException, IOException {
		System.out.println("Enter the type of file you want to write to : TXT , CSV, JSON");
		String extension = sc.nextLine();
		AddressBookFileService fileService = new AddressBookFileService(AddressBookList);
		if (extension.equals("TXT"))
			fileService.writeAddressBookToFile();
		else if (extension.equals("CSV"))
			fileService.writeAddressBookToCSVFile();
		else
			fileService.writeAddressBookToJSONFile();

	}

	public static void processReadRequest() throws IOException {
		System.out.println("Enter the type of file you want to write to : TXT , CSV, JSON");
		String extension = sc.nextLine();
		AddressBookFileService fileService = new AddressBookFileService(AddressBookList);
		if (extension.equals("TXT"))
			fileService.readAddressBookFromFile();
		else if (extension.equals("CSV"))
			fileService.readAddressBookFromCSVFile();
		else
			fileService.readAddressBookFromJSONFile();

	}

	public static void createAddressBook() {
		bookName = getAddressBookName();
		AddressBookList.put(bookName, new AddressBook());
		System.out.println("A new AddressBook with name " + bookName + " is created succesfully");
	}

	public static void addAContactToAddressBook() {
		bookName = getAddressBookName();
		if (AddressBookList.containsKey(bookName)) {
			addressBookObj = (AddressBook) AddressBookList.get(bookName);
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

	public static void editAContactInAddressBook() {
		bookName = getAddressBookName();
		if (AddressBookList.containsKey(bookName)) {
			addressBookObj = (AddressBook) AddressBookList.get(bookName);
			System.out.println("Enter the First Name of the Contact to be edited");
			fname = sc.nextLine();
			check = addressBookObj.checkIfContactExists(fname);
			if (check) {
				addressBookObj.editContact(consoleInput());
				AddressBookList.replace(bookName, addressBookObj);
				System.out.println("Details Edited Succesfully");
			}

			else {
				System.out.println("No Contact Exists with that First Name");
			}
		} else {
			System.out.println("No AddressBook exists with the name " + bookName);
		}
	}

	public static void deleteAContactInAddressBook() {
		bookName = getAddressBookName();
		if (AddressBookList.containsKey(bookName)) {
			addressBookObj = (AddressBook) AddressBookList.get(bookName);
			System.out.println("Enter the First Name of the Contact to be edited");
			fname = sc.nextLine();
			check = addressBookObj.checkIfContactExists(fname);
			if (check) {
				addressBookObj.deleteContact(fname);
				AddressBookList.replace(bookName, addressBookObj);
				System.out.println("Contact Deleted Succesfully");
			}

			else {
				System.out.println("No Contact Exists with that First Name");
			}
		} else {
			System.out.println("No AddressBook exists with the name " + bookName);
		}
	}

	public static void addMultipleContactToAddressBook() {
		bookName = getAddressBookName();
		if (AddressBookList.containsKey(bookName)) {
			addressBookObj = (AddressBook) AddressBookList.get(bookName);
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

	public static void searchPersonBasedOnState() {
		if (!AddressBookList.isEmpty()) {
			System.out.println("Enter the name of the state");
			state = sc.nextLine();
			System.out.println("The list of people in the state " + state + " :");
			for (HashMap.Entry<String, AddressBook> entry : AddressBookList.entrySet()) {
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

	public static void viewPersonBasedOnState() {
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

	public static void viewCountOfPersosBasedOnState() {
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

	public static void sortEntriesByName() {
		if(getContactList()!=null) {
			ArrayList<Contact> sortedContactList = new ArrayList<Contact>(contanctList.stream()
					.sorted(Comparator.comparing(Contact::getFirstName)).collect(Collectors.toList()));
			addressBookObj.setAddressBook(sortedContactList);
			System.out.println("The Contacts in the address book are sorted succesfully.");
		} else {
			System.out.println("No AddressBook exists with the name " + bookName);
		}
	}

	public static void sortEntriesByState() {
			if(getContactList()!=null) {
			ArrayList<Contact> sortedContactList = new ArrayList<Contact>(
					contanctList.stream().sorted(Comparator.comparing(Contact::getState)).collect(Collectors.toList()));
			addressBookObj.setAddressBook(sortedContactList);
			System.out.println("The Contacts in the address book are sorted succesfully.");
		} else {
			System.out.println("No AddressBook exists with the name " + bookName);
		}
	}
	
	

	public static void printAddressBook() {
		System.out.println("Enter the name of the address book");
		bookName = sc.nextLine();
		if (AddressBookList.containsKey(bookName)) {
			addressBookObj = (AddressBook) AddressBookList.get(bookName);
			contanctList = addressBookObj.getAddressBook();
			System.out.println("The contacts in the address book " + bookName + " are :");
			for (Contact cc : contanctList) {
				addressBookObj.printContact(cc.getFirstName());
			}
		} else {
			System.out.println("No AddressBook exists with the name " + bookName);
		}
	}
	
	private static List<Contact> getContactList() {
		if (getAddressBookName() != null) {
			addressBookObj = (AddressBook) AddressBookList.get(bookName);
			contanctList = addressBookObj.getAddressBook();
			return contanctList;
		}
		return null;
	}

	public static String getAddressBookName() {
		System.out.println("Enter the name of the address book");
		 bookName = sc.nextLine();
		return bookName;
	}

	public static Contact consoleInput() {
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
}
