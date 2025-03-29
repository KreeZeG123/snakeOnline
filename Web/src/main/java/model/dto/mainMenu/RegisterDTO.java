package model.dto.mainMenu;

//Login et password

public class RegisterDTO extends MainMenuDTO{
    private String login;
    private String email;
    private String password;
    private String confirmPassword;

    public RegisterDTO(String login, String email, String password, String confirmPassword) {
        this.login = login;
        this.email = email;
        this.password = password;
        this.confirmPassword = confirmPassword;
    }

    public String getLogin() {
        return login;
    }
    
    public String getEmail() {
		return email;
	}

    public String getPassword() {
        return password;
    }

    public String getConfirmPassword() { return confirmPassword; }
}
