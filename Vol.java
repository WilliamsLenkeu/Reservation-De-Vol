import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Vol {
    private String numeroVol;
    private String compagnieAerienne;
    private String destinationDepart;  // Destination de départ
    private String destinationArrivee;  // Destination d'arrivée
    private Date dateDepart;
    private String heureDepart;
    private Date dateArrivee;  // Date d'arrivée
    private String heureArrivee;  // Heure d'arrivée
    private int placesDisponibles;  // Nombre de places disponibles
    private List<Passager> passagers;  // Passagers dans le vol

    // Constructeur
    public Vol(String numeroVol, String compagnieAerienne, String destinationDepart, String destinationArrivee,
               Date dateDepart, String heureDepart, Date dateArrivee, String heureArrivee, int placesDisponibles) {
        this.numeroVol = numeroVol;
        this.compagnieAerienne = compagnieAerienne;
        this.destinationDepart = destinationDepart;
        this.destinationArrivee = destinationArrivee;
        this.dateDepart = dateDepart;
        this.heureDepart = heureDepart;
        this.dateArrivee = dateArrivee;
        this.heureArrivee = heureArrivee;
        this.placesDisponibles = placesDisponibles;
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

    public String getDestinationDepart() {
        return destinationDepart;
    }

    public void setDestinationDepart(String destinationDepart) {
        this.destinationDepart = destinationDepart;
    }

    public String getDestinationArrivee() {
        return destinationArrivee;
    }

    public void setDestinationArrivee(String destinationArrivee) {
        this.destinationArrivee = destinationArrivee;
    }

    public Date getDateArrivee() {
        return dateArrivee;
    }

    public void setDateArrivee(Date dateArrivee) {
        this.dateArrivee = dateArrivee;
    }

    public String getHeureArrivee() {
        return heureArrivee;
    }

    public void setHeureArrivee(String heureArrivee) {
        this.heureArrivee = heureArrivee;
    }

    public int getPlacesDisponibles() {
        return placesDisponibles;
    }

    public void setPlacesDisponibles(int placesDisponibles) {
        this.placesDisponibles = placesDisponibles;
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