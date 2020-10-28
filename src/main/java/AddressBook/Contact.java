package AddressBook;

import com.opencsv.bean.CsvBindByName;

public class Contact {
	
	@CsvBindByName
	public String firstName;

	@CsvBindByName
	public String lastName;
	
	@CsvBindByName
	public String address;
	
	@CsvBindByName
	public String city;
	
	@CsvBindByName
	public String state;
	
	@CsvBindByName
	public long zip;
	
	@CsvBindByName
	public long phoneNumber;
	
	@CsvBindByName
	public String email;

	public String bookName;
	
	public String bookType;

	public Contact() {

	}

	public Contact(String firstName, String lastName, String address, String city, String state, long zip, long phoneNumber,
			String email) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.address = address;
		this.city = city;
		this.state = state;
		this.zip = zip;
		this.phoneNumber = phoneNumber;
		this.email = email;
	}
	
	public Contact(String firstName, String lastName, String address, String city, String state, long zip,
			long phoneNumber, String email, String bookName, String bookType) {
		this(firstName,lastName,address,city,state,zip,phoneNumber,email);
		this.bookName = bookName;
		this.bookType = bookType;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public String getAddress() {
		return address;
	}
	
	public String getCity() {
		return city;
	}

	public String getState() {
		return state;
	}

	public long getZip() {
		return zip;
	}

	public long getPhoneNumber() {
		return phoneNumber;
	}

	public String getEmail() {
		return email;
	}

	public void setFirstName() {
		this.firstName = firstName;
	}

	public void setLastName() {
		this.lastName = lastName;
	}

	public void setAddress() {
		this.address = address;
	}
	
	public void setCity(String city) {
		this.city = city;
	}

	public void setState() {
		this.state = state;
	}

	public void setZip() {
		this.zip = zip;
	}

	public void setPhoneNumber() {
		this.phoneNumber = phoneNumber;
	}

	public void setEmail() {
		this.email = email;
	}
	
	public String getBookName() {
		return bookName;
	}

	public void setBookName(String bookName) {
		this.bookName = bookName;
	}

	public String getBookType() {
		return bookType;
	}

	public void setBookType(String bookType) {
		this.bookType = bookType;
	}

	public String toString() {
		return "FirstName=" + firstName + ", LastName=" + lastName + ", Address=" + address+ ", City=" + city + ", State=" + state
				+ ", zip=" + zip + ", phoneNumber=" + phoneNumber + ", email=" + email;
	}
}