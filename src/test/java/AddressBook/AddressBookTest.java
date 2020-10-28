package AddressBook;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

public class AddressBookTest {
	
	@Test
	public void readingFromDB_NoOfEntries_ShouldMatchActual() {
		AddressBookDBService addressBookDBService = new AddressBookDBService();
		List<Contact> entries = addressBookDBService.readData();
			boolean result = entries.size() == 7 ? true : false;
			Assert.assertTrue(result);		
	}
	
	@Test
	public void givenNewSalary_UpdatinginDB_UsingPreparedStatement_ShouldMatch() {
		AddressBookDBService addressBookDBService = new AddressBookDBService();
			List<Contact> entries = addressBookDBService.readData();
			addressBookDBService.updateContactPhoneNumber("Steve", 99989796);
			boolean result = addressBookDBService.checkDBInSyncWithList("Steve");
			Assert.assertTrue(result);	
	}
	
	@Test
	public void readingFromDB_NoOfEntries_InGivenDateRange_ShouldMatchActual() {
		AddressBookDBService addressBookDBService = new AddressBookDBService();
		List<Contact> entries = addressBookDBService.readDataInDateRange("2010-04-29","2018-04-29");
			boolean result = entries.size() == 3 ? true : false;
			Assert.assertTrue(result);		
	}
	
	@Test
	public void readingFromDB_NoOfEntries_ByGivenCity_ShouldMatchActual() {
		AddressBookDBService addressBookDBService = new AddressBookDBService();
		int entries = addressBookDBService.getNoOfContactsByCity("New York City");
			boolean result = entries == 2 ? true : false;
			Assert.assertTrue(result);		
	}

}
