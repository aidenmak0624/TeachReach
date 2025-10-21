package comp3350.teachreach.data.hsqldb;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import comp3350.teachreach.data.interfaces.ICoursePersistence;
import comp3350.teachreach.objects.Course;
import comp3350.teachreach.objects.interfaces.ICourse;

public
class CourseHSQLDB implements ICoursePersistence
{
    private final String dbPath;

    public
    CourseHSQLDB(final String dbPath)
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
    ICourse fromResultSet(final ResultSet rs) throws SQLException
    {
        final String courseCode = rs.getString("course_id");
        final String courseName = rs.getString("course_name");
        return new Course(courseCode, courseName);
    }

    @Override
    public
    Map<String, ICourse> getCourses() throws PersistenceException
    {
        Map<String, ICourse> resultMap = new HashMap<>();
        try (final Connection c = this.connection()) {
            final Statement st = c.createStatement();
            final ResultSet rs = st.executeQuery("SELECT * FROM courses");
            ICourse         theCourse;
            while (rs.next()) {
                theCourse = fromResultSet(rs);
                resultMap.put(theCourse.getCourseCode(), theCourse);
            }
            st.close();
            rs.close();
            return resultMap;
        } catch (SQLException e) {
            throw new PersistenceException(e);
        }
    }

    @Override
    public
    ICourse addCourse(String courseCode, String courseName)
    {
        try (final Connection c = this.connection()) {
            final PreparedStatement pst = c.prepareStatement(
                    "INSERT INTO courses VALUES(?, ?)");
            pst.setString(1, courseCode);
            pst.setString(2, courseName);
            pst.executeUpdate();
            pst.close();
            return new Course(courseCode, courseName);
        } catch (SQLException e) {
            throw new PersistenceException(e);
        }
    }

    @Override
    public
    Optional<ICourse> getCourseByCourseCode(String courseCode)
    {
        ICourse resultCourse = null;
        try (final Connection c = this.connection()) {
            final PreparedStatement pst = c.prepareStatement(
                    "SELECT * FROM courses WHERE course_id = ?");
            pst.setString(1, courseCode);
            final ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                resultCourse = fromResultSet(rs);
            }
            pst.close();
            rs.close();
            return Optional.ofNullable(resultCourse);
        } catch (SQLException e) {
            throw new PersistenceException(e);
        }
    }

    @Override
    public
    List<ICourse> getCoursesByName(String courseName)
    {
        List<ICourse> resultCourses = new ArrayList<>();
        try (final Connection c = this.connection()) {
            final PreparedStatement pst = c.prepareStatement(
                    "SELECT * FROM courses WHERE course_name LIKE ?");
            pst.setString(1, "%" + courseName + "%");
            final ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                resultCourses.add(fromResultSet(rs));
            }
            pst.close();
            rs.close();
            return resultCourses;
        } catch (SQLException e) {
            throw new PersistenceException(e);
        }
    }
}
