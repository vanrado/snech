package snech.web.pages.client;

import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import snech.core.CustomAuthenticatedWebSession;
import snech.core.services.IDatabaseService;
import snech.core.types.User;
import snech.web.base.MemberBasePage;

/**
 *
 * @author Radovan
 */
public class ChangeDetailsPage extends MemberBasePage {

    private String firstName;
    private String lastName;
    private String email;
    private String occupation;
    private User user;
    @SpringBean
    private IDatabaseService databaseService;

    public ChangeDetailsPage() {
        user = CustomAuthenticatedWebSession.get().getUser();
        firstName = user.getFirstName();
        lastName = user.getLastName();
        email = user.getEmail();
        occupation = user.getOccupation();

        Form editForm = new Form("edit.form");

        editForm.add(new FeedbackPanel("feedback"));

        TextField firstNameField = new TextField("firstName.textfield", new PropertyModel(this, "firstName"));
        firstNameField.setRequired(true);
        editForm.add(firstNameField);

        TextField lastNameField = new TextField("lastName.textfield", new PropertyModel(this, "lastName"));
        lastNameField.setRequired(true);
        editForm.add(lastNameField);

        TextField emailField = new TextField("email.textfield", new PropertyModel(this, "email"));
        editForm.add(emailField);

        TextField occupationField = new TextField("occupation.textfield", new PropertyModel(this, "occupation"));
        editForm.add(occupationField);

        editForm.add(new Link("backpage.button") {

            @Override
            public void onClick() {
                setResponsePage(AccountManagementPage.class);
            }

        });

        editForm.add(new Button("save.button") {

            @Override
            public void onError() {
                super.onError();
                error("Niekde nastala chyba!");
            }

            @Override
            public void onSubmit() {
                super.onSubmit();

                if (!user.getFirstName().equals(firstName)) {
                    user.setFirstName(firstName);
                    info("Zmena mena na " + firstName);
                }

                if (!user.getLastName().equals(lastName)) {
                    user.setLastName(lastName);
                    info("Zmena priezviska na " + lastName);
                }

                if (!user.getEmail().equals(email)) {
                    user.setEmail(email);
                    info("Zmena emailu na " + email);
                }

                if (!user.getOccupation().equals(user.getOccupation())) {
                    user.setOccupation(occupation);
                    info("Zmena zamestnania na " + occupation);
                }
                
                if (databaseService.updateUser(user)) {
                    info("Aktualizacia uzivatela prebehla uspesne!");
                } else {
                    error("Nastala chyba pri aktualizacii databazy!");
                }
            }
        });

        add(editForm);
    }
}
