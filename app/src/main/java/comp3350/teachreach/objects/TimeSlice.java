package comp3350.teachreach.objects;

import org.threeten.bp.Duration;
import org.threeten.bp.LocalDateTime;

import comp3350.teachreach.objects.interfaces.ITimeSlice;

public class TimeSlice implements ITimeSlice {
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Duration duration;

    public TimeSlice(LocalDateTime startTime, LocalDateTime endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.duration = Duration.between(startTime, endTime);
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public int getStartYear() {
        return startTime.getYear();
    }

    public int getStartMonth() {
        return startTime.getMonthValue();
    }

    public int getStartDay() {
        return startTime.getDayOfMonth();
    }

    public int getStartHour() {
        return startTime.getHour();
    }

    public int getStartMinute() {
        return startTime.getMinute();
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public int getEndYear() {
        return endTime.getYear();
    }

    public int getEndMonth() {
        return endTime.getMonthValue();
    }

    public int getEndDay() {
        return endTime.getDayOfMonth();
    }

    public int getEndHour() {
        return endTime.getHour();
    }

    public int getEndMinute() {
        return endTime.getMinute();
    }

    public Duration getDuration() {
        return duration;
    }

    public boolean conflictsWith(ITimeSlice that) {
        return that.getEndTime().isAfter(this.startTime) && that.getStartTime().isBefore(this.endTime);
    }

    public boolean canContain(ITimeSlice that) {
        return this.equals(that) ||
                (this.startTime.isBefore(that.getStartTime()) &&
                        this.endTime.isAfter(that.getEndTime())) ||
                (this.startTime.equals(that.getStartTime()) &&
                        this.endTime.isAfter(that.getEndTime())) ||
                (this.endTime.equals(that.getEndTime()) &&
                        this.startTime.isBefore(that.getStartTime()));
    }

    public TimeSlice mergeWith(ITimeSlice that) {
        if (this.startTime.isAfter(that.getStartTime())) {
            this.startTime = that.getStartTime();
        }
        if (this.endTime.isBefore(that.getEndTime())) {
            this.endTime = that.getEndTime();
        }
        this.duration = Duration.between(this.startTime, this.endTime);
        return this;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof TimeSlice that) {
            return this.startTime.equals(that.startTime) && this.endTime.equals(that.endTime);
        }
        return false;
    }
}