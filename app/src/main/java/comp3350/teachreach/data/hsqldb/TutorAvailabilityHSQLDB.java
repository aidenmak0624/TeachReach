package comp3350.teachreach.data.hsqldb;

import org.threeten.bp.DateTimeUtils;
import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalDateTime;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import comp3350.teachreach.data.interfaces.ITutorAvailabilityPersistence;
import comp3350.teachreach.objects.TimeSlice;
import comp3350.teachreach.objects.interfaces.ITimeSlice;
import comp3350.teachreach.objects.interfaces.ITutor;

public class TutorAvailabilityHSQLDB implements ITutorAvailabilityPersistence
{

    private final String dbPath;

    public TutorAvailabilityHSQLDB(final String dbPath)
    {
        this.dbPath = dbPath;
    }

    private Connection connection() throws SQLException
    {
        return DriverManager.getConnection(String.format(
                "jdbc:hsqldb:file:%s;shutdown=true",
                dbPath), "SA", "");
    }

    private TimeSlice fromResultSet(final ResultSet rs) throws SQLException
    {
        final LocalDateTime startDateTime
                                        =
                DateTimeUtils.toLocalDateTime(rs.getTimestamp(
                "START_DATE_TIME"));
        final LocalDateTime endDateTime
                                        =
                DateTimeUtils.toLocalDateTime(rs.getTimestamp(
                "END_DATE_TIME"));

        return new TimeSlice(startDateTime, endDateTime);
    }

    public List<ITimeSlice> getAvailability(ITutor tutor)
    {
        final List<ITimeSlice> availability = new ArrayList<>();
        try (final Connection c = connection()) {
            final PreparedStatement pst = c.prepareStatement(
                    "SELECT * " + "FROM TUTOR_AVAILABILITY " +
                    "WHERE TUTOR_ID = ? " +
                    "  AND START_DATE_TIME > CURRENT_TIMESTAMP;");
            pst.setInt(1, tutor.getTutorID());
            final ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                final TimeSlice theTimeSlice = fromResultSet(rs);
                availability.add(theTimeSlice);
            }
            pst.close();
            rs.close();
            return availability;
        } catch (final SQLException e) {
            throw new PersistenceException(e);
        }
    }

    @Override
    public List<ITimeSlice> getAvailabilityOnDay(ITutor tutor, LocalDate date)
    {
        final List<ITimeSlice> availability = new ArrayList<>();
        try (final Connection c = connection()) {
            Date sqlDate = DateTimeUtils.toSqlDate(date);
            final PreparedStatement pst = c.prepareStatement(
                    "SELECT * " + "FROM TUTOR_AVAILABILITY " +
                            "WHERE TUTOR_ID = ? " +
                            "  AND CAST(? AS DATE) BETWEEN CAST(START_DATE_TIME AS DATE) AND CAST(END_DATE_TIME AS DATE) " +
                            "  AND START_DATE_TIME > CURRENT_TIMESTAMP;");
            pst.setInt(1, tutor.getTutorID());
            pst.setDate(2, sqlDate);
            final ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                final TimeSlice theTimeSlice = fromResultSet(rs);
                availability.add(theTimeSlice);
            }
            pst.close();
            rs.close();
            return availability;
        } catch (final SQLException e) {
            throw new PersistenceException(e);
        }
    }

    public void addAvailability(ITutor tutor, ITimeSlice timeRange)
    {
        try (final Connection c = this.connection()) {
            final PreparedStatement pst = c.prepareStatement(
                    "INSERT INTO TUTOR_AVAILABILITY VALUES(?, ?, ?)");

            pst.setInt(1, tutor.getTutorID());
            pst.setTimestamp(2,
                             DateTimeUtils.toSqlTimestamp(timeRange.getStartTime()));
            pst.setTimestamp(3,
                             DateTimeUtils.toSqlTimestamp(timeRange.getEndTime()));

            final boolean success = pst.executeUpdate() == 1;
            if (!success) {
                throw new PersistenceException(
                        "New tutor availability mightn't be " + "stored!");
            }
        } catch (final SQLException e) {
            throw new PersistenceException(e);
        }
    }

    @Override
    public void removeAvailability(ITutor tutor, ITimeSlice timeRange)
    {
        try (final Connection c = this.connection()) {
            final PreparedStatement pst = c.prepareStatement(
                    "DELETE FROM TUTOR_AVAILABILITY WHERE TUTOR_ID = ? AND " +
                    "START_DATE_TIME = ? AND END_DATE_TIME = ?");

            pst.setInt(1, tutor.getTutorID());
            pst.setTimestamp(2,
                             DateTimeUtils.toSqlTimestamp(timeRange.getStartTime()));
            pst.setTimestamp(3,
                             DateTimeUtils.toSqlTimestamp(timeRange.getEndTime()));

            final boolean success = pst.executeUpdate() == 1;
            if (!success) {
                throw new PersistenceException(
                        "Tutor availability mightn't be " + "removed!");
            }
        } catch (final SQLException e) {
            throw new PersistenceException(e);
        }
    }
}
