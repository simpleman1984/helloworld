package sigarUtils;

import java.util.Properties;

import org.hyperic.sigar.NetInterfaceConfig;
import org.hyperic.sigar.NetInterfaceStat;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;

public class T {

	public static void main(String[] args) throws SigarException {
		Properties props = System.getProperties(); // 获得系统属性集
		String osName = props.getProperty("os.name");
		String arch = props.getProperty("os.arch");
		String library = props.getProperty("java.library.path");
		//windows系统
		if (osName.startsWith("Windows")) {
			library += ";" + T.class.getResource("/").getFile();
			System.setProperty("java.library.path", library);
			if (arch.startsWith("amd64")) {
				System.load(T.class.getResource("/sigar-amd64-winnt.dll").getFile());
			} else {

			}
		} else if (osName.startsWith("Linux")) {
			library += ":" + T.class.getResource("/").getFile();
			System.setProperty("java.library.path", library);
			if (arch.startsWith("amd64")) {
				System.load(T.class.getResource("/libsigar-amd64-linux.so").getFile());
			} else {
			}
		}
		Sigar sigar = new Sigar();
		
		String ifNames[] = sigar.getNetInterfaceList();
		for (String s : ifNames) {
			NetInterfaceConfig config = sigar.getNetInterfaceConfig(s);
			NetInterfaceStat statStart = sigar.getNetInterfaceStat(config.getName());
			System.err.println(osName + "        "  + config.getAddress() + "        " + s + "           ===========              " + statStart.getRxBytes());
		}
	}

}
