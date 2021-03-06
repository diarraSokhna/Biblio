package fr.khady.webAppliBiblio.action;

import java.util.List;
import java.util.Map;

import com.opensymphony.xwork2.ActionSupport;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.interceptor.SessionAware;
import org.jasypt.util.password.StrongPasswordEncryptor;

import fr.khady.wsBiblioClient.BibliothequeException;
import fr.khady.wsBiblioClient.Utilisateur;
import fr.khady.wsBiblioClient.UtilisateurService;
import fr.khady.wsBiblioClient.UtilisateurService_Service;
import javassist.NotFoundException;

public class GestionUtilisateurAction extends ActionSupport implements SessionAware{
	private static final long serialVersionUID = 1L;

	public Utilisateur utilisateur = new Utilisateur();
	public List<Utilisateur> utilisateurs;
	UtilisateurService_Service service = new UtilisateurService_Service();
	UtilisateurService utilisateurPort = service.getUtilisateurServicePort();

//	StrongPasswordEncryptor passwordEncryptor = new StrongPasswordEncryptor();

	 private Map<String, Object> session;
	 
	public String index() {

		// utilisateurs = utilisateurPort.listerUtilisateur();
		return SUCCESS;
	}

	public String ajouterUtilisateur() {

		// Par défaut, le result est "input"
		
		String vResult = ActionSupport.INPUT;
		String password=utilisateur.getPassword();
		String email=utilisateur.getEmail();
		
//		utilisateurs = utilisateurPort.listerUtilisateur();
		
		StrongPasswordEncryptor passwordEncryptor = new StrongPasswordEncryptor();
		String motDePasseChiffre = passwordEncryptor.encryptPassword(password);

//		utilisateur.setPassword(motDePasseChiffre);
		
		
			if (!this.hasErrors()) {
				utilisateurPort.creerUtilisateur(utilisateur.getNom(), utilisateur.getPrenom(), utilisateur.getAdress(),
						email, utilisateur.getLogin(), password,
						utilisateur.getPhoto());
				vResult = ActionSupport.SUCCESS;
				this.addActionMessage("Inscription effectée avec succès");

			}
		
		return vResult;
	}
	
	public String login()   {
		String vResult = ActionSupport.INPUT;
		String email=utilisateur.getEmail();
		String password = utilisateur.getPassword();
		
		 if (!org.apache.commons.lang3.StringUtils.isAllEmpty(email, password)) {
	            try {
	                Utilisateur vUtilisateur
	                        = utilisateurPort.trouverUtilisateurParEmail(email, password);
	                // Ajout de l'utilisateur en session
	                this.session.put("utilisateur", vUtilisateur);
	                vResult = ActionSupport.SUCCESS;
	            } catch (Exception pEx) {
	                this.addActionError("Identifiant ou mot de passe invalide !");
	            }
	        }
	        return vResult;
	}

	@Override
	public void setSession(Map<String, Object> pSession) {
		 this.session = pSession;
		
	}
	
	public String doLogout() {
	    // Suppression de l'utilisateur en session
	    this.session.remove("utilisateur");

	    return ActionSupport.SUCCESS;
	}
}
