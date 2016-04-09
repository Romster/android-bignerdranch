package ru.romster.bignerdranch.criminalintent.controller.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import ru.romster.bignerdranch.criminalintent.R;
import ru.romster.bignerdranch.criminalintent.controller.CrimeLab;
import ru.romster.bignerdranch.criminalintent.model.Crime;
import ru.romster.bignerdranch.criminalintent.util.Utils;

/**
 * Created by romster on 09/04/16.
 */
public class CrimeListFragment extends Fragment {
	private RecyclerView recyclerView;
	private CrimeAdapter adapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_crime_list, container, false);
		recyclerView = (RecyclerView) v.findViewById(R.id.crime_recycle_view);
		recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
		updateUI();
		return v;
	}

	private void updateUI(){
		CrimeLab crimeLab = CrimeLab.getInstance(getActivity());
		List<Crime> crimes = crimeLab.getCrimeList();
		adapter = new CrimeAdapter(crimes);
		recyclerView .setAdapter(adapter);
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
			Toast.makeText(getActivity(), crime.getTitle() + " clicked!", Toast.LENGTH_SHORT).show();
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


	}



}
