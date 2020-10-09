import java.util.*;
public class AddressBook
{
	 ArrayList<Contact> contact ;



	public AddressBook()
	{
       contact = new ArrayList<Contact>();
	}

  public void setAddressBook(ArrayList<Contact> contact)
  {
      this.contact = contact;
  }

  public ArrayList<Contact> getAddressBook()
  {
    return contact;
  }

	public void addcont(Contact c1)
	{
		contact.add(c1);
	}

  public boolean check_if_contact_exists(String fname)
  {
    if(!contact.isEmpty())
    {
    for(Contact s : contact)
    {
      if(fname.equals(s.getFirstName()))
      {
        return true;
      }
    }
  }
  return false;
}




	public void editcont(Contact c)
    {
    	Iterator iter = contact.iterator();
		  int i=0;
      while (iter.hasNext()) {
        
      	 Contact c1 = (Contact) iter.next();
         if(c.getFirstName().equals(c1.getFirstName()))
         {
         	contact.set(i,c);    	
         	break;
         }
         i+=1;
         
      }
      
    }

    public void delcont(String firstName)
    {
    	Iterator iter = contact.iterator();
		int i=0;
      while (iter.hasNext()) {
        
      	Contact c = (Contact) iter.next();
   
      	
         if(firstName.equals(c.getFirstName()))
         {
         	contact.remove(i);
         	break;
         }
         i+=1;
         
      }

    }

public void printcont(String firstName)
  {
    
    for(Contact s : contact)
    {
      if(firstName.equals(s.getFirstName()))
      {
        System.out.println("First Name : "+s.getFirstName());
        System.out.println("Last Name : "+s.getLastName());
        System.out.println("Address : "+s.getAddress());
        System.out.println("State: "+s.getState());
        System.out.println("Zip : "+s.getZip());
        System.out.println("Phone Number : "+s.getPhoneNumber());
        System.out.println("Email : "+s.getEmail());
        System.out.println(" ");
      }
    }
  }


}