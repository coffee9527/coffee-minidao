package com.coffee.minidao.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;

import com.coffee.minidao.annotation.Param;

public class ParameterNameUtils {
	/**
	 * 获取指定方法的参数名
	 * 
	 * @param method
	 *            要获取参数名的方法
	 * @return 按参数顺序排列的参数名列表
	 */
	public static String[] getMethodParameterNamesByAnnotation(Method method) {
		Annotation[][] parameterAnnotations = method.getParameterAnnotations();
		if (parameterAnnotations == null || parameterAnnotations.length == 0) {
			return null;
		}
		String[] parameterNames = new String[parameterAnnotations.length];
		int i = 0;
		for (Annotation[] parameterAnnotation : parameterAnnotations) {
			for (Annotation annotation : parameterAnnotation) {
				if (annotation instanceof Param) {
					Param param = (Param) annotation;
					parameterNames[i++] = param.value();
				}
			}
		}
		return parameterNames;
	}

	public void method1(@Param("parameter1") String param1, @Param("parameter2") String param2) {
		System.out.println(param1 + param2);
	}

	public static void main(String[] args) {
		Method method = null;
		try {
			method = Class.forName("com.coffee.web.cgdynamgraph.dao.core.CgDynamGraphDao").getDeclaredMethod("queryCgDynamGraphItemssss", String.class, String.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		String[] parameterNames = ParameterNameUtils.getMethodParameterNamesByAnnotation(method);
		System.out.println(Arrays.toString(parameterNames));
	}
}
