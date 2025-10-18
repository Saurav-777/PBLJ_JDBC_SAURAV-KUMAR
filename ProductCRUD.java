import java.sql.*;
import java.util.Scanner;

public class ProductCRUD {
    static final String URL = "jdbc:mysql://localhost:3306/yourDatabase";
    static final String USER = "root";
    static final String PASS = "yourPassword";

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(URL, USER, PASS);
            conn.setAutoCommit(false);

            while (true) {
                System.out.println("\nMenu: 1-Create 2-Read 3-Update 4-Delete 5-Exit");
                int choice = sc.nextInt();
                sc.nextLine(); // Consume newline

                switch (choice) {
                    case 1:
                        System.out.print("Product Name: ");
                        String name = sc.nextLine();
                        System.out.print("Price: ");
                        double price = sc.nextDouble();
                        System.out.print("Quantity: ");
                        int qty = sc.nextInt();

                        PreparedStatement psInsert = conn.prepareStatement(
                            "INSERT INTO Product(ProductName, Price, Quantity) VALUES (?, ?, ?)");
                        psInsert.setString(1, name);
                        psInsert.setDouble(2, price);
                        psInsert.setInt(3, qty);
                        psInsert.executeUpdate();
                        conn.commit();
                        System.out.println("Product added!");
                        break;

                    case 2:
                        Statement stmt = conn.createStatement();
                        ResultSet rs = stmt.executeQuery("SELECT * FROM Product");
                        System.out.println("ProductID\tName\tPrice\tQuantity");
                        while (rs.next()) {
                            System.out.println(rs.getInt("ProductID") + "\t" +
                                               rs.getString("ProductName") + "\t" +
                                               rs.getDouble("Price") + "\t" +
                                               rs.getInt("Quantity"));
                        }
                        rs.close();
                        stmt.close();
                        break;

                    case 3:
                        System.out.print("Enter ProductID to update: ");
                        int updateID = sc.nextInt();
                        sc.nextLine();
                        System.out.print("New Price: ");
                        double newPrice = sc.nextDouble();
                        System.out.print("New Quantity: ");
                        int newQty = sc.nextInt();

                        PreparedStatement psUpdate = conn.prepareStatement(
                            "UPDATE Product SET Price=?, Quantity=? WHERE ProductID=?");
                        psUpdate.setDouble(1, newPrice);
                        psUpdate.setInt(2, newQty);
                        psUpdate.setInt(3, updateID);
                        psUpdate.executeUpdate();
                        conn.commit();
                        System.out.println("Product updated!");
                        break;

                    case 4:
                        System.out.print("Enter ProductID to delete: ");
                        int deleteID = sc.nextInt();
                        PreparedStatement psDelete = conn.prepareStatement(
                            "DELETE FROM Product WHERE ProductID=?");
                        psDelete.setInt(1, deleteID);
                        psDelete.executeUpdate();
                        conn.commit();
                        System.out.println("Product deleted!");
                        break;

                    case 5:
                        conn.close();
                        System.out.println("Exiting...");
                        System.exit(0);

                    default:
                        System.out.println("Invalid choice!");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        sc.close();
    }
}
