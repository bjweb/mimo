package com.modules.jules.microcredit;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;

import java.util.ArrayList;

public class CustomListHistoDema extends BaseAdapter{
	private Activity activity;
	private LayoutInflater inflater;
	private ArrayList<Rech_histo_demande> histo;
	ImageLoader imageLoader = AppController.getInstance().getImageLoader();

	public CustomListHistoDema(Activity activity, ArrayList<Rech_histo_demande> histos) {
		this.activity = activity;
		this.histo = histos;
	}

	@Override
	public int getCount() {
		return histo.size();
	}

	@Override
	public Object getItem(int pos) {
		return histo.get(pos);
	}

	@Override
	public long getItemId(int pos) {
		return histo.indexOf(getItem(pos));
	}

	@Override
	public View getView(int pos, View convertView, ViewGroup parent) {

		if (inflater == null)
			inflater = (LayoutInflater) activity
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		if (convertView == null)
			convertView = inflater.inflate(R.layout.list_histo_demande, null);

		TextView montant = (TextView) convertView.findViewById(R.id.lemontant);
		TextView date = (TextView) convertView.findViewById(R.id.ladate);
		TextView code = (TextView) convertView.findViewById(R.id.lecode);

		Rech_histo_demande m = histo.get(pos);

		montant.setText(m.getMontant()+" Fcfa");

        date.setText(m.getDate());

		code.setText(m.getCode());

		return convertView;
	}
}