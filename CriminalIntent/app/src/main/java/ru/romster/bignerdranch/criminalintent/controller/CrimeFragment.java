package ru.romster.bignerdranch.criminalintent.controller;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import ru.romster.bignerdranch.criminalintent.R;
import ru.romster.bignerdranch.criminalintent.model.Crime;
import ru.romster.bignerdranch.criminalintent.util.TextChangeListener;
import ru.romster.bignerdranch.criminalintent.util.Utils;

/**
 * Created by romster on 07/04/16.
 */
public class CrimeFragment extends Fragment {


	private EditText titleField;
	private Button dateButton;
	private CheckBox solvedCheckBox;

	private Crime crime;

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.crime = new Crime();
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_crime, container, false);

		titleField = (EditText) v.findViewById(R.id.crime_title);
		titleField.addTextChangedListener(new TextChangeListener() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				crime.setTitle(s.toString());
			}
		});

		dateButton = (Button) v.findViewById(R.id.crime_date);
		dateButton.setText(Utils.convertDateToString(crime.getDate()));
		dateButton.setEnabled(false);

		solvedCheckBox = (CheckBox) v.findViewById(R.id.crime_solved);
		solvedCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				crime.setSolved(isChecked);
			}
		});

		return v;

	}


}
