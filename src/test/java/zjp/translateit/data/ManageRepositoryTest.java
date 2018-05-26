package zjp.translateit.data;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import zjp.translateit.domain.Donation;
import zjp.translateit.test.SpringJdbcTest;

import java.time.LocalDateTime;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static zjp.translateit.Constant.CACHE_NAME_DONATION;

public class ManageRepositoryTest extends SpringJdbcTest {

    @Autowired
    ManageRepository manageRepository;
    @Autowired
    CacheManager cacheManager;
    @Autowired
    DonationRepository donationRepository;

    @Test
    public void testCacheEvict() {
        DonationRepositoryTest donationRepositoryTest = new DonationRepositoryTest();
        donationRepositoryTest.setCacheManager(cacheManager);
        donationRepositoryTest.setRepository(donationRepository);
        donationRepositoryTest.testCache();
        manageRepository.saveDonation(new Donation(LocalDateTime.now(), "anonymous", 1, "comment"));
        Cache cache = cacheManager.getCache(CACHE_NAME_DONATION);
        assertNotNull(cache);
        assertNull(cache.get(0));
        assertNull(cache.get(10));
    }
}