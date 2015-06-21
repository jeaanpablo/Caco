package br.com.caco.model;

public class Store {
	
	private String name;
	private String address;
	private boolean fidelity; //Only for demo
	private int storeLogo; //Only for demo
	
	
	public Store (String name, String address, int storeLogo)
	{
		this.name = name;
		this.address = address;
		this.storeLogo = storeLogo;
	}
	
	public int getStoreLogo() {
		return storeLogo;
	}
	public void setStoreLogo(int storeLogo) {
		this.storeLogo = storeLogo;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public boolean isFidelity() {
		return fidelity;
	}
	public void setFidelity(boolean fidelity) {
		this.fidelity = fidelity;
	}
}
