package util;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
public class DBPropertyUtil {
	public static String getConnectionString(String fileName) throws IOException {
        String connStr = null;
        Properties props = new Properties();
        try (InputStream input = DBPropertyUtil.class.getClassLoader().getResourceAsStream(fileName)) {
            if (input == null) {
                throw new IOException("Unable to find " + fileName);
            }
            props.load(input);
        }
        String user = props.getProperty("user");
        String password = props.getProperty("password");
        String protocol = props.getProperty("protocol");
        String system = props.getProperty("system");
        String database = props.getProperty("database");
        String port = props.getProperty("port");

        connStr = protocol + "//" + system + ":" + port + "/" + database + "?user=" + user + "&password=" + password;
        return connStr;
    }
}
