package ru.romster.bignerdranch.nerdlauncher;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by romster on 22/05/16.
 */
public class NerdLauncherFragment extends Fragment {

	private static String TAG = "NerdLauncherFragment";

	private RecyclerView recyclerView;

	public static NerdLauncherFragment newInstance() {
		NerdLauncherFragment fragment = new NerdLauncherFragment();
		return fragment;
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_nerd_launcher, container, false);

		recyclerView = (RecyclerView) v.findViewById(R.id.fragment_nerd_launcher_recycler_view);
		recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

		setupAdapter();

		return v;
	}

	private void setupAdapter() {
		Intent startupIntent = new Intent(Intent.ACTION_MAIN);
		startupIntent.addCategory(Intent.CATEGORY_LAUNCHER);

		final PackageManager pm = getActivity().getPackageManager();
		List<ResolveInfo> activities = pm.queryIntentActivities(startupIntent, 0);

		Collections.sort(activities, new Comparator<ResolveInfo>() {
			@Override
			public int compare(ResolveInfo a, ResolveInfo b) {
				return String.CASE_INSENSITIVE_ORDER.compare(a.loadLabel(pm).toString(),
						b.loadLabel(pm).toString());
			}
		});

		recyclerView.setAdapter(new ActivityAdapter(activities));
	}

	private class ActivityHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

		private ResolveInfo resolveInfo;
		private TextView nameTextView;
		private ImageView iconImageView;

		public ActivityHolder(View itemView) {
			super(itemView);
			nameTextView = (TextView) itemView.findViewById(R.id.activity_name);
			iconImageView = (ImageView) itemView.findViewById(R.id.activity_icon);
			itemView.setOnClickListener(this);
		}

		public void bindActivity(ResolveInfo resolveInfo) {
			this.resolveInfo = resolveInfo;
			PackageManager pm = getActivity().getPackageManager();

			String appName = resolveInfo.loadLabel(pm).toString();
			nameTextView.setText(appName);

			Drawable icon = resolveInfo.loadIcon(pm);
			iconImageView.setImageDrawable(icon);

		}

		@Override
		public void onClick(View v) {
			ActivityInfo activityInfo = resolveInfo.activityInfo;
			Intent i = new Intent(Intent.ACTION_MAIN)
					.setClassName(activityInfo.applicationInfo.packageName,
							activityInfo.name).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(i);
		}
	}

	private class ActivityAdapter extends RecyclerView.Adapter<ActivityHolder> {
		private final List<ResolveInfo> activities;

		public ActivityAdapter(List<ResolveInfo> activities) {
			this.activities = activities;
		}

		@Override
		public ActivityHolder onCreateViewHolder(ViewGroup parent, int viewType) {
			LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
			View v = layoutInflater.inflate(R.layout.list_item_activity, parent, false);
			return new ActivityHolder(v);
		}

		@Override
		public void onBindViewHolder(ActivityHolder holder, int position) {
			ResolveInfo resolveInfo = activities.get(position);
			holder.bindActivity(resolveInfo);
		}

		@Override
		public int getItemCount() {
			return activities.size();
		}
	}
}
