package ru.romster.bignerdranch.beatbox;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.List;

/**
 * Created by romster on 12/05/16.
 */
public class BeatBoxFragment extends Fragment {

	private BeatBox beatBox;

	public static BeatBoxFragment newInstance() {

		Bundle args = new Bundle();

		BeatBoxFragment fragment = new BeatBoxFragment();
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_beat_box, container, false);

		RecyclerView recyclerView = (RecyclerView) v.findViewById(R.id.fragment_beat_box_recycler_view);
		recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));

		beatBox = new BeatBox(getActivity());
		recyclerView.setAdapter(new SoundAdapter(beatBox.getSounds()));
		return v;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		beatBox.release();
	}

	private class SoundHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
		private Button button;
		private Sound sound;

		public SoundHolder(LayoutInflater layoutInflater, ViewGroup container) {
			super(layoutInflater.inflate(R.layout.list_item_sound, container, false));
			button = (Button) itemView.findViewById(R.id.list_item_sound_button);
			button.setOnClickListener(this);
		}

		public void bindSound(Sound sound) {
			this.sound = sound;
			this.button.setText(sound.getName());
		}

		@Override
		public void onClick(View v) {
			beatBox.play(sound);
		}
	}


	private class SoundAdapter extends RecyclerView.Adapter<SoundHolder> {

		private List<Sound> sounds;

		public SoundAdapter(List<Sound> sounds) {
			this.sounds = sounds;
		}

		@Override
		public SoundHolder onCreateViewHolder(ViewGroup parent, int viewType) {
			LayoutInflater inflater = LayoutInflater.from(getActivity());
			return new SoundHolder(inflater, parent);
		}

		@Override
		public void onBindViewHolder(SoundHolder holder, int position) {
			holder.bindSound(sounds.get(position));
		}

		@Override
		public int getItemCount() {
			return sounds.size();
		}
	}
}
