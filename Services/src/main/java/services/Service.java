package services;

import model.Game;
import model.User;
import repos.GameRepo;
import repos.UserRepo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Service implements IService{
    private UserRepo userRepo;
    private GameRepo gameRepo;
    private List<IObserver> observers;
    private static Integer participants;
    private static Integer responses;
    List<String> categories= Arrays.asList("fruits","animals","clothes");
    List<String> fruits=Arrays.asList("apple", "banana", "mango", "cherry", "berry","orange");
    List<String> animals=Arrays.asList("ferret", "dog", "cat", "panda","otter", "bird");
    List<String> clothes=Arrays.asList("skirt","tshirt", "dress","blouse", "trousers", "socks");
    List<String> users;
    private final int defaultThreadsNo=5;

    public Service(UserRepo userRepo, GameRepo gameRepo) {
        this.userRepo = userRepo;
        this.gameRepo = gameRepo;
        observers=new ArrayList<>();
        users=new ArrayList<>();
        this.participants=0;
        this.responses=0;
    }

    @Override
    public User login(IObserver client, String username, String password) {
        if(participants>=3){
            return null;
        }
        else{
            User user=userRepo.findOne(username, password);
            if(user!=null){
                observers.add(client);
                users.add(username);
                participants+=1;
                if(participants==3){
                    List<String> randomCategories=createRandomCategories();
                    Game game=new Game(randomCategories, this.users);
                    gameRepo.save(game);
                    //notifyUsers();
                    startGame();
                }
            }
            return user;
        }
    }

    List<String> createRandomCategories(){
//        Random random = new Random();
//        List<String> randomCategories=new ArrayList<>();
//        for(int i=0; i<5; i++){
//            Integer category=random.nextInt()%3-1;
//            randomCategories.add(this.categories.get(category));
//        }
        List<String> randomCategories=Arrays.asList("fruits","animals","fruits","clothes","animals");
        return randomCategories;
    }

    @Override
    public void logout(IObserver client, String username) {
        observers.remove(client);
    }

    @Override
    public void updateUser(String username, Integer score) {
        User user=userRepo.findOneByUsername(username);
        user.setScore(score);
        userRepo.update(user);
    }

    @Override
    public void updateGame(String user, String word) {
        Game game=gameRepo.getLast();
        User player=userRepo.findOneByUsername(user);
        List<String> users=game.getUsers();
        Integer poz=users.indexOf(user);
        Integer score=getScore(game,word)+player.getScore();
        player.setScore(score);
        userRepo.update(player);
        if(poz.equals(0)){
            List<String> words=game.getUser1();
            words.add(word);
            game.setUser1(words);
        }

        if(poz.equals(1)){
            List<String> words=game.getUser2();
            words.add(word);
            game.setUser2(words);
        }

        if(poz.equals(2)){
            List<String> words=game.getUser3();
            words.add(word);
            game.setUser3(words);
        }
        this.responses+=1;
        if(this.responses==3){
            Integer round=game.getRound();
            round+=1;
            game.setRound(round);
            this.responses=0;
        }

        gameRepo.update(game);
        if(this.responses==0){
            notifyUsers();
        }
    }

    public boolean alreadySent(String word, Game game){
        List<String> words1=game.getUser1();
        List<String> words2=game.getUser2();
        List<String> words3=game.getUser3();

        if(words1.contains(word)||words2.contains(word)||words3.contains(word))
            return true;
        return false;
    }
    Integer getScore(Game game, String word){
        Integer score=0;

        String category=game.getCategorii().get(game.getRound());
        boolean alreadySent=alreadySent(word, game);
        if(category.equals("animals")){
            if(this.animals.contains(word)){
                if(!alreadySent)
                    score=5;
                else score=2;
            }
        }

        if(category.equals("fruits")){
            if(this.fruits.contains(word)){
                if(!alreadySent)
                    score=5;
                else score=2;
            }
        }

        if(category.equals("clothes")){
            if(this.clothes.contains(word)){
                if(!alreadySent)
                    score=5;
                else score=2;
            }
        }

        return score;

    }
    @Override
    public Game getLastGame() {
        return gameRepo.getLast();
    }

    @Override
    public void startGame() {
        ExecutorService executor= Executors.newFixedThreadPool(defaultThreadsNo);
        for(IObserver observer:observers) {
            executor.execute(() -> {
                try {
                    System.out.println("notifying users there are 3 players...");
                    observer.gameStart();
                } catch (Exception e) {
                    System.out.println("error notifying users...");
                }
            });
        }
        executor.shutdown();
    }

    @Override
    public void actualGameStarted() {
        ExecutorService executor= Executors.newFixedThreadPool(defaultThreadsNo);
        for(IObserver observer:observers) {
            executor.execute(() -> {
                try {
                    System.out.println("notifying users button START was pressed...");
                    observer.actualGameStarted();
                } catch (Exception e) {
                    System.out.println("error notifying users...");
                }
            });
        }
        executor.shutdown();

        notifyUsers();
    }

    private void notifyUsers(){
        ExecutorService executor= Executors.newFixedThreadPool(defaultThreadsNo);
        for(IObserver observer:observers) {
            executor.execute(() -> {
                try {
                    System.out.println("notifying users...");
                    List<User> users=new ArrayList<>();
                    for(User u: userRepo.getAll()) {
                        users.add(u);
                    }
                    Game game=gameRepo.getLast();
                    List<String> categories=new ArrayList<>();
                    Integer round=game.getRound();
                    game.setRound(round);
                    for(String c:game.getCategorii()){
                        categories.add(c);
                    }
                    game.setCategorii(categories);
                    observer.nextRound(game, users);
                } catch (Exception e) {
                    System.out.println("error notifying users...");
                }
            });
        }
        executor.shutdown();
    }
}
