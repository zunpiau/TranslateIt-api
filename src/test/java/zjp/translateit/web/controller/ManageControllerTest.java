package zjp.translateit.web.controller;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import zjp.translateit.test.SpringMvcTest;

import java.util.Objects;
import java.util.concurrent.ConcurrentMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static zjp.translateit.Constant.CACHE_NAME_DONATION;

@TestPropertySource(value = "classpath:application.properties")
public class ManageControllerTest extends SpringMvcTest {

    @Autowired
    WebApplicationContext context;
    @Autowired
    private CacheManager cacheManager;
    @Autowired
    private ManageController manageController;
    @Autowired
    private MiscController miscController;
    @Value("${manager.name}")
    private String name;
    @Value("${manager.password}")
    private String password;

    @Test
    public void evictCache() throws Exception {
        assertEquals(0, cacheManager.getCacheNames().size());
        MockMvcBuilders.standaloneSetup(miscController)
                .build()
                .perform(MockMvcRequestBuilders.get("/donation")
                        .param("offset", "0"));
        assertEquals(1, cacheManager.getCacheNames().size());
        ConcurrentMap store = (ConcurrentMap) Objects.requireNonNull(cacheManager.getCache(CACHE_NAME_DONATION)).getNativeCache();
        assertNotEquals(0, store.size());
        MockMvcBuilders.standaloneSetup(manageController)
                .build()
                .perform(MockMvcRequestBuilders.delete("/manage/donation/cache"));
        assertEquals(0, store.size());
    }
}