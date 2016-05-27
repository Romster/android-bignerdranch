package ru.romster.bignerdranch.photogallery;

import android.net.Uri;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
			JSONObject jsonObject = new JSONObject(jsonString);
			parseItems(galleryItems, jsonObject);
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

	private static void parseItems(List<GalleryItem> galleryItems, JSONObject jsonBody) throws JSONException {
		JSONObject photosJsonObject = jsonBody.getJSONObject("photos");
		JSONArray photoJsonArray = photosJsonObject.getJSONArray("photo");

		for(int i = 0; i < photoJsonArray.length(); i++) {
			JSONObject photoJsonObject = photoJsonArray.getJSONObject(i);

			String smallPhotoUrl = photoJsonObject.optString("url_s", null);
			if(smallPhotoUrl == null)  continue;

			GalleryItem galleryItem = new GalleryItem();

			galleryItem.setId(photoJsonObject.getString("id"));
			galleryItem.setCaption(photoJsonObject.getString("title"));
			galleryItem.setUrl(smallPhotoUrl);

			galleryItems.add(galleryItem);
		}
	}

}
