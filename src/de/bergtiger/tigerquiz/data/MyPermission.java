package de.bergtiger.tigerquiz.data;

public enum MyPermission {
	P_ADMIN ("userlist.admin");
	
	private String permission;
	
	MyPermission(String args){
		this.permission = args;
	}
	
	public String get(){
		return this.permission;
	}
	
	public static String get(MyPermission myPermission){
		return myPermission.get();
	}
}
