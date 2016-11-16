package com.baizhitong.python;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.script.ScriptException;

import org.python.google.common.io.CharStreams;

public class PythonCaller {

	public static void main(String[] args) throws ScriptException, IOException {
		// 启动命令，加入如下参数即可
		// -Dpython.import.site=false
		// PythonInterpreter interp = new PythonInterpreter();
		// interp.execfile(PythonCaller.class.getResourceAsStream("numbers.py"));
		// interp.exec("import sys");
		// interp.exec("print sys");
		// interp.set("a", new PyInteger(42));
		// interp.exec("print a");
		// interp.exec("x = 2+2");
		// PyObject x = interp.get("x");
		// System.out.println("x: " + x);
		// interp.cleanup();
		// interp.close();

//		 ScriptContext context = new SimpleScriptContext();
//		 ScriptEngine engine = new
//		 ScriptEngineManager().getEngineByName("python");
//		 engine.eval(new FileReader("C:\\temp\\numbers.py"),context);
//		 Object x = engine.get("x");
//		 System.out.println("x: " + x);

		ProcessBuilder pb = new ProcessBuilder("python.exe", "numbers.py");
//		ProcessBuilder pb = new ProcessBuilder("ping","-t", "www.baidu.com");
		pb.redirectErrorStream(true);
		pb.directory(new File("C:\\temp"));
		Process p = pb.start();
		try {
			System.err.println(1);
			BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
			System.err.println(2);
			String retValue  = CharStreams.toString(in);
			System.out.println("value is : " + retValue);
			int status = p.waitFor();
			System.out.println("waitFor  status: " + status);
		} catch (Exception e) {
			System.out.println(e);
		}
	}

}
