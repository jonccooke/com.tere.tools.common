package com.tere.utils.io.publish.publishers.acunu;

import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import com.tere.TereException;
import com.tere.logging.LogManager;
import com.tere.logging.Logger;
import com.tere.utils.io.json.JSONObject;

public class AcunuPublisher
{
	private Logger log = LogManager.getLogger(AcunuPublisher.class);
	private URL url;
	
	public AcunuPublisher(URL url)
	{
		this.url = url;
	}
	
	public void publish(JSONObject jsonObject) throws TereException
	{
		HttpURLConnection urlConnection = null;
		OutputStreamWriter writer = null;
		try
		{
			urlConnection = (HttpURLConnection) url.openConnection();
			urlConnection.setDoOutput(true);
			urlConnection.setRequestMethod("POST");
			urlConnection.connect();
			
			String jSONStr = jsonObject.toJSONString();
			log.debug("JSON:%s", jSONStr);
			
			log.debug("Sending HTTP POST to ...%s", url.getPath());
			writer = new OutputStreamWriter(urlConnection.getOutputStream());
			
			writer.write(jSONStr);
			writer.flush();
			writer.close();

			int responseCode = urlConnection.getResponseCode();
			
			if (HttpURLConnection.HTTP_OK != responseCode)
			{
				log.error( urlConnection.getResponseMessage());
			}
			
			log.debug("Done");
			
		}
		catch (Exception e)
		{
			log.error(e);
		}
		finally
		{

		}

	}
}
