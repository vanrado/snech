package snech.core.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import org.springframework.stereotype.Service;
import snech.core.types.Attachment;
import snech.core.types.Issue;
import snech.core.types.IssueLog;
import snech.core.types.Notice;
import snech.core.types.User;
import snech.core.types.enums.EIssueLogType;
import snech.core.types.enums.EIssuePriority;
import snech.core.types.enums.EIssueStatus;
import snech.core.types.enums.EUserRole;

/**
 *
 * @author Radovan Račák
 */
@Service
public class DatabaseServiceImpl implements IDatabaseService {

    private DataSource dataSource;

    public DatabaseServiceImpl() {
        try {
            Context initCtx = new InitialContext();
            Context envCtx = (Context) initCtx.lookup("java:comp/env");
            this.dataSource = (DataSource) envCtx.lookup("jdbc/snechDB");
        } catch (NamingException nEx) {
            throw new RuntimeException("Unable to aquire data source", nEx);
        }
    }

    @Override
    public User getUserLogin(String login) {
        User user = null;
        Connection connection = null;
        PreparedStatement statement = null;
        String selectSQL = "SELECT * FROM user_logins "
                + "inner join users on user_logins.user_id = users.user_id "
                + "inner join user_roles on user_logins.role_id=user_roles.ROLE_ID where login=?";
        ResultSet rs = null;

        try {
            connection = dataSource.getConnection();
            statement = connection.prepareStatement(selectSQL);
            statement.setString(1, login);
            rs = statement.executeQuery();

            if (rs.next()) {
                user = new User();

                //long userId = rs.getLong("login_id");
                String userLogin = rs.getString("login");
                String userFirstName = rs.getString("first_name");
                String userLastName = rs.getString("last_name");
                String userEmail = rs.getString("email");
                String userOccupation = rs.getString("occupation");
                String password = rs.getString("password");
                String salt = rs.getString("salt");
                EUserRole userRole = EUserRole.valueOf(rs.getString("role_name").toUpperCase());

                //user.setId(userId);
                user.setLogin(userLogin);
                user.setFirstName(userFirstName);
                user.setLastName(userLastName);
                user.setEmail(userEmail);
                user.setOccupation(userOccupation);
                user.setPassword(password);
                user.setSalt(salt);
                user.setUserRole(userRole);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(DatabaseServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException ex) {
                    Logger.getLogger(DatabaseServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException ex) {
                    Logger.getLogger(DatabaseServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

        return user;
    }

    @Override
    public List<User> getUsers() {
        Connection connection = null;
        PreparedStatement statement = null;
        String selectSQL = "SELECT first_name, last_name, login, user_roles.role_name FROM user_logins "
                + "inner join users on user_logins.user_id=users.user_id "
                + "inner join user_roles on user_logins.role_id=user_roles.role_id "
                + "where user_logins.role_id != 3";
        ResultSet rs = null;
        ArrayList<User> users = new ArrayList<>();

        try {
            connection = dataSource.getConnection();
            statement = connection.prepareStatement(selectSQL);
            rs = statement.executeQuery();

            while (rs.next()) {
                User user = new User();
                user.setFirstName(rs.getString("first_name"));
                user.setLastName(rs.getString("last_name"));
                user.setLogin(rs.getString("login"));
                user.setUserRole(EUserRole.valueOf(rs.getString("role_name")));
                users.add(user);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(DatabaseServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(DatabaseServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException ex) {
                    Logger.getLogger(DatabaseServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException ex) {
                    Logger.getLogger(DatabaseServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

        return users;
    }

    /**
     * Vrati oznamy
     *
     * @param allNotices ak je parameter true, vrati vsetky oznamy, ak false
     * vrati len oznamy ktore su viditelne
     * @return kontajner oznamov
     */
    @Override
    public List<Notice> getNotices(boolean allNotices) {
        ArrayList<Notice> notices = new ArrayList<>();
        Connection connection = null;
        PreparedStatement statement = null;
        String selectSQL = "select * from notices where visible = '1' order by created_on DESC";
        ResultSet rs = null;

        try {
            connection = dataSource.getConnection();
            statement = connection.prepareStatement(selectSQL);

            rs = statement.executeQuery();

            while (rs.next()) {
                Notice notice = new Notice();
                notice.setAuthor(rs.getLong("author_id") + "");
                notice.setContent(rs.getString("message"));
                notice.setFormatedDate(rs.getTimestamp("created_on"));
                notice.setSubject(rs.getString("subject"));
                notices.add(notice);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(DatabaseServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException ex) {
                    Logger.getLogger(DatabaseServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException ex) {
                    Logger.getLogger(DatabaseServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return notices;
    }

    @Override
    public List<Issue> getIssues(String userId, boolean deleted) {
        ArrayList<Issue> issues = new ArrayList<>();
        Connection connection = null;
        PreparedStatement statement = null;
        String selectSQL;
        if (deleted) {
            selectSQL = "SELECT * FROM issues where user_login=?";
        } else {
            selectSQL = "SELECT * FROM issues where user_login=? and code_status!='VYMAZANA'";
        }

        if (userId.equals("admin")) {
            selectSQL = "SELECT * FROM issues order by created_on DESC";
        }

        ResultSet rs = null;

        try {
            connection = dataSource.getConnection();
            statement = connection.prepareStatement(selectSQL);
            if (!userId.equals("admin")) {
                statement.setString(1, userId);
            }
            rs = statement.executeQuery();

            while (rs.next()) {
                Issue issue = new Issue();
                String message = rs.getString("message");
                String subject = rs.getString("subject");

                issue.setId(rs.getLong("issue_id"));
                issue.setEstimatedDate(rs.getTimestamp("estimated_time"));
                issue.setCreatedDate(rs.getTimestamp("created_on"));
                issue.setLastUpdatedDate(rs.getTimestamp("last_update"));
                issue.setMessage(message != null ? message : "");
                issue.setSubject(subject != null ? subject : "");
                issue.setPriority(EIssuePriority.getPriorityFromString(rs.getString("CODE_PRIORITY")));
                issue.setStatus(EIssueStatus.valueOf(rs.getString("code_status")));
                issue.setProgress(rs.getInt("progress"));
                issue.setUserLogin(rs.getString("user_login"));
                issues.add(issue);
            }

        } catch (SQLException ex) {
            Logger.getLogger(DatabaseServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(DatabaseServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(DatabaseServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException ex) {
                    Logger.getLogger(DatabaseServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException ex) {
                    Logger.getLogger(DatabaseServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return issues;
    }

    @Override
    public List<Issue> getIssues(EIssueStatus status, int numberOfDays, boolean notAssigned) {
        ArrayList<Issue> issues = new ArrayList<>();
        Connection connection = null;
        PreparedStatement statement = null;
        String selectSQL;

        selectSQL = "select subject, trunc(sysdate) - trunc(created_on) as pocetdni from issues"
                + " where code_status='NOVA' and (trunc(sysdate) - trunc(created_on)) < 30 and"
                + " issue_id not in (select issue_id from assigning_issues)";

        if(status != null){
         
       }
        ResultSet rs = null;

        try {
            connection = dataSource.getConnection();
            statement = connection.prepareStatement(selectSQL);
            rs = statement.executeQuery();

            while (rs.next()) {
                Issue issue = new Issue();
                String message = rs.getString("message");
                String subject = rs.getString("subject");

                issue.setId(rs.getLong("issue_id"));
                issue.setEstimatedDate(rs.getTimestamp("estimated_time"));
                issue.setCreatedDate(rs.getTimestamp("created_on"));
                issue.setLastUpdatedDate(rs.getTimestamp("last_update"));
                issue.setMessage(message != null ? message : "");
                issue.setSubject(subject != null ? subject : "");
                issue.setPriority(EIssuePriority.getPriorityFromString(rs.getString("CODE_PRIORITY")));
                issue.setStatus(EIssueStatus.valueOf(rs.getString("code_status")));
                issue.setProgress(rs.getInt("progress"));
                issue.setUserLogin(rs.getString("user_login"));
                issues.add(issue);
            }

        } catch (SQLException ex) {
            Logger.getLogger(DatabaseServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(DatabaseServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(DatabaseServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException ex) {
                    Logger.getLogger(DatabaseServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException ex) {
                    Logger.getLogger(DatabaseServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return issues;
    }

    @Override
    public long insertIssue(Issue issue) {
        Connection connection = null;
        PreparedStatement statement = null;
        String selectSQL = "insert into ISSUES (ISSUE_ID,USER_LOGIN,SUBJECT,CODE_PRIORITY,CODE_STATUS,ESTIMATED_TIME,CREATED_ON,LAST_UPDATE,MESSAGE, PROGRESS) "
                + "values (issue_id_seq.nextval, ?, ?, ?,'NOVA', null, CURRENT_TIMESTAMP, null, ?, ?)";
        ResultSet rs = null;
        boolean success = true;
        long issueId = -1;

        try {
            connection = dataSource.getConnection();
            statement = connection.prepareStatement(selectSQL);
            statement.setString(1, issue.getUserLogin());
            statement.setString(2, issue.getSubject());
            statement.setString(3, issue.getPriority().name());
            statement.setString(4, issue.getMessage());
            statement.setInt(5, 0);
            rs = statement.executeQuery();
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
            Logger.getLogger(DatabaseServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
            success = false;
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(DatabaseServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
                    Logger.getLogger(DatabaseServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException ex) {
                    Logger.getLogger(DatabaseServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
                    Logger.getLogger(DatabaseServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException ex) {
                    Logger.getLogger(DatabaseServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
                    Logger.getLogger(DatabaseServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            if (success) {
                issueId = getIssueId(issue.getSubject());
                insertIssueLog(issueId, EIssueLogType.VYTVORENIE, issue.getUserLogin(), "");
            }
        }

        return issueId;
    }

    @Override
    public Issue getIssue(long issueId) {
        Issue issue = new Issue();

        Connection connection = null;
        PreparedStatement statement = null;
        String selectSQL = "SELECT * FROM ISSUES where issue_id=?";
        ResultSet rs = null;

        String test = "";
        try {
            connection = dataSource.getConnection();
            statement = connection.prepareStatement(selectSQL);
            statement.setLong(1, issueId);
            rs = statement.executeQuery();

            if (rs.next()) {
                String message = rs.getString("message");
                String subject = rs.getString("subject");

                issue.setId(rs.getLong("issue_id"));
                issue.setUserLogin(rs.getString("user_login"));
                //issue.setAssignedAdminId(rs.getLong("ASSIGNED_ADMIN_LOGIN"));
                issue.setEstimatedDate(rs.getTimestamp("estimated_time"));
                issue.setCreatedDate(rs.getTimestamp("created_on"));
                issue.setLastUpdatedDate(rs.getTimestamp("last_update"));
                issue.setMessage(message != null ? message : "");
                issue.setSubject(subject != null ? subject : "");
                issue.setPriority(EIssuePriority.valueOf(rs.getString("CODE_PRIORITY")));
                issue.setStatus(EIssueStatus.valueOf(rs.getString("code_status")));
                issue.setProgress(rs.getInt("progress"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
            Logger.getLogger(DatabaseServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(DatabaseServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
                    Logger.getLogger(DatabaseServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException ex) {
                    Logger.getLogger(DatabaseServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
                    Logger.getLogger(DatabaseServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException ex) {
                    Logger.getLogger(DatabaseServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
                    Logger.getLogger(DatabaseServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return issue;
    }

    @Override
    public boolean removeIssue(long id) {
        boolean success = true;

        Connection connection = null;
        PreparedStatement statement = null;
        String selectSQL = "delete from issues where issue_id=?";
        ResultSet rs = null;

        try {
            connection = dataSource.getConnection();
            statement = connection.prepareStatement(selectSQL);
            statement.setLong(1, id);
            rs = statement.executeQuery();
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
            success = false;
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(DatabaseServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException ex) {
                    Logger.getLogger(DatabaseServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException ex) {
                    Logger.getLogger(DatabaseServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

        return success;
    }

    @Override
    public boolean updateIssue(Issue issue, String login) {
        Connection connection = null;
        PreparedStatement statement = null;
        String selectSQL = "update issues set subject=?, code_priority=?, message=?, progress=?, code_status=?, estimated_time=? where issue_id=?";
        ResultSet rs = null;
        boolean success = true;

        try {
            connection = dataSource.getConnection();
            statement = connection.prepareStatement(selectSQL);
            statement.setString(1, issue.getSubject());
            statement.setString(2, issue.getPriority().name());
            statement.setString(3, issue.getMessage());
            statement.setLong(4, issue.getProgress());
            statement.setString(5, issue.getStatus().name());
            statement.setTimestamp(6, issue.getEstimatedDate());
            statement.setLong(7, issue.getId());

            rs = statement.executeQuery();
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
            success = false;
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(DatabaseServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException ex) {
                    Logger.getLogger(DatabaseServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException ex) {
                    Logger.getLogger(DatabaseServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            if (success) {
                insertIssueLog(issue.getId(), EIssueLogType.AKTUALIZACIA, login, "");
            }
        }

        return success;
    }

    @Override
    public boolean setIssueStatus(EIssueStatus status, long id, String author) {
        boolean success = true;

        Connection connection = null;
        PreparedStatement statement = null;
        String selectSQL = "update issues set code_status = ? where issue_id = ?";
        ResultSet rs = null;

        try {
            connection = dataSource.getConnection();
            statement = connection.prepareStatement(selectSQL);
            statement.setString(1, status.name());
            statement.setLong(2, id);
            rs = statement.executeQuery();
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
            success = false;
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(DatabaseServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException ex) {
                    Logger.getLogger(DatabaseServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException ex) {
                    Logger.getLogger(DatabaseServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            if (success) {
                if (status.equals(EIssueStatus.VYMAZANA)) {
                    insertIssueLog(id, EIssueLogType.ZMAZANIE, author, "");
                } else {
                    insertIssueLog(id, EIssueLogType.INE, author, "");
                }
            }
        }

        return success;
    }

    @Override
    public User getUser(long userId) {
        Connection connection = null;
        PreparedStatement statement = null;
        String selectSQL = "SELECT * FROM users where user_id=?";
        ResultSet rs = null;
        User user = null;

        try {
            connection = dataSource.getConnection();
            statement = connection.prepareStatement(selectSQL);
            statement.setLong(1, userId);
            rs = statement.executeQuery();

            if (rs.next()) {
                user = new User();
                user.setId(rs.getLong("user_id"));
                user.setFirstName(rs.getString("first_name"));
                user.setLastName(rs.getString("last_name"));
                user.setOccupation(rs.getString("occupation"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(DatabaseServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException ex) {
                    Logger.getLogger(DatabaseServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException ex) {
                    Logger.getLogger(DatabaseServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

        return user;
    }

    @Override
    public boolean updateUser(User user) {
        final String UNKNOWN = "-";
        Connection connection = null;
        PreparedStatement statement = null;
        String selectSQL = "update users set first_name=?, last_name=?, email=?, occupation=? where user_id in (select user_id from user_logins where user_logins.login=?)";
        ResultSet rs = null;
        boolean success = true;
        String firstName = user.getFirstName() != null ? user.getFirstName() : UNKNOWN;
        String lastName = user.getLastName() != null ? user.getLastName() : UNKNOWN;
        String email = user.getEmail() != null ? user.getEmail() : UNKNOWN;
        String occupation = user.getOccupation() != null ? user.getOccupation() : UNKNOWN;

        try {
            connection = dataSource.getConnection();
            statement = connection.prepareStatement(selectSQL);
            statement.setString(1, firstName);
            statement.setString(2, lastName);
            statement.setString(3, email);
            statement.setString(4, occupation);
            statement.setString(5, user.getLogin());

            rs = statement.executeQuery();
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
            success = false;
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(DatabaseServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException ex) {
                    Logger.getLogger(DatabaseServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException ex) {
                    Logger.getLogger(DatabaseServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

        return success;
    }

    @Override
    public boolean updateLoginPassword(String login, String newPassword, String newSalt) {
        Connection connection = null;
        PreparedStatement statement = null;
        String selectSQL = "update user_logins set password=?, salt=? where login=?";
        ResultSet rs = null;
        boolean success = true;

        try {
            connection = dataSource.getConnection();
            statement = connection.prepareStatement(selectSQL);
            statement.setString(1, newPassword);
            statement.setString(2, newSalt);
            statement.setString(3, login);
            rs = statement.executeQuery();
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
            success = false;
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(DatabaseServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException ex) {
                    Logger.getLogger(DatabaseServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException ex) {
                    Logger.getLogger(DatabaseServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

        return success;
    }

    @Override
    public boolean insertIssueLog(long issueId, EIssueLogType logType, String author, String description) {
        boolean success = true;
        Connection connection = null;
        PreparedStatement statement = null;

        String selectSQL = "insert into ISSUE_LOGS (LOG_ID, ISSUE_ID, LOG_TYPE, AUTHOR_LOGIN, DESCRIPTION, CREATED_ON) values (issue_logs_seq.nextval, ?, ?, ?, ?, CURRENT_TIMESTAMP)";
        ResultSet rs = null;

        try {
            connection = dataSource.getConnection();
            statement = connection.prepareStatement(selectSQL);
            statement.setLong(1, issueId);
            statement.setString(2, logType.name());
            statement.setString(3, author);
            statement.setString(4, description);
            rs = statement.executeQuery();
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
            success = false;
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(DatabaseServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException ex) {
                    Logger.getLogger(DatabaseServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException ex) {
                    Logger.getLogger(DatabaseServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

        return success;
    }

    @Override
    public List<IssueLog> getIssueLogs(String userLogin) {
        List<IssueLog> issueLogs = new ArrayList<>();
        Connection connection = null;
        PreparedStatement statement = null;
        String selectSQL = "select * from ISSUE_LOGS where issue_id in (select issue_id from issues where user_login=?) order by created_on DESC";
        ResultSet rs = null;

        String test = "";

        try {
            connection = dataSource.getConnection();
            statement = connection.prepareStatement(selectSQL);
            statement.setString(1, userLogin);
            rs = statement.executeQuery();

            while (rs.next()) {
                IssueLog log = new IssueLog();
                log.setAuthorLogin(rs.getString("author_login"));
                log.setCreatedOn(rs.getTimestamp("created_on"));
                log.setDescription(rs.getString("description"));
                log.setLogId(rs.getLong("log_id"));
                log.setIssueId(rs.getLong("issue_id"));
                log.setLogType(EIssueLogType.valueOf(rs.getString("log_type")));
                issueLogs.add(log);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(DatabaseServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException ex) {
                    Logger.getLogger(DatabaseServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException ex) {
                    Logger.getLogger(DatabaseServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

        return issueLogs;
    }

    @Override
    public String getLoginSalt(String userLogin) {
        Connection connection = null;
        PreparedStatement statement = null;
        String selectSQL = "SELECT salt FROM user_logins where login=?";
        ResultSet rs = null;
        String salt = null;

        try {
            connection = dataSource.getConnection();
            statement = connection.prepareStatement(selectSQL);
            statement.setString(1, userLogin);
            rs = statement.executeQuery();

            if (rs.next()) {
                salt = rs.getString("salt");
            }
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(DatabaseServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException ex) {
                    Logger.getLogger(DatabaseServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException ex) {
                    Logger.getLogger(DatabaseServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

        return salt;
    }

    @Override
    public long getUploadsCount() {
        Connection connection = null;
        PreparedStatement statement = null;
        //String selectSQL = "select last_number from user_sequences where sequence_name='UPLOAD_FOLDER_SEQ'";
        String selectSQL = "select upload_folder_seq.NEXTVAL from dual";
        ResultSet rs = null;
        long count = -1;

        try {
            connection = dataSource.getConnection();
            statement = connection.prepareStatement(selectSQL);
            rs = statement.executeQuery();

            if (rs.next()) {
                count = rs.getLong("nextval");
            }
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(DatabaseServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException ex) {
                    Logger.getLogger(DatabaseServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException ex) {
                    Logger.getLogger(DatabaseServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

        return count;
    }

    @Override
    public void incrementUploads() {
        Connection connection = null;
        PreparedStatement statement = null;
        String selectSQL = "select upload_folder_seq.nextval from dual";
        ResultSet rs = null;

        try {
            connection = dataSource.getConnection();
            statement = connection.prepareStatement(selectSQL);
            rs = statement.executeQuery();
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(DatabaseServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException ex) {
                    Logger.getLogger(DatabaseServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException ex) {
                    Logger.getLogger(DatabaseServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    @Override
    public boolean insertAttachment(Attachment attachment) {
        Connection connection = null;
        PreparedStatement statement = null;
        String selectSQL = "insert into attachments(attachment_id, ISSUE_ID, file_url, file_name, file_size) values (ATTACHMENT_ID_seq.nextval, ?, ?, ?, ?)";
        ResultSet rs = null;
        boolean success = true;
        try {
            connection = dataSource.getConnection();
            statement = connection.prepareStatement(selectSQL);
            statement.setLong(1, attachment.getIssueId());
            statement.setString(2, attachment.getFileUrl());
            statement.setString(3, attachment.getFileName());
            statement.setLong(4, attachment.getFileSize());
            rs = statement.executeQuery();
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
            success = false;
        } catch (Exception ex) {
            Logger.getLogger(DatabaseServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
            success = false;
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(DatabaseServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException ex) {
                    Logger.getLogger(DatabaseServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException ex) {
                    Logger.getLogger(DatabaseServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

        return success;
    }

    /**
     *
     * @param issueId -1 ak hladame podla messageId
     * @return
     */
    @Override
    public List<Attachment> getAttachments(long issueId) {
        List<Attachment> attachments = new ArrayList<>();
        Connection connection = null;
        PreparedStatement statement = null;
        String selectSQL = "select * from attachments where issue_id=?";
        ResultSet rs = null;

        try {
            connection = dataSource.getConnection();
            statement = connection.prepareStatement(selectSQL);
            statement.setLong(1, issueId);
            rs = statement.executeQuery();

            while (rs.next()) {
                Attachment attachment = new Attachment();
                attachment.setId(rs.getLong("attachment_id"));
                attachment.setIssueId(rs.getLong("issue_id"));
                attachment.setFileUrl(rs.getString("file_url"));
                attachment.setFileName(rs.getString("file_name"));
                attachment.setFileSize(rs.getLong("file_size"));
                attachments.add(attachment);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(DatabaseServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException ex) {
                    Logger.getLogger(DatabaseServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException ex) {
                    Logger.getLogger(DatabaseServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

        return attachments.size() > 0 ? attachments : null;
    }

    @Override
    public boolean removeAttachment(long attachmentId) {
        Connection connection = null;
        PreparedStatement statement = null;
        String selectSQL = "delete from attachments where attachment_id=?";
        ResultSet rs = null;

        boolean success = true;

        try {
            connection = dataSource.getConnection();
            statement = connection.prepareStatement(selectSQL);
            statement.setLong(1, attachmentId);
            rs = statement.executeQuery();
        } catch (SQLException ex) {
            success = false;
            Logger.getLogger(DatabaseServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            success = false;
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(DatabaseServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException ex) {
                    Logger.getLogger(DatabaseServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException ex) {
                    Logger.getLogger(DatabaseServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return success;
    }

    @Override
    public List<User> getTechnicians() {
        List<User> developers = new ArrayList<>();
        Connection connection = null;
        PreparedStatement statement = null;
        String selectSQL = "select users.FIRST_NAME, users.LAST_NAME, user_roles.role_name, login, users.email from user_logins "
                + "inner join user_roles on user_roles.ROLE_ID = user_logins.ROLE_ID "
                + "inner join users on users.USER_ID = user_logins.USER_ID "
                + "where role_name='TECHNIK'";
        ResultSet rs = null;

        try {
            connection = dataSource.getConnection();
            statement = connection.prepareStatement(selectSQL);

            rs = statement.executeQuery();

            while (rs.next()) {
                User user = new User();
                user.setLogin(rs.getString("login"));
                user.setFirstName(rs.getString("first_name"));
                user.setLastName(rs.getString("last_name"));
                user.setUserRole(EUserRole.valueOf(rs.getString("role_name")));
                user.setEmail(rs.getString("email"));
                developers.add(user);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(DatabaseServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException ex) {
                    Logger.getLogger(DatabaseServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException ex) {
                    Logger.getLogger(DatabaseServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

        return developers;
    }

    @Override
    public boolean assignIssueToTechnician(long issueId, User user) {
        Connection connection = null;
        PreparedStatement statement = null;
        String selectSQL = "insert into assigning_issues(login, issue_id) values(?, ?)";
        ResultSet rs = null;
        boolean success = true;

        try {
            connection = dataSource.getConnection();
            statement = connection.prepareStatement(selectSQL);
            statement.setString(1, user.getLogin());
            statement.setLong(2, issueId);
            rs = statement.executeQuery();
            System.out.println("Pridane");
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
            success = false;
        } catch (Exception ex) {
            Logger.getLogger(DatabaseServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(DatabaseServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException ex) {
                    Logger.getLogger(DatabaseServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException ex) {
                    Logger.getLogger(DatabaseServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

        return success;
    }

    @Override
    public List<User> getAssignedTechnicians(long issueId) {
        ArrayList<User> technicians = new ArrayList<>();
        Connection connection = null;
        PreparedStatement statement = null;
        String selectSQL = "select users.first_name, users.last_name, user_logins.login, role_name, users.email from ASSIGNING_ISSUES "
                + "inner join user_logins on ASSIGNING_ISSUES.LOGIN = user_logins.login "
                + "inner join users on user_logins.user_id=users.user_id "
                + "inner join user_roles on user_roles.ROLE_ID = user_logins.ROLE_ID "
                + "where issue_id=?";
        ResultSet rs = null;

        try {
            connection = dataSource.getConnection();
            statement = connection.prepareStatement(selectSQL);
            statement.setLong(1, issueId);
            rs = statement.executeQuery();

            while (rs.next()) {
                User user = new User();
                user.setFirstName(rs.getString("first_name"));
                user.setLastName(rs.getString("last_name"));
                user.setLogin(rs.getString("login"));
                user.setUserRole(EUserRole.valueOf(rs.getString("role_name")));
                user.setEmail(rs.getString("email"));
                technicians.add(user);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(DatabaseServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException ex) {
                    Logger.getLogger(DatabaseServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException ex) {
                    Logger.getLogger(DatabaseServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

        return technicians;
    }

    @Override
    public boolean deleteAssignedTechnicians(long issueId, String technicianLogin) {
        Connection connection = null;
        PreparedStatement statement = null;
        String selectSQL = "delete from ASSIGNING_ISSUES where login=? and issue_id=?";
        ResultSet rs = null;
        boolean success = true;

        try {
            connection = dataSource.getConnection();
            statement = connection.prepareStatement(selectSQL);
            statement.setString(1, technicianLogin);
            statement.setLong(2, issueId);
            rs = statement.executeQuery();
        } catch (SQLException ex) {
            success = false;
            Logger.getLogger(DatabaseServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            success = false;
            Logger.getLogger(DatabaseServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(DatabaseServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException ex) {
                    Logger.getLogger(DatabaseServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException ex) {
                    Logger.getLogger(DatabaseServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

        return success;
    }

    @Override
    public List<Issue> getAssignedIssues(String login) {
        ArrayList<Issue> issues = new ArrayList<>();
        Connection connection = null;
        PreparedStatement statement = null;
        String selectSQL = "select * from assigning_issues inner join issues on assigning_issues.ISSUE_ID = issues.ISSUE_ID where assigning_issues.login=?";

        ResultSet rs = null;

        try {
            connection = dataSource.getConnection();
            statement = connection.prepareStatement(selectSQL);
            statement.setString(1, login);
            rs = statement.executeQuery();

            while (rs.next()) {
                Issue issue = new Issue();
                String message = rs.getString("message");
                String subject = rs.getString("subject");

                issue.setId(rs.getLong("issue_id"));
                issue.setEstimatedDate(rs.getTimestamp("estimated_time"));
                issue.setCreatedDate(rs.getTimestamp("created_on"));
                issue.setLastUpdatedDate(rs.getTimestamp("last_update"));
                issue.setMessage(message != null ? message : "");
                issue.setSubject(subject != null ? subject : "");
                issue.setPriority(EIssuePriority.getPriorityFromString(rs.getString("CODE_PRIORITY")));
                issue.setStatus(EIssueStatus.valueOf(rs.getString("code_status")));
                issue.setProgress(rs.getInt("progress"));

                issues.add(issue);
            }

        } catch (SQLException ex) {
            Logger.getLogger(DatabaseServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(DatabaseServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(DatabaseServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException ex) {
                    Logger.getLogger(DatabaseServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException ex) {
                    Logger.getLogger(DatabaseServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return issues;
    }

    @Override
    public String testSelect() {
        Connection connection = null;
        PreparedStatement statement = null;
        String selectSQL = "SELECT * FROM users";
        ResultSet rs = null;

        String test = "";

        try {
            connection = dataSource.getConnection();
            statement = connection.prepareStatement(selectSQL);

            rs = statement.executeQuery();

            if (rs.next()) {
                test += rs.getString("first_name") + " " + rs.getString("last_name");
            }
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(DatabaseServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(DatabaseServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException ex) {
                    Logger.getLogger(DatabaseServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException ex) {
                    Logger.getLogger(DatabaseServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

        return test;
    }

    /**
     * Vrati issue id podla subject - subject je v db unikatna hodnota
     *
     * @param subject
     * @return -1 ak nenajde
     */
    private long getIssueId(String subject) {
        Connection connection = null;
        PreparedStatement statement = null;
        String selectSQL = "SELECT * from ISSUES where subject=?";
        ResultSet rs = null;
        long id = -1;

        try {
            connection = dataSource.getConnection();
            statement = connection.prepareStatement(selectSQL);
            statement.setString(1, subject);
            rs = statement.executeQuery();

            if (rs.next()) {
                id = rs.getLong("issue_id");
            }
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(DatabaseServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException ex) {
                    Logger.getLogger(DatabaseServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException ex) {
                    Logger.getLogger(DatabaseServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

        return id;
    }
}
