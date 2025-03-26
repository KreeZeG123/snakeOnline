package model.forms;

import javax.servlet.http.HttpServletRequest;

public class FormConnexion {
	private String result;
	private String username;
	private String pwd;
	
	public void verif( HttpServletRequest request) {
		this.username = request.getParameter("username"); 
		this.pwd = request.getParameter("password"); 
		if(this.pwd.equals("motdepasse")) {
			result = "Connexion acceptée";
		}else {
			result = "Erreur";
		}

	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

}
