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
import snech.core.types.Notice;
import snech.core.types.User;
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
        User user = new User();
        user.setId("jack");
        user.setPassword("1234");

        if (user.getId().equals(id) && user.getPassword().equals(password)) {
            return user;
        } else {
            return null;
        }
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
        ArrayList<Notice> notices = new ArrayList<Notice>();
        Notice notice1 = new Notice();
        notice1.setAuthor("Janko Mrkvicka");
        notice1.setContent("Class aptent taciti sociosqu ad litora torquent per conubia nostra, per inceptos himenaeos. Phasellus tempor elementum enim. Cum sociis natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Nulla bibendum tincidunt sapien");
        notice1.setFormatedDate("15. 02. 2015, 17:30");
        notice1.setHeading("Class aptent taciti");

        Notice notice2 = new Notice();
        notice2.setAuthor("Hlavny spravca");
        notice2.setContent("Class aptent taciti sociosqu ad litora torquent per conubia nostra, per inceptos himenaeos. Phasellus tempor elementum enim. Cum sociis natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Nulla bibendum tincidunt sapien");
        notice2.setFormatedDate("07. 03. 2015, 17:30");
        notice2.setHeading("Per inceptos himenaeos");

        notices.add(notice1);
        notices.add(notice2);
        return notices;
    }

    @Override
    public List<Issue> getIssues(String userId) {
        ArrayList<Issue> issues = new ArrayList<>();
//        Issue issue1 = new Issue();
//        issue1.setAssignedAdminId("admin1");
//        issue1.setEstimatedDate(new Timestamp(1425767181));
//        issue1.setId(100);
//        issue1.setLastUpdatedDate(new Timestamp(1425767200));
//        issue1.setPriority(EIssuePriority.A);
//        issue1.setStatus(EIssueStatus.NOVA);
//        issue1.setSubject("Chyba vo vypise");
//
//        Issue issue2 = new Issue();
//        issue2.setAssignedAdminId("Janko Hrasko");
//        issue2.setEstimatedDate(new Timestamp(1425767381));
//        issue2.setId(101);
//        issue2.setLastUpdatedDate(new Timestamp(1425767300));
//        issue2.setPriority(EIssuePriority.B);
//        issue2.setStatus(EIssueStatus.PREBIEHA);
//        issue2.setSubject("Runtime exception na obrazovke");
//
//        issues.add(issue1);
//        issues.add(issue2);

        return issues;
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
            //statement.setLong(1, issueId);
            statement.setInt(1, 1);
            rs = statement.executeQuery();
            
            if (rs.next()) {
                Timestamp estimatedDate = rs.getTimestamp("estimated_time");
                Timestamp createdDate = rs.getTimestamp("created_on");
                Timestamp lastUpdateDate = rs.getTimestamp("last_update");
                String message = rs.getString("message");
                String subject = rs.getString("subject");
                long adminId = rs.getLong("admin_login_id");
                
                issue.setId(issueId);
                issue.setAssignedAdminId(adminId);
                issue.setEstimatedDate(new Timestamp(estimatedDate != null ? estimatedDate.getTime() : 1));
                issue.setLastUpdatedDate(new Timestamp(createdDate != null ? createdDate.getTime() : 1));
                issue.setCreatedDate(new Timestamp(lastUpdateDate != null ? lastUpdateDate.getTime() : 1));
                issue.setMessage(message != null ? message : "");
                issue.setSubject(subject != null ? subject : "");

                // TODO metoda co vrati dany enum podla stringu z DB
                issue.setPriority(EIssuePriority.A);
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
        return issue;
    }

    @Override
    public String getAdminFullName(String adminId) {
        // TODO Pouzit FormatUtils na formatovanie mena a priezviska - zac. pismena velkym!!
        if (adminId != null) {
            if (adminId.equals("100")) {
                return "Janko Hrasko";
            } else if (adminId.equals("101")) {
                return "Jack Langdon";
            }
        }

        return "Nezname Meno";
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

}
