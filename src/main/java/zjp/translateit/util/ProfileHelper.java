package zjp.translateit.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class ProfileHelper {

    public static final String PROFILE_DEV = "dev";
    public static final String PROFILE_PROD = "prod";
    final private Environment env;

    @Autowired
    public ProfileHelper(Environment env) {
        this.env = env;
    }

    public boolean isDev() {
        String[] profiles = env.getActiveProfiles();
        for (String profile : profiles) {
            if (profile.equals(PROFILE_DEV)) {
                return true;
            }
        }
        return false;
    }

}
