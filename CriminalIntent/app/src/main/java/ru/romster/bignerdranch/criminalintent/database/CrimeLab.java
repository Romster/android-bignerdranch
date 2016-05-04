package ru.romster.bignerdranch.criminalintent.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import ru.romster.bignerdranch.criminalintent.database.CrimeDbSchema.CrimeTable;
import ru.romster.bignerdranch.criminalintent.model.Crime;

/**
 * Created by romster on 09/04/16.
 */
public class CrimeLab {

	private volatile static CrimeLab instance;
	private final Context appContext;
	private SQLiteDatabase database;

	public static CrimeLab getInstance(Context context) {
		if (instance == null) {
			synchronized (CrimeLab.class) {
				if (instance == null) {
					instance = new CrimeLab(context.getApplicationContext());
				}
			}
		}
		return instance;
	}


	public void addCrime(Crime crime) {
		ContentValues cv = getContentValues(crime);
		database.insert(CrimeTable.NAME, null, cv);
	}

	public List<Crime> getCrimeList() {
		List<Crime> crimes = new ArrayList<>();
		CrimeCursorWrapper cursor = queryCrimes(null, null);
		try {
			cursor.moveToFirst();
			while (!cursor.isAfterLast()) {
				crimes.add(cursor.getCrime());
				cursor.moveToNext();
			}
		} finally {
			cursor.close();
		}
		return crimes;
	}

	public Crime getCrime(UUID id) {
		CrimeCursorWrapper cursor = queryCrimes(CrimeTable.Cols.UUID + " = ?",
				new String[]{id.toString()});
		try {
			if (cursor.getCount() == 0) {
				return null;
			}
			cursor.moveToFirst();
			return cursor.getCrime();
		} finally {
			cursor.close();
		}
	}

	public void updateCrime(Crime crime) {
		String uuidString = crime.getId().toString();
		ContentValues cv = getContentValues(crime);

		database.update(CrimeTable.NAME, cv,
				CrimeTable.Cols.UUID + " = ?", new String[]{uuidString});
	}

	private CrimeCursorWrapper queryCrimes(String whereClause, String[] whereArgs) {
		Cursor cursor = database.query(CrimeTable.NAME,
				null, // all columns
				whereClause,
				whereArgs,
				null,//groupBy
				null,//having
				null //orderBy
		);
		return new CrimeCursorWrapper(cursor);
	}

	private static ContentValues getContentValues(Crime crime) {
		ContentValues values = new ContentValues();
		values.put(CrimeTable.Cols.UUID, crime.getId().toString());
		values.put(CrimeTable.Cols.TITLE, crime.getTitle());
		values.put(CrimeTable.Cols.DATE, crime.getDate().getTime());
		values.put(CrimeTable.Cols.SOLVED, crime.isSolved() ? 1 : 0);
		values.put(CrimeTable.Cols.SUSPECT, crime.getSuspect());
		values.put(CrimeTable.Cols.SUSPECT_ID, crime.getSuspectId());

		return values;
	}




	private CrimeLab(Context appContext) {
		this.appContext = appContext;
		this.database = new CrimeBaseHelper(appContext).getWritableDatabase();
	}
}
