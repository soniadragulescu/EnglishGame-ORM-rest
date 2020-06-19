package repos;

import model.User;

public interface IUserRepo {
    User findOne(String username, String password);
    void update(User user);
    Iterable<User> getAll();
    User findOneByUsername(String username);
}
