package model.forms;

public class FormValidationException extends Exception {
   
	private static final long serialVersionUID = 1L;
	
	private String specifiedAttr = null;

	/*
     * Constructeur
     */
    public FormValidationException( String message ) {
        super( message );
    }
    
    public FormValidationException( String message, String specifiedAttr ) {
        super( message );
        this.specifiedAttr = specifiedAttr;
    }
    
    public String getSpecifiedAttr() {
		return specifiedAttr;
	}
}