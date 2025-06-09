package models.dto.create;

public class CreateDrejtor {
    private String emri;
    private String mbiemri;
    private String email;
    private String tel;
    private int adresaId;
    private int shkollaId;

    public CreateDrejtor(String emri, String mbiemri, String email, String tel, int adresaId, int shkollaId) {
        this.emri = emri;
        this.mbiemri = mbiemri;
        this.email = email;
        this.tel = tel;
        this.adresaId = adresaId;
        this.shkollaId = shkollaId;
    }

    public String getEmri() { return emri; }
    public String getMbiemri() { return mbiemri; }
    public String getEmail() { return email; }
    public String getTel() { return tel; }
    public int getAdresaId() { return adresaId; }
    public int getShkollaId() { return shkollaId; }
}
