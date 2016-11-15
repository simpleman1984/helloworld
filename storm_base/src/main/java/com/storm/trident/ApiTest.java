package com.storm.trident;

import java.util.ArrayList;
import java.util.List;

import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.trident.TridentTopology;
import org.apache.storm.trident.operation.BaseAggregator;
import org.apache.storm.trident.operation.BaseFilter;
import org.apache.storm.trident.operation.BaseFunction;
import org.apache.storm.trident.operation.FlatMapFunction;
import org.apache.storm.trident.operation.MapFunction;
import org.apache.storm.trident.operation.TridentCollector;
import org.apache.storm.trident.operation.builtin.Sum;
import org.apache.storm.trident.testing.FixedBatchSpout;
import org.apache.storm.trident.tuple.TridentTuple;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Values;

import com.storm.debug.DebugFilter;

/**
 * 最基础的api示例，请仔细体会每个方法的作用~
 * 
 * @author Administrator
 *
 */
public class ApiTest {

	public static class MyFunction extends BaseFunction {
		public void execute(TridentTuple tuple, TridentCollector collector) {
			for (int i = 0; i < tuple.getInteger(0); i++) {
				collector.emit(new Values(i));
			}
		}
	}

	public static class MyFilter extends BaseFilter {
		public boolean isKeep(TridentTuple tuple) {
			return tuple.getInteger(0) == 1 && tuple.getInteger(1) == 2;
		}
	}

	/**
	 * 还是1条记录
	 * 
	 * @author Administrator
	 *
	 */
	public static class UpperCase implements MapFunction {
		@Override
		public Values execute(TridentTuple input) {
			System.err.println("=============================================" + input.getInteger(0) + 1);
			return new Values(input.getInteger(0) + 1, 11, 45);
		}
	}

	/**
	 * 1条记录变 多条记录
	 * 
	 * @author Administrator
	 *
	 */
	public static class Split implements FlatMapFunction {
		@Override
		public Iterable<Values> execute(TridentTuple input) {
			List<Values> valuesList = new ArrayList<>();

			int sum = input.getInteger(0) + input.getInteger(1) + input.getInteger(2);

			valuesList.add(new Values(sum, 0, 1));
			valuesList.add(new Values(sum, 2, 3));

			return valuesList;
		}
	}
	
    static class CountState {
        long count = 0;
    }

	public static  class CountAgg extends BaseAggregator<CountState> {
		
	    public CountState init(Object batchId, TridentCollector collector) {
	        return new CountState();
	    }

	    public void aggregate(CountState state, TridentTuple tuple, TridentCollector collector) {
	        state.count+=1;
	    }

	    public void complete(CountState state, TridentCollector collector) {
	        collector.emit(new Values(state.count));
	    }
	}

	public static void main(String[] args) {
		TridentTopology topology = new TridentTopology();

		FixedBatchSpout spout = new FixedBatchSpout(new Fields("a", "b", "c"), 4, new Values(1, 2, 3),
				new Values(4, 1, 6), new Values(3, 0, 8), new Values(3, 0, 8));
		spout.setCycle(false);

		topology.newStream("spout1", spout)
				// .filter(new MyFilter())
//				.each(new Fields("b"), new MyFunction(), new Fields("d"))
				// .map(new UpperCase())
				// .flatMap(new Split())

		.filter(new DebugFilter("原始"))
//		.partitionBy(new Fields("b"))
		.groupBy(new Fields("b"))
		.chainedAgg()
		.partitionAggregate(new Fields("a"),new CountAgg(), new Fields("count"))
		.partitionAggregate(new Fields("a"), new Sum(), new Fields("sum"))
		.chainEnd()
		
				// .minBy("a")
				// .maxBy("c")

		.filter(new DebugFilter("计算后"));

		Config conf = new Config();
		conf.setNumWorkers(20);
		conf.setMaxSpoutPending(5000);

		LocalCluster cluster = new LocalCluster();
		cluster.submitTopology("mytopology", conf, topology.build());
	}

}
