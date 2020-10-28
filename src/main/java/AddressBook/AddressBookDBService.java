package AddressBook;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class AddressBookDBService {

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
