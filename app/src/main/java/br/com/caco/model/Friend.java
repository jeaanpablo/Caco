package br.com.caco.model;

import android.graphics.Bitmap;

public class Friend {
	private int id;
	private String name;
	private String image;
	private int idUser;
	private boolean add;
	private Bitmap bitmapImg;

	public Bitmap getBitmapImg() {
		return bitmapImg;
	}

	public void setBitmapImg(Bitmap bitmapImg) {
		this.bitmapImg = bitmapImg;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getIdUser() {
		return idUser;
	}

	public void setIdUser(int idUser) {
		this.idUser = idUser;
	}

	public Friend()
	{
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public boolean isAdd() {
		return add;
	}
	public void setAdd(boolean add) {
		this.add = add;
	}
	
	

}
