package com.coffee.minidao.util;

import java.lang.reflect.Modifier;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;
import javassist.bytecode.CodeAttribute;
import javassist.bytecode.LocalVariableAttribute;
import javassist.bytecode.MethodInfo;

/**
 * 依赖javassit的工具类，获取方法的参数名
 * @author coffee
 *
 */
public class Classes {
	private Classes() {}
	
	protected static String[] getMethodParamNames(CtMethod cm) {
		CtClass cc = cm.getDeclaringClass();
		MethodInfo methodInfo = cm.getMethodInfo();
		CodeAttribute codeAttribute = methodInfo.getCodeAttribute();
		LocalVariableAttribute attr = null;
		if(codeAttribute != null) 
			attr = (LocalVariableAttribute)codeAttribute.getAttribute(LocalVariableAttribute.tag);
		else 
			return null;
		
		String[] paramNames = null;
		try {
			paramNames = new String[cm.getParameterTypes().length];
		}catch(NotFoundException e) {
			e.printStackTrace();
		}
		
		int pos = Modifier.isStatic(cm.getModifiers()) ? 0 : 1;
		for(int i=0; i<paramNames.length; i++) {
			paramNames[i] = attr.variableName(i + pos);
		}
		return paramNames;
	}
	
	public static String[] getMethodParamNames(Class<?> clazz, String method, Class<?>... paramTypes) {
		ClassPool pool = ClassPool.getDefault();
		CtClass cc = null;
		CtMethod cm = null;
		try {
			cc = pool.get(clazz.getName());
			
			String[] paramTypeNames = new String[paramTypes.length];
			for(int i=0;i<paramTypes.length;i++) {
				paramTypeNames[i] = paramTypes[i].getName();
			}
			
			cm = cc.getDeclaredMethod(method, pool.get(paramTypeNames));
		} catch (NotFoundException e) {
			e.printStackTrace();
		}
		return getMethodParamNames(cm);
	}
	
	public static String[] getMethodParamNames(Class<?> clazz, String method) {
		 
        ClassPool pool = ClassPool.getDefault();
        CtClass cc;
        CtMethod cm = null;
        try {
            cc = pool.get(clazz.getName());
            cm = cc.getDeclaredMethod(method);
        } catch (NotFoundException e) {
//            Exceptions.uncheck(e);
        }
        return getMethodParamNames(cm);
    }
	
	public static void main(String[] args) {
		try {
			String[] s = getMethodParamNames(Class.forName("com.coffee.web.demo.service.test.TransactionTestServiceI"), "insertData");
			for(String c:s){
				System.out.println(c);
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
}
