package services;

import models.Klasa;
import models.Lenda;
import models.dto.create.CreateLenda;
import repositories.KlasaRepository;
import repositories.LendaRepository;
import utils.SceneNavigator;

import java.util.ArrayList;
import java.util.List;

public class LendaService {
    private final LendaRepository repository = new LendaRepository();
    private final SceneNavigator sn = new SceneNavigator();

    public boolean shto(CreateLenda lenda) {
        return repository.shto(lenda);
    }

    public int lookupId(String table, String column, String value) {
        return sn.lookupId(table, column, value);
    }
    public List<Lenda> gjejTeGjitha() {
        return repository.gjejTeGjitha();
    }
}

