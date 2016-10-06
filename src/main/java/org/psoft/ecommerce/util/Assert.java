package org.psoft.ecommerce.util;

import java.lang.reflect.Constructor;

import org.apache.commons.lang3.StringUtils;

public class Assert {

	public static String value(String value, String message) {
		if (StringUtils.isBlank(value)) {
			throw new IllegalArgumentException(message);
		}
		
		return StringUtils.trim(value);
	}
	
	public static <T> T notNull(T object, String message) {
		if (object == null) {
			throw new IllegalArgumentException(message);
			
		}
		return object;
	}
	
	public static void notNull(Object object, Class<? extends RuntimeException> exceptionClazz) {
		if (object == null) {
			RuntimeException exception = null;
			try {
				exception = exceptionClazz.newInstance();
			} catch (Exception e) {
				throw new RuntimeException("Unable to create excception " + exceptionClazz.getName(), e);
			}
			throw exception;
		}
	}

	public static void notNull(Object object, Class<? extends RuntimeException> exceptionClazz, Object arg1) {
		if (object == null) {
			RuntimeException exception = null;
			try {
				Constructor<? extends RuntimeException> constructor = exceptionClazz.getConstructor(arg1.getClass());
				exception = constructor.newInstance(arg1);
			} catch (Exception e) {
				throw new RuntimeException("Unable to create excception " + exceptionClazz.getName(), e);
			}
			throw exception;
		}

	}

	public static void notNull(Object object, Class<? extends RuntimeException> exceptionClazz, Object arg1,
			Object arg2) {
		if (object == null) {
			RuntimeException exception = null;
			try {
				Constructor<? extends RuntimeException> constructor = exceptionClazz.getConstructor(arg1.getClass(),
						arg2.getClass());
				exception = constructor.newInstance(arg1, arg2);
			} catch (Exception e) {
				throw new RuntimeException("Unable to create excception " + exceptionClazz.getName(), e);
			}
			throw exception;
		}
	}

	public static void notNull(Object object, Class<? extends RuntimeException> exceptionClazz, Object arg1,
			Object arg2, Object arg3) {

		if (object == null) {
			RuntimeException exception = null;
			try {
				Constructor<? extends RuntimeException> constructor = exceptionClazz.getConstructor(arg1.getClass(),
						arg2.getClass(), arg3.getClass());
				exception = constructor.newInstance(arg1, arg2, arg3);
			} catch (Exception e) {
				throw new RuntimeException("Unable to create excception " + exceptionClazz.getName(), e);
			}
			throw exception;
		}
	}

}
