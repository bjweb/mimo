package com.modules.jules.microcredit;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class CustomListOffre extends BaseAdapter{
	private Activity activity;
	private LayoutInflater inflater;
	private ArrayList<Model_Offre> offres;
	ImageLoader imageLoader = AppController.getInstance().getImageLoader();

	public CustomListOffre(Activity activity, ArrayList<Model_Offre> offres) {
		this.activity = activity;
		this.offres = offres;
	}

	@Override
	public int getCount() {
		return offres.size();
	}

	@Override
	public Object getItem(int pos) {
		return offres.get(pos);
	}

	@Override
	public long getItemId(int pos) {
		return offres.indexOf(getItem(pos));
	}

	@Override
	public View getView(int pos, View convertView, ViewGroup parent) {

		if (inflater == null)
			inflater = (LayoutInflater) activity
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		if (convertView == null)
			convertView = inflater.inflate(R.layout.list_offre, null);

		if (imageLoader == null)
			imageLoader = AppController.getInstance().getImageLoader();
		ImageView thumbNail = (ImageView) convertView
				.findViewById(R.id.photo);
		TextView contact = (TextView) convertView.findViewById(R.id.contact);
		TextView montant = (TextView) convertView.findViewById(R.id.montant2);
		TextView taux = (TextView) convertView.findViewById(R.id.taux);
		TextView email = (TextView) convertView.findViewById(R.id.email);
		TextView date = (TextView) convertView.findViewById(R.id.date);

		Model_Offre m = offres.get(pos);

        Glide.with(activity).load(m.getPhoto()).into(thumbNail);

		contact.setText(m.getContact());

        taux.setText(m.getTaux()+" %");

		email.setText(m.getEmail());

		montant.setText(m.getMontant()+" Fcfa");

		date.setText(m.getDate());

		return convertView;
	}
}