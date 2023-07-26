package LibraryProject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Library {

    public static void main(String[] args)throws IOException {
        BufferedReader br=new BufferedReader(new InputStreamReader(System.in));
        System.out.println("LIBRARY MANAGEMENT SYSTEM");
        System.out.println("==========================");
        System.out.println("Please enter your library card number");
        int libraryCardNumber=Integer.parseInt(br.readLine());
        String itemName="";
        int choice;
        if(libraryCardNumber!=1111) {
            do {
                System.out.println("Please select an option:");
                System.out.println("1.request book\n" +
                        "2.return book\n" +
                        "3.renew book\n" +
                        "4.get user information\n" +
                        "5.optionToExit\n");
                choice = Integer.parseInt(br.readLine());
                switch (choice) {
                    case 1: {
                        System.out.println("Please enter name of item to be requested");
                        itemName = br.readLine();
                        ItemRequest requestObject = new ItemRequest(itemName);
                        break;

                    }
                    case 2: {
                        System.out.println("Please enter name of item to be returned");
                        itemName = br.readLine();
                        ItemReturn returnObject = new ItemReturn(itemName);
                        break;


                    }
                    case 3: {
                        System.out.println("Please enter name of item to be renewed");
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
                System.out.println("Do you want to continue ,if you want to exit press 5 otherwise enter 0 to continue:");
                choice = Integer.parseInt(br.readLine());


            } while (choice != 5);
        }
        else{
            do{
                System.out.println("Please select an option:");
                System.out.println("1.Get information about all users\n" +
                        "2.Get a particular users checked out book details\n" +
                        "3.exit\n");
                choice=Integer.parseInt(br.readLine());
                switch (choice) {
                    case 1: {
                        User userObject=new User(libraryCardNumber);
                        userObject.displayUserDetails();
                        break;
                    }
                    case 2:
                        System.out.println("Enter name of user to get issued book details");
                        break;

                    case 3:
                        break;

                }

            }while(choice!=3);

        }
    }

}
