package com.table.schedule.DBeditor;

/**
 * user, created schedule
 */
public class User {
    private final int ID;
    private final String Login;
    private final String Password;
    private final boolean Availability;

    public User(int id,String login,String password,boolean availability) {
        ID = id;
        Login= login;
        Password=password;
        Availability = availability;
    }
    public int getId()
    {
        return ID;
    }
    public String getLogin()
    {
        return Login;
    }
    public String getPassword()
    {
        return Password;
    }
    public boolean getAvailability()
    {
        return Availability;
    }
}
