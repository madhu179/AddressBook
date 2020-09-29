import java.util.*;
public class AddressBookMain
{

	public static void main(String args[])
	{
      AddressBook ab = new AddressBook();
      Scanner sc = new Scanner(System.in);
      int option,count;
      String fname,lname,address,state,email;
      long zip,phno;
      boolean check;
      while(true)
       {
       	System.out.println("Choose one of the following : ");
       	System.out.println("1. Add a Contact to Address Book");
       	System.out.println("2. Edit a Contact");
       	System.out.println("3. Delete a Contact");
       	System.out.println("4. Add Multiple Contacts");
       	System.out.println("5. Print Details of a Contact");
       	System.out.println("6. Exit");

       	option = Integer.parseInt(sc.nextLine());

       	if(option == 1)
       	{
           ab.addcont(Console_Input());
           System.out.println("Contact added Succesfully");
       	}

       	else if(option == 2)
       	{
           System.out.println("Enter the First Name of the Contact to be edited");
           fname = sc.nextLine();
           check = ab.check_if_contact_exists(fname);
           if(check)
           {
	           ab.editcont(Console_Input());
	           System.out.println("Details Edited Succesfully");
           }

           else
           {
           	   System.out.println("No Contact Exists with that First Name");
           }
       	}

       	else if(option == 3)
       	{
           System.out.println("Enter the First Name of the Contact to be deleted");
           fname = sc.nextLine();
           check = ab.check_if_contact_exists(fname);
           if(check)
           {
               	ab.delcont(fname);
           	    System.out.println("Contact Deleted Succesfully");
           }

           else
           {
           		  System.out.println("No Contact Exists with that First Name");
           }
       	}

       	else if(option == 4)
       	{
       	   System.out.println("Enter the No of Contacts to add");
       	   count = Integer.parseInt(sc.nextLine());
       	   for(int i=0; i<count;i++)
       	   {
  	          ab.addcont(Console_Input());
           }
           System.out.println("All contacts added succesfully");
       	}

       	else if(option == 5)
       	{
           System.out.println("Enter the First Name of the Contact to be displayed");
           fname = sc.nextLine();
           check = ab.check_if_contact_exists(fname);
           if(check)
           {
               	ab.printcont(fname);
           }

           else
           {
           		System.out.println("No Contact Exists with that First Name");
           }
       	}

       	else
       	{
       		break;
       	}

       }
	   	 
	}

  public static Contact Console_Input()
  {
     Scanner sc = new Scanner(System.in);
     System.out.println("Enter First Name");
     String fname = sc.nextLine();
     System.out.println("Enter Last Name");
     String lname = sc.nextLine();
     System.out.println("Enter Address");
     String address = sc.nextLine();
     System.out.println("Enter State");
     String state = sc.nextLine();
     System.out.println("Enter Zip");
     long zip = Long.parseLong(sc.nextLine());
     System.out.println("Enter Phone Number");
     long phno = Long.parseLong(sc.nextLine());
     System.out.println("Enter Email");
     String email = sc.nextLine();

     Contact c = new Contact(fname,lname,address,state,zip,phno,email);
     return c;
  } 
}