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

}
