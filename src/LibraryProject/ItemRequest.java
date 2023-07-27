package LibraryProject;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ItemRequest {
    String itemName;
    String itemId;
    String itemCategory;
    int libraryCardNumber;
    int durationInWeek;
    int isBestSellerBook;

    public ItemRequest(String itemName, int libraryCardNumber){
        this.itemName=itemName;
        this.libraryCardNumber = libraryCardNumber;
    }

    // get item id and item category from itemname
    public void newRequest() {
        try
        {
            //Class.forName("com.mysql.jdbc.Driver");
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/selibrary","root","admin@1234");
            PreparedStatement stmt=conn.prepareStatement("select itemId, itemName, itemCategory, durationInWeek, isBestSellerBook from `item` where itemName='" + this.itemName + "';");
            ResultSet item=stmt.executeQuery();
            if(item.next()){
                this.itemId = item.getString(1);
                this.itemCategory = item.getString(3);
                this.durationInWeek = item.getInt(4);
                this.isBestSellerBook = item.getInt(5);
                System.out.println(this.itemId);
                PreparedStatement stmt1=conn.prepareStatement("insert into `request` (`libraryCardNumber`, `itemId`, `itemCategory`) values ('"+this.libraryCardNumber+"', '"+this.itemId+"', '"+this.itemCategory+"')");
                boolean res=stmt1.execute();
                PreparedStatement stmt2=conn.prepareStatement("update `item` set isAvailable = 0 where itemId='" + this.itemId + "'");
                boolean res2=stmt2.execute();
            }

        } catch(SQLException e) {
            System.out.println(e);
        }
    }
    // set itemrequest row

}
