package guru.sfg.brewery.web.controllers;

import guru.sfg.brewery.repositories.BeerInventoryRepository;
import guru.sfg.brewery.repositories.BeerRepository;
import guru.sfg.brewery.repositories.CustomerRepository;
import guru.sfg.brewery.services.BeerService;
import guru.sfg.brewery.services.BreweryService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class BeerRestControllerIT extends BaseIT {

    @MockBean
    protected BeerRepository beerRepository;
    @MockBean
    protected BeerInventoryRepository beerInventoryRepository;
    @MockBean
    protected BreweryService breweryService;
    @MockBean
    protected CustomerRepository customerRepository;
    @MockBean
    protected BeerService beerService;

    @Test
    void deleteBeerByIdBadCredentialsURL() throws Exception {
        mockMvc.perform(delete("/api/v1/beer/97df8c39-98c4-4ae0-b663-453e8e19c311")
                .param("apiKey", "admin")
                .param("apiSecret", "passwordXXX"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void deleteBeerHttpBasicUserRole() throws Exception {
        mockMvc.perform(delete("/api/v1/beer/97df8c39-98c4-4ae0-b663-453e8e19c311")
                .with(httpBasic("user", "password")))
                .andExpect(status().isForbidden());
    }

    @Test
    void deleteBeerHttpBasicCustomerRole() throws Exception {
        mockMvc.perform(delete("/api/v1/beer/97df8c39-98c4-4ae0-b663-453e8e19c311")
                .with(httpBasic("scott", "tiger")))
                .andExpect(status().isForbidden());
    }

    @Test
    void deleteBeerByIdURL() throws Exception {
        mockMvc.perform(delete("/api/v1/beer/97df8c39-98c4-4ae0-b663-453e8e19c311")
                .param("apiKey", "admin")
                .param("apiSecret", "admin"))
                .andExpect(status().isOk());
    }

    @Test
    void deleteBeerByIdBadCredentials() throws Exception {
        mockMvc.perform(delete("/api/v1/beer/97df8c39-98c4-4ae0-b663-453e8e19c311")
                .header("Api-Key", "admin")
                .header("Api-Secret", "passwordXXX"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void deleteBeerById() throws Exception {
        mockMvc.perform(delete("/api/v1/beer/97df8c39-98c4-4ae0-b663-453e8e19c311")
                .header("Api-Key", "admin")
                .header("Api-Secret", "admin"))
                .andExpect(status().isOk());
    }

    @Test
    void deleteBeerHttpBasic() throws Exception {
        mockMvc.perform(delete("/api/v1/beer/97df8c39-98c4-4ae0-b663-453e8e19c311")
                .with(httpBasic("admin", "admin")))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    void deleteBeerNoAuth() throws Exception {
        mockMvc.perform(delete("/api/v1/beer/97df8c39-98c4-4ae0-b663-453e8e19c311"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void findBeer() throws Exception {
        mockMvc.perform(get("/api/v1/beer/"))
                .andExpect(status().isOk());
    }

    @Test
    void findBeerById() throws Exception {
        mockMvc.perform(get("/api/v1/beer/97df8c39-98c4-4ae0-b663-453e8e19c311"))
                .andExpect(status().isOk());
    }

    @Test
    void findBeerByUpc() throws Exception {
        mockMvc.perform(get("/api/v1/beerUpc/0631234200036"))
                .andExpect(status().isOk());
    }
}
