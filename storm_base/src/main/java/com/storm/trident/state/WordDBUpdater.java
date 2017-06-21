package com.storm.trident.state;

import org.apache.storm.trident.operation.TridentCollector;
import org.apache.storm.trident.state.BaseStateUpdater;
import org.apache.storm.trident.tuple.TridentTuple;

import java.util.List;

/**
 * Created by xuaihua on 2017/5/16.
 */
public class WordDBUpdater  extends BaseStateUpdater<WordDB> {
    @Override
    public void updateState(WordDB state, List<TridentTuple> tuples, TridentCollector collector) {
        for(TridentTuple tuple : tuples)
        {
            state.add(tuple.getStringByField("word"),tuple.getLongByField("count"));
        }
    }
}
