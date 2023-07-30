package LibraryProject;

import java.sql.*;

public class ItemRenew {
    int libraryCardNumber;
    String itemName;
    int renewCount=0;
     int itemId;

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
            }
            PreparedStatement stmt = conn.prepareStatement("select renewCount from renew where itemId=? and itemName=? and libraryCardNumber=?;");
            stmt.setString(2, itemName);
            stmt.setInt(3,libraryCardNumber);
            stmt.setInt(1,itemId);
            ResultSet rs = stmt.executeQuery();
            if(rs.next()){
                renewCount= rs.getInt(1);
            }
            PreparedStatement stmt1= conn.prepareStatement("select COUNT(*) from request where itemId=? and libraryCardNumber=?;");
            //PreparedStatement stmt1= conn.prepareStatement("select COUNT(*) from request where itemId=? itemName=? and libraryCardNumber=?;");
            //stmt1.setString(1, itemName);
            stmt1.setInt(2,libraryCardNumber);
            stmt1.setInt(1,itemId);
            ResultSet rs1 = stmt1.executeQuery();
            int requestCheck=0;
            if(rs1.next())
                requestCheck=rs1.getInt(1);


            if(renewCount>=1){
                System.out.println("Error:item Cannot be renewed more than once, returning this item");
                ItemReturn.returnItem(libraryCardNumber,itemName);
                renewCount=0;

            }
            else if(requestCheck!=0) {
                System.out.println("Error:item Cannot be renewed, there's an outstanding request for this item, returning this item");
                ItemReturn.returnItem(libraryCardNumber,itemName);
                renewCount=0;
            } else{
                PreparedStatement stmt3= conn.prepareStatement("delete from issuedItem where itemId=? and libraryCardNumber=?;");
                stmt3.setInt(1, itemId);
                stmt3.setInt(2,libraryCardNumber);
                 stmt3.executeUpdate();
                //IssuedItem issuedItem=new IssuedItem(itemName,libraryCardNumber);
                //issuedItem.issueItem();
                renewCount++;
            }
            PreparedStatement psmt= conn.prepareStatement("update renew set renewCount=? , itemId=? where itemName=? and libraryCardNumber=?;");
            psmt.setInt(1,renewCount);
            psmt.setString(3,itemName);
            psmt.setInt(4,libraryCardNumber);
            psmt.setInt(2,itemId);
            psmt.executeUpdate();


        }
        catch(SQLException e){
            e.printStackTrace();
            System.out.println(e);
        }


    }

}
