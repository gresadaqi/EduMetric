package services;

import database.DBConnector;
import models.Mesuesi;
import models.dto.create.CreateMesuesi;
import models.dto.update.UpdateMesuesi;
import repositories.MesuesiRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MesuesiService {

    private final MesuesiRepository mesuesiRepository = new MesuesiRepository();

    public boolean shto(CreateMesuesi m) {
        return mesuesiRepository.shto(m);
    }

    public boolean perditeso(UpdateMesuesi m) {
        return mesuesiRepository.perditeso(m);
    }
    public List<Mesuesi> gjejTeGjitheMesuesit() {
        return mesuesiRepository.gjejTeGjithe();
    }
}