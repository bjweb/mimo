package com.modules.jules.microcredit;

public class Rech_histo_demande {
	private String montant, date, code, interet, taux, total, frais, jours;

	public Rech_histo_demande() {
	}

	public Rech_histo_demande(String montant, String date, String code, String interet, String taux, String total, String frais, String jours) {
		this.montant = montant;
		this.date = date;
		this.code = code;
		this.interet = interet;
		this.taux = taux;
		this.total = total;
		this.frais = frais;
		this.jours = jours;
	}

	public String getMontant() {
		return montant;
	}

	public void setMontant(String montant) {
		this.montant = montant;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	public String getInteret() {
		return interet;
	}

	public void setInteret(String interet) {
		this.interet = interet;
	}
	public String getTaux() {
		return taux;
	}

	public void setTaux(String taux) {
		this.taux = taux;
	}

	public String getTotal() {
		return total;
	}

	public void setTotal(String total) {
		this.total = total;
	}

	public String getFrais() {
		return frais;
	}

	public void setFrais(String frais) {
		this.frais = frais;
	}

	public String getJours() {
		return jours;
	}

	public void setJours(String jours) {
		this.jours = jours;
	}
}
