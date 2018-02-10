package booking.service;

import booking.domain.Auditorium;

import java.util.List;

public interface AuditoriumService {

    List<Auditorium> getAll();

    //TODO remove and remove "id" and "name" from controller paths
    Auditorium getByName(String auditoriumName);

    Auditorium getById(Long auditoriumId);

    int getSeatsNumber(String auditoriumName);

    List<Integer> getVipSeats(String auditoriumName);

    Auditorium create(Auditorium auditorium);

    void delete(Long auditoriumId);
}
