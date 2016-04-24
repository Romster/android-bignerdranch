package ru.romster.bignerdranch.criminalintent.model;

import java.util.Date;
import java.util.UUID;

/**
 * Created by romster on 06/04/16.
 */
public class Crime {
	private UUID id;
	private String title;
	private Date date;
	private boolean solved;

	public Crime() {
		this(UUID.randomUUID());
	}

	public Crime(UUID uuid) {
		id = uuid;
		date = new Date();
	}

	public UUID getId() {
		return id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public boolean isSolved() {
		return solved;
	}

	public void setSolved(boolean solved) {
		this.solved = solved;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Crime crime = (Crime) o;

		if (solved != crime.solved) return false;
		if (id != null ? !id.equals(crime.id) : crime.id != null) return false;
		if (title != null ? !title.equals(crime.title) : crime.title != null) return false;
		return date != null ? date.equals(crime.date) : crime.date == null;

	}

	@Override
	public int hashCode() {
		int result = id != null ? id.hashCode() : 0;
		result = 31 * result + (title != null ? title.hashCode() : 0);
		result = 31 * result + (date != null ? date.hashCode() : 0);
		result = 31 * result + (solved ? 1 : 0);
		return result;
	}
}