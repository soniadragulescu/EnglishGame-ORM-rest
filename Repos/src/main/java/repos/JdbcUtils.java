package repos;

import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

public class JdbcUtils {
    public static SessionFactory sessionFactory=null;
    public JdbcUtils(){

    }
    private void getNewSessionFactory(){
        // A SessionFactory is set up once for an application!
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure() // configures settings from hibernate.cfg.xml
                .build();
        try {
            sessionFactory = new MetadataSources( registry ).buildMetadata().buildSessionFactory();
        }
        catch (Exception e) {
            StandardServiceRegistryBuilder.destroy( registry );
        }
    }

    public SessionFactory getSessionFactory(){
        try {
            if (sessionFactory==null || sessionFactory.isClosed())
                getNewSessionFactory();

        } catch (Exception e) {
            System.out.println("Error DB "+e);
        }
        //logger.traceExit(instance);
        return sessionFactory;
    }

    public static void close(){
        if ( sessionFactory != null ) {
            sessionFactory.close();
        }

    }
}

