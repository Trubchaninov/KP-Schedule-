package com.table.schedule.DBeditor;

import de.saxsys.javafx.test.JfxRunner;
import javafx.embed.swing.JFXPanel;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;

import java.time.LocalDateTime;

@RunWith(JfxRunner.class)
class EventsListTest {


    @Test
    void getAllElements() {
        JFXPanel fxPanel = new JFXPanel();
        DBConnection.getDBConnection();
        EventsList eventsList= new EventsList(1);
        eventsList.events.clear();
        eventsList.addElement(LocalDateTime.of(24,4,8,0,0),
                LocalDateTime.of(24,4,8,0,0),"k",1);
        eventsList.addElement(LocalDateTime.of(24,4,8,0,0),
                LocalDateTime.of(24,4,8,0,0),"k",1);
        Assert.assertEquals(eventsList.getAllElements().size(), 2);
    }

    @Test
    void testAddElement() {
        JFXPanel fxPanel = new JFXPanel();

        DBConnection.getDBConnection();
        EventsList eventsList= new EventsList(1);
        eventsList.events.clear();
        eventsList.addElement(LocalDateTime.of(24,4,8,0,0),
                LocalDateTime.of(24,4,8,0,0),"k",1);
        Assert.assertEquals(eventsList.getAllElements().size(), 1);
    }

    @Test
    void updateElement() {
        JFXPanel fxPanel = new JFXPanel();
        DBConnection.getDBConnection();
        EventsList eventsList= new EventsList(0);
        eventsList.addElement(LocalDateTime.of(24,4,8,0,0),
                LocalDateTime.of(24,4,8,0,0),"k",0);
        eventsList.updateElement(new ScheduleEvent.ScheduleEventBuilder(0,
                LocalDateTime.of(24,4,8,8,9),0).setTitle("d").build());
        Assert.assertEquals(eventsList.getAllElements().get(0).getTitle(), "k");
    }

    @Test
    void deleteElement() {
        JFXPanel fxPanel = new JFXPanel();
        DBConnection.getDBConnection();
        EventsList eventsList= new EventsList(0);
        eventsList.addElement(LocalDateTime.of(24,4,8,0,0),
                LocalDateTime.of(24,4,8,0,0),"k",8);
        eventsList.deleteElement(eventsList.getAllElements().get(0).getID());
        Assert.assertEquals(eventsList.getAllElements().size(), 0);

    }
}