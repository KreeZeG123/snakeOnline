package model.beans;

public class Joueur {
	
	private long id = -1; 
	private String username = "";
	private String email = "";
	private String motDePasse = "";
	private int nbPieces = 0;
	private String skins = "";
	private int score = 0;
	
	public int getScore() {
		return score;
	}
	
	public void setScore(int score) {
		this.score = score;
	}
	
	public String getSkins() {
		return skins;
	}
	
	public void addSkins(Item item) {
		this.skins+=item.getId()+",";
	}
	
	public void setSkins(String skins) {
		this.skins = skins;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getMotDePasse() {
		return motDePasse;
	}
	public void setMotDePasse(String motDePasse) {
		this.motDePasse = motDePasse;
	}
	public int getNbPieces() {
		return nbPieces;
	}
	public void setNbPieces(int nbPieces) {
		this.nbPieces = nbPieces;
	}
	
	
}
