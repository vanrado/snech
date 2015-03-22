package snech.core.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import org.springframework.stereotype.Service;
import snech.core.types.Issue;
import snech.core.types.IssueLog;
import snech.core.types.Notice;
import snech.core.types.User;
import snech.core.types.enums.EIssueLogType;
import snech.core.types.enums.EIssuePriority;
import snech.core.types.enums.EIssueStatus;

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
    public User getClient(String id, String password) {
        User user = null;
        Connection connection = null;
        PreparedStatement statement = null;
        String selectSQL = "SELECT * FROM user_logins inner join users on user_logins.user_id = users.user_id where login=? and password=?";
        ResultSet rs = null;

        try {
            connection = dataSource.getConnection();
            statement = connection.prepareStatement(selectSQL);
            statement.setString(1, id);
            statement.setString(2, password);
            rs = statement.executeQuery();

            if (rs.next()) {
                user = new User();

                long userId = rs.getLong("login_id");
                String userLogin = rs.getString("login");
                String userFirstName = rs.getString("first_name");
                String userLastName = rs.getString("last_name");
                String userEmail = rs.getString("email");
                String userOccupation = rs.getString("occupation");

                user.setId(userId);
                user.setLogin(userLogin);
                user.setFirstName(userFirstName);
                user.setLastName(userLastName);
                user.setEmail(userEmail);
                user.setOccupation(userOccupation);
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    System.out.println(ex.getMessage());
                }
            }
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException ex) {
                    System.out.println(ex.getMessage());
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException ex) {
                    System.out.println(ex.getMessage());
                }
            }
        }

        return user;
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
            System.out.println(ex.getMessage());
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    System.out.println(ex.getMessage());
                }
            }
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException ex) {
                    System.out.println(ex.getMessage());
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException ex) {
                    System.out.println(ex.getMessage());
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
        ResultSet rs = null;

        try {
            connection = dataSource.getConnection();
            statement = connection.prepareStatement(selectSQL);
            statement.setString(1, userId);
            rs = statement.executeQuery();

            while (rs.next()) {
                Issue issue = new Issue();
                String message = rs.getString("message");
                String subject = rs.getString("subject");

                issue.setId(rs.getLong("issue_id"));
                issue.setAssignedAdminId(rs.getLong("admin_login"));
                issue.setEstimatedDate(rs.getTimestamp("estimated_time"));
                issue.setCreatedDate(rs.getTimestamp("created_on"));
                issue.setLastUpdatedDate(rs.getTimestamp("last_update"));
                issue.setMessage(message != null ? message : "");
                issue.setSubject(subject != null ? subject : "");
                issue.setPriority(EIssuePriority.getPriorityFromString(rs.getString("CODE_PRIORITY")));
                issue.setStatus(EIssueStatus.NOVA);

                issues.add(issue);
            }

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    System.out.println(ex.getMessage());
                }
            }
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException ex) {
                    System.out.println(ex.getMessage());
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException ex) {
                    System.out.println(ex.getMessage());
                }
            }
        }
        return issues;
    }

    @Override
    public long insertIssue(Issue issue) {
        Connection connection = null;
        PreparedStatement statement = null;
        String selectSQL = "insert into ISSUES (ISSUE_ID,USER_LOGIN,SUBJECT,CODE_PRIORITY,CODE_STATUS,ESTIMATED_TIME,CREATED_ON,LAST_UPDATE,MESSAGE,ADMIN_LOGIN) values (issue_id_seq.nextval,'" + issue.getUserLogin() + "','" + issue.getSubject() + "','" + issue.getPriority().name() + "','NOVA', null, CURRENT_TIMESTAMP, null,'" + issue.getMessage() + "', null)";
        ResultSet rs = null;
        boolean success = true;
        long issueId = -1;
        
        try {
            connection = dataSource.getConnection();
            statement = connection.prepareStatement(selectSQL);
            rs = statement.executeQuery();
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            success = false;
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    System.out.println(ex.getMessage());
                }
            }
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException ex) {
                    System.out.println(ex.getMessage());
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException ex) {
                    System.out.println(ex.getMessage());
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
                issue.setAssignedAdminId(rs.getLong("admin_login"));
                issue.setEstimatedDate(rs.getTimestamp("estimated_time"));
                issue.setCreatedDate(rs.getTimestamp("created_on"));
                issue.setLastUpdatedDate(rs.getTimestamp("last_update"));
                issue.setMessage(message != null ? message : "");
                issue.setSubject(subject != null ? subject : "");
                issue.setPriority(EIssuePriority.valueOf(rs.getString("CODE_PRIORITY")));
                issue.setStatus(EIssueStatus.NOVA);
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    System.out.println(ex.getMessage());
                }
            }
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException ex) {
                    System.out.println(ex.getMessage());
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException ex) {
                    System.out.println(ex.getMessage());
                }
            }
        }
        System.out.println(issue.toString());
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
            System.out.println(ex.getMessage());
            success = false;
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    System.out.println(ex.getMessage());
                }
            }
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException ex) {
                    System.out.println(ex.getMessage());
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException ex) {
                    System.out.println(ex.getMessage());
                }
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
            System.out.println(ex.getMessage());
            success = false;
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    System.out.println(ex.getMessage());
                }
            }
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException ex) {
                    System.out.println(ex.getMessage());
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException ex) {
                    System.out.println(ex.getMessage());
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
            System.out.println(ex.getMessage());
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    System.out.println(ex.getMessage());
                }
            }
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException ex) {
                    System.out.println(ex.getMessage());
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException ex) {
                    System.out.println(ex.getMessage());
                }
            }
        }

        return user;
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
            System.out.println(ex.getMessage());
            success = false;
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    System.out.println(ex.getMessage());
                }
            }
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException ex) {
                    System.out.println(ex.getMessage());
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException ex) {
                    System.out.println(ex.getMessage());
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
            System.out.println(ex.getMessage());
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    System.out.println(ex.getMessage());
                }
            }
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException ex) {
                    System.out.println(ex.getMessage());
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException ex) {
                    System.out.println(ex.getMessage());
                }
            }
        }

        return issueLogs;
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
            System.out.println(ex.getMessage());
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    System.out.println(ex.getMessage());
                }
            }
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException ex) {
                    System.out.println(ex.getMessage());
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException ex) {
                    System.out.println(ex.getMessage());
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
            System.out.println(ex.getMessage());
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    System.out.println(ex.getMessage());
                }
            }
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException ex) {
                    System.out.println(ex.getMessage());
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException ex) {
                    System.out.println(ex.getMessage());
                }
            }
        }

        return id;
    }
}
