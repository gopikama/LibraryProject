package LibraryProject;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class ItemRequest {
    String itemName;
    int itemId;
    String itemCategory;
    int libraryCardNumber;
    String timeStamp;

    public ItemRequest(String itemName, int libraryCardNumber){
        this.itemName=itemName;
        this.libraryCardNumber = libraryCardNumber;
    }

    // get item id and item category from itemname
    public void newRequest() {
        try
        {
            //Class.forName("com.mysql.jdbc.Driver");
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/selibrary","root","sakshi1234");
            PreparedStatement stmt=conn.prepareStatement("select itemId, itemName, itemCategory, durationInWeek, isBestSellerBook from `item` where itemName='" + this.itemName + "';");
            ResultSet item=stmt.executeQuery();
            if(item.next()){
                this.itemId = item.getInt(1);
                this.itemCategory = item.getString(3);
            } else {
                PreparedStatement stmt1=conn.prepareStatement("select max(itemId) from `request`;");
                ResultSet res=stmt1.executeQuery();
                if(res.next()) {
                    this.itemId = res.getInt(1) + 1;
                } else {
                    this.itemId = 0;
                }
                this.itemCategory = "new item";
            }

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String requestTimeStamp = sdf.format(calendar.getTime());
            PreparedStatement stmt2=conn.prepareStatement("insert into `request` (`libraryCardNumber`, `itemId`, `itemCategory`, `itemName`,`requestTimeStamp`) values ('"+this.libraryCardNumber+"', '"+this.itemId+"', '"+this.itemCategory+"', '"+this.itemName+"', '"+requestTimeStamp+"')");
            boolean res=stmt2.execute();

        } catch(SQLException e) {
            System.out.println(e);
        }
    }
    // set itemrequest row

}
