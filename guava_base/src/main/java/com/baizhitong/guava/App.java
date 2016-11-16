package com.baizhitong.guava;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.Lists;

/**
 * Hello world!
 *
 */
public class App {
	public static void main(String[] args) throws ExecutionException, IOException {
		Collections.unmodifiableList(Lists.newArrayList());

		// Preconditions.checkNotNull(null);

		LoadingCache<String, String> graphs = CacheBuilder.newBuilder().maximumSize(1000).build(new CacheLoader<String, String>() {
			public String load(String key) {
				System.out.println("read from db........");
				return "xxxxxxxxxxxx";
			}
		});

		String string = graphs.get("dddddddd",new Callable<String>() {
		    public String call() throws Exception{
		    	System.out.println("read from db2........");
		      return "yyyyyyyyyyyyyyy";
		    }
		  });
		System.out.println("Hello World!" + string);
		string = graphs.get("dddddddd");
		System.out.println("Hello World!" + string);
		
		Kryo kryo = new Kryo();
	    Output output = new Output(new FileOutputStream("file.bin"));
	    Person  person = new Person();
	    person.setId("2");
	    person.setName("中文");
	    
	    kryo.writeObject(output, person);
	    output.writeBoolean(false);
	    
	    output.close();
	    
	    Input input = new Input(new FileInputStream("file.bin"));
	    
	    System.err.println(input.available());
	    person = kryo.readObject(input, Person.class);
	    
	    System.err.println(input.available());
	    System.err.println(person.getName());
	    
	    System.err.println(input.readBoolean());
	    System.err.println(input.available());
	    input.close();
	}
}
