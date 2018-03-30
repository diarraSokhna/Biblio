package fr.khady.wsBiblio.dao;

import java.sql.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import fr.khady.wsBiblio.entity.Utilisateur;

@Stateless
public class UtilisateurDao {

	@PersistenceContext(unitName = "bdd_biblio_PU")
	private EntityManager entityManager;
	
	private static final String JPQL_SELECT_PAR_DATE_RETOUR = "SELECT u.email FROM utilisateur u,pret p WHERE u.id_user=p.id_user " + 
			"AND p.rendu = false AND DATE_PART('day', NOW()::timestamp - '2018-03-28'::timestamp) > 0";

	public void creerUtilisateur(String nom, String prenom, String adress, String email, String login, String password,
			String photo) throws DaoException {
		Utilisateur utilisateur = new Utilisateur();
		utilisateur.setNom(nom);
		utilisateur.setPrenom(prenom);
		utilisateur.setAdress(adress);
		utilisateur.setEmail(email);
		utilisateur.setLogin(login);
		utilisateur.setPassword(password);
		utilisateur.setPhoto(photo);

		try {
			entityManager.persist(utilisateur);
		} catch (Exception e) {
			throw new DaoException(e);
		}

	}

	@SuppressWarnings("unchecked")
	public List<Utilisateur> listerUtilisateur() throws DaoException {
		Query req = entityManager.createQuery("select u from Utilisateur u");
		try {
			return req.getResultList();
		} catch (Exception e) {
			throw new DaoException(e);
		}

	}

	public void modifierUtilisateur(String nom, String prenom, String adress, String email, String login, String password,
			String photo) throws DaoException {
		Utilisateur utilisateur = new Utilisateur();
		utilisateur.setNom(nom);
		utilisateur.setPrenom(prenom);
		utilisateur.setAdress(adress);
		utilisateur.setEmail(email);
		utilisateur.setLogin(login);
		utilisateur.setPassword(password);
		utilisateur.setPhoto(photo);
		try {
			entityManager.merge(utilisateur);
		} catch (Exception e) {
			throw new DaoException("ko");
		}

	}

	public Utilisateur trouverUtilisateurParId(long id_user) {

		Utilisateur utilisateur = entityManager.find(Utilisateur.class, id_user);
		return utilisateur;
	}

	@SuppressWarnings("unchecked")
	public List<Utilisateur> listerUtilisateurRelance() throws DaoException {
		Query req = entityManager.createNativeQuery( JPQL_SELECT_PAR_DATE_RETOUR );
		try {
			return req.getResultList();
		} catch (Exception e) {
			throw new DaoException(e);
		}

	}


}