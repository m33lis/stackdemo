package ee.goodsandservices.stackservice.web.rest;

import ee.goodsandservices.stackservice.StackServiceApp;

import ee.goodsandservices.stackservice.domain.Stack;
import ee.goodsandservices.stackservice.repository.StackRepository;
import ee.goodsandservices.stackservice.service.StackService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the StackResource REST controller.
 *
 * @see StackResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = StackServiceApp.class)
public class StackResourceIntTest {

    private static final String DEFAULT_VAL = "AAAAAAAAAA";
    private static final String UPDATED_VAL = "BBBBBBBBBB";

    private static final String DEFAULT_SESSION = "AAAAAAAAAA";
    private static final String UPDATED_SESSION = "BBBBBBBBBB";

    @Inject
    private StackRepository stackRepository;

    @Inject
    private StackService stackService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restStackMockMvc;

    private Stack stack;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        StackResource stackResource = new StackResource();
        ReflectionTestUtils.setField(stackResource, "stackService", stackService);
        this.restStackMockMvc = MockMvcBuilders.standaloneSetup(stackResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Stack createEntity(EntityManager em) {
        Stack stack = new Stack()
                .val(DEFAULT_VAL)
                .session(DEFAULT_SESSION);
        return stack;
    }

    @Before
    public void initTest() {
        stack = createEntity(em);
    }

    @Test
    @Transactional
    public void createStack() throws Exception {
        int databaseSizeBeforeCreate = stackRepository.findAll().size();

        // Create the Stack

        restStackMockMvc.perform(post("/api/stacks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stack)))
            .andExpect(status().isCreated());

        // Validate the Stack in the database
        List<Stack> stackList = stackRepository.findAll();
        assertThat(stackList).hasSize(databaseSizeBeforeCreate + 1);
        Stack testStack = stackList.get(stackList.size() - 1);
        assertThat(testStack.getVal()).isEqualTo(DEFAULT_VAL);
        assertThat(testStack.getSession()).isEqualTo(DEFAULT_SESSION);
    }

    @Test
    @Transactional
    public void createStackWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = stackRepository.findAll().size();

        // Create the Stack with an existing ID
        Stack existingStack = new Stack();
        existingStack.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restStackMockMvc.perform(post("/api/stacks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(existingStack)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Stack> stackList = stackRepository.findAll();
        assertThat(stackList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllStacks() throws Exception {
        // Initialize the database
        stackRepository.saveAndFlush(stack);

        // Get all the stackList
        restStackMockMvc.perform(get("/api/stacks?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(stack.getId().intValue())))
            .andExpect(jsonPath("$.[*].val").value(hasItem(DEFAULT_VAL.toString())))
            .andExpect(jsonPath("$.[*].session").value(hasItem(DEFAULT_SESSION.toString())));
    }

    @Test
    @Transactional
    public void getStack() throws Exception {
        // Initialize the database
        stackRepository.saveAndFlush(stack);

        // Get the stack
        restStackMockMvc.perform(get("/api/stacks/{id}", stack.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(stack.getId().intValue()))
            .andExpect(jsonPath("$.val").value(DEFAULT_VAL.toString()))
            .andExpect(jsonPath("$.session").value(DEFAULT_SESSION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingStack() throws Exception {
        // Get the stack
        restStackMockMvc.perform(get("/api/stacks/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateStack() throws Exception {
        // Initialize the database
        stackService.save(stack);

        int databaseSizeBeforeUpdate = stackRepository.findAll().size();

        // Update the stack
        Stack updatedStack = stackRepository.findOne(stack.getId());
        updatedStack
                .val(UPDATED_VAL)
                .session(UPDATED_SESSION);

        restStackMockMvc.perform(put("/api/stacks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedStack)))
            .andExpect(status().isOk());

        // Validate the Stack in the database
        List<Stack> stackList = stackRepository.findAll();
        assertThat(stackList).hasSize(databaseSizeBeforeUpdate);
        Stack testStack = stackList.get(stackList.size() - 1);
        assertThat(testStack.getVal()).isEqualTo(UPDATED_VAL);
        assertThat(testStack.getSession()).isEqualTo(UPDATED_SESSION);
    }

    @Test
    @Transactional
    public void updateNonExistingStack() throws Exception {
        int databaseSizeBeforeUpdate = stackRepository.findAll().size();

        // Create the Stack

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restStackMockMvc.perform(put("/api/stacks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stack)))
            .andExpect(status().isCreated());

        // Validate the Stack in the database
        List<Stack> stackList = stackRepository.findAll();
        assertThat(stackList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteStack() throws Exception {
        // Initialize the database
        stackService.save(stack);

        int databaseSizeBeforeDelete = stackRepository.findAll().size();

        // Get the stack
        restStackMockMvc.perform(delete("/api/stacks/{id}", stack.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Stack> stackList = stackRepository.findAll();
        assertThat(stackList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
