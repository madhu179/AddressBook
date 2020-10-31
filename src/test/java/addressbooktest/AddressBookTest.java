package addressbooktest;

import java.util.List;
import org.junit.Assert;
import org.junit.Test;

import pojo.Contact;
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
				"New York", 11375, 32333435, "peterparker@gmail.com", "2019-06-29", "book1", "family");
		boolean result = entries.size() == 8 ? true : false;
		Assert.assertTrue(result);
	}

}
