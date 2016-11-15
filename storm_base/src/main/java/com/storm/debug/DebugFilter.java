package com.storm.debug;

import java.util.Date;

import org.apache.storm.trident.operation.BaseFilter;
import org.apache.storm.trident.tuple.TridentTuple;
import org.apache.storm.tuple.Fields;

public class DebugFilter extends BaseFilter {
	private final String name;

	public DebugFilter() {
		name = "DEBUG: ";
	}

	/**
	 * Creates a `Debug` filter with a string identifier.
	 * 
	 * @param name
	 */
	public DebugFilter(String name) {
		this.name = "DEBUG(" + name + "): ";
	}

	@Override
	public boolean isKeep(TridentTuple tuple) {
		Fields fields = tuple.getFields();

		String msg = "";
		for (int i = 0; i < fields.size(); i++) {
			String field = fields.get(i);
			try {
				msg += "(" + field + ":" + tuple.getValueByField(field) + "),";
			} catch (Exception e) {
			}
		}

		//非空则显示
		if(msg!=null && !"".equals(msg))
		{
			System.err.println("<" + new Date() + "> " + name + msg);
		}

		// System.err.println("<" + new Date() + "> " + name +
		// tuple.toString());
		return true;
	}
}
