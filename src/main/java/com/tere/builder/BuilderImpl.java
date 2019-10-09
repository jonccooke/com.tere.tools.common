package com.tere.builder;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import com.google.gson.JsonElement;
import com.tere.TereException;

public abstract class BuilderImpl<C, E extends TereException> implements Builder<C, E>
{
	protected C value;

	protected abstract C createInstance() throws E;

	protected BuilderImpl() throws E
	{
		value = createInstance();
	}
	protected void check() throws BuilderException
	{
		check(value);
	}
	protected void check(Object val) throws BuilderException
	{
		Class<?> clazz = val.getClass();

		while (clazz != null && clazz != Object.class)
		{

			for (Field field : clazz.getDeclaredFields())
			{
				field.setAccessible(true);
//				for (Annotation annotation : field.getDeclaredAnnotations())
//				{
//					System.out.print(annotation.toString());
//				}
				if (field.isAnnotationPresent(NotNull.class))
				{
					try
					{
						if (null == field.get(val))
						{
							throw new FieldNotSetException(clazz.getName(), field.getName());
						}
					} catch (IllegalArgumentException | IllegalAccessException e)
					{
						throw new BuilderException(e);
					}
				}
				if (field.isAnnotationPresent(NotNullIIfSet.class))
				{
					try
					{
						Annotation annotation= field.getAnnotation(NotNullIIfSet.class);
						NotNullIIfSet notNullIIfSet = (NotNullIIfSet)annotation;
						Field fieldToCompare = clazz.getDeclaredField(notNullIIfSet.fieldName());
						field.setAccessible(true);
						fieldToCompare.setAccessible(true);
						Object f1val = field.get(val);
						Object f2val = fieldToCompare.get(val);
						if (null == f1val && null == f2val)
						{
							throw new FieldNotSetException(clazz.getName(), field.getName());
						}
					} catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e)
					{
						throw new BuilderException(e);
					}
				}
			}
			clazz = clazz.getSuperclass();
		}

	}

	public <T> BuilderImpl<C, E> setObject(String fieldName, T newValue) throws BuilderException
	{
		try
		{
			Field field = value.getClass().getDeclaredField(fieldName);
			
			field.set(value, newValue);
			
		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e)
		{
			throw new BuilderException(e);
		}
		return this;
	}

	public BuilderImpl<C, E> set(String fieldName, String newValue) throws BuilderException
	{
		return setObject(fieldName, newValue);
	}

	public BuilderImpl<C, E> set(String fieldName, int newValue) throws BuilderException
	{
		return setObject(fieldName, newValue);
	}

	public BuilderImpl<C, E> set(String fieldName, byte[] newValue) throws BuilderException
	{
		return setObject(fieldName, newValue);
	}

	@Override
	public C build() throws E, BuilderException
	{
		check();
		return value;
	}

	@Override
	public  BuilderImpl<C, E> fromJson(JsonElement jsonElement) throws TereException
	{
		return this;
	}



}
