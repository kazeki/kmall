package com.kzk.kmall.web.rest;

import com.kzk.kmall.KmallApp;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
/**
 * Test class for the TryController REST controller.
 *
 * @see TryControllerResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = KmallApp.class)
public class TryControllerResourceIntTest {

    private MockMvc restMockMvc;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        TryControllerResource tryControllerResource = new TryControllerResource();
        restMockMvc = MockMvcBuilders
            .standaloneSetup(tryControllerResource)
            .build();
    }

    /**
    * Test trySayHello
    */
    @Test
    public void testTrySayHello() throws Exception {
        restMockMvc.perform(get("/api/try-controller/try-say-hello"))
            .andExpect(status().isOk());
    }
    /**
    * Test tryPostMsg
    */
    @Test
    public void testTryPostMsg() throws Exception {
        restMockMvc.perform(post("/api/try-controller/try-post-msg"))
            .andExpect(status().isOk());
    }

}
