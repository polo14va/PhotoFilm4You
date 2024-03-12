package edu.uoc.epcsd.user.entities;


import edu.uoc.epcsd.user.entities.Alert;
import edu.uoc.epcsd.user.entities.User;
import edu.uoc.epcsd.user.repositories.AlertRepository;
import edu.uoc.epcsd.user.services.AlertService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
class AlertUnitTest {

    private final Long PRODUCT_ID = 1L;
    private final Long USER_ID = 1L;
    private final Long USER_ID2 = 2L;
    private final Long ALERT_ID = 1L;

   
    private static final Long PRODUCT1_ID = 1L;
    private static final Long PRODUCT2_ID = 2L;
    private static final Long PRODUCT3_ID = 3L;

    private final LocalDate START_DATE = LocalDate.of(2023, 11, 13);
    private final LocalDate END_DATE = LocalDate.of(2023, 12, 13);

    @Mock
    private AlertRepository alertRepository;

    @InjectMocks
    private AlertService alertService;


    @Test
     void whenValidAlertid_thenAlertShouldBeFound() {

        User user = User.builder().id(USER_ID).email("ejemplo@gmail.com").password("1234").phoneNumber("685842338").build();
        Alert alert = Alert.builder().from(START_DATE).to(END_DATE).productId(PRODUCT_ID).user(user).build();

        //Alert alert = new Alert(ALERT_ID, START_DATE, END_DATE, PRODUCT_ID, user.getId());

        when(alertRepository.findById((ALERT_ID))).thenReturn(Optional.of(alert));

        Optional<Alert> _alert = alertService.findById(ALERT_ID);
        assertThat(_alert.isPresent()).isTrue();
        assertEquals(alert, _alert.get());

    }

    @Test
    void whenInvalidAlertId_thenExceptionShow() {

        Long WRONG_ALERT_ID = 10L;
        when(alertRepository.findById(WRONG_ALERT_ID)).thenReturn(Optional.empty());

        Optional<Alert> alert = alertService.findById(WRONG_ALERT_ID);

        assertThat(alert.isEmpty()).isTrue();

    }

    @Test
    void whenFindAllAlertsByProductAndDate_thenSouldReturnAll() throws Exception {

        User user = User.builder().id(USER_ID).email("ejemplo@gmail.com").password("1234").phoneNumber("685842338").build();
        User user2 = User.builder().id(USER_ID2).email("ejemplo2@gmail.com").password("1234").phoneNumber("685842338").build();

        Alert alert = Alert.builder().from(START_DATE).to(END_DATE).productId(PRODUCT_ID).user(user).build();
        Alert alert2 = Alert.builder().from(START_DATE).to(END_DATE).productId(PRODUCT_ID).user(user2).build();
        Alert alert3 = Alert.builder().from(START_DATE).to(END_DATE).productId(PRODUCT_ID).user(user2).build();


        List<Alert> alerts = Arrays.asList(alert, alert2, alert3);

        when(alertRepository.findByProductIdAndAlertDate(PRODUCT_ID, START_DATE)).thenReturn(alerts);

        List<Alert> list = alertService.getAlertsByProductAndDate(PRODUCT_ID, START_DATE);
        System.out.println(list.size());
        assertEquals(list.size(), alerts.size());
        
        assertTrue(user.isEnabled());
        assertTrue(user.isCredentialsNonExpired());
        assertTrue(user.isAccountNonExpired());
        assertTrue(user.isAccountNonLocked());
        assertEquals("ejemplo@gmail.com", user.getUsername());

    }


    @Test
   void whenFindAlertsByUserAndInterval_then_shouldReturnAlle() {


        User user = User.builder().id(USER_ID).email("ejemplo@gmail.com").password("1234").phoneNumber("685842338").build();

        Alert alert = Alert.builder().from(START_DATE).to(END_DATE).productId(PRODUCT1_ID).user(user).build();
        Alert alert2 = Alert.builder().from(START_DATE).to(END_DATE).productId(PRODUCT2_ID).user(user).build();
        Alert alert3 = Alert.builder().from(START_DATE).to(END_DATE).productId(PRODUCT3_ID).user(user).build();
        List<Alert> alerts = Arrays.asList(alert, alert2, alert3);

        when(alertRepository.findByUserIdAndDateInterval(USER_ID, START_DATE, END_DATE)).thenReturn(alerts);

        List<Alert> result = alertService.getAlertsByUserAndDateInterval(USER_ID, START_DATE, END_DATE);
        assertThat(result.isEmpty()).isFalse();
        assertEquals(result.size(), alerts.size());
    }

}
