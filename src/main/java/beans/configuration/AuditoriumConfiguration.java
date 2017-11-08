package beans.configuration;

import beans.models.Auditorium;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.util.Arrays;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Dmytro_Babichev
 * Date: 2/12/2016
 * Time: 1:36 PM
 */
@Configuration
@PropertySource({"classpath:auditoriums/blueHall.properties", "classpath:auditoriums/redHall.properties",
                 "classpath:auditoriums/yellowHall.properties"})
public class AuditoriumConfiguration {

    @Value("${blue.hall.name}")
    private String blueHallName;

    @Value("${blue.hall.seatsNumber}")
    private int blueHallSeatsNumber;

    @Value("#{'${blue.hall.vipSeats}'.split(',')}")
    private List<Integer> blueHallVipSeats;


    @Value("${red.hall.name}")
    private String redHallName;

    @Value("${red.hall.seatsNumber}")
    private int redHallSeatsNumber;

    @Value("#{'${red.hall.vipSeats}'.split(',')}")
    private List<Integer> redHallVipSeats;


    @Value("${yellow.hall.name}")
    private String yellowHallName;

    @Value("${yellow.hall.seatsNumber}")
    private int yellowHallSeatsNumber;

    @Value("#{'${yellow.hall.vipSeats}'.split(',')}")
    private List<Integer> yellowHallVipSeats;

    @Bean
    public Auditorium blueHall() {
        return new Auditorium(blueHallName, blueHallSeatsNumber, blueHallVipSeats);
    }

    @Bean
    public Auditorium redHall() {
        return new Auditorium(redHallName, redHallSeatsNumber, redHallVipSeats);
    }

    @Bean
    public Auditorium yellowHall() {
        return new Auditorium(yellowHallName, yellowHallSeatsNumber, yellowHallVipSeats);
    }

    @Bean(name = "auditoriumList")
    public List<Auditorium> auditoriumList() {
        return Arrays.asList(blueHall(), redHall(), yellowHall());
    }
}
