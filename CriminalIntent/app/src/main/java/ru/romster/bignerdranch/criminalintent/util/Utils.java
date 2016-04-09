package ru.romster.bignerdranch.criminalintent.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by romster on 09/04/16.
 */
public class Utils {

	public static String convertDateToString(Date date) {
		DateFormat df = new SimpleDateFormat("EEEE, MMM d, yyyy");
		return df.format(date);

	}
}
