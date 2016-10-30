package com.apu.news.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author apurbapandey
 * This class is for loading property file.
 */
public class PropUtil {

	public static final Properties newsProps = new Properties();
	
	static{
		InputStream resource = PropUtil.class.getResourceAsStream("/news.properties");
		try {
			newsProps.load(resource);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
}
