package models;

public class Drejtor {
    private int id;
    private String emri;
    private String mbiemri;
    private String email;
    private String tel;
    private Adresa adresa;
    private Shkolla shkolla;

    public Drejtor(int id, String emri, String mbiemri, String email, String tel, Adresa adresa, Shkolla shkolla) {
        this.id = id;
        this.emri = emri;
        this.mbiemri = mbiemri;
        this.email = email;
        this.tel = tel;
        this.adresa = adresa;
        this.shkolla = shkolla;
    }

    public Drejtor(String emri, String mbiemri, String email, String tel, Adresa adresa, Shkolla shkolla) {
        this.emri = emri;
        this.mbiemri = mbiemri;
        this.email = email;
        this.tel = tel;
        this.adresa = adresa;
        this.shkolla = shkolla;
    }

    public int getId() { return id; }
    public String getEmri() { return emri; }
    public String getMbiemri() { return mbiemri; }
    public String getEmail() { return email; }
    public String getTel() { return tel; }
    public Adresa getAdresaId() { return adresa; }
    public Shkolla getShkollaId() { return shkolla; }

    public void setId(int id) { this.id = id; }
    public void setEmri(String emri) { this.emri = emri; }
    public void setMbiemri(String mbiemri) { this.mbiemri = mbiemri; }
    public void setEmail(String email) { this.email = email; }
    public void setTel(String tel) { this.tel = tel; }
    public void setAdresa(Adresa adresa) { this.adresa = adresa; }
    public void setShkolla(Shkolla shkolla) { this.shkolla = shkolla; }
}
