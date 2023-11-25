public class Passager {
    private String nom;
    private String adresse;
    private String numeroPasseport;

    // Constructeur
    public Passager(String nom, String adresse, String numeroPasseport) {
        this.nom = nom;
        this.adresse = adresse;
        this.numeroPasseport = numeroPasseport;
    }

    // Getters et setters
    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getNumeroPasseport() {
        return numeroPasseport;
    }

    public void setNumeroPasseport(String numeroPasseport) {
        this.numeroPasseport = numeroPasseport;
    }
}

