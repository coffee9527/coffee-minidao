package com.coffee.minidao.aspect;

import java.lang.reflect.Field;

public interface EmptyInterceptor {
	public boolean onInsert(Field[] fields, Object obj);
	public boolean onUpdate(Field[] fields, Object obj);
}
