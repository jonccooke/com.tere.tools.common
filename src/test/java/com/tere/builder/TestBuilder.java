package com.tere.builder;
import org.junit.Test;

import com.google.gson.JsonElement;
import com.tere.TereException;
import com.tere.builder.BuilderException;
import com.tere.builder.BuilderImpl;
import com.tere.builder.NotNull;
import com.tere.builder.NotNullIIfSet;

public class TestBuilder
{

	public class TestBuilderObject
	{
		@NotNull
		private String val1;
		@NotNull
		private int val2;
		@NotNullIIfSet (fieldName="val4")
		private String val3;
		@NotNullIIfSet (fieldName="val3")
		private int val4;

		protected String getVal1()
		{
			return val1;
		}

		protected int getVal2()
		{
			return val2;
		}

	}

	class TestObjectBuilder extends BuilderImpl<TestBuilderObject, TereException>
	{

		protected TestObjectBuilder() throws TereException
		{
			super();
		}

		public TestObjectBuilder val1(String val1)
		{
			this.value.val1 = val1;
			return this;
		}

		public TestObjectBuilder val2(int val2)
		{
			this.value.val2 = val2;
			return this;
		}

		public TestObjectBuilder val3(String val3)
		{
			this.value.val3 = val3;
			return this;
		}

		public TestObjectBuilder val4(int val4)
		{
			this.value.val4 = val4;
			return this;
		}

		@Override
		protected TestBuilderObject createInstance()
		{
			return new TestBuilderObject();
		}

		@Override
		public Builder<TestBuilderObject, TereException> fromJson(JsonElement jsonElement) throws TereException
		{
			// TODO Auto-generated method stub
			return null;
		}

	}

	@Test
	public void testBuilder() throws BuilderException, TereException
	{
		new TestObjectBuilder().val1("blah").val3("5").build();
	}

}
