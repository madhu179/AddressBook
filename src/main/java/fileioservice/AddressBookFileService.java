package fileioservice;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.google.gson.Gson;
import com.opencsv.CSVWriter;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;

import models.AddressBook;
import models.Contact;

public class AddressBookFileService {
	private static final String HOME = System.getProperty("user.home");
	private static final String FOLDER = "AddressBookFolder";
	private static String folderPath = HOME + "\\capg-training\\assignment2\\AddressBook\\AddressBook\\" + FOLDER;
	private static Scanner sc = new Scanner(System.in);
	private static ArrayList<Contact> contanctList;
	private static AddressBook addressBookObj;
	private static String bookName;
	private static HashMap<String, AddressBook> AddressBookList;

	public AddressBookFileService(HashMap<String, AddressBook> AddressBookList) {
		this.AddressBookList = AddressBookList;
	}

	public void writeAddressBookToJSONFile() throws IOException {
		createMainFolderAndFileFolderIfNotExist(folderPath, "\\JSONFiles");

		if (getContactList() != null) {
			String stringFilePath = HOME + "\\capg-training\\assignment2\\AddressBook\\AddressBook\\" + FOLDER
					+ "\\JSONFiles\\" + bookName + ".json";

			createFileIfNotExists(stringFilePath);

			Gson gson = new Gson();
			String jsonString = gson.toJson(contanctList);
			FileWriter writer = new FileWriter(stringFilePath);
			writer.write(jsonString);
			System.out.println("Details succesfully added to address book file");
			writer.close();
		}
	}

	public void writeAddressBookToCSVFile()
			throws CsvDataTypeMismatchException, CsvRequiredFieldEmptyException, IOException {

		createMainFolderAndFileFolderIfNotExist(folderPath, "\\CSVFiles");

		if (getContactList() != null) {
			String stringFilePath = HOME + "\\capg-training\\assignment2\\AddressBook\\AddressBook\\" + FOLDER
					+ "\\CSVFiles\\" + bookName + ".csv";

			createFileIfNotExists(stringFilePath);

			FileWriter writer = new FileWriter(stringFilePath);

			StatefulBeanToCsvBuilder<Contact> builder = new StatefulBeanToCsvBuilder(writer)
					.withQuotechar(CSVWriter.NO_QUOTE_CHARACTER);
			StatefulBeanToCsv beanWriter = builder.build();

			beanWriter.write(contanctList);
			System.out.println("Details succesfully added to address book file");

			writer.close();
		} else {
			System.out.println("No AddressBook exists with the name " + bookName);
		}

	}

	public void writeAddressBookToFile() throws IOException {
		createMainFolderAndFileFolderIfNotExist(folderPath, "\\TextFiles");

		if (getContactList() != null) {
			StringBuffer data = new StringBuffer();
			for (Contact cc : contanctList) {
				data.append(cc.toString() + "\n");
			}
			String stringFilePath = HOME + "\\capg-training\\assignment2\\AddressBook\\AddressBook\\" + FOLDER
					+ "\\TextFiles\\" + bookName + ".txt";

			createFileIfNotExists(stringFilePath);

			try {
				Files.write(Paths.get(stringFilePath), data.toString().getBytes());
				System.out.println("Details succesfully added to address book file");
			} catch (IOException e) {
				e.printStackTrace();
			}

		} else {
			System.out.println("No AddressBook exists with the name " + bookName);
		}
	}

	public void readAddressBookFromJSONFile() throws IOException {
		if (checkAddressBookListContainsBook() != null) {
			String stringFilePath = HOME + "\\capg-training\\assignment2\\AddressBook\\AddressBook\\" + FOLDER
					+ "\\JSONFiles\\" + bookName + ".json";
			if (Files.exists(Paths.get(stringFilePath))) {
				Gson gson = new Gson();
				BufferedReader buffRead = new BufferedReader(new FileReader(stringFilePath));
				Contact[] userObject = gson.fromJson(buffRead, Contact[].class);
				List<Contact> contactData = Arrays.asList(userObject);

				for (Contact contact : contactData) {
					System.out.println("FirstName=" + contact.getFirstName() + ", LastName=" + contact.getLastName()
							+ ", Address=" + contact.getAddress() + ", City=" + contact.getCity() +", State=" + contact.getState() + ", zip="
							+ contact.getZip() + ", phoneNumber=" + contact.getPhoneNumber() + ", email="
							+ contact.getEmail());
				}

			} else {
				System.out.println("No AddressBook exists with the name " + bookName);
			}
		} else {
			System.out.println("No AddressBook exists with the name " + bookName);
		}

	}

	public void readAddressBookFromCSVFile() throws IOException {
		if (checkAddressBookListContainsBook() != null) {
			Path filePath = Paths.get(HOME + "\\capg-training\\assignment2\\AddressBook\\AddressBook\\" + FOLDER
					+ "\\CSVFiles\\" + bookName + ".csv");
			if (Files.exists(filePath)) {
				Reader reader = Files.newBufferedReader(filePath);

				CsvToBean<Contact> csvToBean = new CsvToBeanBuilder(reader).withType(Contact.class)
						.withIgnoreLeadingWhiteSpace(true).build();

				List<Contact> contactData = csvToBean.parse();

				for (Contact contact : contactData) {
					System.out.println("FirstName=" + contact.getFirstName() + ", LastName=" + contact.getLastName()
							+ ", Address=" + contact.getAddress() + ", City=" + contact.getCity() + ", State=" + contact.getState() + ", zip="
							+ contact.getZip() + ", phoneNumber=" + contact.getPhoneNumber() + ", email="
							+ contact.getEmail());
				}

			} else {
				System.out.println("No AddressBook exists with the name " + bookName);
			}
		}

		else {
			System.out.println("No AddressBook exists with the name " + bookName);
		}
	}

	public void readAddressBookFromFile() throws IOException {
		if (checkAddressBookListContainsBook() != null) {
			List<String[]> listOfElements = null;
			Path filePath = Paths.get(FOLDER + "\\TextFiles\\" + bookName + ".txt");
			if (Files.exists(filePath)) {
				Stream<String> stringStr = Files.lines(filePath);
				listOfElements = stringStr.map(s -> s.split(", ")).collect(Collectors.toList());
				System.out.println("The contacts in the address book are : ");
				for (String[] e : listOfElements) {
					for (String str : e) {
						System.out.println(str);
					}
					System.out.println("");
				}
			} else {
				System.out.println("No AddressBook exists with the name " + bookName);
			}

		} else {
			System.out.println("No AddressBook exists with the name " + bookName);
		}
	}

	private void createFileIfNotExists(String filePath) {
		if (Files.notExists(Paths.get(filePath))) {
			try {
				Files.createFile(Paths.get(filePath));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private void createMainFolderAndFileFolderIfNotExist(String path, String folderName) {
		if (Files.notExists(Paths.get(path))) {
			try {
				Files.createDirectory(Paths.get(path + folderName));
				Files.createDirectory(Paths.get(path));
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			if (Files.notExists(Paths.get(path + folderName))) {
				try {
					Files.createDirectory(Paths.get(path + folderName));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private String checkAddressBookListContainsBook() {
		System.out.println("Enter the name of the address book");
		bookName = sc.nextLine();
		if (AddressBookList.containsKey(bookName)) {
			return bookName;
		}
		return null;
	}

	private List<Contact> getContactList() {
		if (checkAddressBookListContainsBook() != null) {
			addressBookObj = (AddressBook) AddressBookList.get(bookName);
			contanctList = addressBookObj.getAddressBook();
			return contanctList;
		}
		return null;
	}

}
