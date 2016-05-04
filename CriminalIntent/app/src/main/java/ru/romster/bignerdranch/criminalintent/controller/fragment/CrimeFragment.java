package ru.romster.bignerdranch.criminalintent.controller.fragment;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.ShareCompat;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Date;
import java.util.UUID;

import ru.romster.bignerdranch.criminalintent.R;
import ru.romster.bignerdranch.criminalintent.database.CrimeLab;
import ru.romster.bignerdranch.criminalintent.model.Crime;
import ru.romster.bignerdranch.criminalintent.util.TextChangeListener;
import ru.romster.bignerdranch.criminalintent.util.Utils;

/**
 * Created by romster on 07/04/16.
 */
public class CrimeFragment extends Fragment {

	private static final String ARG_CRIME_ID = "crime_id";
	private static final String DIALOG_DATE = "DialogDate";

	private static final int REQUEST_DATE = 0;
	private static final int REQUEST_CONTACT = 1;

	private EditText titleField;
	private Button dateButton;
	private Button reportButton;
	private Button suspectButton;
	private Button callButton;
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
		updateDate();

		dateButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				FragmentManager fragmentManager = getFragmentManager();
				DatePickerFragment dialog = DatePickerFragment.newInstance(crime.getDate());
				dialog.setTargetFragment(CrimeFragment.this, REQUEST_DATE);
				dialog.show(fragmentManager, DIALOG_DATE);
			}
		});

		reportButton = (Button) v.findViewById(R.id.crime_report);
		reportButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				ShareCompat.IntentBuilder iBulder = ShareCompat.IntentBuilder.from(getActivity());
				iBulder.setType("text/plain")
						.setText(getCrimeReport())
						.setSubject(getString(R.string.crime_report_subject))
						.setChooserTitle(R.string.send_report)
						.startChooser();
			}
		});

		final Intent pickIntent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);

		suspectButton = (Button) v.findViewById(R.id.crime_suspect);
		suspectButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivityForResult(pickIntent, REQUEST_CONTACT);
			}
		});
		if(crime.getSuspect() != null) {
			suspectButton.setText(crime.getSuspect());
		}

		callButton = (Button) v.findViewById(R.id.crime_call);
		callButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Cursor c = getActivity().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
						new String[] {ContactsContract.CommonDataKinds.Phone.NUMBER},
						ContactsContract.CommonDataKinds.Phone.CONTACT_ID+"='"+crime.getSuspectId()+"'",
						null, null);
				try {
					if (c.getCount() == 0) {
						Toast.makeText(getActivity(), getString(R.string.no_phone_error, crime.getSuspect()), Toast.LENGTH_SHORT).show();
						return;
					}
					c.moveToFirst();
					String phone = c.getString(0);
					Uri phoneUri = Uri.parse("tel:"+phone);
					Intent intent = new Intent(Intent.ACTION_DIAL, phoneUri);
					startActivity(intent);
				} finally {
					c.close();
				}
			}
		});
		if(crime.getSuspectId() !=  null) {
			callButton.setText(getString(R.string.crime_call, crime.getSuspect()));
		}

		solvedCheckBox = (CheckBox) v.findViewById(R.id.crime_solved);
		solvedCheckBox.setChecked(crime.isSolved());
		solvedCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				crime.setSolved(isChecked);
			}
		});

		PackageManager packageManager = getActivity().getPackageManager();
		if (packageManager.resolveActivity(pickIntent, PackageManager.MATCH_DEFAULT_ONLY) == null) {
			suspectButton.setEnabled(false);
		}
		if(crime.getSuspectId() == null) {
			callButton.setVisibility(View.GONE);
		}

		return v;

	}

	@Override
	public void onPause() {
		super.onPause();
		CrimeLab.getInstance(getActivity()).updateCrime(crime);
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
			}
			case REQUEST_CONTACT: {
				if(data != null) {
					Uri contactUri = data.getData();

					String[] queryFields = new String[]{ContactsContract.Contacts.DISPLAY_NAME, ContactsContract.Contacts._ID};
					Cursor  c = getActivity().getContentResolver().query(contactUri, queryFields, null, null, null);
					try {
						if (c.getCount() == 0) {
							return;
						}
						c.moveToFirst();
						String suspect = c.getString(0);
						crime.setSuspect(suspect);
						crime.setSuspectId(c.getInt(1));
						suspectButton.setText(suspect);
						callButton.setVisibility(View.VISIBLE);
						callButton.setText(getString(R.string.crime_call, suspect));

					} finally {
						c.close();
					}
				}
				break;
			}
		}

	}

	private void updateDate() {
		dateButton.setText(Utils.convertDateToString(crime.getDate()));
	}

	private String getCrimeReport() {
		String solvedString;
		if (crime.isSolved()) {
			solvedString = getString(R.string.crime_report_solved);
		} else {
			solvedString = getString(R.string.crime_report_not_solved);
		}

		String dateFormat = "EEE, MMM dd";
		String dateString = DateFormat.format(dateFormat, crime.getDate()).toString();

		String suspect = crime.getSuspect();
		if (suspect == null) {
			suspect = getString(R.string.crime_report_no_suspect);
		} else {
			suspect = getString(R.string.crime_report_suspect, suspect);
		}

		String report = getString(R.string.crime_report, crime.getTitle(), dateString, solvedString, suspect);

		return report;
	}

}
