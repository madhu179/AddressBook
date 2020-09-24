public class Contact
{
	
	public String firstName;
	public String lastName;
	public String address;
	public String State;
	public double zip;
	public double phoneNumber;
	public String email;

	public Contact()
	{

	}

	public Contact(String firstName,String lastName,String address,String State,double zip,double phoneNumber,String email)
	{
		this.firstName = firstName;
		this.lastName= lastName;
		this.address= address;
		this.State = State;
		this.zip = zip;
		this.phoneNumber = phoneNumber;
		this.email = email;
	}

	public String getFirstName()
	{
        return firstName;
	}

	public String getLastName()
	{
        return lastName;
	}

	public String getAddress()
	{
        return address;
	}

	public String getState()
	{
        return State;
	}
    public double getZip()
	{
        return zip;
	}
	public double getPhoneNumber()
	{
        return phoneNumber;
	}
	public String getEmail()
	{
        return email;
	}


	public void setFirstName()
	{
        this.firstName = firstName;
	}

	public void setLastName()
	{
        this.lastName=lastName;
	}

	public void setAddress()
	{
        this.address=address;
	}

	public void setState()
	{
        this.State=State;
	}
    public void setZip()
	{
        this.zip=zip;
	}
	public void setPhoneNumber()
	{
        this.phoneNumber=phoneNumber;
	}
	public void setEmail()
	{
        this.email=email;
	}
}