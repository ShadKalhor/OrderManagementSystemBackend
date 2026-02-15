package ordermanager.shared.extensions;

public class RegexFormats {
    public String NameRegex = "^[A-Za-zÀ-ÖØ-öø-ÿ' -]{1,50}$";
    public String PhoneRegex = "^07\\d{9}$";
    public String PasswordRegex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";

}
