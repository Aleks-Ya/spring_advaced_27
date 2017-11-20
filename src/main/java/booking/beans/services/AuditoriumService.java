package booking.beans.services;

import booking.beans.models.Auditorium;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Dmytro_Babichev
 * Date: 2/3/2016
 * Time: 11:04 AM
 */
public interface AuditoriumService {

    List<Auditorium> getAuditoriums();

    Auditorium getByName(String auditoriumName);

    Auditorium getById(Long auditoriumId);

    int getSeatsNumber(String auditoriumName);

    List<Integer> getVipSeats(String auditoriumName);

    Auditorium create(Auditorium auditorium);
}
