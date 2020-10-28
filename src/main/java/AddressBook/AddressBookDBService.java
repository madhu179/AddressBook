package AddressBook;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;


public class AddressBookDBService {
	
	private PreparedStatement preparedStatement;
	private List<Contact> contactDataList = new ArrayList<Contact>();
	
	public List<Contact> readData() {
		List<Contact> contactList = new ArrayList<Contact>();
		String query = "select * from contact join book_contact on contact.id = book_contact.contact_id join addressbook a on a.id = book_contact.book_id";
		Statement statement;
		ResultSet result = null;
		try (Connection connection = this.getConnection();) {
				statement = connection.createStatement();
				result = statement.executeQuery(query);
				contactList = getDatafromResultset(result);
			} catch (SQLException e) {
				e.printStackTrace();
			}	
		contactDataList = contactList;
		return contactList;
	}
	
	private List<Contact> getDatafromResultset(ResultSet result) {
		List<Contact> contactList = new ArrayList<Contact>();
		try {
			while(result.next())
			{
				contactList.add(new Contact(result.getString("firstName"),result.getString("lastName"),result.getString("address"),result.getString("city"),
						result.getString("state"),result.getLong("zip"),result.getLong("phone"),result.getString("email"),
						result.getString("book_name"),result.getString("book_type")));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return contactList;
	}
	
	public void updateContactPhoneNumber(String name, long phone) {
		try (Connection connection = this.getConnection();) {
			preparedStatement = connection.prepareStatement("update contact set phone = ? where firstName = ?");
			preparedStatement.setLong(1, phone);
			preparedStatement.setString(2, name);
			int result = preparedStatement.executeUpdate();
			if(result==1)
				updatePhoneNumberInList(name,phone);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void updatePhoneNumberInList(String name, long phone) {
			for (Contact e : contactDataList) {
				if (e.getFirstName().equals(name)) {
					e.setPhoneNumber(phone);
				}
			}
		}

	public boolean checkDBInSyncWithList(String name) {
		Contact contactDB = getContact(name);
		Contact contactInList=null;
		for (Contact e : contactDataList) {
			if (e.getFirstName().equals(name)) {
				contactInList = e;
			}
		}
		return contactDB.equals(contactInList);
	}

	private Contact getContact(String name) {
		Contact contact=null;
		String query = String.format("select * from contact join book_contact on contact.id = book_contact.contact_id join addressbook a on a.id = book_contact.book_id where firstName = '%s'",name);
		Statement statement;
		ResultSet result = null;
		try (Connection connection = this.getConnection();) {
				statement = connection.createStatement();
				result = statement.executeQuery(query);
				while(result.next())
				{
					contact = new Contact(result.getString("firstName"),result.getString("lastName"),result.getString("address"),result.getString("city"),
						result.getString("state"),result.getLong("zip"),result.getLong("phone"),result.getString("email"),
						result.getString("book_name"),result.getString("book_type"));
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}	
		return contact;
	}

	private Connection getConnection() {
		String jdbcURL = "jdbc:mysql://localhost:3306/addressbook_service?useSSL=false";
		String userName = "root";
		String password = "Fightclub@8.8";
		Connection connection = null;	
			try {
				connection = DriverManager.getConnection(jdbcURL, userName, password);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		
		return connection;
	}


}
