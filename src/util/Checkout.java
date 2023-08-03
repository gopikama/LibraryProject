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

    public CheckoutStatus isCheckoutAllowed() {
        CheckoutStatus checkoutStatus = new CheckoutStatus();
        try
        {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/selibrary","root","GopuSri123@");
            PreparedStatement stmt=conn.prepareStatement("select itemCategory, isAvailable from `item` where itemName='" + this.itemName + "';");
            ResultSet item=stmt.executeQuery();
            if(item.next()) {
                String itemCategory = item.getString(1);
                int isAvailable = item.getInt(2);
                if((!itemCategory.equalsIgnoreCase("Book") && !itemCategory.equalsIgnoreCase("Audio") && !itemCategory.equalsIgnoreCase("Video"))) {
                    System.out.println("The item " + this.itemName + " cannot be checked out due to not eligible (Magazines / Reference books) for checkout!");
                    return checkoutStatus;
                }
                if(isAvailable == 0) {
                    System.out.println("The item " + this.itemName + " cannot be checked out due to unavailability for checkout!");
                    checkoutStatus.shouldAddToRequest = true;
                    return checkoutStatus;
                }
                PreparedStatement stmt1=conn.prepareStatement("select age from `user` where libraryCardNumber='" + this.libraryCardNumber + "';");
                ResultSet user=stmt1.executeQuery();
                if(user.next()){
                    int age = Integer.parseInt(user.getString(1));
                    if(age > 12) {
                        checkoutStatus.isCheckoutAllowed = true;
                        return checkoutStatus;
                    }
                    //System.out.println(age);

                    PreparedStatement stmt2=conn.prepareStatement("select count(*) from `issuedItem` where libraryCardNumber='" + this.libraryCardNumber + "';");
                    ResultSet res=stmt2.executeQuery();
                    if (res.next()) {
                        int count = res.getInt(1);
                        // System.out.println(count);
                        if(count >= 5) {
                            System.out.println("The item " + this.itemName + " cannot be checked out due to limit of checkout for age <= 12");
                            checkoutStatus.isCheckoutAllowed = false;
                            return checkoutStatus;
                        } else {
                            checkoutStatus.isCheckoutAllowed = true;
                            return checkoutStatus;
                        }
                    }
                }
            } else {
                checkoutStatus.shouldAddToRequest = true;
            }

        } catch(SQLException e) {
            System.out.println(e);
            checkoutStatus.isCheckoutAllowed = false;
            return checkoutStatus;
        }

        checkoutStatus.isCheckoutAllowed = false;
        return checkoutStatus;
    }


}
