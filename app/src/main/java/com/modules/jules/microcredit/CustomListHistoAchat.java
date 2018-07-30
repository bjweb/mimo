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

public class CustomListHistoAchat extends BaseAdapter{
	private Activity activity;
	private LayoutInflater inflater;
	private ArrayList<Rech_histo_demande> histo;
	ImageLoader imageLoader = AppController.getInstance().getImageLoader();

	public CustomListHistoAchat(Activity activity, ArrayList<Rech_histo_demande> histos) {
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

		montant.setText("Montant : "+m.getMontant()+" Fcfa\nTaux : "+m.getTaux()+"%\nInterêt : "+m.getInteret()+" Fcfa\nFrais : "+m.getFrais()+" Fcfa" +
				"\nTOTAL : "+m.getTotal());

        int j = Integer.parseInt(m.getJours());

        String dt = m.getDate();
        SimpleDateFormat ajj = new SimpleDateFormat("dd-MM-yyyy");
        Calendar c = Calendar.getInstance();
        try {
            c.setTime(ajj.parse(dt));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        c.add(Calendar.DATE, j);
        SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MM-yyyy");
        String output = sdf1.format(c.getTime());

        DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        String premier = df.format(Calendar.getInstance().getTime());

        date.setText("acheté le : "+premier+"\nà rembourser le : "+output);
        //////////////////////

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        Date date1 = null;
        Date date2 = null;
        try {
            date1 = sdf.parse(premier);
            date2 = sdf.parse(output);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        long diff = date2.getTime() - date1.getTime();
        String jours = String.valueOf(TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS));

        int jj = Integer.parseInt(jours);

        if (jj > 0)
        {
            code.setText(jours+" jour(s) restant");
        }else if (jj < 0){
            code.setText(jj+" jour(s) de retard");
        }else if (jj == 0){
            code.setText("Aujourdhui");
        }



        return convertView;
	}
}