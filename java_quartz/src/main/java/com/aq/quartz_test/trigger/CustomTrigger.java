package com.aq.quartz_test.trigger;

import org.quartz.Calendar;
import org.quartz.ScheduleBuilder;
import org.quartz.impl.triggers.AbstractTrigger;

import java.util.Date;

/**
 * Created by xuaihua on 2017/5/3.
 */
public class CustomTrigger extends AbstractTrigger{
    public void triggered(Calendar calendar) {
    }

    public Date computeFirstFireTime(Calendar calendar) {
        return new Date();
    }

    public boolean mayFireAgain() {
        return true;
    }

    public Date getStartTime() {
        return null;
    }

    public void setStartTime(Date date) {

    }

    public void setEndTime(Date date) {

    }

    public Date getEndTime() {
        return null;
    }

    public Date getNextFireTime() {
        return new Date();
    }

    public Date getPreviousFireTime() {
        return null;
    }

    public Date getFireTimeAfter(Date date) {
        return null;
    }

    public Date getFinalFireTime() {
        return null;
    }

    protected boolean validateMisfireInstruction(int i) {
        return false;
    }

    public void updateAfterMisfire(Calendar calendar) {

    }

    public void updateWithNewCalendar(Calendar calendar, long l) {

    }

    public void setNextFireTime(Date date) {

    }

    public void setPreviousFireTime(Date date) {

    }

    public ScheduleBuilder getScheduleBuilder() {
        return null;
    }
}
