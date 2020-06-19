package repos;

import model.Game;
import model.Game;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class GameRepo implements IGameRepo{
    static SessionFactory sessionFactory;
    private JdbcUtils jdbcUtils=new JdbcUtils();

    public GameRepo() {
        System.out.println("Initializing GameRepo... ");
        sessionFactory=jdbcUtils.getSessionFactory();
    }
    @Override
    public void update(Game game) {
        try(Session session = sessionFactory.openSession()){
            Transaction tx=null;
            try{
                tx = session.beginTransaction();
                Game oldGame =
                        (Game) session.load( Game.class, game.getId());
                oldGame.setRound(game.getRound());
                oldGame.setUser1(game.getUser1());
                oldGame.setUser2(game.getUser2());
                oldGame.setUser3(game.getUser3());
                System.err.println("We've updated game "+game.getId().toString());
                tx.commit();

            } catch(RuntimeException ex){
                if (tx!=null)
                    tx.rollback();
            }
        }
    }

    @Override
    public void save(Game game) {
        try(Session session=sessionFactory.openSession()){
            Transaction tx = null;
            try {
                tx = session.beginTransaction();
                session.save(game);
                tx.commit();
                System.err.println("Am adaugat jocul "+game.getId().toString());
            } catch (RuntimeException ex) {
                if (tx != null)
                    tx.rollback();
            }
        }
    }

    @Override
    public Game getLast() {
        try(Session session=sessionFactory.openSession()){
            Transaction tx=null;
            try{
                tx=session.beginTransaction();
                List<Game> games =
                        session.createQuery("from Game", Game.class)
                                .setFirstResult(0).setMaxResults(100).list();
                tx.commit();
                System.out.println("Last game has the id "+games.get(games.size()-1).getId().toString());
                return games.get(games.size()-1);
            }catch (Exception e){
                if (tx != null)
                    tx.rollback();
                e.printStackTrace();
                return null;
            }
        }
    }

    public Game findOne(Integer id) {
        try(Session session=sessionFactory.openSession()){
            Transaction tx=null;
            try{
                tx=session.beginTransaction();
                List<Game> games =
                        session.createQuery("from Game as g where g.id= ?", Game.class)
                                .setParameter(0, id)
                                .setFirstResult(0).setMaxResults(100).list();
                tx.commit();
                //System.out.println("Last game has the id "+games.get(games.size()-1).getId().toString());
                return games.get(0);
            }catch (Exception e){
                if (tx != null)
                    tx.rollback();
                e.printStackTrace();
                return null;
            }
        }
    }
}
