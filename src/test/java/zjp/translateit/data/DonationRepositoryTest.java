package zjp.translateit.data;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import zjp.translateit.test.SpringJdbcTest;

import java.util.Collection;
import java.util.List;

import static org.junit.Assert.*;
import static zjp.translateit.Constant.CACHE_NAME_DONATION;

public class DonationRepositoryTest extends SpringJdbcTest {

    private DonationRepository repository;
    private CacheManager cacheManager;

    @Autowired
    public void setRepository(DonationRepository repository) {
        this.repository = repository;
    }

    @Autowired
    public void setCacheManager(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    @Test
    public void testCache() {
        testListDonation();
        ConcurrentMapCacheManager manager = (ConcurrentMapCacheManager) cacheManager;
        Collection<String> caches = manager.getCacheNames();
        assertEquals(1, caches.size());
        Cache cache = manager.getCache(CACHE_NAME_DONATION);
        assertNotNull(cache);
        assertEquals(15, cache.get(0, List.class).size());
        assertEquals(6, cache.get(10, List.class).size());
        assertNull(cache.get(20));
    }

    @Test
    public void testListDonation() {
        assertEquals(15, repository.listDonation(0).size());
        assertEquals(6, repository.listDonation(10).size());
        assertEquals(0, repository.listDonation(20).size());
    }

}