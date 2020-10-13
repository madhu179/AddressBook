package AddressBook;

public class Contact {

	public String firstName;
	public String lastName;
	public String address;
	public String state;
	public long zip;
	public long phoneNumber;
	public String email;

	public Contact() {

	}

	public Contact(String firstName, String lastName, String address, String state, long zip, long phoneNumber,
			String email) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.address = address;
		this.state = state;
		this.zip = zip;
		this.phoneNumber = phoneNumber;
		this.email = email;
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

	public String toString() {
		return "FirstName=" + firstName + ", LastName=" + lastName + ", Address=" + address + ", State=" + state
				+ ", zip=" + zip + ", phoneNumber=" + phoneNumber + ", email=" + email;
	}
}