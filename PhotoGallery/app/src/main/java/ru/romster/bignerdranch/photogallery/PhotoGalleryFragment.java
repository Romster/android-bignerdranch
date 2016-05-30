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
import android.view.ViewTreeObserver;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import ru.romster.bignerdranch.photogallery.model.GalleryItem;

/**
 * Created by romster on 23/05/16.
 */
public class PhotoGalleryFragment extends Fragment {

	private static final String TAG = PhotoGalleryFragment.class.getName();

	private boolean wasScrolledDown = false;
	private boolean ignoreScrollEvents = false;

	private RecyclerView photoRecyclerView;
	private List<GalleryItem> galleryItems = new ArrayList<>();

	private FlickrFetchr flickrFetchr;

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
		ignoreScrollEvents = false;
		wasScrolledDown = false;
		flickrFetchr = new FlickrFetchr();
		new FetchItemsTask().execute();
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_photo_gallery, container, false);

		photoRecyclerView = (RecyclerView) v.findViewById(R.id.photo_gallery_recycler_view);

		photoRecyclerView.addOnScrollListener(new OnScrollListener());
		photoRecyclerView.setAdapter(new PhotoAdapter());

		photoRecyclerView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
			@Override
			public void onGlobalLayout() {
				photoRecyclerView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
				int columnsCount = photoRecyclerView.getWidth() / 500;
				photoRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), columnsCount));
			}
		});

		return v;
	}

	private void setupAdapter() {
		if (isAdded()) {
			if (photoRecyclerView.getAdapter() == null)
				photoRecyclerView.setAdapter(new PhotoAdapter());
			else
				photoRecyclerView.getAdapter().notifyDataSetChanged();
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


		@Override
		public PhotoHolder onCreateViewHolder(ViewGroup parent, int viewType) {
			TextView tv = new TextView(getActivity());
			return new PhotoHolder(tv);
		}

		@Override
		public void onBindViewHolder(PhotoHolder holder, int position) {
			GalleryItem galleryItem = galleryItems.get(position);
			holder.bindGalleryItem(galleryItem);
			if (position >= galleryItems.size() * 5 / 6 && wasScrolledDown && !ignoreScrollEvents) {
				ignoreScrollEvents = true;
				new FetchItemsTask().execute();
			}
		}

		@Override
		public int getItemCount() {
			return galleryItems.size();
		}
	}

	private class FetchItemsTask extends AsyncTask<Void, Void, List<GalleryItem>> {

		@Override
		protected void onPreExecute() {
		}

		@Override
		protected List<GalleryItem> doInBackground(Void... params) {
			return flickrFetchr.fetchItems(flickrFetchr.getCurrentPage() + 1);
		}

		@Override
		protected void onPostExecute(List<GalleryItem> galleryItems) {
			PhotoGalleryFragment.this.galleryItems.addAll(galleryItems);
			setupAdapter();
			Toast.makeText(getActivity(), "Page: " + flickrFetchr.getCurrentPage(), Toast.LENGTH_SHORT).show();
			ignoreScrollEvents = false;
			wasScrolledDown = false;
		}
	}

	private class OnScrollListener extends RecyclerView.OnScrollListener {

		@Override
		public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
			//nothing
		}

		@Override
		public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
			super.onScrolled(recyclerView, dx, dy);
			wasScrolledDown = !ignoreScrollEvents && dy > 0;
		}
	}
}
