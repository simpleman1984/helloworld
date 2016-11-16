import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class Test {

	public static void main(String[] args) throws IOException
	{
		ConfigurableStreamHandlerFactory fac = new ConfigurableStreamHandlerFactory("classpath",new Handler());
		URL.setURLStreamHandlerFactory(fac);
		URL url = new URL("classpath:org/my/package/resource.extension");
		url.openConnection();
		
	}
	
}
