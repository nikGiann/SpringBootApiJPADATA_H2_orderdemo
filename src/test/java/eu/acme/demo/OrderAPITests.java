package eu.acme.demo;

import static org.hamcrest.CoreMatchers.is;
import com.fasterxml.jackson.databind.ObjectMapper;
import eu.acme.demo.domain.Order;
import eu.acme.demo.domain.enums.OrderStatus;
import eu.acme.demo.repository.OrderRepository;
import eu.acme.demo.web.dto.OrderDto;
import java.math.BigDecimal;
import java.util.UUID;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
class OrderAPITests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private OrderRepository orderRepository;

        
    @Test
    void testOrderAPI() throws Exception {

        //TODO: submit order request
        // 1. create order request
        Order order = new Order("111111", "Test the Test", BigDecimal.valueOf(100.23), 3, OrderStatus.PROCESSED);
        
        // 2. convert to json string using Jackson Object Mapper
            String jsonString = objectMapper.writeValueAsString(order);
            
        // 3. set json string to content param
        MvcResult orderResult = this.mockMvc.perform(post("http://api.okto-demo.eu/orders")
                //                content(orderRequestAsString)
                .content(jsonString)
                .contentType("application/json")
                .accept("application/json"))
                .andExpect(status().isOk())
                .andReturn();

        OrderDto orderDto;
        
        
        //TODO: retrieve order dto from response
        String jsonStringToObject = orderResult.getResponse().getContentAsString();
        
        //convert orderResult.getResponse().getContentAsString() to OrderDto using Jackson Object Mapper
        orderDto = objectMapper.readValue(jsonStringToObject, OrderDto.class);
        
        //make sure OrderDto contains correct data
        assertEquals(order.getClientReferenceCode(), orderDto.getClientReferenceCode());
        assertEquals(order.getDescription(), orderDto.getDescription());
        assertEquals(order.getItemCount(), orderDto.getItemCount());
        assertEquals(order.getItemTotalAmount(), orderDto.getTotalAmount());
        assertEquals(order.getStatus(), orderDto.getStatus());
        

    }

    
    @Test
    void testOrderDoubleSubmission() throws Exception {
        //TODO: write a test to trigger validation error when submit the same order twice (same client reference code)
        Order order = new Order();
        order.setClientReferenceCode("111111");
        order.setDescription("Test the Test");
        order.setItemCount(3);
        order.setItemTotalAmount(BigDecimal.valueOf(35));
        order.setStatus(OrderStatus.valueOf("UNDER_PROCESS"));
        orderRepository.save(order);
        
        String jsonAsString = objectMapper.writeValueAsString(order);
        
        mockMvc.perform(post("/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonAsString)      // .content("{\"clientReferenceCode\": \"111111\" , \"description\" : \"Test the Test\", \"itemTotalAmount\": 35 , \"itemCount\": 3 , \"status\": \"UNDER_PROCESS\"}")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
    
  
    @Test
    void testFetchAllOrders() throws Exception {
        //TODO: create 2 orders (by directly saving to database) and then invoke API call to fetch all orders
        // check that response contains 2 orders

        Order o1 = new Order("002", "Test the Test", BigDecimal.valueOf(100.23), 3, OrderStatus.PROCESSED);
        orderRepository.saveAndFlush(o1);
        Order o2 = new Order("001", "Testing the Test", BigDecimal.valueOf(500.23), 13, OrderStatus.SUBMITTED);
        orderRepository.saveAndFlush(o2);

        mockMvc.perform(get("/orders").contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));

    }

    @Test
    void testFetchCertainOrder() throws Exception {
        //TODO: create 1 order (by directly saving to database) and then invoke API call to fetch order
        // check response contains the correct order
        //         String clientReferenceCode, String description, BigDecimal itemTotalAmount, int itemCount, OrderStatus status
        
          Order o1 = new Order("002", "Test the Test", BigDecimal.valueOf(100.23), 3, OrderStatus.PROCESSED);
        orderRepository.saveAndFlush(o1);
        UUID newId = o1.getId();
        
        mockMvc.perform(get("/orders/" + newId.toString()))
                .andDo(print())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(newId.toString())))
                .andExpect(jsonPath("$.clientReferenceCode", is("002")))
                .andExpect(jsonPath("$.description", is("Test the Test")))
                .andExpect(jsonPath("$.totalAmount", is(100.23)))
                .andExpect(jsonPath("$.itemCount", is(3)))
                .andExpect(jsonPath("$.status", is("PROCESSED")));
 
    }
    
    
    @Test
    void testFetchOrdersDoesNotExist() throws Exception {
        //TODO: write one more test to check that when an order not exists, server responds with http 400
        
        mockMvc.perform(get("/orders/123456789") 
          .accept(MediaType.APPLICATION_JSON))
          .andExpect(status().isBadRequest());
    }
    
}
