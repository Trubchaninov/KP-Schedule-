package com.table.schedule.DBeditor;

import de.saxsys.javafx.test.JfxRunner;
import javafx.embed.swing.JFXPanel;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;

@RunWith(JfxRunner.class)
class UsersListTest {

    @Test
    void getUsers() {
        JFXPanel fxPanel = new JFXPanel();
        UsersList usersList=new UsersList();
        DBConnection.addUser(new User(1,"d","d",false));
        Assert.assertEquals(usersList.getUsers().size(),3);
    }

    @Test
    void getUser() {
        JFXPanel fxPanel = new JFXPanel();
        UsersList usersList=new UsersList();
        DBConnection.addUser(new User(1,"d","d",false));
        Assert.assertEquals(usersList.getUser(1).getLogin(),"a");
    }
}