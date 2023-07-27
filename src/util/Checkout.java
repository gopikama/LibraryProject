package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Checkout {
    int libraryCardNumber;
    String itemName;

    public Checkout(int libraryCardNumber, String itemName) {
        this.libraryCardNumber = libraryCardNumber;
        this.itemName = itemName;
    }

    public boolean isCheckoutAllowed() {
        try
        {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/selibrary","root","admin@1234");
            PreparedStatement stmt=conn.prepareStatement("select itemCategory, isAvailable from `item` where itemName='" + this.itemName + "';");
            ResultSet item=stmt.executeQuery();
            if(item.next()) {
                String itemCategory = item.getString(1);
                int isAvailable = item.getInt(2);
                if((itemCategory != "Book" && itemCategory != "Audio" && itemCategory == "Video") || isAvailable != 1) {
                    System.out.println("The item " + this.itemName + " cannot be checked out due to unavailability or not eligible for checkout");
                    return false;
                }
                PreparedStatement stmt1=conn.prepareStatement("select age from `user` where libraryCardNumber='" + this.libraryCardNumber + "';");
                ResultSet user=stmt1.executeQuery();
                if(user.next()){
                    int age = Integer.parseInt(user.getString(1));
                    if(age > 12) {
                        return true;
                    }
                    System.out.println(age);

                    PreparedStatement stmt2=conn.prepareStatement("select count(*) from `issuedItem` where libraryCardNumber='" + this.libraryCardNumber + "';");
                    ResultSet res=stmt2.executeQuery();
                    if (res.next()) {
                        int count = res.getInt(1);
                        // System.out.println(count);
                        if(count >= 5) {
                            System.out.println("The item " + this.itemName + " cannot be checked out due to limit of checkout for age <= 12");
                            return false;
                        } else {
                            return true;
                        }
                    }
                }
            }

        } catch(SQLException e) {
            System.out.println(e);
            return false;
        }

        return false;
    }
}