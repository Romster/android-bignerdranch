package ru.romster.bignerdranch.criminalintent.controller.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.Date;

import ru.romster.bignerdranch.criminalintent.R;

/**
 * Created by romster on 21/04/16.
 */
public class TimePickerFragment extends DialogFragment {
	public static final String EXTRA_TIME_HOUR = "ru.romster.bignerdranch.criminalintent.time_hour";
	public static final String EXTRA_TIME_MINUTE = "ru.romster.bignerdranch.criminalintent.time_minute";

	private static final String ARG_TIMESTAMP = "timestamp";

	private TimePicker timePicker;

	public static TimePickerFragment newInstance(Date timestamp) {
		Bundle args = new Bundle();
		args.putSerializable(ARG_TIMESTAMP, timestamp);

		TimePickerFragment fragment = new TimePickerFragment();
		fragment.setArguments(args);
		return fragment;
	}


	@NonNull
	@Override
	@SuppressWarnings("deprecation")
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		Date date = (Date) getArguments().getSerializable(ARG_TIMESTAMP);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		int minute = calendar.get(Calendar.MINUTE);

		View v = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_time, null);
		timePicker = (TimePicker) v.findViewById(R.id.dialog_time_time_picker);
		timePicker.setIs24HourView(true);
		timePicker.setCurrentHour(hour);
		timePicker.setCurrentMinute(minute);

		return new AlertDialog.Builder(getActivity())
				.setView(v)
				.setTitle(R.string.date_picker_title)
				.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						sendResult(Activity.RESULT_OK, timePicker.getCurrentHour(), timePicker.getCurrentMinute());
					}
				})
				.create();
	}

	private void sendResult(int resultCode, int hour, int minute) {
		if (getTargetFragment() == null) return;

		Intent intent = new Intent();
		intent.putExtra(EXTRA_TIME_HOUR, hour);
		intent.putExtra(EXTRA_TIME_MINUTE, minute);

		getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, intent);
	}
}
