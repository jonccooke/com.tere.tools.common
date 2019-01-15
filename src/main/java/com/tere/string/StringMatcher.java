package com.tere.string;

import java.util.Collection;
import java.util.concurrent.atomic.AtomicInteger;

public class StringMatcher<T>
{

	public interface MatchingFunction<T>
	{
		public boolean match(String patternRegex, T matchingObject);
	}

	public int match(String pattern, Collection<T> collection, MatchingFunction<T> matchingFunction)
	{

		AtomicInteger matched = new AtomicInteger(0);
		String patternRegex = createRegexFromGlob(pattern);

		collection.forEach(t ->
			{
				if  (matchingFunction.match(patternRegex, t))
				{
					matched.incrementAndGet();
				}
			});
		return matched.get();
	}

	private String createRegexFromGlob(String glob)
	{
		StringBuilder out = new StringBuilder("^");
		for (int i = 0; i < glob.length(); ++i)
		{
			final char c = glob.charAt(i);
			switch (c)
			{
			case '*':
				out.append(".*");
				break;
			case '?':
				out.append('.');
				break;
			case '.':
				out.append("\\.");
				break;
			case '\\':
				out.append("\\\\");
				break;
			default:
				out.append(c);
			}
		}
		out.append('$');
		return out.toString();
	}
}