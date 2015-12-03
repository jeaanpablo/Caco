package br.com.caco.model;

public class Notification {
	
	private int id;
    private int idUserApprover;
	private int idUserRequester;
	private String nameUserRequester;
	private int idStore;
	private String type;
	private String nameStore;
	private String imgPath;
	private int statusNotification;


    public int getIdUserApprover() {
        return idUserApprover;
    }

    public void setIdUserApprover(int idUserApprover) {
        this.idUserApprover = idUserApprover;
    }

    public int getStatusNotification() {
        return statusNotification;
    }

    public void setStatusNotification(int statusNotification) {
        this.statusNotification = statusNotification;
    }

    public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getIdUserRequester() {
		return idUserRequester;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setIdUserRequester(int idUserRequester) {
		this.idUserRequester = idUserRequester;
	}

	public String getNameUserRequester() {
		return nameUserRequester;
	}

	public void setNameUserRequester(String nameUserRequester) {
		this.nameUserRequester = nameUserRequester;
	}

	public int getIdStore() {
		return idStore;
	}

	public void setIdStore(int idStore) {
		this.idStore = idStore;
	}

	public String getImgPath() {
		return imgPath;
	}

	public void setImgPath(String imgPath) {
		this.imgPath = imgPath;
	}

	public String getNameStore() {
		return nameStore;
	}

	public void setNameStore(String nameStore) {
		this.nameStore = nameStore;
	}
}
