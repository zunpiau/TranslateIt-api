package zjp.translateit.data;


import zjp.translateit.domain.User;

public interface UserRepository {

    User getUserByAccount(String account);

    boolean hasEmail(String email);

    void saveUser(User user);

    int modifyPassword(long uid, String passwordSalted);

}
