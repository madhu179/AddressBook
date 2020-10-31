package databaseservice;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import models.AddressBook;
import models.Contact;

public class AddressBookDBService {

	private PreparedStatement preparedStatement;
	private List<Contact> contactDataList = new ArrayList<Contact>();
	private HashMap<String, AddressBook> addressBookDataList = new HashMap<String, AddressBook>();

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
			while (result.next()) {
				contactList.add(new Contact(result.getString("firstName"), result.getString("lastName"),
						result.getString("address"), result.getString("city"), result.getString("state"),
						result.getLong("zip"), result.getLong("phone"), result.getString("email"),
						result.getString("book_name"), result.getString("book_type")));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return contactList;
	}

	public List<Contact> updateContactPhoneNumber(String name, long phone) {
		try (Connection connection = this.getConnection();) {
			preparedStatement = connection.prepareStatement("update contact set phone = ? where firstName = ?");
			preparedStatement.setLong(1, phone);
			preparedStatement.setString(2, name);
			int result = preparedStatement.executeUpdate();
			if (result == 1)
				return readData();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
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
		Contact contactInList = null;
		for (Contact e : contactDataList) {
			if (e.getFirstName().equals(name)) {
				contactInList = e;
			}
		}
		return contactDB.equals(contactInList);
	}

	public Contact getContact(String name) {
		Contact contact = null;
		String query = String.format(
				"select * from contact join book_contact on contact.id = book_contact.contact_id join addressbook a on a.id = book_contact.book_id where firstName = '%s'",
				name);
		Statement statement;
		ResultSet result = null;
		try (Connection connection = this.getConnection();) {
			statement = connection.createStatement();
			result = statement.executeQuery(query);
			while (result.next()) {
				contact = new Contact(result.getString("firstName"), result.getString("lastName"),
						result.getString("address"), result.getString("city"), result.getString("state"),
						result.getLong("zip"), result.getLong("phone"), result.getString("email"),
						result.getString("book_name"), result.getString("book_type"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return contact;
	}

	public List<Contact> readDataInDateRange(String startDate, String endDate) {
		List<Contact> contactList = new ArrayList<Contact>();
		String query = String.format(
				"select * from contact join book_contact on contact.id = book_contact.contact_id join addressbook a on a.id = book_contact.book_id where date_added between cast('%s' as date) and cast('%s' as date)",
				startDate, endDate);
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

	public int getNoOfContactsByCity(String city) {
		int count = 0;
		String query = String.format("select * from contact where city = '%s'", city);
		Statement statement;
		ResultSet result = null;
		try (Connection connection = this.getConnection();) {
			statement = connection.createStatement();
			result = statement.executeQuery(query);
			while (result.next()) {
				count += 1;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return count;
	}

	public List<Contact> addNewContact(String firstName, String lastName, String address, String city, String state,
			long zip, long phoneNumber, String email, LocalDate dateAdded, String bookName, String bookType) {
		readData();
		int contactId = 0;
		String query = String.format(
				"insert into contact(firstName,lastName,address,city,state,zip,phone,email,date_added) "
						+ "values('%s','%s','%s','%s','%s',%s,%s,'%s','%s')",
				firstName, lastName, address, city, state, zip, phoneNumber, email, dateAdded);
		Connection connection = null;
		try {
			connection = this.getConnection();
			connection.setAutoCommit(false);
		} catch (SQLException e2) {
			e2.printStackTrace();
		}
		System.out.println(connection);
		try (Statement statement = connection.createStatement();) {
			System.out.println(firstName);
			int rowAffected = statement.executeUpdate(query, statement.RETURN_GENERATED_KEYS);
			System.out.println("rowAffected "+rowAffected);
			if (rowAffected == 1) {
				ResultSet result = statement.getGeneratedKeys();
				if(result.next()) 
					contactId = result.getInt(1);		
			}
		} catch (SQLException e) {
			try {
				connection.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}
		System.out.println("contactId " + contactId);
		int bookId = 0;
		try {
			query = String.format("select * from addressbook where book_name = '%s' and book_type = '%s'", bookName,
					bookType);
			Statement statement = connection.createStatement();
			ResultSet result3 = statement.executeQuery(query);
			while (result3.next()) {
				bookId = (result3.getInt("id"));
			}
		} catch (SQLException e2) {
			try {
				connection.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}
		if (bookId == 0) {
			query = String.format("insert into addressbook(book_name,book_type) " + "values('%s','%s')", bookName,
					bookType);
			try {
				connection = this.getConnection();
				connection.setAutoCommit(false);
			} catch (SQLException e2) {
				e2.printStackTrace();
			}
			try (Statement statement = connection.createStatement();) {

				int rowAffected = statement.executeUpdate(query, statement.RETURN_GENERATED_KEYS);
				if (rowAffected == 1) {
					ResultSet result = statement.getGeneratedKeys();
					if (result.next())
						bookId = result.getInt(1);
				}
			} catch (SQLException e) {
				try {
					connection.rollback();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
		}

		query = String.format("insert into book_contact(contact_id,book_id) " + "values('%s','%s')", contactId, bookId);
		try (Statement statement = connection.createStatement();) {
			int rowAffected = statement.executeUpdate(query);
			if (rowAffected == 1) {
				contactDataList.add(new Contact(firstName, lastName, address, city, state, zip, phoneNumber, email,
						bookName, bookType));
			}
		} catch (SQLException e) {
			try {
				connection.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}
		try {
			connection.commit();
		} catch (SQLException e1) {
			e1.printStackTrace();
		} finally {
			if (connection != null)
				try {
					connection.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
		}

		return contactDataList;
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
