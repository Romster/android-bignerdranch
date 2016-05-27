package ru.romster.bignerdranch.photogallery;

import android.net.Uri;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import ru.romster.bignerdranch.photogallery.model.GalleryItem;

/**
 * Created by romster on 24/05/16.
 */
public class FlickrFetchr {

	private static final String TAG = FlickrFetchr.class.getName();

	public static final String API_KEY = "ad158930d3f41bb6f26b39eded421e83";

	public List<GalleryItem> fetchItems() {
		List<GalleryItem> galleryItems = new ArrayList<>();

		try {
			String url = Uri.parse("https://api.flickr.com/services/rest/")
					.buildUpon()
					.appendQueryParameter("method", "flickr.photos.getRecent")
					.appendQueryParameter("api_key", API_KEY)
					.appendQueryParameter("format", "json")
					.appendQueryParameter("nojsoncallback", "1")
					.appendQueryParameter("extras", "url_s")
					.build().toString();
			String jsonString =  getUrlString(url);
			JsonElement jsonBody = new JsonParser().parse(jsonString);
			Log.i(TAG, "JSON: " + jsonString);
			parseItems(galleryItems, jsonBody);
		} catch (IOException ex) {
			Log.e(TAG, "Failed to fetch URL: ", ex);
		} catch (JSONException ex) {
			Log.e(TAG, "Failed to parse JSON", ex);
		}
		return galleryItems;
	}

	public byte[] getUrlBytes(String urlString) throws IOException {
		URL url = new URL(urlString);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();

		try {
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			InputStream in = connection.getInputStream();

			if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
				throw new IOException(connection.getResponseMessage()
						+ ": with " + urlString);
			}

			int bytesRead = 0;
			byte[] buffer = new byte[1024];
			while ((bytesRead = in.read(buffer)) > 0) {
				out.write(buffer, 0, bytesRead);
			}
			out.close();
			return  out.toByteArray();
		} finally {
			connection.disconnect();
		}
	}

	public String getUrlString(String urlString) throws IOException {
		return new String(getUrlBytes(urlString));
	}


	private static void parseItems(List<GalleryItem> galleryItems, JsonElement jsonBody) throws JSONException {
		JsonObject photosJsonObject = jsonBody.getAsJsonObject().getAsJsonObject("photos");
		JsonArray photoJsonArray = photosJsonObject.getAsJsonArray("photo");

		Gson gson = new Gson();

		for(int i = 0; i < photoJsonArray.size(); i++) {
			GalleryItem item = gson.fromJson(photoJsonArray.get(i), GalleryItem.class);
			if(item.getUrl() != null) galleryItems.add(item);
		}
	}

}
