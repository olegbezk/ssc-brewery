package guru.sfg.brewery.web.controllers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.anonymous;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

public class ControllersIT extends BaseIT {

    @Test
    void initCreationFormWithAdmin() throws Exception {
        mockMvc.perform(get("/beers/new").with(httpBasic("admin", "admin")))
                .andExpect(status().isOk())
                .andExpect(view().name("beers/createBeer"))
                .andExpect(model().attributeExists("beer"));
    }

    @Test
    void initCreationFormWithScottCredentials() throws Exception {
        mockMvc.perform(get("/beers/new").with(httpBasic("scott", "tiger")))
                .andExpect(status().isOk())
                .andExpect(view().name("beers/createBeer"))
                .andExpect(model().attributeExists("beer"));
    }

    @Test
    void initCreationForm() throws Exception {
        mockMvc.perform(get("/beers/new").with(httpBasic("admin", "admin")))
                .andExpect(status().isOk())
                .andExpect(view().name("beers/createBeer"))
                .andExpect(model().attributeExists("beer"));
    }

    @ParameterizedTest(name = "#{index} with [{arguments}]")
    @MethodSource("guru.sfg.brewery.web.controllers.BeerControllerIT#getStreamAllUsers")
    void findBeers(String user, String pwd) throws Exception {
        mockMvc.perform(get("/beers/find").with(httpBasic(user, pwd)))
                .andExpect(status().isOk())
                .andExpect(view().name("beers/findBeers"))
                .andExpect(model().attributeExists("beer"));
    }

    @Test
    void findCustomersWithHttpBasic() throws Exception {
        mockMvc.perform(get("/customers/find").with(httpBasic("admin", "admin")))
                .andExpect(status().isOk())
                .andExpect(view().name("customers/findCustomers"))
                .andExpect(model().attributeExists("customer"));
    }

    @Test
    void findBeersPermitByRoleAdmin() throws Exception {
        mockMvc.perform(get("/beers/find")
                .with(httpBasic("admin", "admin")))
                .andExpect(status().isOk())
                .andExpect(view().name("beers/findBeers"))
                .andExpect(model().attributeExists("beer"));
    }

    @Test
    void findBeersPermitByRoleUser() throws Exception {
        mockMvc.perform(get("/beers/find")
                .with(httpBasic("user", "password")))
                .andExpect(status().isOk())
                .andExpect(view().name("beers/findBeers"))
                .andExpect(model().attributeExists("beer"));
    }

    @Test
    void findBeersPermitByRoleCustomer() throws Exception {
        mockMvc.perform(get("/beers/find")
                .with(httpBasic("scott", "tiger")))
                .andExpect(status().isOk())
                .andExpect(view().name("beers/findBeers"))
                .andExpect(model().attributeExists("beer"));
    }

    @Test
    void findBeersPermitByRole() throws Exception {
        mockMvc.perform(get("/beers/find")
                .with(anonymous()))
                .andExpect(status().isUnauthorized());
    }
}
