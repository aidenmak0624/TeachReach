package comp3350.teachreach.data.hsqldb;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import comp3350.teachreach.data.exceptions.DuplicateEmailException;
import comp3350.teachreach.data.interfaces.IAccountPersistence;
import comp3350.teachreach.objects.Account;
import comp3350.teachreach.objects.interfaces.IAccount;

public
class AccountHSQLDB implements IAccountPersistence
{
    private final String dbPath;

    public
    AccountHSQLDB(final String dbPath)
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
    IAccount fromResultSet(final ResultSet rs) throws SQLException
    {
        final int    accountID       = rs.getInt("account_id");
        final String accountEmail    = rs.getString("email");
        final String accountPassword = rs.getString("password");
        final String userName        = rs.getString("name");
        final String userPronouns    = rs.getString("pronouns");
        final String userMajor       = rs.getString("major");

        return new Account(accountEmail,
                           accountPassword,
                           userName,
                           userPronouns,
                           userMajor,
                           accountID);
    }

    @Override
    public IAccount storeAccount(IAccount newAccount) throws DuplicateEmailException {
        try (final Connection c = connection()) {
            final PreparedStatement pst = c.prepareStatement(
                    "INSERT INTO ACCOUNTS(EMAIL, PASSWORD, NAME, PRONOUNS, " +
                            "MAJOR) VALUES(?, ?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS);
            pst.setString(1, newAccount.getAccountEmail());
            pst.setString(2, newAccount.getAccountPassword());
            pst.setString(3, newAccount.getUserName());
            pst.setString(4, newAccount.getUserPronouns());
            pst.setString(5, newAccount.getUserMajor());
            final boolean success = pst.executeUpdate() == 1;
            if (!success) {
                pst.close();
                throw new PersistenceException(
                        "Potential accountID " + "collision!");
            }
            final ResultSet rs = pst.getGeneratedKeys();
            if (rs.next()) {
                newAccount.setAccountID(rs.getInt(1));
            }
            pst.close();
            rs.close();
            return newAccount;
        } catch (final SQLIntegrityConstraintViolationException e) {
            throw new DuplicateEmailException("Cannot create account with duplicate email");
        } catch (final Exception e) {
            throw new PersistenceException(e);
        }
    }

    @Override
    public
    IAccount updateAccount(IAccount existingAccount)
    {
        try (final Connection c = connection()) {
            final PreparedStatement pst = c.prepareStatement(
                    "UPDATE ACCOUNTS SET EMAIL = ?, PASSWORD = ?, " +
                    "NAME = ?, PRONOUNS = ?, MAJOR = ? " +
                    "WHERE ACCOUNT_ID = ?");
            pst.setString(1, existingAccount.getAccountEmail());
            pst.setString(2, existingAccount.getAccountPassword());
            pst.setString(3, existingAccount.getUserName());
            pst.setString(4, existingAccount.getUserPronouns());
            pst.setString(5, existingAccount.getUserMajor());
            pst.setInt(6, existingAccount.getAccountID());

            final boolean success = pst.executeUpdate() == 1;
            if (!success) {
                pst.close();
                throw new PersistenceException(
                        "Account not found/not " + "updated!");
            }
            pst.close();
            return existingAccount;
        } catch (final SQLException e) {
            throw new PersistenceException(e);
        }
    }

    @Override
    public synchronized
    Map<Integer, IAccount> getAccounts()
    {
        try (final Connection c = connection()) {
            final Statement st = c.createStatement();
            final ResultSet rs = st.executeQuery("SELECT * FROM accounts");

            Map<Integer, IAccount> resultMap = new HashMap<>();
            while (rs.next()) {
                IAccount resultAccount = fromResultSet(rs);
                resultMap.put(resultAccount.getAccountID(), resultAccount);
            }
            rs.close();
            st.close();
            return resultMap;
        } catch (final SQLException e) {
            throw new PersistenceException(e);
        }
    }
}
