package ru.romster.bignerdranch.criminalintent.controller.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import ru.romster.bignerdranch.criminalintent.R;

/**
 * Created by romster on 20/04/16.
 */
public class DatePickerFragment extends DialogFragment {

	public static final String EXTRA_DATE = "ru.romster.bignerdranch.criminalintent.date";

	private static final String ARG_DATE = "date";

	private DatePicker datePicker;

	public static DatePickerFragment newInstance(Date date){
		Bundle args = new Bundle();
		args.putSerializable(ARG_DATE, date);

		DatePickerFragment fragment = new DatePickerFragment();
		fragment.setArguments(args);
		return fragment;
	}


	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater,
	                         @Nullable ViewGroup container,
	                         @Nullable Bundle savedInstanceState) {
		Date date = (Date) getArguments().getSerializable(ARG_DATE);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH);
		final int day = calendar.get(Calendar.DAY_OF_MONTH);

		View v = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_date, null);
		datePicker = (DatePicker) v.findViewById(R.id.dialog_date_date_picker);
		datePicker.init(year, month, day, null);

		Button okButton = (Button) v.findViewById(R.id.dialog_date_ok_button);
		okButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Date resultDate = new GregorianCalendar(datePicker.getYear(),
						datePicker.getMonth(),
						datePicker.getDayOfMonth()).getTime();
				sendResult(Activity.RESULT_OK, resultDate);
				dismiss();
			}
		});
		return v;
	}

	private void sendResult(int resultCode, Date date) {
		Intent intent = new Intent();
		intent.putExtra(EXTRA_DATE, date);

		if (getTargetFragment() != null) { //called as dialog
			getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, intent);
		} else { //called as activity
			getActivity().setResult(Activity.RESULT_OK, intent);
			getActivity().finish();
		}
	}
}
