package snech.web.pages;

import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;
import snech.core.CustomAuthenticatedWebSession;
import snech.core.services.IDatabaseService;
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
                
                if(databaseService.getUserLogin(user.getLogin(), currentPasswordTextField.getModelObject()) != null){
                    String newPass = newPasswordTextField.getModelObject();
                    String newPassRepeat = newPasswordRepeatTextField.getModelObject();
                    
                    if(newPass.equals(newPassRepeat)){
                        if(databaseService.updateLoginPassword(newPass, user.getLogin())){
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
