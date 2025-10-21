package comp3350.teachreach.data.hsqldb;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import comp3350.teachreach.data.interfaces.ITutorLocationPersistence;

public class TutorLocationHSQLDB implements ITutorLocationPersistence
{
    private final String dbPath;

    public TutorLocationHSQLDB(final String dbPath)
    {
        this.dbPath = dbPath;
    }

    private Connection connection() throws SQLException
    {
        return DriverManager.getConnection(String.format(
                "jdbc:hsqldb:file:%s;shutdown=true",
                dbPath), "SA", "");
    }

    private String fromResultSet(final ResultSet rs) throws SQLException
    {
        final String location = rs.getString("location_name");

        return location;
    }

    @Override
    public List<String> getTutorLocationByTutorID(int tutorID)
    {
        final List<String> tutorLocation = new ArrayList<>();
        try (final Connection c = connection()) {
            final PreparedStatement pst = c.prepareStatement(
                    "SELECT * FROM TUTOR_LOCATIONS WHERE tutor_id = ?");
            pst.setInt(1, tutorID);
            final ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                final String theLocation = fromResultSet(rs);
                tutorLocation.add(theLocation);
            }
            pst.close();
            rs.close();
            return tutorLocation;
        } catch (final SQLException e) {
            throw new PersistenceException(e);
        }
    }

    @Override
    public boolean storeTutorLocation(int tutorID, String location)
    {
        try (final Connection c = this.connection()) {
            final PreparedStatement pst = c.prepareStatement(
                    "INSERT INTO tutor_locations VALUES(?, ?)");

            pst.setInt(1, tutorID);
            pst.setString(2, location);

            final boolean success = pst.executeUpdate() == 1;
            if (!success) {
                throw new PersistenceException(
                        "New tutor location mightn't be " + "stored!");
            }
            return true;
        } catch (final SQLException e) {
            throw new PersistenceException(e);
        }
    }
}
