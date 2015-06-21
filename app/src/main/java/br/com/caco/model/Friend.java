package br.com.caco.model;

public class Friend {
	
	private String name;
	private int image;
	private boolean add;
	
	
	public Friend(String name, boolean add, int image)
	{
		this.name = name;
		this.image = image;
		this.add = add;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getImage() {
		return image;
	}
	public void setImage(int image) {
		this.image = image;
	}
	public boolean isAdd() {
		return add;
	}
	public void setAdd(boolean add) {
		this.add = add;
	}
	
	

}
