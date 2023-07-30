package LibraryProject;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class User {
    String name;
    String address;
    Long phoneNumber;
    int libraryCardNumber;
    int age;
    Float totalOverDueFine;


    public User(int libraryCardNumber){

        this.libraryCardNumber=libraryCardNumber;
    }
    public void displayUserDetails() {

        try
        {
            //Class.forName("com.mysql.jdbc.Driver");
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/selibrary","root","GopuSri123@");
            PreparedStatement stmt=conn.prepareStatement("select * from user;");
            ResultSet rs=stmt.executeQuery();

            int flag=0;
            while(rs.next()){
                //fetching all user details for admin/library

                if (libraryCardNumber == 1111){

                    flag=1;
                    if (Integer.parseInt(rs.getString(2))!=libraryCardNumber) {
                        System.out.println("");
                        System.out.print("Name: " + rs.getString(1) + "\n");
                    System.out.print("Library Card Number: " + rs.getString(2) + "\n");
                    System.out.print("Address: " + rs.getString(3) + "\n");
                    System.out.print("Phone Number: " + rs.getString(4) + "\n");
                    System.out.print("Age: " + rs.getString(5) + "\n");
                    //run query to get from return table for each returned item for userid get sum(fine)

                        PreparedStatement stmt1=conn.prepareStatement("SELECT SUM(fineForReturn) FROM returnitem WHERE libraryCardNumber=?;");
                        stmt1.setInt(1,Integer.parseInt(rs.getString(2)));
                        ResultSet rs1=stmt1.executeQuery();

                        while(rs1.next()){
                            if(rs1.getString(1)!=null)
                            System.out.print("Total Over Due Fine Payed: " + rs1.getString(1) + "\n");
                            else
                                System.out.print("Total Over Due Fine Payed:0 \n");
                        }
                    }

                }
                //fetching a particular user's details for user

                else if (Integer.parseInt(rs.getString(2))==(libraryCardNumber)){

                    flag=1;
                    System.out.println("");
                    System.out.print("Name: "+rs.getString(1)+"\n");
                    System.out.print("Library Card Number: "+rs.getString(2)+"\n");
                    System.out.print("Address: "+rs.getString(3)+"\n");
                    System.out.print("Phone Number: "+rs.getString(4)+"\n");
                    System.out.print("Age: "+rs.getString(5)+"\n");
                    //run query to get from return table
                    PreparedStatement stmt1=conn.prepareStatement("SELECT SUM(fineForReturn) FROM returnitem WHERE libraryCardNumber=?;");
                    stmt1.setInt(1,Integer.parseInt(rs.getString(2)));
                    ResultSet rs1=stmt1.executeQuery();

                    while(rs1.next()){
                        if(rs.getString(1)!=null)
                        System.out.print("Total Over Due Fine: " + rs1.getString(1) + "\n");
                        else
                            System.out.print("Total Over Due Fine Payed: 0 \n");
                    }


                }

            }

            if(flag==0 )
                    System.out.println("Invalid card number,user not found!");

        }
        catch(SQLException  e)
        {

            System.out.println(e);


        }

    }

    public void displayIssuedBookDetails() {

        try {
            //Class.forName("com.mysql.jdbc.Driver");
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/selibrary", "root", "GopuSri123@");
            PreparedStatement stmt = conn.prepareStatement("select * from issueditem;");
            ResultSet rs = stmt.executeQuery();

            int flag = 0;
            while (rs.next()) {
                //fetching all  books issued to user details
                if (libraryCardNumber == 1111) {
                    flag = 1;
                    System.out.println("");
                    System.out.print("Library Card Number: " + rs.getString(3) + "\n");
                    System.out.print("Issued item's ID: " + rs.getString(2) + "\n");
                    System.out.print("Due Date: " + rs.getString(4) + "\n");
                    //if due date>current date calculate currentdate-duedate then multiply by 0.10 if this value<=itemvalue keep otherwise put itemValue
                    //select datediff(now(),dueDate) from issueditem where libraryCardNumber=1222;
                    PreparedStatement stmtDate = conn.prepareStatement("select datediff(now(),dueDate) from issueditem where libraryCardNumber=? and itemId=?;");
                    stmtDate.setInt(1, Integer.parseInt(rs.getString(3)));
                    stmtDate.setInt(2, Integer.parseInt(rs.getString(2)));
                    ResultSet rs3 = stmtDate.executeQuery();
                    if (rs3.next()) {
                        int days = Integer.parseInt(rs3.getString(1));
                        if (days > 0) {
                            double fine = days * 0.10;
                            double itemVal=0;
                            PreparedStatement stmtToGetItemValue = conn.prepareStatement("select itemValue from item where itemId=? ;");
                            stmtToGetItemValue.setInt(1, Integer.parseInt(rs.getString(2)));
                            ResultSet rs4 = stmtDate.executeQuery();
                            if (rs4.next()) {
                                itemVal = Double.parseDouble(rs4.getString(1));
                            }
                            if (fine < itemVal) {
                                System.out.print("Outstanding days overdue fine: " + (double) Math.round(fine * 100) / 100 + "\n");
                            } else if (fine >= itemVal) {
                                System.out.print("Outstanding days overdue fine: " + itemVal + "\n");
                            }
                        }
                        else{
                            System.out.println("No outstanding days overdue fine");
                        }

                    }
                }
            }
            if (flag == 0)
                System.out.println("Invalid card number,user not found!");

        } catch (SQLException e) {

            System.out.println(e);


        }
    }
        public void displayBooksIssuedToUser(int libCard){

            System.out.println("Books issued to library card number: "+libCard+" are: ");
            try
            {
                //Class.forName("com.mysql.jdbc.Driver");
                Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/selibrary","root","GopuSri123@");
                PreparedStatement stmt=conn.prepareStatement("select * from issueditem natural join item natural join user where libraryCardNumber=?;");
                stmt.setInt(1,libCard);

                ResultSet rs=stmt.executeQuery();

                double sum=0;
                int flag=0;
                int count =0;
                while(rs.next()){
                    //fetching all user details
                    if (libraryCardNumber == 1111){
                        flag=1;
                        count=count+1;
                        System.out.println("");
                        System.out.print("User Name: " + rs.getString(13) + "\n");
                        System.out.print("Issued Item's ID: " + rs.getString(3) + "\n");
                        System.out.print("Issued item's name: " + rs.getString(8) + "\n");
                        System.out.print("Issued Item's due date: " + rs.getString(4) + "\n");
                        System.out.print("Issued Item's issue date: " + rs.getString(5) + "\n");
                        PreparedStatement stmtDate = conn.prepareStatement("select datediff(now(),dueDate) from issueditem where libraryCardNumber=? and itemId=?;");
                        stmtDate.setInt(1, libCard);
                        stmtDate.setInt(2, Integer.parseInt(rs.getString(3)));
                        ResultSet rs3 = stmtDate.executeQuery();
                        if (rs3.next()) {
                            int days = Integer.parseInt(rs3.getString(1));
                            if (days > 0) {
                                double fine = days * 0.10;
                                double itemVal=0;
                                PreparedStatement stmtToGetItemValue = conn.prepareStatement("select itemValue from item where itemId=? ;");
                                stmtToGetItemValue.setInt(1, Integer.parseInt(rs.getString(3)));
                                ResultSet rs4 = stmtDate.executeQuery();
                                if (rs4.next()) {
                                    itemVal = Double.parseDouble(rs4.getString(1));
                                }
                                if (fine < itemVal) {
                                    System.out.print("No outstanding days overdue fine: " + (double) Math.round(fine * 100) / 100 + "\n");
                                    sum=sum+fine;
                                } else if (fine >= itemVal) {
                                    System.out.print("No outstanding days overdue fine: " + itemVal + "\n");
                                    sum=sum+fine;
                                }
                            }
                            else{
                                System.out.println("No  outstanding days overdue fine");
                            }

                        }

                    }


                }
                System.out.println("========================================");
                System.out.println("Total outstanding overdue fine:"+(double) Math.round(sum * 100) / 100);
                System.out.println("========================================");
                if(flag==1) {
                    System.out.println("Number of books user has borrowed:"+count);
                }
                else if(flag==0)
                    System.out.println("Invalid card number,user not found!");

                }
            catch(SQLException  e)
            {

                System.out.println(e);


            }

        }
}
