package webtree.dao;



import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.springframework.context.annotation.Bean;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;


/**
 Фабрика сессий Hibernate (синглтон)
 */

@Component
@Service
public class Factory{


    private static SessionFactory sessionFactory=Factory.getSessionFactory();
private static Session session;





    public static Session getSession() {
        if(session ==null||!session.isOpen())
            session=sessionFactory.openSession();
        return session;
    }

    public static void setSession(Session session) {
        Factory.session = session;
    }
    @Bean
    public static synchronized SessionFactory getSessionFactory() {

            if(Factory.sessionFactory == null)
               sessionFactory = new Configuration().configure().buildSessionFactory();



            return sessionFactory;
    }

    public static void setSessionFactory(SessionFactory sessionFactory) {
        Factory.sessionFactory = sessionFactory;
    }
}
