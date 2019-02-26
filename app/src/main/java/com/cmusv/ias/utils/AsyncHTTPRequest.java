package com.cmusv.ias.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import android.os.AsyncTask;
import android.util.Log;

public class AsyncHTTPRequest {
	private static final String TAG = "AsyncHTTPRequest";

	private String readStream(InputStream in) throws IOException {

		Log.d(TAG, "readStream");

		InputStreamReader reader = new InputStreamReader(in);
		char[] buffer = new char[1000];
		String str = "";
		int i = reader.read(buffer, 0, 1000);
		while (i != -1) {
			str = str + String.valueOf(buffer, 0, i);
			i = reader.read(buffer, 0, 1000);
			Log.d(TAG +"read" , str);
		}
		Log.d(TAG, str);
		reader.close();
		return str;
	}
	
	private void writeStream(OutputStream out, String params)
			throws IOException {

		Log.d(TAG, "writeStream");

		OutputStreamWriter writer = new OutputStreamWriter(out);
		writer.write(params);
		writer.flush();
		writer.close();
	}
	
	// HTTP GET
	public String getFromURL(String address) {

		GetFromURLTask task = new GetFromURLTask();
		String result = new String();

		try {

			task.execute(address);
			result = task.get(); // TODO - using get() for now, normally should
									// pickup result
									// using onPostExecute(), problem with get()
									// is that
									// it will look like a blocking call from
									// UI's perspective,
									// it will block the main thread, so we can
									// get away with this
									// temporarily with small data sets.

		} catch (Exception e) {
			Log.d(TAG, e.getClass().getName());
			if (e.getLocalizedMessage() != null)
				Log.d(TAG, e.getLocalizedMessage());
		}
		return result;
		// new GetFromURLTask().execute(address);
	}
	
	private class GetFromURLTask extends AsyncTask<String, Void, String> {

		@Override
		protected String doInBackground(String... params) {
			Log.d(TAG, "GetFromURLTask");

			URL url;
			HttpURLConnection urlConnection = null;
			String str = "";

			try {
				url = new URL(params[0]);
				urlConnection = (HttpURLConnection) url.openConnection();
				urlConnection.setRequestMethod("GET");
				InputStream in = new BufferedInputStream(
						urlConnection.getInputStream());
				str = readStream(in);

			} catch (Exception e) {
				Log.d(TAG, e.getClass().getName());
				if (e.getLocalizedMessage() != null)
					Log.d(TAG, e.getLocalizedMessage());
			} finally {
				if (urlConnection != null)
					urlConnection.disconnect();
			}

			return str;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Update the UI thread with the final result

		}
	}

	// HTTP POST
	public String postToURL(String address, String params) {

		PostToURLTask task = new PostToURLTask();
		String result = new String();

		try {

			task.execute(address, params);
			result = task.get(); // TODO - using get() for now, normally should
									// pickup result
									// using onPostExecute(), problem with get()
									// is that
									// it will look like a blocking call from
									// UI's perspective,
									// it will block the main thread, so we can
									// get away with this
									// temporarily with small data sets.

		} catch (Exception e) {
			Log.d(TAG, e.getClass().getName());
			if (e.getLocalizedMessage() != null)
				Log.d(TAG, e.getLocalizedMessage());
		}
		return result;
		// new PostToURLTask().execute(address, params);
	}

	private class PostToURLTask extends AsyncTask<String, Void, String> {

		protected String doInBackground(String... params) {
			URL url;
			HttpURLConnection urlConnection = null;
			String str = new String();
			
			Log.d(TAG, "PostToURLTask");

			try {

				url = new URL(params[0]);
				urlConnection = (HttpURLConnection) url.openConnection();
				urlConnection.setReadTimeout(1000);
				urlConnection.setConnectTimeout(1500);
				urlConnection.setDoInput(true);
				urlConnection.setDoOutput(true);
				// urlConnection.setChunkedStreamingMode(0);
				urlConnection.setRequestMethod("POST");
				urlConnection.setRequestProperty("Content-Type",
						"application/json");
				urlConnection.setRequestProperty("User-Agent",
						"Mozilla/5.0 ( compatible ) ");
				urlConnection.setRequestProperty("Accept", "*/*");

				OutputStream out = new BufferedOutputStream(
						urlConnection.getOutputStream());
				writeStream(out, params[1]);

				int status = urlConnection.getResponseCode();
				str = String.valueOf(status);

				Log.d(TAG, "POST RESPONSE CODE:" + str);

				return str;

			} catch (Exception e) {
				Log.d(TAG, e.getClass().getName());
				if (e.getLocalizedMessage() != null)
					Log.d(TAG, e.getLocalizedMessage());
			} finally {
				if (urlConnection != null)
					urlConnection.disconnect();
			}

			return str;
		}
	}
	
	// HTTP POST
	public String postGetToURL(String address, String params) {

		PostGetToURLTask task = new PostGetToURLTask();
		String result = new String();

		try {

			task.execute(address, params);
			result = task.get(); // TODO - using get() for now, normally should
									// pickup result
									// using onPostExecute(), problem with get()
									// is that
									// it will look like a blocking call from
									// UI's perspective,
									// it will block the main thread, so we can
									// get away with this
									// temporarily with small data sets.

		} catch (Exception e) {
			Log.d(TAG, e.getClass().getName());
			if (e.getLocalizedMessage() != null)
				Log.d(TAG, e.getLocalizedMessage());
		}
		return result;
		// new PostToURLTask().execute(address, params);
	}

	private class PostGetToURLTask extends AsyncTask<String, Void, String> {

		protected String doInBackground(String... params) {
			URL url;
			HttpURLConnection urlConnection = null;
			String str = new String();

			Log.d(TAG, "PostGetToURLTask");

			try {

				url = new URL(params[0]);
				urlConnection = (HttpURLConnection) url.openConnection();
				urlConnection.setReadTimeout(1000);
				urlConnection.setConnectTimeout(1500);
				urlConnection.setDoInput(true);
				urlConnection.setDoOutput(true);
				// urlConnection.setChunkedStreamingMode(0);
				urlConnection.setRequestMethod("POST");
				urlConnection.setRequestProperty("Content-Type",
						"application/json");
				urlConnection.setRequestProperty("User-Agent",
						"Mozilla/5.0 ( compatible ) ");
				urlConnection.setRequestProperty("Accept", "*/*");

				OutputStream out = new BufferedOutputStream(
						urlConnection.getOutputStream());
				writeStream(out, params[1]);

				int status = urlConnection.getResponseCode();
				str = String.valueOf(status);

				try {

					if (status == 200) {

						InputStream in = new BufferedInputStream(
								urlConnection.getInputStream());
						str = readStream(in);
					}

				} catch (Exception e) {
					Log.d(TAG, e.getClass().getName());
					if (e.getLocalizedMessage() != null)
						Log.d(TAG, e.getLocalizedMessage());
				} finally {
					if (urlConnection != null)
						urlConnection.disconnect();
				}

				Log.d(TAG, "POST RESPONSE CODE:" + status);

				return str;

			} catch (Exception e) {
				Log.d(TAG, e.getClass().getName());
				if (e.getLocalizedMessage() != null)
					Log.d(TAG, e.getLocalizedMessage());
			} finally {
				if (urlConnection != null)
					urlConnection.disconnect();
			}

			return str;
		}
	}
}
