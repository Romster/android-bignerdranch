package ru.romster.bignerdranch.beatbox;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;


/**
 * Created by romster on 09/04/16.
 */
public abstract class SingleFragmentActivity extends AppCompatActivity {

	protected abstract Fragment createFragment();

	protected int getLayoutResId() {
		return R.layout.activity_fragment;
	}

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(getLayoutResId());

		FragmentManager fragmentManager = getSupportFragmentManager();
		Fragment fragment = fragmentManager.findFragmentById(R.id.fragment_container);

		if(fragment == null) {
			fragment = createFragment();
			fragmentManager.beginTransaction()
					.add(R.id.fragment_container, fragment)
					.commit();
		}
	}
}
