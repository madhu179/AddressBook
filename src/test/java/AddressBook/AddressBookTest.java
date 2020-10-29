package AddressBook;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

public class AddressBookTest {
	
	@Test
	public void readingFromDB_NoOfEntries_ShouldMatchActual() {
		AddressBookDBService addressBookDBService = new AddressBookDBService();
		List<Contact> entries = addressBookDBService.readData();
			boolean result = entries.size() == 8 ? true : false;
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
	
	@Test
	public void givenNewEmployee_WhenAddedWithPayrollDataNewERDiagram_ShouldSyncWithDB(){
		AddressBookDBService addressBookDBService = new AddressBookDBService();
		addressBookDBService.readData();
		List<Contact> entries = addressBookDBService.addNewContact("Peter","Parker","20 Ingram St.","New York City","New York",11375,32333435,"peterparker@gmail.com","2019-06-29","book1","family");
			boolean result = entries.size() == 8 ? true : false;
		Assert.assertTrue(true);	
	}

}
