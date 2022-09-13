package com.table.schedule;

import com.table.schedule.DBeditor.EventsList;
import com.table.schedule.DBeditor.ScheduleEvent;
import com.table.schedule.DBeditor.User;
import com.table.schedule.DBeditor.UsersList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import tornadofx.control.DateTimePicker;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Objects;

public class MainController {
    @FXML
    public ListView<User> schedulesList;
    public VBox creator;
    public Accordion listEvents;
    public DateTimePicker stop;
    public TextField titleEvent;
    public DateTimePicker start;
    public BorderPane parent;
    EventsList eventsList;
    UsersList usersList;
    int UserID;
    User currentSchedule;

    /** init a elements
     * @param user entered
     */
    public void init(int user) {
        UserID = user;
        eventsList = new EventsList(UserID);
        usersList = new UsersList();
        currentSchedule = usersList.getUser(UserID);
        schedulesList.setItems(usersList.getUsers());
        schedulesList.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(User item, boolean empty) {
                String current="",your="";
                super.updateItem(item, empty);
                if (empty || item.getLogin() == null) {
                    setText(null);
                }
                else {
                    if(item==currentSchedule || item.getId()==currentSchedule.getId()) current="(текущая)";
                    if(item.getId()==UserID) your="(ваша)";
                    setText(item.getLogin()+current+your);
                }
            }
        });
        parent.getChildren().remove(creator);
        initEvents(true);
    }

    /**
     * select a schedule
     */
    @FXML
    private void selectSchedule()
    {
        currentSchedule=schedulesList.getSelectionModel().getSelectedItem();
        boolean owner = currentSchedule.getId() == UserID;
        eventsList = new EventsList(currentSchedule.getId());
        initEvents(owner);
        schedulesList.refresh();
    }

    /**
     * new event
     */
    @FXML
    private void createEvent()
    {
        start.setDateTimeValue(LocalDateTime.parse(start.getEditor().getCharacters(),DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
        if(!Objects.equals(stop.getEditor().getText(), "")) {
            stop.setDateTimeValue(LocalDateTime.parse(stop.getEditor().getCharacters(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
            eventsList.addElement(start.dateTimeValueProperty().get(), stop.dateTimeValueProperty().get(), titleEvent.getText(), UserID);
        }
        else eventsList.addElement(start.dateTimeValueProperty().get(),titleEvent.getText(),UserID);
        initEvents(true);
    }

    /**
     * @param owner user created
     */
    private void initEvents(boolean owner)
    {
        if(owner)
            parent.setLeft(creator);
        else parent.setLeft(null);
        listEvents.getPanes().clear();
        for (ScheduleEvent eventSchedule: eventsList.getAllElements()) {
            TitledPane pane=new TitledPane();
            pane.textProperty().setValue(eventSchedule.getStart().format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT)));
            VBox content = new VBox();
            createContent(pane,content,eventSchedule,owner);
            pane.setContent(content);
            listEvents.getPanes().add(pane);
        }
    }

    /**
     * @param pane created
     * @param content with elements
     * @param scheduleEvent created
     * @param owner created
     */
    private void createContent(TitledPane pane,VBox content,ScheduleEvent scheduleEvent,boolean owner)
    {
        content.getChildren().add(new Label("Название"));
        TextField title = new TextField();
        title.textProperty().setValue(scheduleEvent.getTitle());
        title.textProperty().addListener((observable, oldValue, newValue) -> {
            scheduleEvent.setTitle(newValue);
            eventsList.updateElement(scheduleEvent);
        });
        title.setEditable(owner);
        content.getChildren().add(title);
        content.getChildren().add(new Label("Начало"));
        DateTimePicker start = new DateTimePicker();
        start.dateTimeValueProperty().setValue(scheduleEvent.getStart());
        start.focusedProperty().addListener(e->{
            scheduleEvent.setStart(LocalDateTime.parse(start.getEditor().getCharacters(),DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
            eventsList.updateElement(scheduleEvent);
            pane.setText(LocalDateTime.parse(start.getEditor().getCharacters(),DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")).
                    format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT)));
        });
        start.setDisable(!owner);
        content.getChildren().add(start);
        start.setStyle("-fx-opacity: 1.0");
        content.getChildren().add(new Label("Окончание"));
        DateTimePicker stop = new DateTimePicker();
        stop.dateTimeValueProperty().setValue(scheduleEvent.getStop());
        stop.focusedProperty().addListener(e->{
            if(!Objects.equals(stop.getEditor().getText(), "")) {
                scheduleEvent.setStop(LocalDateTime.parse(stop.getEditor().getText(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
                eventsList.updateElement(scheduleEvent);
            }
        });
        stop.setDisable(!owner);
        content.getChildren().add(stop);
        stop.setStyle("-fx-opacity: 1.0");
        Button delete = new Button("Удалить");
        delete.setVisible(owner);
        delete.setOnAction(e->{
            eventsList.deleteElement(scheduleEvent.getID());
            listEvents.getPanes().remove(pane);
        });
        content.getChildren().add(delete);
    }
}
