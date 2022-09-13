package com.table.schedule.DBeditor;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * list of users
 */
public class UsersList {
    ObservableList<User> users;

    public UsersList()
    {
        users = FXCollections.observableArrayList();
        DBConnection.getDBConnection();
        DBConnection.getUsers(users);
    }

    /**
     * @return all users
     */
    public ObservableList<User> getUsers() {
        return users;
    }

    /**
     * @param id of user
     * @return selected user
     */
    public User getUser(int id)
    {
        return DBConnection.getUser(id);
    }

}
