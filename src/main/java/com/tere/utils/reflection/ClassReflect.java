package com.tere.utils.reflection;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ClassReflect
{
    protected static String GET_STR = "get";

    protected static String SET_STR = "set";

    protected static String ADD_STR = "add";

    protected static String REMOVE_STR = "remove";

    protected static String GET_CLASS_STR = "getClass";

    private static ClassReflect inst = new ClassReflect();
    
    public static ClassReflect inst()
    {
        return inst;
    }

    public static String getProperName(String str)
    {
    	if ((null == str) || (0 == str.length()))
    	{
    		return null;
    	}
        StringBuffer sb = new StringBuffer();
        char[] chars = new char[str.length()];
        chars = str.toCharArray();
        char c;
        boolean nextup = false;
        for (int i = 0; i < chars.length; i++)
        {
            if (i == 0)
                c = Character.toUpperCase(chars[i]);
            else if (chars[i] == '_')
            {
                nextup = true;
                sb.append(" ");
                continue;
            } else if (nextup)
            {
                nextup = false;
                c = Character.toUpperCase(chars[i]);
            } else
            	c = Character.toLowerCase(chars[i]);
                //connection = Character.toLowerCase(chars[i]);
            sb.append(c);
        }
        return sb.toString();
    	
    }

    public static String getLabelName(String str)
    {
    	if ((null == str) || (0 == str.length()))
    	{
    		return null;
    	}
        StringBuffer sb = new StringBuffer();
        char[] chars = new char[str.length()];
        chars = str.toCharArray();
        char c;
        boolean nextup = false;
        for (int i = 0; i < chars.length; i++)
        {
            if (i == 0)
                c = Character.toUpperCase(chars[i]);
            else
            if (Character.isUpperCase(chars[i]))
            {
            	c = chars[i];
                sb.append(" ");
            }
            else if (chars[i] == '_')
            {
                nextup = true;
                sb.append(" ");
                continue;
            } else if (nextup)
            {
                nextup = false;
                c = Character.toUpperCase(chars[i]);
            } else
            	c = Character.toLowerCase(chars[i]);
                //connection = Character.toLowerCase(chars[i]);
            sb.append(c);
        }
        return sb.toString();
    	
    }
    
    
    public static String getClassName(String str)
    {
    	if ((null == str) || (0 == str.length()))
    	{
    		return null;
    	}
        StringBuffer sb = new StringBuffer();
        char[] chars = new char[str.length()];
        chars = str.toCharArray();
        char c;
        boolean nextup = false;
        for (int i = 0; i < chars.length; i++)
        {
            if (i == 0)
                c = Character.toUpperCase(chars[i]);
            else if (chars[i] == '_')
            {
                nextup = true;
                continue;
            } else if (nextup)
            {
                nextup = false;
                c = Character.toUpperCase(chars[i]);
            } else
            	c = chars[i];
                //connection = Character.toLowerCase(chars[i]);
            sb.append(c);
        }
        return sb.toString();
    }

    /* (non-Javadoc)
     * @see org.apache.ojb.tools.mapping.reversedb.Namer#nameField(java.lang.String, java.lang.String)
     */
    public static String getFieldName(String str)
    {
    	if ((null == str) || (0 == str.length()))
    	{
    		return null;
    	}
        StringBuffer sb = new StringBuffer();
        char[] chars = new char[str.length()];
        chars = str.toCharArray();
        char c;
        boolean nextup = false;
        boolean first = true;
        for (int i = 0; i < chars.length; i++) {
            if (chars[i]=='_') {
                 nextup = true;
                 continue;
            }
            else if (nextup) {
                nextup = false;
                c = Character.toUpperCase(chars[i]);
            } 
            else c=chars[i]; //connection = Character.toLowerCase(chars[i]);
            if (first)
            {
            	sb.append(Character.toLowerCase(c));
            	first = false;
            }
            else
            {
            	sb.append(c);
            }
        }
        return sb.toString();
    }

    public static Map getFieldNames(Object object)
    {
        if (null == object)
        {
            throw new NullPointerException("getGetterMethods:null object received");
        }

        List methodList = getGetterMethods(object);
        Map fieldNameMap = new LinkedHashMap();

        Iterator methodItr = methodList.iterator();

        while (methodItr.hasNext())
        {
            Method method = (Method) methodItr.next();

            //StringBuffer fieldNameBuf = new StringBuffer(method.getName()
            //        .substring(GET_STR.length()));
            //fieldNameBuf.setCharAt(0, Character.toLowerCase(fieldNameBuf
            //        .charAt(0)));

            Class returnType = method.getReturnType();
            
            fieldNameMap.put(getFieldFromGetter(method.getName()), returnType.getName());
        }

        return fieldNameMap;
    }

    public static List getGetterMethods(Object object)
    {
        if (null == object)
        {
            throw new NullPointerException("getGetterMethods:null object received");
        }
        List methodList = new Vector();

        Method[] methodArray = object.getClass().getMethods();

        for (int nMethodLoop = 0; nMethodLoop < methodArray.length; nMethodLoop++)
        {
            Method method = methodArray[nMethodLoop];

            if (GET_CLASS_STR.equals(method.getName()))
            {
                continue;
            }
            if ((method.getName().substring(0, GET_STR.length())
                    .equals(GET_STR))
                    && (0 == method.getParameterTypes().length))
            {
                methodList.add(method);
            }
        }

        return methodList;
    }

    public static String getFieldFromGetter(String getterMethodName)
    {
        String fieldName = null;
        
        if ((GET_STR.equals(getterMethodName.substring(0, GET_STR.length()))) && (!getterMethodName.equals(GET_CLASS_STR)))
        {
            StringBuffer fieldNameBuf = new StringBuffer(getterMethodName.substring(GET_STR.length()));
            
            fieldNameBuf.setCharAt(0, Character.toLowerCase(fieldNameBuf
                    .charAt(0)));
            
            fieldName = fieldNameBuf.toString();
        }
        
        return fieldName;
    }

    public static String getGetterFromField(String fieldName)
    {
    	if ((null == fieldName) || (0 == fieldName.length()))
    	{
    		return null;
    	}

        StringBuffer getterNameBuf = new StringBuffer(fieldName.trim());
        
        getterNameBuf.setCharAt(0, Character.toUpperCase(getterNameBuf.charAt(0)));
        
        getterNameBuf.insert(0, GET_STR);

        return getterNameBuf.toString();
    }
    
    public static String getSetterFromField(String fieldName)
    {
    	if ((null == fieldName) || (0 == fieldName.length()))
    	{
    		return null;
    	}

        StringBuffer getterNameBuf = new StringBuffer(fieldName.trim());
        
        getterNameBuf.setCharAt(0, Character.toUpperCase(getterNameBuf.charAt(0)));
        
        getterNameBuf.insert(0, SET_STR);

        return getterNameBuf.toString();
    }
    
    public static String getAdderFromField(String fieldName)
    {
    	if ((null == fieldName) || (0 == fieldName.length()))
    	{
    		return null;
    	}

        StringBuffer adderNameBuf = new StringBuffer(fieldName.trim());
        
        adderNameBuf.setCharAt(0, Character.toUpperCase(adderNameBuf.charAt(0)));
        
        adderNameBuf.insert(0, ADD_STR);

        return adderNameBuf.toString();
    }
    
    public static String getRemoverFromField(String fieldName)
    {
    	if ((null == fieldName) || (0 == fieldName.length()))
    	{
    		return null;
    	}

        StringBuffer adderNameBuf = new StringBuffer(fieldName.trim());
        
        adderNameBuf.setCharAt(0, Character.toUpperCase(adderNameBuf.charAt(0)));
        
        adderNameBuf.insert(0, REMOVE_STR);

        return adderNameBuf.toString();
    }
    
    public static List getGetterFields(Object object) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException, MethodNotFoundException, InvalidArgumentException
    {
        if (null == object)
        {
            throw new NullPointerException("getGetterMethods");
        }
        List nameValueList = new ArrayList();

        List methodList = getGetterMethods(object);
        
        Iterator itr = methodList.iterator();
        
        while (itr.hasNext())
        {
            Method method = (Method)itr.next();
            
            String fieldName = getFieldFromGetter(method.getName());
            
            Object value = method.invoke(object, null);

            NameValue nameValue = new NameValue();
            nameValue.setName(fieldName); 
            nameValue.setValue(value);

            nameValueList.add(nameValue);
        }
        /*
        Method[] methodArray = object.getClass().getMethods();

        for (int nMethodLoop = 0; nMethodLoop < methodArray.length; nMethodLoop++)
        {
            Method method = methodArray[nMethodLoop];

            String fieldName = getFieldFromGetter(method.getName());
            
            if (null == fieldName)
            {
                continue;
            }
            
            NameValue nameValue = new NameValue();
            nameValue.setName(fieldName);
            nameValue.setValue(method.invoke(object, null));
            
            nameValueList.add(nameValue);
        }
*/
        return nameValueList;
    }
    
    public static List getSetterMethods(Object object)
    {
        if (null == object)
        {
            throw new NullPointerException("getSetterMethods");
        }
        List methodList = new ArrayList();

        Method[] methodArray = object.getClass().getMethods();

        for (int nMethodLoop = 0; nMethodLoop < methodArray.length; nMethodLoop++)
        {
            Method method = methodArray[nMethodLoop];

            if ((method.getName().substring(0, SET_STR.length())
                    .equals(SET_STR))
                    && (0 == method.getParameterTypes().length))
            {
                methodList.add(method);
            }
        }

        return methodList;
    }

    public static Method getMethod(Object object, String methodName)
            throws MethodNotFoundException
    {
        if (null == object)
        {
            throw new NullPointerException("getMethod : " + methodName);
        }
        Method[] methodArray = object.getClass().getMethods();

        for (int nMethodLoop = 0; nMethodLoop < methodArray.length; nMethodLoop++)
        {
            Method method = methodArray[nMethodLoop];

            if (method.getName().equals(methodName))
            {
                return method;
            }
        }
        throw new MethodNotFoundException();
    }

    public static String getGetterName(String methodString)
    {
        StringBuffer methodName = new StringBuffer(methodString.trim());
        methodName.setCharAt(0, Character.toUpperCase(methodName.charAt(0)));
        methodName.insert(0, "get");
        return methodName.toString();
    }

    public static String getSetterName(String methodString)
    {
    	if (null == methodString)
    	{
    		return null;
    	}
        StringBuffer methodName = new StringBuffer(methodString.trim());
        methodName.setCharAt(0, Character.toUpperCase(methodName.charAt(0)));
        methodName.insert(0, "set");
        return methodName.toString();
    }

    public static void executeSetter(Object object, String methodString,
            Object value) throws IllegalArgumentException,
            IllegalAccessException, InvocationTargetException,
            MethodNotFoundException, InvalidArgumentException
    {
        if (null == object)
        {
            throw new InvalidArgumentException(
                    "executeSetter: null Object param:" + methodString);
        }
        if ((null == methodString) || (0 == methodString.length()))
        {
            throw new InvalidArgumentException(
                    "executeSetter: null or zero length methodString");
        }
        Method method = getMethod(object, getSetterName(methodString));

        method.invoke(object, new Object[]
        { value });
    }

    public static Object executeGetter(Object object, String methodString)
            throws IllegalArgumentException, IllegalAccessException,
            InvocationTargetException, MethodNotFoundException,
            InvalidArgumentException
    {
        if (null == object)
        {
            throw new InvalidArgumentException(
                    "executeGetter: null Object param:" + methodString);
        }
        if ((null == methodString) || (0 == methodString.length()))
        {
            throw new InvalidArgumentException(
                    "executeGetter: null or zero length methodString");
        }
        Method method = getMethod(object, getGetterName(methodString));

        return method.invoke(object, null);

    }
}
