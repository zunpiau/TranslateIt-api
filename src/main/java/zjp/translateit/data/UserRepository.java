package zjp.translateit.data;


import zjp.translateit.domain.User;

public interface UserRepository {

    User findUserByName(String username);

    User findUserByEmail(String username);

    User findUserByNameAndPassword(String username, String passwordEncrypted);

    User add(User user);

    void updateUserStatus(long id, int status);

}
