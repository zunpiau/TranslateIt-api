package zjp.translateit.data;


import zjp.translateit.domain.User;

public interface UserRepository {

    User getUserByName(String username);

    User getUserByEmail(String email);

    void saveUser(User user);

}
