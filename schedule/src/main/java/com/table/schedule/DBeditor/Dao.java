package com.table.schedule.DBeditor;

import javafx.collections.ObservableList;

public interface Dao {
    void getDBConnection();
    void getUsers(ObservableList<User> elementsDAO);
    int getUser(String Login,String Password);
    User getUser(int id);
    int addUser(User user);
    void getEvents(ObservableList<ScheduleEvent> elementsDAO);
    void getEventsForUser(ObservableList<ScheduleEvent> elementsDAO,int UserID);
    int addEvent(ScheduleEvent element);
    boolean editEvent(ScheduleEvent element);
    boolean deleteEvent(int ID);
}
