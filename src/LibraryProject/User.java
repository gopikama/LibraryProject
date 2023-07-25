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
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/selibrary","root","GopuSri123@");
            PreparedStatement stmt=conn.prepareStatement("select * from user;");
            ResultSet rs=stmt.executeQuery();

            int flag=0;
            while(rs.next()){


                if (Integer.parseInt(rs.getString(2))==(libraryCardNumber)){
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
                    System.out.println("Invalid card number");

        }
        catch(SQLException  e)
        {

            System.out.println(e);


        }

    }
}
