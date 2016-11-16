
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;

import rx.Observable;
import rx.functions.Action1;

public class CommandHelloWorld extends HystrixCommand<String> {

    private final String name;

    public CommandHelloWorld(String name) {
        super(HystrixCommandGroupKey.Factory.asKey("ExampleGroup"));
        this.name = name;
    }

    @Override
    protected String run() {
        return "Hello " + name + "!";
    }

	public static void main(String[] args) throws InterruptedException, ExecutionException {
		String s = new CommandHelloWorld("Bob").execute();
		System.err.println(s);
		
		String s2 = new CommandHelloWorld("Bob").execute();
		System.err.println(s2);
		
		Future<String> s1 = new CommandHelloWorld("Bob").queue();
		System.err.println(s1.get());
		
		
		Observable<String> ho = new CommandHelloWorld("Bob").observe();
		ho.subscribe(new Action1<String>() {
		    @Override
		    public void call(String s) {
		    	System.err.println("-----------------"+s);
		    }
		});
		
	}

}