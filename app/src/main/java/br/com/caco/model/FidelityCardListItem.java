package br.com.caco.model;

import android.graphics.Bitmap;

public class FidelityCardListItem {
	
	private String storeName;
	private String points;
	//private Bitmap storeLogo;
	private int storeLogo;
	private String data; //provisorio, apenas para screenshot
	public String getDistance() {
		return distance;
	}

	public void setDistance(String distance) {
		this.distance = distance;
	}
	private String distance; //provisorio apenas para screenshot 
	
	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public FidelityCardListItem(String storeName, String points, int storeLogo, String data, String distance)
	{
		this.storeLogo = storeLogo;
		this.points = points;
		this.storeName = storeName;
		this.data = data;
		this.distance = distance;
	}
	
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
	public int getStoreLogo() {
		return storeLogo;
	}
	public void setStoreLogo(int storeLogo) {
		this.storeLogo = storeLogo;
	}
	
	
	

}
