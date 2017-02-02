package ee.goodsandservices.stackservice.service;

import ee.goodsandservices.stackservice.StackServiceApp;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;

/**
 * Test class for Stack service
 *
 * Created by m3l on 2/2/17.
 */

@RunWith(SpringRunner.class)
@SpringBootTest(classes = StackServiceApp.class)
@Transactional
public class StackServiceTest {

    @Inject
    private StackService stackService;

}
