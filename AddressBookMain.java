import java.util.*;
public class AddressBookMain
{
	public static void main(String args[])
	{
	   Scanner sc = new Scanner(System.in);
	   Contact c = new Contact("Arjun","Sama","Building 1 Hyd","Telangana",523443,9999999,"arjunsama@gmail.com");
	   
	   AddressBook ab = new AddressBook();
	   ab.addcont(c);
	   System.out.println("Enter FirstName,LastName,Address,State,zip,phone number,email to add into address book");
	   String fn = sc.nextLine();
	   String ln = sc.nextLine();
	   String addr = sc.nextLine();
	   String stt = sc.nextLine();
	   Double zp = Double.parseDouble(sc.nextLine());
	   Double phn = Double.parseDouble(sc.nextLine());
	   String email = sc.nextLine();

	   Contact c1 = new Contact(fn,ln,addr,stt,zp,phn,email);
	   ab.addcont(c1);
	   System.out.println("Details added Succesfully");

	   

	   
	   
	 
	}
}