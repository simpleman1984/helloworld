package python;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Cmd {

	public static void main(String[] args) throws IOException {
		try {
			// Process p = Runtime.getRuntime().exec(new String[]{"python",
			// "script.py"});
			// BufferedWriter writer = new BufferedWriter(new
			// OutputStreamWriter(p.getOutputStream()));
			// writer.write("password");
			// writer.newLine();
			// writer.close();

			ProcessBuilder pb = new ProcessBuilder("python", "test1.py");
			Process p = pb.start();

			BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
			System.out.println("value is : " + in.readLine());
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

}
