package com.table.schedule.DBeditor;

import javafx.collections.ObservableList;

import java.sql.*;
import java.time.LocalDateTime;

/**
 * Class for work with DataBase
 */
public class DBConnection {
    public static final String DB_URL = "jdbc:h2:/db/SchedulesDB";
    public static final String DB_Driver = "org.h2.Driver";
    public static final String TableSchedules="Schedules";
    public static final String TableUser="Users";

    /**
     * get connection
     */
    public static void getDBConnection() {
        try {
            Class.forName(DB_Driver);
            Connection connection = DriverManager.getConnection(DB_URL);
            DatabaseMetaData md = connection.getMetaData();
            ResultSet rs = md.getTables(null, null, TableSchedules, null);
            //deleteTable(TableSchedules);
            //deleteTable(TableUser);
            if (!rs.next()) {
                createTableSchedule();
                createTableUser();
            }
            connection.close();

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            System.out.println("JDBC драйвер для СУБД не найден!");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Ошибка SQL!");
        }
    }

    /**
     * @param Table deletable
     */
    private static void deleteTable(String Table)
    {
        String deleteTableSQL = "DROP TABLE "+Table;
        try (Connection dbConnection = DriverManager.getConnection(DB_URL)) {
            assert dbConnection != null;
            try (Statement statement = dbConnection.createStatement()) {
                statement.execute(deleteTableSQL);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * create new table
     */
    private static void createTableSchedule() {
        String createTableSQL = "CREATE TABLE "+ TableSchedules+ " ("
                + "ID INT NOT NULL auto_increment, "
                + "Start DATETIME NOT NULL, "
                + "Stop DATETIME NULL, "
                + "Title varchar(20) NULL, "
                + "UserID int NOT NULL, "
                + "PRIMARY KEY (ID), "
                + "FOREIGN KEY (UserID) references Users(ID) "
                + ")";
        try (Connection dbConnection = DriverManager.getConnection(DB_URL)) {
            assert dbConnection != null;
            try (Statement statement = dbConnection.createStatement()) {
                statement.execute(createTableSQL);
            }
        } catch (SQLException ignored) {
        }
    }

    /**
     * create new table
     */
    private static void createTableUser() {
        String createTableSQL = "CREATE TABLE "+ TableUser+ " ("
                + "ID INT NOT NULL auto_increment, "
                + "Login varchar(20) NOT NULL UNIQUE, "
                + "Password varchar(20) NOT NULL, "
                + "Availability bit NULL, "
                + "PRIMARY KEY (ID) "
                + ")";
        try (Connection dbConnection = DriverManager.getConnection(DB_URL)) {
            assert dbConnection != null;
            try (Statement statement = dbConnection.createStatement()) {
                statement.execute(createTableSQL);
            }
        } catch (SQLException ignored) {
        }
    }

    /**
     * @param elementsDAO list of users
     */
    public static void getUsers(ObservableList<User> elementsDAO) {
        String selection = "select * from "+ TableUser +" where Availability = False";
        try (Connection dbConnection = DriverManager.getConnection(DB_URL)) {
            assert dbConnection != null;
            try (Statement statement = dbConnection.createStatement()) {
                ResultSet rs = statement.executeQuery(selection);
                while (rs.next()) {
                    User user = new User(rs.getInt("ID"),
                            rs.getString("Login"),
                            rs.getString("Password"),
                            rs.getBoolean("Availability"));
                    elementsDAO.add(user);
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * @param Login of user
     * @param Password of user
     * @return id
     */
    public static int getUser(String Login,String Password) {
        String selection = "select * from "+ TableUser+" where Login='"+Login+"' AND Password='"+Password+"'";
        try (Connection dbConnection = DriverManager.getConnection(DB_URL)) {
            assert dbConnection != null;
            try (Statement statement = dbConnection.createStatement()) {
                ResultSet rs = statement.executeQuery(selection);
                if (rs.next()) {
                    return rs.getInt("ID");
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return -1;
    }

    /**
     * @param id of user
     * @return user
     */
    public static User getUser(int id) {
        String selection = "select * from "+ TableUser+" where ID="+id;
        try (Connection dbConnection = DriverManager.getConnection(DB_URL)) {
            assert dbConnection != null;
            try (Statement statement = dbConnection.createStatement()) {
                ResultSet rs = statement.executeQuery(selection);
                if (rs.next()) {
                    return new User(rs.getInt("ID"),
                            rs.getString("Login"),
                            rs.getString("Password"),
                            rs.getBoolean("Availability"));
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    /**
     * @param elementsDAO list of events
     */
    public static void getEvents(ObservableList<ScheduleEvent> elementsDAO) {
        String selection = "select * from "+ TableSchedules;
        try (Connection dbConnection = DriverManager.getConnection(DB_URL)) {
            assert dbConnection != null;
            try (Statement statement = dbConnection.createStatement()) {
                ResultSet rs = statement.executeQuery(selection);
                while (rs.next()) {
                    ScheduleEvent element;
                    if(rs.getDate("Stop")!=null)
                    element = new ScheduleEvent.ScheduleEventBuilder(rs.getInt("ID"),
                            LocalDateTime.of(rs.getDate("Start").toLocalDate(),rs.getTime("Start").toLocalTime()),
                            rs.getInt("UserID")).
                            setStop(LocalDateTime.of(rs.getDate("Stop").toLocalDate(), rs.getTime("Stop").toLocalTime())).
                            setTitle(rs.getString("Title")).build();
                    else element = new ScheduleEvent.ScheduleEventBuilder(rs.getInt("ID"),
                            LocalDateTime.of(rs.getDate("Start").toLocalDate(),rs.getTime("Start").toLocalTime()),
                            rs.getInt("UserID")).
                            setTitle(rs.getString("Title")).build();
                    elementsDAO.add(element);
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /** list of events for selected user
     * @param elementsDAO list of events
     * @param UserID id
     */
    public static void getEventsForUser(ObservableList<ScheduleEvent> elementsDAO,int UserID) {
        String selection = "select * from "+ TableSchedules + " where UserID=" + UserID +" order by Start";
        try (Connection dbConnection = DriverManager.getConnection(DB_URL)) {
            assert dbConnection != null;
            try (Statement statement = dbConnection.createStatement()) {
                ResultSet rs = statement.executeQuery(selection);
                while (rs.next()) {
                    ScheduleEvent element;
                    if(rs.getDate("Stop")!=null)
                        element = new ScheduleEvent.ScheduleEventBuilder(rs.getInt("ID"),
                                LocalDateTime.of(rs.getDate("Start").toLocalDate(),rs.getTime("Start").toLocalTime()),
                                rs.getInt("UserID")).
                                setStop(LocalDateTime.of(rs.getDate("Stop").toLocalDate(), rs.getTime("Stop").toLocalTime())).
                                setTitle(rs.getString("Title")).build();
                    else element = new ScheduleEvent.ScheduleEventBuilder(rs.getInt("ID"),
                            LocalDateTime.of(rs.getDate("Start").toLocalDate(),rs.getTime("Start").toLocalTime()),
                            rs.getInt("UserID")).
                            setTitle(rs.getString("Title")).build();
                    elementsDAO.add(element);
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /** create new user
     * @param user new
     * @return id of user
     */
    public static int addUser(User user) {
        String insertTableSQL = "INSERT INTO "+ TableUser
                + " (Login,Password,Availability) " + "VALUES "
                + "('"+user.getLogin()+"','"+user.getPassword()+"','"+user.getAvailability()+"')";
        try (Connection dbConnection = DriverManager.getConnection(DB_URL)) {
            assert dbConnection != null;
            try (Statement statement = dbConnection.createStatement()) {
                statement.executeUpdate(insertTableSQL);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return -1;
        }
        try (Connection dbConnection = DriverManager.getConnection(DB_URL)) {
            assert dbConnection != null;
            try(Statement statement = dbConnection.createStatement()) {
                ResultSet rs=statement.executeQuery("SELECT TOP 1 ID FROM "+TableUser+" ORDER BY ID DESC");
                rs.next();
                return rs.getInt("ID");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return -1;
    }

    /**
     * @param element addable
     * @return id
     */
    public static int addEvent(ScheduleEvent element) {
        String insertTableSQL;
        if(element.getStop()!=null)
        insertTableSQL = "INSERT INTO "+ TableSchedules
                + " (Start,Stop,Title,UserID) " + "VALUES "
                + "('"+element.getStart()+"','"+element.getStop()+"','"+element.getTitle()+"',"+element.getUserID()+")";
        else insertTableSQL = "INSERT INTO "+ TableSchedules
                + " (Start,Title,UserID) " + "VALUES "
                + "('"+element.getStart()+"','"+element.getTitle()+"',"+element.getUserID()+")";
        try (Connection dbConnection = DriverManager.getConnection(DB_URL)) {
            assert dbConnection != null;
            try (Statement statement = dbConnection.createStatement()) {
                statement.executeUpdate(insertTableSQL);
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        try (Connection dbConnection = DriverManager.getConnection(DB_URL)) {
            assert dbConnection != null;
            try(Statement statement = dbConnection.createStatement()) {
                ResultSet rs=statement.executeQuery("SELECT TOP 1 ID FROM "+TableSchedules+" ORDER BY ID DESC");
                rs.next();
                return rs.getInt("ID");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return -1;
    }

    /** edit event
     * @param element editable
     * @return result
     */
    public static boolean editEvent(ScheduleEvent element) {
        String updateTableSQL = String.format("UPDATE "+ TableSchedules
                        + " SET Start='%s',Stop='%s',Title='%s' where ID=%s;",
                element.getStart(),element.getStop(),element.getTitle(),element.getID());
        try (Connection dbConnection = DriverManager.getConnection(DB_URL)) {
            assert dbConnection != null;
            try (Statement statement = dbConnection.createStatement()) {
                statement.executeUpdate(updateTableSQL);
                return true;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    /** delete event
     * @param ID of event
     * @return result
     */
    public static boolean deleteEvent(int ID) {
        String deleteTableSQL = String.format("DELETE from "+ TableSchedules +" WHERE ID=%s;",ID);
        try (Connection dbConnection = DriverManager.getConnection(DB_URL)) {
            assert dbConnection != null;
            try (Statement statement = dbConnection.createStatement()) {
                statement.executeUpdate(deleteTableSQL);
                return true;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }
}
