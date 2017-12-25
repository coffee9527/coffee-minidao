package com.coffee.minidao.aspect;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class MinidaoInterceptor implements EmptyInterceptor {

	public boolean onInsert(Field[] fields, Object obj) {
		Map<Object, Object> map = new HashMap<Object, Object>();
		for(int j=0; j<fields.length; j++) {
			fields[j].setAccessible(true);
			String fieldName = fields[j].getName();
			if("createBy".equals(fieldName))
				map.put("createBy", "scott");
			if("createDate".equals(fieldName))
				map.put("createDate", new Date());
		}
		try {
			setFieldValue(map, obj);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public boolean onUpdate(Field[] fields, Object obj) {
		Map<Object, Object> map = new HashMap<Object, Object>();
		for(int j=0; j<fields.length; j++) {
			fields[j].setAccessible(true);
			String fieldName = fields[j].getName();
			if("udpateBy".equals(fieldName))
				map.put("udpateBy", "scott");
			if("updateDate".equals(fieldName))
				map.put("updateDate", new Date());
		}
		try {
			setFieldValue(map, obj);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public static void setFieldValue(Map<Object, Object> map, Object bean) throws Exception {
		Class<?> cls = bean.getClass(); 
		Method[] methods = cls.getDeclaredMethods();
		Field[] fields = cls.getDeclaredFields();
		
		for(Field field : fields) {
			String fldtype = field.getType().getSimpleName();
			String fldSetName = field.getName();
			String setMethod = pareSetName(fldSetName);
			if(!checkMethod(methods, setMethod))continue;
			if(!map.containsKey(fldSetName)) continue;
			Object value = map.get(fldSetName);
			System.out.println(value.toString());
			Method method = cls.getMethod(setMethod, field.getType());
			System.out.println(method.getName());
			if(null != value) {
				if("String".equals(fldtype))
					method.invoke(bean, (String)value);
				else if("Double".equals(fldtype))
					method.invoke(bean, (Double)value);
				else if("int".equals(fldtype))
					method.invoke(bean, Integer.valueOf((String) value));
				else 
					method.invoke(bean, value);
			}
		}
	}
	
	public static String pareSetName(String fldname) {
		if(null == fldname || "".equals(fldname))
			return null;
		String pro = "set" + fldname.substring(0, 1).toUpperCase() + fldname.substring(1);
		return pro;
	}
	
	public static boolean checkMethod(Method methods[], String met) {
		if(null != methods) {
			for(Method method : methods) {
				if (met.equals(method.getName()))
					return true;
			}
		}
		return false;
	}
}
