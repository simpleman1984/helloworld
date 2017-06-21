package com.storm.trident.state;

import org.apache.storm.trident.operation.TridentCollector;
import org.apache.storm.trident.state.BaseQueryFunction;
import org.apache.storm.trident.tuple.TridentTuple;
import org.apache.storm.tuple.Values;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xuaihua on 2017/5/16.
 */
public class QueryWordFunction extends BaseQueryFunction<WordDB,Long>{
    @Override
    public List<Long> batchRetrieve(WordDB state, List<TridentTuple> inputs) {
        System.err.println("batchRetrieve_______________________" + inputs);
        List<Long> ret = new ArrayList();
        for(TridentTuple input: inputs) {
            ret.add(state.get(input.getString(0)));
        }
        return ret;
    }

    @Override
    public void execute(TridentTuple tuple, Long result, TridentCollector collector) {
        System.err.println("execute________" + result);
        collector.emit(new Values(result));
    }
}
