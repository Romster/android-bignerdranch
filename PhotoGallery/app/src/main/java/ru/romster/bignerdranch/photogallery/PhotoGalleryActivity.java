package ru.romster.bignerdranch.photogallery;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

public class PhotoGalleryActivity extends SingleFragmentActivity {


	@Override
	protected Fragment createFragment() {
		return PhotoGalleryFragment.newInstance();
	}

}
