package ru.romster.bignerdranch.criminalintent.controller.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import ru.romster.bignerdranch.criminalintent.R;
import ru.romster.bignerdranch.criminalintent.controller.CrimeLab;
import ru.romster.bignerdranch.criminalintent.model.Crime;
import ru.romster.bignerdranch.criminalintent.util.TextChangeListener;
import ru.romster.bignerdranch.criminalintent.util.Utils;

/**
 * Created by romster on 07/04/16.
 */
public class CrimeFragment extends Fragment {

	private static final String ARG_CRIME_ID = "crime_id";
	private static final String DIALOG_DATE = "DialogDate";
	private static final String DIALOG_TIME = "DialogTime";

	private static final int REQUEST_DATE = 0;
	private static final int REQUEST_TIME = 1;

	private EditText titleField;
	private Button dateButton;
	private Button timeButton;
	private CheckBox solvedCheckBox;

	private Crime crime;

	public static CrimeFragment newInstance(UUID crimeId) {
		Bundle args = new Bundle();
		args.putSerializable(ARG_CRIME_ID, crimeId);

		CrimeFragment fragment = new CrimeFragment();
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		UUID crimeId = (UUID) getArguments().getSerializable(ARG_CRIME_ID);
		crime = CrimeLab.getInstance(getContext()).getCrime(crimeId);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_crime, container, false);

		titleField = (EditText) v.findViewById(R.id.crime_title);
		titleField.setText(crime.getTitle());
		titleField.addTextChangedListener(new TextChangeListener() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				crime.setTitle(s.toString());
			}
		});

		dateButton = (Button) v.findViewById(R.id.crime_date);
		dateButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				FragmentManager fragmentManager = getFragmentManager();
				DatePickerFragment dialog = DatePickerFragment.newInstance(crime.getDate());
				dialog.setTargetFragment(CrimeFragment.this, REQUEST_DATE);
				dialog.show(fragmentManager, DIALOG_DATE);
			}
		});
		updateDate();

		timeButton = (Button) v.findViewById(R.id.crime_time);
		timeButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				FragmentManager fragmentManager = getFragmentManager();
				TimePickerFragment dialog = TimePickerFragment.newInstance(crime.getDate());
				dialog.setTargetFragment(CrimeFragment.this, REQUEST_TIME);
				dialog.show(fragmentManager, DIALOG_TIME);
			}
		});
		updateTime();

		solvedCheckBox = (CheckBox) v.findViewById(R.id.crime_solved);
		solvedCheckBox.setChecked(crime.isSolved());
		solvedCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				crime.setSolved(isChecked);
			}
		});

		return v;

	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(resultCode != Activity.RESULT_OK) return;

		switch (requestCode) {
			case REQUEST_DATE: {
				Date date = (Date) data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
				crime.setDate(date);
				updateDate();
				break;
			}case REQUEST_TIME: {
				int hour = data.getIntExtra(TimePickerFragment.EXTRA_TIME_HOUR, -1);
				int minute = data.getIntExtra(TimePickerFragment.EXTRA_TIME_MINUTE, -1);
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(calendar.getTime());
				calendar.set(Calendar.HOUR_OF_DAY, hour);
				calendar.set(Calendar.MINUTE, minute);
				crime.setDate(calendar.getTime());
				updateTime();
				break;
			}
		}
	}

	private void updateDate() {
		dateButton.setText(Utils.convertDateToString(crime.getDate()));
	}
	private void updateTime() {
		timeButton.setText(Utils.convertTimeToString(crime.getDate()));
	}

}
