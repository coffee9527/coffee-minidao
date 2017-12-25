package com.coffee.minidao.factory;

import java.lang.reflect.Proxy;

import org.springframework.beans.factory.FactoryBean;

import com.coffee.minidao.aop.MiniDaoHandler;

public class MiniDaoBeanFactory<T> implements FactoryBean<T> {
	private Class<T> daoInterface;
	
	private MiniDaoHandler proxy;

	public T getObject() throws Exception {
		return newInstance();
	}

	public Class<?> getObjectType() {
		return daoInterface;
	}

	public boolean isSingleton() {
		return true;
	}
	
	public MiniDaoHandler getProxy() {
		return proxy;
	}
	
	private T newInstance() {
		return (T)Proxy.newProxyInstance(daoInterface.getClassLoader(), new Class[] {daoInterface}, proxy);
	}
	
	public void setProxy(MiniDaoHandler proxy) {
		this.proxy = proxy;
	}
	
	public void setDaoInterface(Class<T> daoInterface) {
		this.daoInterface = daoInterface;
	}

}
