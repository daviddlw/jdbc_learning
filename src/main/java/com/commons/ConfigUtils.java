package com.commons;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

/**
 * 公共类
 * 
 * @author David.Dai
 * 
 */
public final class ConfigUtils
{

	private static final String CONFIG_PATH = "/config.properties";
	private static Properties _properties = new Properties();
	private static InputStream _inStream;

	public ConfigUtils()
	{
		super();
		try
		{
			initConfig(CONFIG_PATH);
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public ConfigUtils(String configPath)
	{
		super();
		try
		{
			initConfig(configPath);
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void initConfig(String configPath) throws IOException
	{
		// 读取相应的配置文件
		_inStream = ConfigUtils.class.getResourceAsStream(configPath);
		_properties.load(_inStream);
	}

	/**
	 * 根据属性名获取属性值
	 * 
	 * @param propName
	 * @return
	 */
	public String getProperty(String propName)
	{
		return _properties.getProperty(propName, "").trim();
	}

	/**
	 * 获取到所有属性键值对
	 * 
	 * @return
	 */
	public Set<Entry<Object, Object>> getProperties()
	{
		return _properties.entrySet();
	}
}
