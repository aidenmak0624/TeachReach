package comp3350.teachreach.data.hsqldb;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import comp3350.teachreach.data.interfaces.IStudentPersistence;
import comp3350.teachreach.objects.Student;
import comp3350.teachreach.objects.interfaces.IStudent;

public
class StudentHSQLDB implements IStudentPersistence
{
    private final String dbPath;

    public
    StudentHSQLDB(final String dbPath)
    {
        this.dbPath = dbPath;
    }

    private
    Connection connection() throws SQLException
    {
        return DriverManager.getConnection(String.format(
                "jdbc:hsqldb:file:%s;shutdown=true",
                dbPath), "SA", "");
    }

    private
    IStudent fromResultSet(final ResultSet rs) throws SQLException
    {
        final int studentID = rs.getInt("student_id");
        final int accountID = rs.getInt("account_id");

        return new Student(studentID, accountID);
    }

    @Override
    public
    IStudent storeStudent(IStudent newStudent)
    {
        return storeStudent(newStudent.getAccountID());
    }

    @Override
    public
    IStudent storeStudent(int accountID) throws RuntimeException
    {
        try (final Connection c = connection()) {
            final PreparedStatement pst = c.prepareStatement(
                    "INSERT INTO students (account_id) VALUES(?)",
                    Statement.RETURN_GENERATED_KEYS);
            pst.setInt(1, accountID);
            pst.executeUpdate();
            final ResultSet rs = pst.getGeneratedKeys();
            if (rs.next()) {
                Student newStudent = new Student(rs.getInt(1), accountID);
                rs.close();
                pst.close();
                return newStudent;
            } else {
                rs.close();
                pst.close();
                throw new PersistenceException("New student not stored!");
            }
        } catch (final SQLException e) {
            throw new PersistenceException(e);
        }
    }

    @Override
    public
    Map<Integer, IStudent> getStudents()
    {
        final Map<Integer, IStudent> students = new HashMap<>();
        try (final Connection c = connection()) {
            final Statement st = c.createStatement();
            final ResultSet rs = st.executeQuery("SELECT * FROM students");
            while (rs.next()) {
                final IStudent student = fromResultSet(rs);
                students.put(student.getAccountID(), student);
            }
            rs.close();
            st.close();
            return students;
        } catch (final SQLException e) {
            throw new PersistenceException(e);
        }
    }
}
