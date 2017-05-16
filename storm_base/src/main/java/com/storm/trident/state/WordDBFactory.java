package com.storm.trident.state;

import org.apache.storm.task.IMetricsContext;
import org.apache.storm.trident.state.State;
import org.apache.storm.trident.state.StateFactory;

import java.util.Map;
import java.util.UUID;

/**
 * Created by xuaihua on 2017/5/16.
 */
public class WordDBFactory implements StateFactory {
    String _id;

    public WordDBFactory() {
        _id = UUID.randomUUID().toString();
    }

    @Override
    public State makeState(Map conf, IMetricsContext metrics, int partitionIndex, int numPartitions) {
        System.err.println("_____________________makeState____________________" + partitionIndex);
        return new WordDB();
    }
}
