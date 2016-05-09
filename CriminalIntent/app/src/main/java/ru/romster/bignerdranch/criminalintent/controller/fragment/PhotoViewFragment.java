package ru.romster.bignerdranch.criminalintent.controller.fragment;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import java.io.File;

import ru.romster.bignerdranch.criminalintent.R;
import ru.romster.bignerdranch.criminalintent.util.PictureUtils;

/**
 * Created by romster on 09/05/16.
 */
public class PhotoViewFragment extends DialogFragment {

	private static final String ARG_PHOTO_FILE = "photo_file";

	private static ImageView photoView;

	public static PhotoViewFragment newInstance(String photoFile) {

		Bundle args = new Bundle();
		args.putSerializable(ARG_PHOTO_FILE, photoFile);

		PhotoViewFragment fragment = new PhotoViewFragment();
		fragment.setArguments(args);
		return fragment;
	}

	@NonNull
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		String photoFilePath = getArguments().getString(ARG_PHOTO_FILE);

		View v = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_photo_view, null);
		photoView = (ImageView) v.findViewById(R.id.dialog_photo_image_view);
		photoView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dismiss();
			}
		});
		File f = new File(photoFilePath);
		if(f.exists()) {
			Bitmap bitmap = PictureUtils.getScaledBitmap(f.getPath(), getActivity());
			photoView.setImageBitmap(bitmap);
		}

		return new AlertDialog.Builder(getActivity())
				.setView(v)
				.create();
	}
}
