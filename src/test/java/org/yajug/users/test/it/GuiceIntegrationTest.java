package org.yajug.users.test.it;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import org.jukito.JukitoRunner;
import org.junit.runner.RunWith;


@RunWith(JukitoRunner.class)
public abstract class GuiceIntegrationTest {

	private final static String testProperties = "/config.properties";
	
	
	/**
	 * Load the configuration properties
	 * @return the properties
	 */
	protected static Properties getTestProperties(){
		Properties properties = new Properties();
		
		try(InputStream input = GuiceIntegrationTest.class.getResourceAsStream(testProperties)){
			properties.load(input);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return properties;
	}
}
