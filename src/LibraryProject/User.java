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
        System.out.println("The details for all users are:");
        try
        {
            //Class.forName("com.mysql.jdbc.Driver");
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/selibrary","root","admin@1234");
            PreparedStatement stmt=conn.prepareStatement("select * from user;");
            ResultSet rs=stmt.executeQuery();

            int flag=0;
            while(rs.next()){
                //fetching all user details for admin/library
                if (libraryCardNumber == 1111){
                    flag=1;
                    if (Integer.parseInt(rs.getString(2))!=libraryCardNumber) {
                        System.out.print("Name: " + rs.getString(1) + "\n");
                    System.out.print("Library Card Number: " + rs.getString(2) + "\n");
                    System.out.print("Address: " + rs.getString(3) + "\n");
                    System.out.print("Phone Number: " + rs.getString(4) + "\n");
                    System.out.print("Age: " + rs.getString(5) + "\n");
                    System.out.print("Total Over Due Fine: " + rs.getString(6) + "\n");
                }
                }
                //fetching a particular user's details for user

                else if (Integer.parseInt(rs.getString(2))==(libraryCardNumber)){
                        flag=1;
                        System.out.print("Name: "+rs.getString(1)+"\n");
                    System.out.print("Library Card Number: "+rs.getString(2)+"\n");
                    System.out.print("Address: "+rs.getString(3)+"\n");
                    System.out.print("Phone Number: "+rs.getString(4)+"\n");
                    System.out.print("Age: "+rs.getString(5)+"\n");
                    System.out.print("Total Over Due Fine: "+rs.getString(6)+"\n");

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
                //fetching all user details
                if (libraryCardNumber == 1111) {
                    flag = 1;
                    System.out.println("");
                    System.out.print("Library Card Number: " + rs.getString(3) + "\n");
                    System.out.print("Issued item's ID: " + rs.getString(2) + "\n");
                    System.out.print("Due Date: " + rs.getString(4) + "\n");
                    System.out.print("Outstanding days: " + rs.getString(7) + "\n");

                }


            }

            if (flag == 0)
                System.out.println("Invalid card number,user not found!");

        } catch (SQLException e) {

            System.out.println(e);


        }
    }
        public void displayBooksIssuedToUser(int libCard,String name){

            System.out.println("Books issued to library card number: "+libCard+" are: ");
            try
            {
                //Class.forName("com.mysql.jdbc.Driver");
                Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/selibrary","root","GopuSri123@");
                PreparedStatement stmt=conn.prepareStatement("select * from issueditem natural join item natural join user where libraryCardNumber=? and name=? ;");
                stmt.setInt(1,libCard);
                stmt.setString(2,name);
                ResultSet rs=stmt.executeQuery();

                int flag=0;
                while(rs.next()){
                    //fetching all user details
                    if (libraryCardNumber == 1111){
                        flag=1;
                        System.out.println("");
                        System.out.print("User Name: " + rs.getString(13) + "\n");
                        System.out.print("Issued Item's ID: " + rs.getString(3) + "\n");
                        System.out.print("Issued item's name: " + rs.getString(8) + "\n");
                        System.out.print("Issued Item's due date: " + rs.getString(4) + "\n");
                        System.out.print("Issued Item's issue date: " + rs.getString(5) + "\n");
                        System.out.print("Outstanding days: " + rs.getString(7) + "\n");//issue

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
}
