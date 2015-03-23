package snech.web.validators;

import java.util.regex.Pattern;
import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.IValidator;
import org.apache.wicket.validation.ValidationError;

/**
 *
 * @author Radovan Račák
 */
public class PasswordValidator implements IValidator<String> {

    //heslo musi mat aspon 1 cislo, 1 male pismeno, 1 velke pismeno, v rozmedzi od 6 do 20 znakov
    private final String PASSWORD_PATTERN = "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{6,20})";
    private Pattern pattern;

    public PasswordValidator() {
        pattern = Pattern.compile(PASSWORD_PATTERN);
    }

    @Override
    public void validate(IValidatable<String> validatable) {
        final String password = validatable.getValue();

        // validacia hesla
        if (pattern.matcher(password).matches() == false) {
            error(validatable, "not-strong-password");

        }
    }

    private void error(IValidatable<String> validatable, String errorKey) {
        ValidationError error = new ValidationError();
        error.setMessage("Heslo musi mat aspon 1 cislo, 1 male pismeno, 1 velke pismeno, v rozmedzi od 6 do 20 znakov ");//addKey("Heslo ma tieto poziadavky: ");//getClass().getSimpleName() + "." + errorKey);
        validatable.error(error);
    }

}
