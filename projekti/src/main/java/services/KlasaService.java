package services;

import models.Klasa;
import models.dto.create.CreateKlasa;
import repositories.KlasaRepository;
import utils.SceneNavigator;

import java.util.List;

public class KlasaService {
    private final KlasaRepository repository = new KlasaRepository();
    private final SceneNavigator sn = new SceneNavigator();

    public boolean shtoKlasa(CreateKlasa klasa) {
        return repository.shtoKlasa(klasa);
    }


    public int lookupId(String table, String column, String value) {
        return sn.lookupId(table, column, value);
    }
    public List<Klasa> gjejTeGjitha() {
        return repository.gjejTeGjitha();
    }

}
