import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Vol {
    private String numeroVol;
    private String compagnieAerienne;
    private Date dateDepart;
    private String heureDepart;
    private String destination;
    private List<Passager> passagers;

    // Constructeur
    public Vol(String numeroVol, String compagnieAerienne, Date dateDepart, String heureDepart, String destination) {
        this.numeroVol = numeroVol;
        this.compagnieAerienne = compagnieAerienne;
        this.dateDepart = dateDepart;
        this.heureDepart = heureDepart;
        this.destination = destination;
        this.passagers = new ArrayList<>();
    }

    // Getters et setters
    public String getNumeroVol() {
        return numeroVol;
    }

    public void setNumeroVol(String numeroVol) {
        this.numeroVol = numeroVol;
    }

    public String getCompagnieAerienne() {
        return compagnieAerienne;
    }

    public void setCompagnieAerienne(String compagnieAerienne) {
        this.compagnieAerienne = compagnieAerienne;
    }

    public Date getDateDepart() {
        return dateDepart;
    }

    public void setDateDepart(Date dateDepart) {
        this.dateDepart = dateDepart;
    }

    public String getHeureDepart() {
        return heureDepart;
    }

    public void setHeureDepart(String heureDepart) {
        this.heureDepart = heureDepart;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public List<Passager> getPassagers() {
        return passagers;
    }

    public void ajouterPassager(Passager passager) {
        passagers.add(passager);
    }

    public void supprimerPassager(Passager passager) {
        passagers.remove(passager);
    }
}