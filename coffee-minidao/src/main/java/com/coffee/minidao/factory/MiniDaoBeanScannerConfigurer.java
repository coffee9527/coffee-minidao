package com.coffee.minidao.factory;

import java.lang.annotation.Annotation;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.util.StringUtils;

import com.coffee.minidao.annotation.MiniDao;
import com.coffee.minidao.aop.MiniDaoHandler;
import com.coffee.minidao.aspect.EmptyInterceptor;

public class MiniDaoBeanScannerConfigurer implements BeanDefinitionRegistryPostProcessor {
	private String basePackage;
	//默认是IDao，推荐使用Repository
	private Class<? extends Annotation> annotation = MiniDao.class;
	//Map key类型
	private String keyType = "origin";
	//是否格式化sql
	private boolean formatSql = false;
	//是否输出sql
	private boolean showSql = false;
	//数据库类型
	private String dbType;
	//Minidao拦截器
	private EmptyInterceptor emptyInterceptor;
	public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {

	}

	public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
		/**
		 * 注册代理类
		 */
		registerRequestProxyHandler(registry);

		MiniDaoClassPathMapperScanner scanner = new MiniDaoClassPathMapperScanner(registry, annotation);
		/**
		 * 加载Dao层接口
		 */
		scanner.scan(StringUtils.tokenizeToStringArray(this.basePackage, ConfigurableApplicationContext.CONFIG_LOCATION_DELIMITERS));
	}
	
	private void registerRequestProxyHandler(BeanDefinitionRegistry registry) {
		GenericBeanDefinition jdbcDaoProxyDefinition = new GenericBeanDefinition();
		jdbcDaoProxyDefinition.setBeanClass(MiniDaoHandler.class);
		jdbcDaoProxyDefinition.getPropertyValues().add("formatSql", formatSql);
		jdbcDaoProxyDefinition.getPropertyValues().add("keyType", keyType);
		jdbcDaoProxyDefinition.getPropertyValues().add("showSql", showSql);
		jdbcDaoProxyDefinition.getPropertyValues().add("dbType", dbType);
		jdbcDaoProxyDefinition.getPropertyValues().add("emptyInterceptor", emptyInterceptor);
		registry.registerBeanDefinition("miniDaoHandler", jdbcDaoProxyDefinition);
	}
	
	public void setAnnotation(Class<? extends Annotation> annotation) {
		this.annotation = annotation;
	}

	public void setBasePackage(String basePackage) {
		this.basePackage = basePackage;
	}

	public void setDbType(String dbType) {
		this.dbType = dbType;
	}

	public void setFormatSql(boolean formatSql) {
		this.formatSql = formatSql;
	}

	public void setKeyType(String keyType) {
		this.keyType = keyType;
	}

	public void setShowSql(boolean showSql) {
		this.showSql = showSql;
	}

	public EmptyInterceptor getEmptyInterceptor() {
		return emptyInterceptor;
	}

	public void setEmptyInterceptor(EmptyInterceptor emptyInterceptor) {
		this.emptyInterceptor = emptyInterceptor;
	}
}
