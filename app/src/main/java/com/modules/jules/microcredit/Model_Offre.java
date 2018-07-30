package com.modules.jules.microcredit;

public class Model_Offre {
	private String contact, photo, email, montant, taux, date;

	public Model_Offre() {
	}

	public Model_Offre(String contact, String photo, String email, String montant, String taux, String date) {
		this.contact = contact;
		this.photo = photo;
		this.email = email;
		this.montant = montant;
		this.taux = taux;
		this.date = date;
	}

	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMontant() {
		return montant;
	}

	public void setMontant(String montant) {
		this.montant = montant;
	}

	public String getTaux() {
		return taux;
	}

	public void setTaux(String taux) {
		this.taux = taux;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

}
