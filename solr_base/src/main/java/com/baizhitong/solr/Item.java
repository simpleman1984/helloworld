package com.baizhitong.solr;

import java.util.List;

import org.apache.solr.client.solrj.beans.Field;

public class Item {
	@Field
	String id;

	@Field("cat")
	String[] categories;

	@Field
	List<String> features;
}
