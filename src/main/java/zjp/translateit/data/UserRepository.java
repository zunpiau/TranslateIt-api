package zjp.translateit.data;


import zjp.translateit.domain.User;

public interface UserRepository {

    User getUserByAccount(String account);

    boolean hasEmail(String email);

    boolean hasUser(String userName);

    void saveUser(User user);

    int modifyPassword(long uid, String passwordSalted);

    int modifyUserName(long uid, String userName);

}
