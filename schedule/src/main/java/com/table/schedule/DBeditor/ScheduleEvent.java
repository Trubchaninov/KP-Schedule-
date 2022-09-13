package com.table.schedule.DBeditor;

import java.time.LocalDateTime;

/**
 * event in schedule
 */
public class ScheduleEvent {
    private final int ID;
    private LocalDateTime Start;
    private LocalDateTime Stop;
    private String Title;
    private final int UserID;
    public ScheduleEvent(ScheduleEventBuilder scheduleEventBuilder) {
        ID = scheduleEventBuilder.id;
        Title =scheduleEventBuilder.title;
        Start = scheduleEventBuilder.start;
        Stop = scheduleEventBuilder.stop;
        UserID = scheduleEventBuilder.userID;
    }
   public int getID() {
        return ID;
    }

    public int getUserID() {
        return UserID;
    }

    public LocalDateTime getStart() {
        return Start;
    }

    public LocalDateTime getStop() {
        return Stop;
    }

    public String getTitle() {
        return Title;
    }

    public void setStart(LocalDateTime start)
    {
        Start=start;
    }
    public void setStop(LocalDateTime stop) {
        Stop = stop;
    }

    public void setTitle(String title) {
        Title = title;
    }
    public static class ScheduleEventBuilder {
        private final int id;
        private final LocalDateTime start;
        private LocalDateTime stop;//optional
        private final int userID;
        private String title;//optional

        public ScheduleEventBuilder(int id, LocalDateTime start,int userID) {
            this.id = id;
            this.start = start;
            this.userID = userID;
        }

        public ScheduleEventBuilder setStop(LocalDateTime stop) {
            this.stop = stop;
            return this;
        }

        public ScheduleEventBuilder setTitle(String title) {
            this.title = title;
            return this;
        }

        //Build the Employee object
        public ScheduleEvent build() {
            return new ScheduleEvent(this);
        }
    }
}
