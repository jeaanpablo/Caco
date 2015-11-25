package br.com.caco.model;

import android.graphics.Bitmap;

public class LoyalityCard {
	
	private String storeName;
	private String points;
	//private Bitmap storeLogo;
	private String storeLogo;
	private String data; //provisorio, apenas para screenshot
	private String distance; //provisorio apenas para screenshot

	public String getStoreName() {
		return storeName;
	}

	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}

	public String getPoints() {
		return points;
	}

	public void setPoints(String points) {
		this.points = points;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public String getStoreLogo() {
		return storeLogo;
	}

	public void setStoreLogo(String storeLogo) {
		this.storeLogo = storeLogo;
	}

	public String getDistance() {
		return distance;
	}

	public void setDistance(String distance) {
		this.distance = distance;
	}
}
