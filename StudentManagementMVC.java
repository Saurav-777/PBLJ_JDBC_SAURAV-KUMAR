import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

// Model
class Student {
    private int studentID;
    private String name;
    private String department;
    private double marks;

    public Student(int studentID, String name, String department, double marks) {
        this.studentID = studentID;
        this.name = name;
        this.department = department;
        this.marks = marks;
    }

    public int getStudentID() { return studentID; }
    public String getName() { return name; }
    public String getDepartment() { return department; }
    public double getMarks() { return marks; }
}

// Controller
class StudentController {
    private Connection conn;

    public StudentController() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/yourDatabase", "root", "yourPassword");
            conn.setAutoCommit(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addStudent(Student s) throws SQLException {
        PreparedStatement ps = conn.prepareStatement(
            "INSERT INTO Student(StudentID, Name, Department, Marks) VALUES (?, ?, ?, ?)");
        ps.setInt(1, s.getStudentID());
        ps.setString(2, s.getName());
        ps.setString(3, s.getDepartment());
        ps.setDouble(4, s.getMarks());
        ps.executeUpdate();
        conn.commit();
    }

    public List<Student> getAllStudents() throws SQLException {
        List<Student> list = new ArrayList<>();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM Student");
        while (rs.next()) {
            list.add(new Student(rs.getInt("StudentID"),
                                 rs.getString("Name"),
                                 rs.getString("Department"),
                                 rs.getDouble("Marks")));
        }
        return list;
    }

    public void updateStudent(Student s) throws SQLException {
        PreparedStatement ps = conn.prepareStatement(
            "UPDATE Student SET Name=?, Department=?, Marks=? WHERE StudentID=?");
        ps.setString(1, s.getName());
        ps.setString(2, s.getDepartment());
        ps.setDouble(3, s.getMarks());
        ps.setInt(4, s.getStudentID());
        ps.executeUpdate();
        conn.commit();
    }

    public void deleteStudent(int studentID) throws SQLException {
        PreparedStatement ps = conn.prepareStatement(
            "DELETE FROM Student WHERE StudentID=?");
        ps.setInt(1, studentID);
        ps.executeUpdate();
        conn.commit();
    }
}

// View
public class StudentManagementMVC {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        StudentController controller = new StudentController();

        while (true) {
            System.out.println("\nMenu:\n1-Add Student\n2-View Students\n3-Update Student\n4-Delete Student\n5-Exit");
            int choice = sc.nextInt();
            sc.nextLine();

            try {
                switch (choice) {
                    case 1:
                        System.out.print("StudentID: "); int id = sc.nextInt(); sc.nextLine();
                        System.out.print("Name: "); String name = sc.nextLine();
                        System.out.print("Department: "); String dept = sc.nextLine();
                        System.out.print("Marks: "); double marks = sc.nextDouble();
                        controller.addStudent(new Student(id, name, dept, marks));
                        System.out.println("Student added!");
                        break;

                    case 2:
                        List<Student> students = controller.getAllStudents();
                        System.out.println("ID\tName\tDepartment\tMarks");
                        for (Student s : students) {
                            System.out.println(s.getStudentID() + "\t" + s.getName() + "\t" +
                                               s.getDepartment() + "\t" + s.getMarks());
                        }
                        break;

                    case 3:
                        System.out.print("Enter StudentID to update: "); int upID = sc.nextInt(); sc.nextLine();
                        System.out.print("New Name: "); String newName = sc.nextLine();
                        System.out.print("New Department: "); String newDept = sc.nextLine();
                        System.out.print("New Marks: "); double newMarks = sc.nextDouble();
                        controller.updateStudent(new Student(upID, newName, newDept, newMarks));
                        System.out.println("Student updated!");
                        break;

                    case 4:
                        System.out.print("Enter StudentID to delete: "); int delID = sc.nextInt();
                        controller.deleteStudent(delID);
                        System.out.println("Student deleted!");
                        break;

                    case 5:
                        System.out.println("Exiting..."); System.exit(0);

                    default:
                        System.out.println("Invalid choice!");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
