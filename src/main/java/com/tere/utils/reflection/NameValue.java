package com.tere.utils.reflection;

public class NameValue
{
    private String name;
    private Object value;
    
    public NameValue()
    {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @return Returns the name.
     */
    public String getName()
    {
        return name;
    }

    /**
     * @param name
     * @param value
     */
    public NameValue(String name, Object value)
    {
        super();
        // TODO Auto-generated constructor stub
        this.name = name;
        this.value = value;
    }

    /**
     * @param name The name to set.
     */
    public void setName(String name)
    {
        this.name = name;
    }

    /**
     * @return Returns the value.
     */
    public Object getValue()
    {
        return value;
    }

    /**
     * @param value The value to set.
     */
    public void setValue(Object value)
    {
        this.value = value;
    }

}
