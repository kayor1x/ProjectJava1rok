package services;

import models.User;
import models.UserRole;
import org.hibernate.Session;
import org.hibernate.query.Query;
import util.HibernateUtil;
import java.time.LocalDateTime;

public class AuthService {
    private static User currentUser;

    public static User getCurrentUser() {
        return currentUser;
    }

    public static boolean login(String username, String password) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<User> query = session.createQuery("FROM User WHERE username = :username", User.class);
            query.setParameter("username", username);
            User user = query.uniqueResult();

            if (user != null && org.mindrot.jbcrypt.BCrypt.checkpw(password, user.getPassword())) {
                session.beginTransaction();
                session.merge(user);
                session.getTransaction().commit();
                currentUser = user;
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void logout() {
        currentUser = null;
    }

    public static boolean isAdmin() {
        return currentUser != null && currentUser.getRole() == models.UserRole.ADMIN;
    }

    public static boolean register(String username, String password) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<User> query = session.createQuery("FROM User WHERE username = :username", User.class);
            query.setParameter("username", username);
            User existingUser = query.uniqueResult();
            if (existingUser != null) {
                return false;
            }
            String hashedPassword = org.mindrot.jbcrypt.BCrypt.hashpw(password, org.mindrot.jbcrypt.BCrypt.gensalt());
            User newUser = new User();
            newUser.setUsername(username);
            newUser.setPassword(hashedPassword);
            newUser.setRole(models.UserRole.USER);
            newUser.setCreatedAt(LocalDateTime.now());
            session.beginTransaction();
            session.persist(newUser);
            session.getTransaction().commit();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean register(String username, String password, String role) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<User> query = session.createQuery("FROM User WHERE username = :username", User.class);
            query.setParameter("username", username);
            User existingUser = query.uniqueResult();
            if (existingUser != null) {
                return false;
            }
            String hashedPassword = org.mindrot.jbcrypt.BCrypt.hashpw(password, org.mindrot.jbcrypt.BCrypt.gensalt());
            User newUser = new User();
            newUser.setUsername(username);
            newUser.setPassword(hashedPassword);
            newUser.setRole(UserRole.valueOf(role));
            newUser.setCreatedAt(java.time.LocalDateTime.now());
            session.beginTransaction();
            session.persist(newUser);
            session.getTransaction().commit();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
