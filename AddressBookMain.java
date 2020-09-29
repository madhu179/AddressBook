import java.util.*;
public class AddressBookMain
{

	public static void main(String args[])
	{  
      HashMap<String, AddressBook> AddressBookList = new  HashMap<String, AddressBook>();
      AddressBook ab;
      ArrayList<Contact> cl;
      HashMap<String, ArrayList<String>> statemap = new HashMap<String,ArrayList<String>>();
      Scanner sc = new Scanner(System.in);
      ArrayList<String> al;
      int option,count;
      String fname,lname,address,state,email,bookName="";
      long zip,phno;
      boolean check;
      Contact c;
      while(true)
       {
       	System.out.println("Choose one of the following : ");
       	System.out.println("1. Create a AddressBook");
       	System.out.println("2. Add a contact to a particular AddressBook");
        System.out.println("3. Edit a contact to a particular AddressBook");
       	System.out.println("4. Delete a Contact in a particular AddressBook");
       	System.out.println("5. Add Multiple Contacts to a particular AddressBook");
        System.out.println("6. Search person based on state across all AddressBooks");
        System.out.println("7. View person based on state across all AddressBooks");
        System.out.println("8. View No of contact persons from a state across all AddressBooks");
       	System.out.println("9. Print Details of a AddressBook");
       	System.out.println("10. Exit");

       	option = Integer.parseInt(sc.nextLine());

        if(option == 1)
        {  
          bookName = get_book_name();
          AddressBookList.put(bookName,new AddressBook());
          System.out.println("A new AddressBook with name "+bookName+" is created succesfully");
        }

       	else if(option == 2)
       	{
          bookName = get_book_name();
          if(AddressBookList.containsKey(bookName)){
            ab = (AddressBook) AddressBookList.get(bookName);
            c = Console_Input();
            check = ab.check_if_contact_exists(c.getFirstName());
            if(check)
            {
              System.out.println("A person already exists with the same FirstName,Duplicate entry not allowed!");
            }
            else
            {
            if(!statemap.isEmpty())
            {
              if(statemap.containsKey(c.getState()))
              {
              al = statemap.get(c.getState());
              al.add(c.getFirstName());
              statemap.replace(c.getState(),al);
              }
              else
              {
              ArrayList<String> ll = new ArrayList<String>();
              ll.add(c.getFirstName());
              statemap.put(c.getState(),ll);
              }
            }
            else
            {
            ArrayList<String> ll = new ArrayList<String>();
            ll.add(c.getFirstName());
            statemap.put(c.getState(),ll);
            }
            ab.addcont(c);
            System.out.println("Contact added succesfully to the AddressBook "+bookName);
            }
          }
          else
          {
            System.out.println("No AddressBook exists with the name "+bookName);
          }
          
       	}

       	else if(option == 3)
       	{

          bookName = get_book_name();
          if(AddressBookList.containsKey(bookName)){
            ab = (AddressBook) AddressBookList.get(bookName);
            System.out.println("Enter the First Name of the Contact to be edited");
            fname = sc.nextLine();
            check = ab.check_if_contact_exists(fname);
            if(check)
           {
             ab.editcont(Console_Input());
             AddressBookList.replace(bookName,ab);
             System.out.println("Details Edited Succesfully");
           }

           else
           {
               System.out.println("No Contact Exists with that First Name");
           }
          }
          else
          {
            System.out.println("No AddressBook exists with the name "+bookName);
          }

       	}

       	else if(option == 4)
       	{
          bookName = get_book_name();
          if(AddressBookList.containsKey(bookName)){
            ab = (AddressBook) AddressBookList.get(bookName);
            System.out.println("Enter the First Name of the Contact to be edited");
            fname = sc.nextLine();
            check = ab.check_if_contact_exists(fname);
            if(check)
           {
             ab.delcont(fname);
             AddressBookList.replace(bookName,ab);
             System.out.println("Contact Deleted Succesfully");
           }

           else
           {
               System.out.println("No Contact Exists with that First Name");
           }
          }
          else
          {
            System.out.println("No AddressBook exists with the name "+bookName);
          }

       	}

       	else if(option == 5)
       	{
          bookName = get_book_name();
          if(AddressBookList.containsKey(bookName))
          {
           ab = (AddressBook) AddressBookList.get(bookName);  
           System.out.println("Enter the No of Contacts to add");
           count = Integer.parseInt(sc.nextLine());
           for(int i=0; i<count;i++)
           { 
            c = Console_Input();
            check = ab.check_if_contact_exists(c.getFirstName());
            if(check)
            {
              System.out.println("A person already exists with the same FirstName,Duplicate entry not allowed!");
            }
            else
            {
              if(!statemap.isEmpty())
            {
              if(statemap.containsKey(c.getState()))
              {
              al = statemap.get(c.getState());
              al.add(c.getFirstName());
              statemap.replace(c.getState(),al);
              }
              else
              {
              ArrayList<String> ll = new ArrayList<String>();
              ll.add(c.getFirstName());
              statemap.put(c.getState(),ll);
              }
            }
            else
            {
            ArrayList<String> ll = new ArrayList<String>();
            ll.add(c.getFirstName());
            statemap.put(c.getState(),ll);
            }
            ab.addcont(c);
            }
           }
           System.out.println("All contacts added succesfully");
          }
          else
          {
            System.out.println("No AddressBook exists with the name "+bookName);
          }
       	   
       	}
        else if(option == 6)
        {
          if(!AddressBookList.isEmpty())
          {
          System.out.println("Enter the name of the state");
          state = sc.nextLine(); 
          System.out.println("The list of people in the state "+state+" :");
          for(HashMap.Entry<String, AddressBook> entry : AddressBookList.entrySet()) {
          AddressBook value = entry.getValue();
          cl = value.getAddressBook();  
          for(Contact cc : cl)
          {
               if(cc.getState().equals(state))
               {
                System.out.println(cc.getFirstName());
               }
          }
        }
      }
      else
      {
        System.out.println("No AddressBooks are added yet.");
      }
      }
        else if(option == 7)
        {
          System.out.println("Enter the name of the state");
          state = sc.nextLine(); 
          if(!statemap.isEmpty())
          {
          for(HashMap.Entry<String, ArrayList<String>> entry : statemap.entrySet()) {
            if(state.equals((String)entry.getKey()))
            {              
            ArrayList<String> value = entry.getValue();
            for(String str : value)
            {
              System.out.println(str);
            }
            }
        }
      }
          else
          {
           System.out.println("No person is from the given state");
          }
        }

        else if(option == 8)
        {
          System.out.println("Enter the name of the state");
          state = sc.nextLine(); 
          if(!statemap.isEmpty())
          {
           for(HashMap.Entry<String, ArrayList<String>> entry : statemap.entrySet()) {
            if(state.equals((String)entry.getKey()))
            {     
              System.out.println(entry.getValue().size());
            } 
          }
        }
        else
        {
          System.out.println("There are 0 persons form the given state");
        }
      }
         

       	else if(option == 9)
       	{
           System.out.println("Enter the name of the address book");
           bookName = sc.nextLine(); 
           if(AddressBookList.containsKey(bookName)){
            ab = (AddressBook) AddressBookList.get(bookName); 
            cl = ab.getAddressBook();
            System.out.println("The contacts in the address book "+bookName+" are :");
            for(Contact cc : cl)
            {
               ab.printcont(cc.getFirstName());
            }
         }
         else
         {
          System.out.println("No AddressBook exists with the name "+bookName);
         }
       	}

       	else if(option == 10)
       	{
       		break;
       	}

       }
	   	 
	}

  public static String get_book_name()
  {
    Scanner sc = new Scanner(System.in);
    System.out.println("Enter the name of the address book");
    String bookName = sc.nextLine();
    return bookName;
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