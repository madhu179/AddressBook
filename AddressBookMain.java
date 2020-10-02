import java.util.*;
import java.util.stream.Collectors;
public class AddressBookMain
{

	public static void main(String args[])
	{  
      HashMap<String, AddressBook> AddressBookList = new  HashMap<String, AddressBook>();
      AddressBook addressBookObj;
      ArrayList<Contact> contanctList;
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
        System.out.println("9. Sort the entries in a particular address book by person name");
        System.out.println("10. Sort the entries in a particular address book by state");
        System.out.println("11. Print Details of a AddressBook");
       	System.out.println("12. Exit");

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
            addressBookObj = (AddressBook) AddressBookList.get(bookName);
            c = Console_Input();
            check = addressBookObj.check_if_contact_exists(c.getFirstName());
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
            addressBookObj.addcont(c);
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
            addressBookObj = (AddressBook) AddressBookList.get(bookName);
            System.out.println("Enter the First Name of the Contact to be edited");
            fname = sc.nextLine();
            check = addressBookObj.check_if_contact_exists(fname);
            if(check)
           {
             addressBookObj.editcont(Console_Input());
             AddressBookList.replace(bookName,addressBookObj);
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
            addressBookObj = (AddressBook) AddressBookList.get(bookName);
            System.out.println("Enter the First Name of the Contact to be edited");
            fname = sc.nextLine();
            check = addressBookObj.check_if_contact_exists(fname);
            if(check)
           {
             addressBookObj.delcont(fname);
             AddressBookList.replace(bookName,addressBookObj);
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
           addressBookObj = (AddressBook) AddressBookList.get(bookName);  
           System.out.println("Enter the No of Contacts to add");
           count = Integer.parseInt(sc.nextLine());
           for(int i=0; i<count;i++)
           { 
            c = Console_Input();
            check = addressBookObj.check_if_contact_exists(c.getFirstName());
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
            addressBookObj.addcont(c);
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
          contanctList = value.getAddressBook();  
          for(Contact cc : contanctList)
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
          final String st = state;
          if(!statemap.isEmpty())
          {             
             statemap.entrySet().stream()
                                      .filter(n->n.getKey().equals(st))
                                      .flatMap(n -> n.getValue().stream())
                                      .forEach(n->System.out.println(n));
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
          final String st = state; 
          if(!statemap.isEmpty())
          {
            System.out.println(statemap.entrySet().stream()
                                       .filter(n->n.getKey().equals(st))
                                       .flatMap(n -> n.getValue().stream())
                                       .count()+
                                       " people are from the state "+state);
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
            addressBookObj = (AddressBook) AddressBookList.get(bookName); 
            contanctList = addressBookObj.getAddressBook();                                  
            ArrayList<Contact> sortedContactList = new ArrayList<Contact>(
                                    contanctList.stream()
                                    .sorted(Comparator.comparing(Contact::getFirstName))
                                    .collect(Collectors.toList())
                                   );
            addressBookObj.setAddressBook(sortedContactList);
            System.out.println("The Contacts in the address book are sorted succesfully.");
         }
         else
         {
          System.out.println("No AddressBook exists with the name "+bookName);
         }
        }

        else if(option == 10)
        {
           System.out.println("Enter the name of the address book");
           bookName = sc.nextLine(); 
           if(AddressBookList.containsKey(bookName)){
            addressBookObj = (AddressBook) AddressBookList.get(bookName); 
            contanctList = addressBookObj.getAddressBook();                                  
            ArrayList<Contact> sortedContactList = new ArrayList<Contact>(
                                    contanctList.stream()
                                    .sorted(Comparator.comparing(Contact::getState))
                                    .collect(Collectors.toList())
                                   );
            addressBookObj.setAddressBook(sortedContactList);
            System.out.println("The Contacts in the address book are sorted succesfully.");
         }
         else
         {
          System.out.println("No AddressBook exists with the name "+bookName);
         }
        }
         

       	else if(option == 11)
       	{
           System.out.println("Enter the name of the address book");
           bookName = sc.nextLine(); 
           if(AddressBookList.containsKey(bookName)){
            addressBookObj = (AddressBook) AddressBookList.get(bookName); 
            contanctList = addressBookObj.getAddressBook();
            System.out.println("The contacts in the address book "+bookName+" are :");
            for(Contact cc : contanctList)
            {
               addressBookObj.printcont(cc.getFirstName());
            }
         }
         else
         {
          System.out.println("No AddressBook exists with the name "+bookName);
         }
       	}

       	else if(option == 12)
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