package eu.acme.demo;

import com.fasterxml.jackson.databind.ObjectMapper;
import eu.acme.demo.domain.Order;
import eu.acme.demo.domain.enums.OrderStatus;
import eu.acme.demo.repository.OrderRepository;
import eu.acme.demo.web.dto.OrderDto;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
class OrderAPITests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;
    
    @MockBean
    private OrderRepository mockRepository;

       
    @Test
    void testOrderAPI() throws Exception {

        //TODO: submit order request
        // 1. create order request
        // 2. convert to json string using Jackson Object Mapper
        // 3. set json string to content param
        MvcResult orderResult = this.mockMvc.perform(post("http://api.okto-demo.eu/orders").
                //                content(orderRequestAsString)
                contentType("application/json")
                .accept("application/json"))
                .andExpect(status().isOk())
                .andReturn();

        OrderDto orderDto;
        // TODO: retrieve order dto from response
        // convert orderResult.getResponse().getContentAsString() to OrderDto using Jackson Object Mapper
        // make sure OrderDto contains correct data

    }

    
    void testOrderDoubleSubmission() {
        //TODO: write a test to trigger validation error when submit the same order twice (same client reference code)
    }

     @Test
    void testFetchAllOrders() throws Exception{
        //TODO: create 2 orders (by directly saving to database) and then invoke API call to fetch all orders
        // check that response contains 2 orders

        List<Order> orders = Arrays.asList(
                new Order("002", "AAAAAA", BigDecimal.valueOf(100.23), 3, OrderStatus.PROCESSED),
                new Order("001", "BBBBBBB", BigDecimal.valueOf(500.23), 13, OrderStatus.SUBMITTED));

        when(mockRepository.findAll()).thenReturn(orders);
        

         mockMvc.perform(get("/orders"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
         
         verify(mockRepository, times(1)).findAll();
    }

    
    @Test
    void testFetchCertainOrder() throws Exception{
        //TODO: create 1 order (by directly saving to database) and then invoke API call to fetch order
        // check response contains the correct order
        
        //TODO: write one more test to check that when an order not exists, server responds with http 400
    }
}
