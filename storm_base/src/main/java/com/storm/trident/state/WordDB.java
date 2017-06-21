package com.storm.trident.state;


import com.google.common.util.concurrent.AtomicLongMap;
import org.apache.storm.trident.state.CombinerValueUpdater;
import org.apache.storm.trident.state.State;
import org.apache.storm.trident.state.ValueUpdater;
import org.apache.storm.trident.state.map.MapState;
import org.apache.storm.trident.tuple.TridentTupleView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by xuaihua on 2017/5/16.
 */
public class WordDB implements State,MapState {

    Map<String,Long> countMap = new HashMap<String,Long>();

    @Override
    public void beginCommit(Long txid) {
        System.err.println("_____________________________________beginCommit");
    }

    @Override
    public void commit(Long txid) {
        System.err.println("_____________________________________commit");
    }

    public void add(String word,long count)
    {
        countMap.put(word,count);
    }

    public long get(String word)
    {
        Long count =  countMap.get(word);
        if(count != null)
        {
            return count;
        }
        else
        {
            return 0;
        }
    }

    @Override
    public List multiUpdate(List keys, List list) {
        int i=0;
        List results= new ArrayList();
        for(Object object : keys)
        {
            List keyList = (List)object;
            ValueUpdater valueUpdater = (ValueUpdater) list.get(i);
//            Object newVal = valueUpdater.update();
            results.add(1);
            i++;
        }
        return results;
    }

    @Override
    public void multiPut(List keys, List vals) {
        System.err.println(keys);
    }

    @Override
    public List multiGet(List keys) {
        return null;
    }
}
