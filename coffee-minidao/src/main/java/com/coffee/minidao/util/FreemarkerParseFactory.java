package com.coffee.minidao.util;

import java.io.StringWriter;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.utility.StringUtil;

public class FreemarkerParseFactory {
	private static final Logger logger = Logger.getLogger(FreemarkerParseFactory.class);
	
	private static final String ENCODE = "utf-8";
	
	//文件缓存
	private static final Configuration _tplConfig = new Configuration();
	//sql缓存
	private static final Configuration _sqlConfig = new Configuration();
	
	private static StringTemplateLoader stringTemplateLoader = new StringTemplateLoader();
	
	//使用内嵌的(?)打开单行和多行模式
	private final static Pattern p = Pattern.compile("(?ms)/\\*.*?\\*/|^\\s*//.*?$");
	
	static {
		_tplConfig.setClassForTemplateLoading(new FreemarkerParseFactory().getClass(), "/");
		_tplConfig.setNumberFormat("0.#####################");
		_sqlConfig.setTemplateLoader(stringTemplateLoader);
		_sqlConfig.setNumberFormat("0.#####################");
	}
	
	/**
	 * 判断模板是否存在
	 * @param tplName
	 * @return
	 */
	public static boolean isExistTemplate(String tplName) {
		try {
			Template mytpl = _tplConfig.getTemplate(tplName, "UTF-8");
			if(mytpl == null)
				return false;
		} catch (Exception e) {
			return false;
		}
		return true;
	}
	
	public static String parseTemplate(String tplName, Map<String, Object> paras) {
		try {
			StringWriter swriter = new StringWriter();
			Template mytpl = _tplConfig.getTemplate(tplName, ENCODE);
			mytpl.process(paras, swriter);
			return getSqlText(swriter.toString());
		} catch (Exception e) {
			logger.error(e.getMessage(), e.fillInStackTrace());
			logger.error("发送一次的模板key:{ " + tplName + " }");
			System.err.println(e.getMessage());
			System.err.println("模板名：{ "+tplName+" }");
			throw new RuntimeException("解析SQL模板异常");
		}
	}
	
	public static String parseTemplateContent(String tplContent, Map<String, Object> paras) {
		try {
			StringWriter swriter = new StringWriter();
			if(stringTemplateLoader.findTemplateSource("sql_" + tplContent.hashCode()) == null) {
				stringTemplateLoader.putTemplate("sql_" + tplContent.hashCode(), tplContent);
			}
			Template mytpl = _sqlConfig.getTemplate("sql_" + tplContent.hashCode(), ENCODE);
            mytpl.process(paras, swriter);
            return getSqlText(swriter.toString());
		}catch (Exception e) {
            logger.error(e.getMessage(), e.fillInStackTrace());
            logger.error("发送一次的模板key:{ "+ tplContent +" }");
            System.err.println(e.getMessage());
            System.err.println("模板内容:{ "+ tplContent +" }");
            throw new RuntimeException("解析SQL模板异常");
        }
	}
	
	/**
	 * 除去无效字段，去掉注解 不然批量处理可能报错 去除无效的等于
	 * @param sql
	 * @return
	 */
	private static String getSqlText(String sql) {
		//将注释替换成""
		sql = p.matcher(sql).replaceAll("");
		sql = sql.replaceAll("\\n", " ").replaceAll("\\t", " ").replaceAll("\\s{1,}", " ").trim();
		
		//去掉最后四where这样的问题
		if (sql.endsWith("where") || sql.endsWith("where "))
			sql = sql.substring(0, sql.lastIndexOf("where"));
		
		//去掉where and 这样的问题
		int index = 0;
		while((index = StringUtils.indexOf(sql, "where and", index)) != -1) {
			sql = sql.substring(0, index + 5) + sql.substring(index + 9, sql.length());
		}
		
		// 去掉 , where 这样的问题
        index = 0;
        while ((index = StringUtils.indexOfIgnoreCase(sql, ", where", index)) != -1) {
            sql = sql.substring(0, index) + sql.substring(index + 1, sql.length());
        }
        
     // 去掉 最后是 ,这样的问题
        if (sql.endsWith(",") || sql.endsWith(", ")) {
            sql = sql.substring(0, sql.lastIndexOf(","));
        }
        return sql;
	}
}
