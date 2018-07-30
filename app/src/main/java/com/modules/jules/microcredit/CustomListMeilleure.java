package com.modules.jules.microcredit;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class CustomListMeilleure extends BaseAdapter{
	private Activity activity;
	private LayoutInflater inflater;
	private ArrayList<Rech_histo_demande> histo;
	ImageLoader imageLoader = AppController.getInstance().getImageLoader();

	public CustomListMeilleure(Activity activity, ArrayList<Rech_histo_demande> histos) {
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
		TextView code = (TextView) convertView.findViewById(R.id.taux);

		Rech_histo_demande m = histo.get(pos);

		montant.setText("Montant : "+m.getMontant()+" Fcfa\n"+"Taux : "+m.getTaux()+"%\n"+"Echeancier : "+m.getJours()+"Jour(s)");


        date.setText("à : "+m.getDate());
        //////////////////////


        return convertView;
	}
}