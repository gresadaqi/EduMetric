package models.dto.create;

public class CreateLenda {
    private String emri;
    private int drejtimiId;
    private int periodaId;
    private int mesuesiId;

    public CreateLenda(String emri, int drejtimiId, int periodaId, int mesuesiId) {
        this.emri = emri;
        this.drejtimiId = drejtimiId;
        this.periodaId = periodaId;
        this.mesuesiId = mesuesiId;
    }

    public String getEmri() {
        return emri;
    }

    public int getDrejtimi() {
        return drejtimiId;
    }

    public int getPerioda() {
        return periodaId;
    }

    public int getMesuesi() {
        return mesuesiId;
    }
}
