package zjp.translateit.data;


import zjp.translateit.domain.User;

public interface UserRepository {

    User findUserByName(String username);

    User findUserByEmail(String username);

    int add(User user);

    int generateUid();

}
