package repositories;

import models.Player;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PlayerRep extends CrudRepository<Player, Long> {


    public List<Player> findByNameContains(String name);
}
