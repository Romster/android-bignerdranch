package ru.romster.bignerdranch.photogallery;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ru.romster.bignerdranch.photogallery.model.GalleryItem;

/**
 * Created by romster on 23/05/16.
 */
public class PhotoGalleryFragment extends Fragment {

	private static final String TAG = PhotoGalleryFragment.class.getName();


	private RecyclerView photoRecyclerView;
	private List<GalleryItem> galleryItems = new ArrayList<>();

	public static PhotoGalleryFragment newInstance() {

		Bundle args = new Bundle();

		PhotoGalleryFragment fragment = new PhotoGalleryFragment();
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
		new FetchItemsTask().execute();
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_photo_gallery, container, false);

		photoRecyclerView = (RecyclerView) v.findViewById(R.id.photo_gallery_recycler_view);
		photoRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));

		return v;
	}

	private void setupAdapter() {
		if(isAdded()) {
			photoRecyclerView.setAdapter(new PhotoAdapter(galleryItems));
		}
	}



	private class PhotoHolder extends RecyclerView.ViewHolder {
		private TextView titleTextView;

		public PhotoHolder(View itemView) {
			super(itemView);
			this.titleTextView = (TextView) itemView;
		}

		public void bindGalleryItem(GalleryItem item) {
			titleTextView.setText(item.toString());
		}
	}


	private class PhotoAdapter extends RecyclerView.Adapter<PhotoHolder> {
		private List<GalleryItem> galleryItems;

		public PhotoAdapter(List<GalleryItem> galleryItems) {
			this.galleryItems = galleryItems;
		}

		@Override
		public PhotoHolder onCreateViewHolder(ViewGroup parent, int viewType) {
			TextView tv = new TextView(getActivity());
			return new PhotoHolder(tv);
		}

		@Override
		public void onBindViewHolder(PhotoHolder holder, int position) {
			GalleryItem galleryItem = galleryItems.get(position);
			holder.bindGalleryItem(galleryItem);
		}

		@Override
		public int getItemCount() {
			return galleryItems.size();
		}
	}

	private class FetchItemsTask extends AsyncTask<Void, Void, List<GalleryItem>> {
		@Override
		protected List<GalleryItem> doInBackground(Void... params) {
			return new FlickrFetchr().fetchItems();
		}

		@Override
		protected void onPostExecute(List<GalleryItem> galleryItems) {
			PhotoGalleryFragment.this.galleryItems = galleryItems;
			setupAdapter();
		}
	}
}
