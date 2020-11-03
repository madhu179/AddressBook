package addressbooktest;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;

import io.restassured.RestAssured;
import io.restassured.config.ConnectionConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import models.AddressBook;
import models.Contact;
import service.AddressBookService;

public class AddressBookTest {

	@Test
	public void readingFromDB_NoOfEntries_ShouldMatchActual() {
		AddressBookService addressBookService = new AddressBookService();
		List<Contact> contactList = addressBookService.readData();
		boolean result = contactList.size() == 8 ? true : false;
		Assert.assertTrue(result);
	}

	@Test
	public void givenNewSalary_UpdatinginDB_UsingPreparedStatement_ShouldMatch() {
		AddressBookService addressBookService = new AddressBookService();
		List<Contact> contactList = addressBookService.readData();
		addressBookService.updateContact("Steve", 77789796);
		boolean result = addressBookService.checkDBInSyncWithList("Steve");
		Assert.assertTrue(result);
	}

	@Test
	public void readingFromDB_NoOfEntries_InGivenDateRange_ShouldMatchActual() {
		AddressBookService addressBookService = new AddressBookService();
		List<Contact> contactList = addressBookService.readDataInDateRange("2010-04-29", "2018-04-29");
		boolean result = contactList.size() == 3 ? true : false;
		Assert.assertTrue(result);
	}

	@Test
	public void readingFromDB_NoOfEntries_ByGivenCity_ShouldMatchActual() {
		AddressBookService addressBookService = new AddressBookService();
		int noOfContacts = addressBookService.getNoOfContactsByCity("New York City");
		boolean result = noOfContacts == 3 ? true : false;
		Assert.assertTrue(result);
	}

	@Test
	public void givenNewEmployee_WhenAddedWithPayrollDataNewERDiagram_ShouldSyncWithDB() {
		AddressBookService addressBookService = new AddressBookService();
		List<Contact> entries = addressBookService.addNewContact("Peter", "Parker", "20 Ingram St.", "New York City",
				"New York", 11375, 32333435, "peterparker@gmail.com", LocalDate.parse("2019-06-29"), "book1", "family");
		boolean result = entries.size() == 8 ? true : false;
		Assert.assertTrue(result);
	}

	@Test
	public void givenMultipleContacts_WhenAddedToAddressbookDB_ShouldBeInSync() {
		AddressBookService addressBookService = new AddressBookService();
		Contact[] contacts = {
				new Contact("Peter", "Parker", "20 Ingram St.", "New York City", "New York", 11375, 32333435,
						"peterparker@gmail.com", LocalDate.parse("2019-06-29"), "book1", "family"),
				new Contact("Wade", "Wilson", "Vancouver, Canada", "Vancouver", "BritishColumbia", 15342, 54345434,
						"wadedeadpool@gmail.com", LocalDate.parse("2016-02-12"), "book1", "family") };
		Instant start = Instant.now();
		List<Contact> entries = addressBookService.addMultipleContacts(Arrays.asList(contacts));
		Instant end = Instant.now();
		System.out.println("Duration with Thread : " + Duration.between(start, end));
		System.out.println(entries.size());
		boolean result = entries.size() == 9 ? true : false;
		Assert.assertTrue(result);
	}

	@Before
	public void setup() {
		RestAssured.baseURI = "http://localhost";
		RestAssured.port = 3000;
		RestAssured.config = RestAssuredConfig.config().connectionConfig(
				new ConnectionConfig().closeIdleConnectionsAfterEachResponseAfter(1, TimeUnit.SECONDS));
	}

	public HashMap<String, List<Contact>> getContactList() {
		String[] resources = { "book1_family", "book2_family", "book1_friends", "book2_friends" };
		Type type;
		List<Contact> contactList;
		HashMap<String, List<Contact>> addressBookList = new HashMap<String, List<Contact>>();
		for (String s : resources) {
			Response response = RestAssured.get("/" + s);
			if (response.getStatusCode() < 400) {
				type = new TypeToken<List<Contact>>() {
				}.getType();
				contactList = new Gson().fromJson(response.asString(), type);
				addressBookList.put(s, contactList);
			}
		}
		return addressBookList;
	}

	public Response addContactToJsonServer(String resource, Contact contact) {
		String jsonString = new Gson().toJson(contact);
		RequestSpecification request = RestAssured.given().config(RestAssuredConfig.config()
				.connectionConfig(ConnectionConfig.connectionConfig().closeIdleConnectionsAfterEachResponse()));
		request.header("Content-Type", "application/json");
		request.body(jsonString);
		return request.post("/" + resource);
	}

	@Test
	public void retreiveContacts_FromJsonServer_ShouldMatchCount() {
		AddressBookService addressbookService = new AddressBookService();
		HashMap<String, List<Contact>> addressBooks = getContactList();
		HashMap<String, AddressBook> addressBookList = addressbookService
				.convertContactListToAddressBookInMap(addressBooks);
		addressbookService.setAddressBookList(addressBookList);
		int CountOfContacts = addressbookService.getContactsCount();
		Assert.assertEquals(6, CountOfContacts);
	}

	@Test
	public void givenAContact_WriteToJsonServer_ShouldReturnSuccessCode() {
		AddressBookService addressbookService = new AddressBookService();
		HashMap<String, List<Contact>> addressBooks = getContactList();
		HashMap<String, AddressBook> addressBookList = addressbookService
				.convertContactListToAddressBookInMap(addressBooks);
		addressbookService.setAddressBookList(addressBookList);
		String resource;
		Contact contact = new Contact("Miles", "Morales", "Vancouver, Canada", "Vancouver", "BritishColumbia", 15342,
				54345434, "wadedeadpool@gmail.com", LocalDate.parse("2016-02-12"), "book1", "family");
		resource = contact.bookName + "_" + contact.bookType;
		Response response = addContactToJsonServer(resource, contact);
		Assert.assertEquals(201, response.getStatusCode());
		addressbookService.addAContactToAddressBookList(contact, contact.bookName, contact.bookType);
		int CountOfContacts = addressbookService.getContactsCount();
		Assert.assertEquals(7, CountOfContacts);
	}

}
