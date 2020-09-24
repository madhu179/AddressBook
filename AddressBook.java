import java.util.*;
public class AddressBook
{
	ArrayList<Contact> cnt = new ArrayList<Contact>();
	public AddressBook()
	{
       
	}

	public void addcont(Contact c1)
	{
		cnt.add(c1);
	}

	public void editcont(String firstName,String lastName,String address,String State,double zip,double phoneNumber,String email)
    {
    	Iterator iter = cnt.iterator();
		int i=0;
      while (iter.hasNext()) {
        
      	Contact c = (Contact) iter.next();
   
      	
         if(firstName.equals(c.getFirstName()) && lastName.equals(c.getLastName()))
         {
         	cnt.remove(i);
         	
         	break;
         }
         i+=1;
         
      }
      Contact cc1 = new Contact(firstName,lastName,address,State,zip,phoneNumber,email);
      cnt.add(cc1);
    }

	
}