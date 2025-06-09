package services;

import models.Drejtor;
import models.Klasa;
import models.dto.create.CreateKlasa;
import repositories.DrejtoriRepository;
import utils.SceneNavigator;

import java.util.List;

public class DrejtorService {
    private final DrejtoriRepository repository = new DrejtoriRepository();
    private final SceneNavigator sn = new SceneNavigator();
    public int lookupId(String table, String column, String value) {
        return sn.lookupId(table, column, value);
    }
    public List<Drejtor> gjejTeGjithe() {
        return repository.gjejTeGjithe();
    }
}
