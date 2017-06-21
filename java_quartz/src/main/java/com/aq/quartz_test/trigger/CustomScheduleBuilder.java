package com.aq.quartz_test.trigger;

import org.quartz.ScheduleBuilder;
import org.quartz.spi.MutableTrigger;

/**
 * Created by xuaihua on 2017/5/3.
 */
public class CustomScheduleBuilder extends ScheduleBuilder {
    protected MutableTrigger build() {
        return new CustomTrigger();
    }
}
