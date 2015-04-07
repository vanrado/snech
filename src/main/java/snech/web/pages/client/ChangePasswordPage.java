package snech.web.pages.client;

import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;
import snech.core.CustomAuthenticatedWebSession;
import snech.core.services.IDatabaseService;
import snech.core.services.IHashUtils;
import snech.core.types.User;
import snech.web.base.MainPage;
import snech.web.validators.PasswordValidator;

/**
 *
 * @author Radovan
 */
public class ChangePasswordPage extends MainPage {
    
    @SpringBean
    private IDatabaseService databaseService;
    
    @SpringBean
    private IHashUtils hashUtils;
    
    private PasswordTextField currentPasswordTextField;
    private PasswordTextField newPasswordTextField;
    private PasswordTextField newPasswordRepeatTextField;
    
    public ChangePasswordPage(){
        Form form = new Form("change.form");
        form.add(new FeedbackPanel("feedback"));
        
        currentPasswordTextField = new PasswordTextField("currentPassword.field", Model.of(""));
        currentPasswordTextField.setRequired(true);
        form.add(currentPasswordTextField);
        
        newPasswordTextField = new PasswordTextField("newPassword.field", Model.of(""));
        newPasswordTextField.setRequired(true);
        newPasswordTextField.add(new PasswordValidator());
        form.add(newPasswordTextField);
        
        newPasswordRepeatTextField = new PasswordTextField("newPasswordRepeat.field", Model.of(""));
        newPasswordRepeatTextField.setRequired(true);
        form.add(newPasswordRepeatTextField);
        
        form.add(new Button("submit.button"){
            @Override
            public void onSubmit() {
                super.onSubmit(); 
                User user = CustomAuthenticatedWebSession.get().getUser();
                
                if(CustomAuthenticatedWebSession.get().authenticate(user.getLogin(), currentPasswordTextField.getModelObject())){
                    //Zahashovat nove heslo, vygenerovat salt a aj ho zahasovat a potom spravit update
                    String newPass = newPasswordTextField.getModelObject();
                    String newPassRepeat = newPasswordRepeatTextField.getModelObject();
                    
                    if(newPass.equals(newPassRepeat)){
                        String newSalt = hashUtils.getRandomSalt();
                        newPass = hashUtils.hashPassword(newPass, newSalt);
                        
                        if(databaseService.updateLoginPassword(user.getLogin(), newPass, newSalt)){
                            info("Heslo bolo aktualizovane!");
                        }else{
                            error("Heslo nebolo aktualizovane! Nastala chyba");
                        }
                    }else{
                        error("Hesla sa nezhoduju!");
                    }
                }else{
                    error("Zadane aktualne heslo nieje spravne");
                }                
            }
        });
        
        form.add(new Link("backpage.link"){

            @Override
            public void onClick() {
                setResponsePage(AccountManagementPage.class);
            }
            
        });
        
        add(form);
    }
}
