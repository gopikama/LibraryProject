package LibraryProject;

import java.sql.Connection;
import java.util.Calendar;
import java.util.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;

public class IssuedItem {
    String itemCategory;
    int itemId;
    int libraryCardNumber;
    Date dueDate;
    Date issueDate;
    String itemName;
    public IssuedItem(String itemName, int libraryCardNumber) {
        this.itemName = itemName;
        this.libraryCardNumber = libraryCardNumber;
    }

    public void issueItem() {
        try
        {
            //Class.forName("com.mysql.jdbc.Driver");
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/selibrary","root","GopuSri123@");
            PreparedStatement stmt=conn.prepareStatement("select itemId, itemName, itemCategory, durationInWeek, isBestSellerBook from `item` where itemName='" + this.itemName + "';");
            ResultSet item=stmt.executeQuery();
            if(item.next()){
                this.itemId = item.getInt(1);
                this.itemCategory = item.getString(3);
                int durationInWeek = item.getInt(4);
                int isBestSellerBook = item.getInt(5);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String issueDate = sdf.format(new Date());
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(new Date());
                calendar.add(Calendar.DAY_OF_YEAR, durationInWeek * 7);
                String dueDate = sdf.format(calendar.getTime());
                //System.out.println(dueDate);
                PreparedStatement stmt1=conn.prepareStatement("insert into `issuedItem` (`itemCategory`, `itemId`, `libraryCardNumber`, `dueDate`, `issueDate`, `noOfOutstandingDays`) values ('"+this.itemCategory+"', '"+this.itemId+"', '"+this.libraryCardNumber+"', '"+dueDate+"', '"+issueDate+"', '"+(durationInWeek * 7)+"')");
                boolean res=stmt1.execute();
                PreparedStatement stmt2=conn.prepareStatement("update `item` set isAvailable = 0 where itemId='" + this.itemId + "'");
                boolean res2=stmt2.execute();

            }

        } catch(SQLException e) {
            System.out.println(e);
        }
    }
}
