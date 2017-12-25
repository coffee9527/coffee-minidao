package com.coffee.minidao.datasource;
/**
 * 获得和设置上下文环境的类，主要负责改变上下文数据源的名称
 * @author coffee
 *
 */
public class DataSourceContextHolder {
	private static final ThreadLocal<DataSourceType> contextHolder = new ThreadLocal<DataSourceType>();
	
	public static void clearDataSourceType() {
		contextHolder.remove();
	}
	
	public static DataSourceType getDataSourceType() {
		return (DataSourceType)contextHolder.get();
	}
	
	public static void setDataSourceType(DataSourceType dataSourceType) {
		contextHolder.set(dataSourceType);
	}
}
