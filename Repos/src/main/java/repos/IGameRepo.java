package repos;

import model.Game;

public interface IGameRepo {
    void update(Game game);
    void save(Game game);
    Game getLast();
}
