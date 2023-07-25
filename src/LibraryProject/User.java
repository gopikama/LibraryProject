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

    }
}
