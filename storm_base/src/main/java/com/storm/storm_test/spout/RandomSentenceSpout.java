/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.storm.storm_test.spout;

import java.util.Date;
import java.util.Map;
import java.util.Random;

import org.apache.storm.spout.SpoutOutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichSpout;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Values;
import org.apache.storm.utils.Utils;

public class RandomSentenceSpout extends BaseRichSpout {
	SpoutOutputCollector _collector;
	Random _rand;

	/**
	 * 初始化collector
	 */
	@Override
	public void open(Map conf, TopologyContext context, SpoutOutputCollector collector) {
		System.out.println("=================================================== open");
		_collector = collector;
		_rand = new Random();
	}

	/**
	 * 在SpoutTracker类中被调用，每调用一次就可以向storm集群中发射一条数据（一个tuple元组），该方法会被不停的调用
	 */
	@Override
	public void nextTuple() {
		Utils.sleep(1000);
		System.out.println("=================================================== nextTuple");
		_collector.emit(new Values("test"));
	}

	@Override
	public void ack(Object id) {
		System.out.println("消息处理完成  -------------- ack------------" + id);
	}

	@Override
	public void fail(Object id) {
		System.out.println("=-------------- fail------------" + id);
	}

	/**
	 * 定义字段id，该id在简单模式下没有用处，但在按照字段分组的模式下有很大的用处。
	 * 该declarer变量有很大作用，我们还可以调用declarer.declareStream();来定义stramId，
	 * 该id可以用来定义更加复杂的流拓扑结构
	 */
	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("word"));
	}

}