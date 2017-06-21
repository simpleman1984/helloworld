package com.storm;

import com.storm.debug.DebugFilter;
import com.storm.trident.state.QueryWordFunction;
import com.storm.trident.state.WordDBFactory;
import com.storm.trident.state.WordDBUpdater;
import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.LocalDRPC;
import org.apache.storm.spout.SpoutOutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichSpout;
import org.apache.storm.trident.TridentState;
import org.apache.storm.trident.TridentTopology;
import org.apache.storm.trident.operation.BaseFunction;
import org.apache.storm.trident.operation.CombinerAggregator;
import org.apache.storm.trident.operation.TridentCollector;
import org.apache.storm.trident.operation.builtin.MapGet;
import org.apache.storm.trident.spout.IBatchSpout;
import org.apache.storm.trident.state.StateFactory;
import org.apache.storm.trident.testing.FixedBatchSpout;
import org.apache.storm.trident.testing.MemoryMapState;
import org.apache.storm.trident.tuple.TridentTuple;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Values;

import java.util.Map;

/**
 * Created by xuaihua on 2017/5/15.
 */
public class TridentTopologyTest {

    public static class Split extends BaseFunction {
        public void execute(TridentTuple tuple, TridentCollector collector) {
            String sentence = tuple.getString(0);
            for(String word: sentence.split(" ")) {
                collector.emit(new Values(word));
            }
        }
    }

    public static class WordRichSpout extends BaseRichSpout
    {

        private  SpoutOutputCollector collector;
        @Override
        public void open(Map map, TopologyContext topologyContext, SpoutOutputCollector spoutOutputCollector) {
            this.collector = spoutOutputCollector;
        }

        @Override
        public void nextTuple() {
            collector.emit(new Values("the"));
            System.err.println("++++++++++++++++ 开始查询+++++++++++++++++");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {
            outputFieldsDeclarer.declare(new Fields("args"));
        }

        @Override
        public void ack(Object msgId) {
            System.err.println("________________ACK");
        }

        @Override
        public void fail(Object msgId) {
            System.err.println("________________fail");
        }
    }

    public static class WordBatchRichSpout implements IBatchSpout{

        @Override
        public void open(Map conf, TopologyContext context) {

        }

        @Override
        public void emitBatch(long batchId, TridentCollector collector) {
            System.err.println("emitBatch________________" + batchId);
            collector.emit(new Values("the"));
        }

        @Override
        public void ack(long batchId) {
            System.err.println("ack________________" + batchId);
        }

        @Override
        public void close() {
        }

        @Override
        public Map<String, Object> getComponentConfiguration() {
            return null;
        }

        @Override
        public Fields getOutputFields() {
            return new Fields("args");
        }
    }

    public static void main(String [] args)
    {
        FixedBatchSpout spout = new FixedBatchSpout(new Fields("sentence"), 3,
                new Values("the cow jumped over the moon"),
                new Values("the man went to the store and bought some candy"),
                new Values("four score and seven years ago"),
                new Values("how many apples can you eat"));
        spout.setCycle(true);

        TridentTopology topology = new TridentTopology();

        //此处不停的进行统计，并保存到数据库
        StateFactory wordDBFactory = new WordDBFactory() ;//new MemoryMapState.Factory() WordDBFactory
        TridentState wordDBState = topology.newStream("txId",spout)
                .each(new Fields("sentence"), new BaseFunction() {
                    @Override
                    public void execute(TridentTuple tuple, TridentCollector collector) {
                        System.err.println("11111===========" + tuple);
                        String sentence = tuple.getString(0);
                        for(String word: sentence.split(" ")) {
                            collector.emit(new Values(word));
                        }
                    }
                }, new Fields("word"))
                .groupBy(new Fields("word"))
                .persistentAggregate(wordDBFactory,new Fields("word"),new CombinerAggregator<Long>(){

                    @Override
                    public Long init(TridentTuple tuple) {
                        System.err.println("init11111___" + tuple);
                        return 1L;
                    }

                    @Override
                    public Long combine(Long val1, Long val2) {
                        System.err.println("combine___" + val1+"_______" + val2);
                        return val1 + val2;
                    }

                    @Override
                    public Long zero() {
                        return 0L;
                    }
                },new Fields("count"))
//                .newValuesStream().partitionPersist(wordDBFactory,new Fields("word","count"),new WordDBUpdater()).parallelismHint(6)
                ;

//        TridentState wordDBState = topology.newStaticState(new WordDBFactory());

        WordRichSpout wordRichSpout = new WordRichSpout();
        WordBatchRichSpout batchRichSpout = new WordBatchRichSpout();

//        LocalDRPC drpc = new LocalDRPC();
//        topology.newDRPCStream("myspout", drpc)

        topology.newStream("myspout",wordRichSpout)
                .stateQuery(wordDBState,new Fields("args"),new QueryWordFunction(),new Fields("count"))//new QueryWordFunction() MapGet
                .filter(new DebugFilter());

        Config conf = new Config();
        conf.setNumWorkers(20);
        conf.setMaxSpoutPending(5000);

        LocalCluster cluster = new LocalCluster();
        cluster.submitTopology("mytopology", conf, topology.build());

        try {
            Thread.sleep(30000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //测试drpc
//        String result = drpc.execute("myspout","the");
//        System.err.println("result=======================" + result);
    }

}
