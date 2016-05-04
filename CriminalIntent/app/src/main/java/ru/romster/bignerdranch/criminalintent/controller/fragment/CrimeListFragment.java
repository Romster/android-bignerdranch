package ru.romster.bignerdranch.criminalintent.controller.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ru.romster.bignerdranch.criminalintent.R;
import ru.romster.bignerdranch.criminalintent.database.CrimeLab;
import ru.romster.bignerdranch.criminalintent.controller.activity.CrimePagerActivity;
import ru.romster.bignerdranch.criminalintent.model.Crime;
import ru.romster.bignerdranch.criminalintent.util.Utils;

/**
 * Created by romster on 09/04/16.
 */
public class CrimeListFragment extends Fragment {

	private static final int REQUEST_CRIME_EDIT = 1;
	private static final String SAVED_SUBTITLE_VISIBLE = "subtitle";

	private RecyclerView recyclerView;
	private CrimeAdapter adapter;
	private boolean subtitleVisible;

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_crime_list, container, false);
		recyclerView = (RecyclerView) v.findViewById(R.id.crime_recycle_view);
		recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
		updateUI();
		if(savedInstanceState != null) {
			subtitleVisible = savedInstanceState.getBoolean(SAVED_SUBTITLE_VISIBLE);
		}
		return v;
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putBoolean(SAVED_SUBTITLE_VISIBLE, subtitleVisible);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode == REQUEST_CRIME_EDIT) {
				adapter.notifyDataSetChanged();
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		updateUI();
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.fragment_crime_list, menu);
		MenuItem subtitleMenuItem = menu.findItem(R.id.menu_item_show_subtitle);
		subtitleMenuItem.setTitle(subtitleVisible? R.string.hide_subtitle : R.string.show_subtitle);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.menu_item_new_crime: {
				Crime crime = new Crime();
				CrimeLab.getInstance(getActivity()).addCrime(crime);
				Intent intent = CrimePagerActivity.newIntent(getActivity(), crime.getId());
				startActivity(intent);
				return true;
			}
			case R.id.menu_item_show_subtitle: {
				subtitleVisible = !subtitleVisible;
				getActivity().invalidateOptionsMenu();
				updateSubtitile();
				return true;
			}
			default: {
				return super.onOptionsItemSelected(item);
			}
		}
	}

	private void updateUI() {
		CrimeLab crimeLab = getCrimeLab();
		List<Crime> crimes = crimeLab.getCrimeList();
		if (adapter == null) {
			adapter = new CrimeAdapter(crimes);
			recyclerView.setAdapter(adapter);
		} else {
			adapter.setCrimes(crimes);
			adapter.notifyDataSetChanged();
		}
		updateSubtitile();
	}

	private void updateSubtitile() {
		AppCompatActivity activity = (AppCompatActivity) getActivity();
		if(subtitleVisible) {
			CrimeLab cl = CrimeLab.getInstance(getActivity());
			int crimeCount = cl.getCrimeList().size();

			String subtitle = getString(R.string.subtitle_format, crimeCount);
			activity.getSupportActionBar().setSubtitle(subtitle);
		} else {
			activity.getSupportActionBar().setSubtitle(null);
		}
	}

	private CrimeLab getCrimeLab() {
		return CrimeLab.getInstance(getContext());
	}

	private class CrimeHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

		public TextView titleTextView;
		public TextView dateTextView;
		public CheckBox solvedCheckBox;

		private Crime crime;

		public CrimeHolder(View itemView) {
			super(itemView);
			itemView.setOnClickListener(this);
			titleTextView = (TextView) itemView.findViewById(R.id.list_item_crime_title_text_view);
			dateTextView = (TextView) itemView.findViewById(R.id.list_item_crime_date_text_view);
			solvedCheckBox = (CheckBox) itemView.findViewById(R.id.list_item_solved_check_box);
		}

		private void bindCrime(Crime c) {
			this.crime = c;
			titleTextView.setText(c.getTitle());
			dateTextView.setText(Utils.convertDateToString(c.getDate()));
			solvedCheckBox.setChecked(c.isSolved());
		}

		@Override
		public void onClick(View v) {
			Intent intent = CrimePagerActivity.newIntent(getContext(), crime.getId());
			startActivityForResult(intent, REQUEST_CRIME_EDIT);
		}
	}

	private class CrimeAdapter extends RecyclerView.Adapter<CrimeHolder> {
		private List<Crime> crimeList;

		public CrimeAdapter(List<Crime> crimeList) {
			this.crimeList = crimeList;
		}

		@Override
		public CrimeHolder onCreateViewHolder(ViewGroup parent, int viewType) {
			LayoutInflater inflater = LayoutInflater.from(getActivity());
			View v = inflater.inflate(R.layout.list_item_crime, parent, false);
			return new CrimeHolder(v);
		}

		@Override
		public void onBindViewHolder(CrimeHolder holder, int position) {
			Crime c = crimeList.get(position);
			holder.bindCrime(c);
		}

		@Override
		public int getItemCount() {
			return crimeList.size();
		}

		private void setCrimes(List<Crime> crimes) {
			this.crimeList = new ArrayList<>(crimes);
		}


	}


}
