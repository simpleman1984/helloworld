import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLConnection;

import reflection.AnInterface2;
import reflection.AnInterface2Impl;

public class MainClass {

	public static void main(String[] args)
			throws ClassNotFoundException, InstantiationException, IllegalAccessException, InterruptedException {
		ClassLoader parentClassLoader = MyClassLoader.class.getClassLoader();

		// create new class loader so classes can be reloaded.
		MyClassLoader classLoader = new MyClassLoader(parentClassLoader);
		Class myObjectClass = classLoader.loadClass("reflection.AnInterface2Impl1");

		// classloader 从远程读取，本地只有接口实现相关的代码~~~~~~~~~~~~~~~~~
		AnInterface2 object1 = (AnInterface2) myObjectClass.newInstance();
		object1.hello();

		// 此处使用了讨巧的做法，申明了一个虚有的类型，用于表达本地classloader已经存在，解决虚拟机运行的时候，提示类不存在的bug
		AnInterface2Impl object2 = (AnInterface2Impl) myObjectClass.newInstance();
		object2.hello();
		
		Thread.sleep(300000);
	}

	/**
	 * 从文件中读取类定义
	 * 
	 * @author xuaihua
	 */
	static class MyClassLoader extends ClassLoader {

		public MyClassLoader(ClassLoader parent) {
			super(parent);
		}

		public Class loadClass(String name) throws ClassNotFoundException {
			if (!"reflection.AnInterface2Impl1".equals(name))
				return super.loadClass(name);

			try {
				String url = "file:C:/temp/AnInterface2Impl1.class";
				URL myUrl = new URL(url);
				URLConnection connection = myUrl.openConnection();
				InputStream input = connection.getInputStream();
				ByteArrayOutputStream buffer = new ByteArrayOutputStream();
				int data = input.read();

				while (data != -1) {
					buffer.write(data);
					data = input.read();
				}

				input.close();

				byte[] classData = buffer.toByteArray();

				return defineClass("reflection.AnInterface2Impl1", classData, 0, classData.length);

			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

			return null;
		}
	}

	/**
	 * 从jar中读取
	 */
	static {
		// URLClassLoader child = new URLClassLoader(new URL("file://./my.jar"),
		// Thread.currentThread().getClassLoader());
		// Class classToLoad = Class.forName("com.MyClass", true, child);
		// Method method = classToLoad.getDeclaredMethod("myMethod");
		// Object instance = classToLoad.newInstance();
		// Object result = method.invoke(instance);
	}
	
}
