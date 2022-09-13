package com.table.schedule.DBeditor;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.time.LocalDateTime;

/**
 * list of events
 */
public class EventsList {
    ObservableList<ScheduleEvent> events;
    public EventsList(int User)
    {
        events = FXCollections.observableArrayList();
        DBConnection.getDBConnection();
        DBConnection.getEventsForUser(events,User);
    }

    /**
     * @return list of events
     */
    public ObservableList<ScheduleEvent> getAllElements() {
        return events;
    }

    /**
     * @param start date
     * @param stop date
     * @param title of element
     * @param userID of user
     */
    public void addElement(LocalDateTime start,LocalDateTime stop,String title,int userID)
    {
        int id;
        ScheduleEvent temp=new ScheduleEvent.ScheduleEventBuilder(-1,start,userID).setStop(stop).setTitle(title).build();
        id=DBConnection.addEvent(temp);
        if(id!=-1){
            temp=new ScheduleEvent.ScheduleEventBuilder(id,start,userID).setStop(stop).setTitle(title).build();
            events.add(temp);
        }
    }

    /**
     * @param start datw
     * @param title date
     * @param userID of user
     */
    public void addElement(LocalDateTime start,String title,int userID)
    {
        int id;
        ScheduleEvent temp=new ScheduleEvent.ScheduleEventBuilder(-1,start,userID).setTitle(title).build();
        id=DBConnection.addEvent(temp);
        if(id!=-1){
            temp=new ScheduleEvent.ScheduleEventBuilder(id,start,userID).setTitle(title).build();
            events.add(temp);
        }
    }

    /**
     * @param NewElement editable
     */
    public void updateElement(ScheduleEvent NewElement) {
        if(DBConnection.editEvent(NewElement))
            for(ScheduleEvent el: events)
                if(el.getID()==NewElement.getID())
                    events.set(events.indexOf(el),NewElement);
    }

    /**
     * @param ID deletable
     */
    public void deleteElement(int ID) {
        if(DBConnection.deleteEvent(ID))
            events.removeIf(el -> el.getID() == ID);
    }
}
