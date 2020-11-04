package com.capgemini.databaseservice;

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

import com.capgemini.models.AddressBook;
import com.capgemini.models.Contact;

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
		HashMap<Integer, Boolean> additionStatus = new HashMap<Integer, Boolean>();
		int contactId[] = {0};
		int bookId[] = {0};
		boolean outcome[] = {false};
		String query;
		Connection[] connection = new Connection[1];
		try {
			connection[0] = this.getConnection();
			connection[0].setAutoCommit(false);
		} catch (SQLException e2) {
			e2.printStackTrace();
		}
		System.out.println(connection[0]);
		additionStatus.put(11, false);

		Runnable task3 = () -> {		
			contactId[0] = insertIntoContactTable(connection[0],firstName, lastName, address, city, state, zip, phoneNumber, email, dateAdded);
			additionStatus.put(11, true);		
		};
		Thread thread3 = new Thread(task3, firstName + "3");
		thread3.start();
		
		while (additionStatus.containsValue(false)) {
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
			
		try {
			query = String.format("select * from addressbook where book_name = '%s' and book_type = '%s'", bookName,
					bookType);
			Statement statement = connection[0].createStatement();
			ResultSet result3 = statement.executeQuery(query);
			while (result3.next()) {
				bookId[0] = (result3.getInt("id"));
			}
		} catch (SQLException e2) {
			try {
				connection[0].rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}
		if (bookId[0] == 0) {
			
			additionStatus.put(1, false);

			Runnable task1 = () -> {
				
					insertIntoAddressbookTable(connection[0], bookName, bookType);
					additionStatus.put(1, true);
				
			};
			Thread thread1 = new Thread(task1, firstName + "1");
			thread1.start();
		}
		
		additionStatus.put(2, false);

		Runnable task2 = () -> {
			
				outcome[0] = insertIntoBookContactTable(connection[0], contactId[0], bookId[0]);
				if (outcome[0] == true) {
					contactDataList.add(new Contact(firstName, lastName, address, city, state, zip, phoneNumber, email,
					bookName, bookType));
				}
				additionStatus.put(2, true);
			
		};
		Thread thread2 = new Thread(task2, firstName + "2");
		thread2.start();

		while (additionStatus.containsValue(false)) {
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		try {
			connection[0].commit();
		} catch (SQLException e1) {
			e1.printStackTrace();
		} finally {
			if (connection != null)
				try {
					connection[0].close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
		}
		return contactDataList;
	}
	
	private int insertIntoContactTable(Connection connection,String firstName, String lastName, String address, String city, String state,
			long zip, long phoneNumber, String email, LocalDate dateAdded){
		
		int contactId=0;
		String query = String.format(
				"insert into contact(firstName,lastName,address,city,state,zip,phone,email,date_added) "
						+ "values('%s','%s','%s','%s','%s',%s,%s,'%s','%s')",
				firstName, lastName, address, city, state, zip, phoneNumber, email, dateAdded);
		
		try (Statement statement = connection.createStatement();) {
			System.out.println(firstName);
			int rowAffected = statement.executeUpdate(query, statement.RETURN_GENERATED_KEYS);
			if (rowAffected == 1) {
				ResultSet result = statement.getGeneratedKeys();
				if(result.next()) 
				{
					contactId = result.getInt(1);	
					return contactId;
				}
			}
		} catch (SQLException e) {
			try {
				connection.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}
		return 0;
	}
	
	private int insertIntoAddressbookTable(Connection connection ,String bookName,String bookType){
		int bookId;
		String query = String.format("insert into addressbook(book_name,book_type) " + "values('%s','%s')", bookName,
				bookType);
		try (Statement statement = connection.createStatement();) {

			int rowAffected = statement.executeUpdate(query, statement.RETURN_GENERATED_KEYS);
			if (rowAffected == 1) {
				ResultSet result = statement.getGeneratedKeys();
				if (result.next())
				{
					bookId = result.getInt(1);
					return bookId;
				}
			}
		} catch (SQLException e) {
			try {
				connection.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}
		return 0;
	}
	
	private boolean insertIntoBookContactTable(Connection connection,int contactId,int bookId){
		String query = String.format("insert into book_contact(contact_id,book_id) " + "values('%s','%s')", contactId, bookId);
		try (Statement statement = connection.createStatement();) {
			int rowAffected = statement.executeUpdate(query);
			if (rowAffected == 1) {
				return true;
			}
		} catch (SQLException e) {
			try {
				connection.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}
		return false;
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
