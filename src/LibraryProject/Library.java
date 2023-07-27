package LibraryProject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import util.Checkout;
import util.CheckoutStatus;

public class Library {

    public static void main(String[] args)throws IOException {
        BufferedReader br=new BufferedReader(new InputStreamReader(System.in));
        System.out.println("LIBRARY MANAGEMENT SYSTEM");
        System.out.println("==========================");
        System.out.println("Please enter your library card number:");
        int libraryCardNumber=Integer.parseInt(br.readLine());
        String itemName="";
        int choice;
        if(libraryCardNumber!=1111) {
            do {
                System.out.println("Please select an option to proceed:");
                System.out.println("1.Request Item\n" +
                        "2.Return Item\n" +
                        "3.Renew Item\n" +
                        "4.Get user information\n" +
                        "5.Exit\n");
                choice = Integer.parseInt(br.readLine());
                switch (choice) {
                    case 1: {
                        System.out.println("Please enter name of item to be requested:");
                        itemName = br.readLine();
                        Checkout checkout = new Checkout(libraryCardNumber, itemName);
                        CheckoutStatus checkoutStatus = checkout.isCheckoutAllowed();
                        //System.out.println(checkoutStatus.isCheckoutAllowed + "" + checkoutStatus.shouldAddToRequest);
                        if(!checkoutStatus.isCheckoutAllowed) {
                            if(checkoutStatus.shouldAddToRequest) {
                                System.out.println("The item is not available currently so your request is added to the system!");
                                ItemRequest requestObject = new ItemRequest(itemName, libraryCardNumber);
                                requestObject.newRequest();
                            }
                            break;
                        }

                        IssuedItem issuedItem = new IssuedItem(itemName, libraryCardNumber);
                        issuedItem.issueItem();
                        System.out.println("The item " + itemName + " is checked out!");
                        break;

                    }
                    case 2: {
                        System.out.println("Please enter name of item to be returned:");
                        itemName = br.readLine();
                        ItemReturn returnObject = new ItemReturn(itemName);
                        break;


                    }
                    case 3: {
                        System.out.println("Please enter name of item to be renewed:");
                        itemName = br.readLine();
                        ItemRenew renewObject = new ItemRenew(itemName);
                        break;


                    }
                    case 4: {
                        User userObject = new User(libraryCardNumber);
                        userObject.displayUserDetails();
                        break;

                    }
                    case 5: {
                        break;

                    }
                }
                System.out.println("Do you want to continue,if you want to exit press 5 otherwise enter 0 to continue:");
                choice = Integer.parseInt(br.readLine());


            } while (choice != 5);
        }
        else{
            do{
                System.out.println("Please select an option:");
                System.out.println("1.Get information about all users\n" +
                        "2.Get details of all  the books issued out to users\n" +
                        "3.Get a particular user's checked out book details\n" +
                        "4.exit\n");
                choice=Integer.parseInt(br.readLine());
                switch (choice) {
                    case 1: {
                        User userObject=new User(libraryCardNumber);
                        userObject.displayUserDetails();
                        break;
                    }
                    case 2: {
                        User userObject = new User(libraryCardNumber);
                        userObject.displayIssuedBookDetails();
                        break;
                    }
                    case 3: {
                        User userObject = new User(libraryCardNumber);
                        System.out.println("Please enter library card number of user,whose issued book details are required:");
                        int libCard = Integer.parseInt(br.readLine());
                        System.out.println("Please enter name of user,whose issued book details are required:");
                        String name = br.readLine();
                        userObject.displayBooksIssuedToUser(libCard,name);
                        break;
                    }
                    case 4:
                        break;

                }

            }while(choice!=4);

        }
    }

}
