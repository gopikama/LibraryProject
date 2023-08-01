package LibraryProject;

import java.sql.*;
import java.time.LocalDate;
import java.time.Period;
import java.util.concurrent.TimeUnit;

public class ItemReturn {
    String itemName;
    static long noOfOutStandingDays;
    int libraryCardNumber;
    static int itemId;
    static double itemValue;
    static double fineForReturn;
    static LocalDate dueDate;
    static String itemCategory;

    static int newLibraryCardNumber;
    static LocalDate returnDateString= LocalDate.now();




    public ItemReturn(int libraryCardNumber, String itemName){
        this.itemName=itemName;
        this.libraryCardNumber=libraryCardNumber;
    }
    public static void returnItem(int libraryCardNumber, String itemName){
                try {
                Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/selibrary", "root", "sakshi1234");
                PreparedStatement stmt2 = conn.prepareStatement("select * from item where itemName=?;");
                stmt2.setString(1, itemName);
                ResultSet rs2 = stmt2.executeQuery();


                while (rs2.next()) {
                    itemValue = rs2.getDouble(6);
                    itemCategory= rs2.getString(2);
                    itemId= rs2.getInt(1);

                }
                    PreparedStatement stmt3 = conn.prepareStatement("select dueDate from issuedItem where  itemId=? and libraryCardNumber=?;");
                    stmt3.setInt(1,itemId);
                    stmt3.setInt(2,libraryCardNumber);

                    ResultSet rs3 = stmt3.executeQuery();
                    if(rs3.next()){
                            dueDate= rs3.getDate(1).toLocalDate();
                        Period diff = Period.between(dueDate, returnDateString);
                        noOfOutStandingDays = diff.getDays();
                        System.out.println("Number of outstanding days: "+noOfOutStandingDays);
                        if(noOfOutStandingDays>0)
                            fineForReturn = (noOfOutStandingDays * .10);
                        else fineForReturn=0;
                        if(fineForReturn>itemValue){
                            fineForReturn=itemValue;
                        }
                        System.out.println("Fine to be paid for this item : %.2f" +fineForReturn);
                        String returnDate =returnDateString.toString();

                        PreparedStatement stmt4 = conn.prepareStatement("insert into selibrary.return(libraryCardNumber, itemId,returnDate,fineForReturn)" + "values(?,?,?,?);");
                        stmt4.setInt(1, libraryCardNumber);
                        stmt4.setInt(2, itemId);
                        stmt4.setString(3, returnDate);
                        stmt4.setDouble(4,fineForReturn);
                        stmt4.executeUpdate();

                        PreparedStatement stmt5= conn.prepareStatement("update item set isAvailable = 1 where itemId=?;");
                        stmt5.setInt(1, itemId);
                        stmt5.executeUpdate();
                        PreparedStatement stmt6= conn.prepareStatement("delete from issuedItem where itemId=?;");
                        stmt6.setInt(1, itemId);
                        stmt6.executeUpdate();
                        System.out.println("The "+itemCategory+ " "+itemName+" is returned successfully");
                        //if this item was requested, delete from request and move to issuedItem
                        try{
                            PreparedStatement stmt7= conn.prepareStatement("select * from request where itemId=? order by requestTimeStamp limit 1;");
                            stmt7.setInt(1, itemId);
                            ResultSet rs7 = stmt7.executeQuery();

                            if(rs7.next()){
                                newLibraryCardNumber= rs7.getInt(1);
                                System.out.println("This item has an outstanding request from user: " + newLibraryCardNumber + " issuing to them now");
                                PreparedStatement stmt8= conn.prepareStatement("delete from request where itemId=? and libraryCardNumber=?;");
                                stmt8.setInt(1, itemId);
                                stmt8.setInt(2,newLibraryCardNumber);
                                stmt8.executeUpdate();

                                //make new object of issuedItem and pass newLibraryCardNumber and itemId to issue it.
                                IssuedItem issuedItem=new IssuedItem(itemName,newLibraryCardNumber);
                                issuedItem.issueItem();
                            }
                            else{
                                System.out.println("No outstanding request for this item");
                            }

                        }
                        catch (SQLException e){
                            System.out.println(e);
                        }

                    }else{
                        System.out.println("This item is not issued");
                        return;
                    }

                //try end here
            }
            catch(SQLException e)
            {   e.printStackTrace();
                System.out.println("item can't be returned");


            }





    }


}




