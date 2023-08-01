package LibraryProject;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class ItemRenew {
    int libraryCardNumber;
    String itemName;
    int renewCount;
     int itemId;
     int durationInWeek;
    int requestCheck;

    public ItemRenew(String itemName, int libraryCardNumber){
        this.itemName=itemName;
        this.libraryCardNumber=libraryCardNumber;
    }
    public void renewItem(int libraryCardNumber, String itemName){
        try{
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/selibrary", "root", "sakshi1234");
            PreparedStatement stmts = conn.prepareStatement("select * from item where itemName=?;");
            stmts.setString(1, itemName);
            ResultSet resultSet = stmts.executeQuery();
            if(resultSet.next()){
                itemId=resultSet.getInt(1);
                durationInWeek=resultSet.getInt(4);
                itemName=resultSet.getString(3);
            }
            PreparedStatement stmt = conn.prepareStatement("select renewCount from issuedItem where itemId=? and libraryCardNumber=?;");

            stmt.setInt(2,libraryCardNumber);
            stmt.setInt(1,itemId);

            ResultSet rs = stmt.executeQuery();
            if(rs.next()){
                renewCount= rs.getInt(1);
            }
            PreparedStatement stmt1= conn.prepareStatement("select COUNT(*) from request where itemId=?;");
            stmt1.setInt(1,itemId);
            ResultSet rs1 = stmt1.executeQuery();


            if(rs1.next()) {
                requestCheck = rs1.getInt(1);
            }

            if(requestCheck!=0){
                System.out.println("Error:item Cannot be renewed, there's an outstanding request for this item, returning this item");
                ItemReturn.returnItem(libraryCardNumber,itemName);

            }
            else if(renewCount>=1) {
                System.out.println("Error:item Cannot be renewed more than once, returning this item");
                ItemReturn.returnItem(libraryCardNumber,itemName);
            } else{
                System.out.println("Item" +itemName+ "renewed");
                //reissue, change issuedate,duedate,renewcount
                renewCount++;
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String issueDate = sdf.format(new java.util.Date());
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(new Date());
                calendar.add(Calendar.DAY_OF_YEAR, durationInWeek * 7);
                String dueDate = sdf.format(calendar.getTime());
                PreparedStatement stmt3=conn.prepareStatement("update issuedItem set dueDate=?,issueDate=?, renewCount=? where libraryCardNumber=? and itemId=?;");
                stmt3.setString(1,dueDate);
                stmt3.setString(2,issueDate);
                stmt3.setInt(3, renewCount);
                stmt3.setInt(4,libraryCardNumber);
                stmt3.setInt(5,itemId);
                stmt3.executeUpdate();


            }



        }
        catch(SQLException e){
            e.printStackTrace();
            System.out.println(e);
        }


    }

}
