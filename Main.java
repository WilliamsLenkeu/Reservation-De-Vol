import java.util.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Scanner;

public class Main{

    private static List<Vol> vols = new ArrayList<>();
    private static List<Passager> passagers = new ArrayList<>();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean exit = false;

        while (!exit) {
            System.out.println("==== Systeme de Reservation de Vols ====");
            System.out.println("1. Enregistrement d'un vol");
            System.out.println("2. Enregistrement d'un passager");
            System.out.println("3. Consultation des vols disponibles et des passagers ayant reserve");
            System.out.println("4. Recherche des vols disponibles");
            System.out.println("5. Reservation d'un vol");
            System.out.println("6. Annulation d'une reservation");
            System.out.println("0. Quitter");
            System.out.print("Choisissez une option : ");

            int option;
            if (scanner.hasNextInt()) {
                option = scanner.nextInt();
            } else {
                System.out.println("Option invalide. Veuillez entrer un entier valide.");
                scanner.nextLine();
                continue;
            }

            switch (option) {
                case 1:
                    System.out.println();
                    enregistrerVol(scanner);
                    System.out.println();
                    break;
                case 2:
                    System.out.println();
                    enregistrerPassager(scanner);
                    System.out.println();
                    break;
                case 3:
                    System.out.println();
                    consulterVolsEtPassagers();
                    System.out.println();
                    break;
                case 4:
                    System.out.println();
                    rechercherVolsDisponibles(scanner);
                    System.out.println();
                    break;
                case 5:
                    System.out.println();
                    reserverVol(scanner);
                    System.out.println();
                    break;
                case 6:
                    System.out.println();
                    annulerReservation(scanner);
                    System.out.println();
                    break;
                case 0:
                    exit = true;
                    break;
                default:
                    System.out.println();
                    System.out.println("Option invalide. Veuillez choisir une option valide.");
                    System.out.println();
                    break;
            }
        }

        scanner.close();
        System.out.println("Programme termine.");
    }

    private static void enregistrerVol(Scanner scanner) {
        System.out.println("==== Enregistrement d'un vol ====");
    
        String numeroVol = null;
        String compagnieAerienne = "";
        Date dateDepart = null;
        String heureDepart = "";
        String destination = "";
    
        boolean champsValides = false;
    
        while (!champsValides) {
            try {
                System.out.print("Numero de vol : ");
                numeroVol = scanner.nextLine();
    
                if (numeroVol.isEmpty()) {
                    throw new IllegalArgumentException("Le champ 'Numero de vol' ne peut pas etre vide.");
                }
    
                if (volExists(numeroVol)) {
                    throw new IllegalArgumentException("Le numero de vol existe déja.");
                }
    
                System.out.print("Compagnie aerienne : ");
                compagnieAerienne = scanner.nextLine();
    
                if (compagnieAerienne.isEmpty()) {
                    throw new IllegalArgumentException("Le champ 'Compagnie aerienne' ne peut pas etre vide.");
                }
    
                System.out.print("Date de depart (jj/mm/aaaa) : ");
                String dateDepartStr = scanner.nextLine();
    
                if (dateDepartStr.isEmpty()) {
                    throw new IllegalArgumentException("Le champ 'Date de depart' ne peut pas etre vide.");
                }
    
                if (!isValidDate(dateDepartStr)) {
                    throw new IllegalArgumentException("Le format de la date est invalide. Veuillez utiliser le format 'jj/mm/aaaa'.");
                }
    
                dateDepart = parseDate(dateDepartStr);
                Date currentDate = new Date();
    
                if (dateDepart.compareTo(currentDate) <= 0) {
                    throw new IllegalArgumentException("La date de depart doit etre ultérieure a la date actuelle.");
                }
    
                boolean heureValide = false;
                while (!heureValide) {
                    System.out.print("Heure de depart (hh:mm) : ");
                    String heureInput = scanner.nextLine();
                    String[] heureParts = heureInput.split(":");
    
                    if (heureParts.length != 2) {
                        throw new IllegalArgumentException("Format d'heure invalide. Veuillez utiliser le format 'hh:mm' (heures entre 0 et 23, minutes entre 0 et 59).");
                    }
    
                    int heures = Integer.parseInt(heureParts[0]);
                    int minutes = Integer.parseInt(heureParts[1]);
    
                    if (heures < 0 || heures > 23 || minutes < 0 || minutes > 59) {
                        throw new IllegalArgumentException("Format d'heure invalide. Veuillez utiliser le format 'hh:mm' (heures entre 0 et 23, minutes entre 0 et 59).");
                    }
    
                    heureDepart = heureInput;
                    heureValide = true;
                }
    
                System.out.print("Destination : ");
                destination = scanner.nextLine();
    
                if (destination.isEmpty()) {
                    throw new IllegalArgumentException("Le champ 'Destination' ne peut pas etre vide.");
                }
    
                champsValides = true;
            } catch (IllegalArgumentException e) {
                System.out.println("Erreur : " + e.getMessage());
            }
        }
    
        Vol vol = new Vol(numeroVol, compagnieAerienne, dateDepart, heureDepart, destination);
        vols.add(vol);
    
        System.out.println("Vol enregistre avec succes.");
    }
    
    private static boolean volExists(String numeroVol) {
        for (Vol vol : vols) {
            if (vol.getNumeroVol().equals(numeroVol)) {
                return true;
            }
        }
        return false;
    }    

    private static boolean isValidDate(String dateStr) {
        try {
            String[] parts = dateStr.split("/");
            int day = Integer.parseInt(parts[0]);
            int month = Integer.parseInt(parts[1]);
            int year = Integer.parseInt(parts[2]);

            Calendar calendar = Calendar.getInstance();
            calendar.setLenient(false);
            calendar.set(year, month - 1, day);
            calendar.getTime();

            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private static Date parseDate(String dateStr) {
        String[] parts = dateStr.split("/");
        int day = Integer.parseInt(parts[0]);
        int month = Integer.parseInt(parts[1]);
        int year = Integer.parseInt(parts[2]);

        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month - 1, day);
        return calendar.getTime();
    }

    private static void enregistrerPassager(Scanner scanner) {
        System.out.println("==== Enregistrement d'un passager ====");
    
        String nom = "";
        String adresse = "";
        String numeroPasseport = "";
    
        boolean champsValides = false;
    
        while (!champsValides) {
            try {
                System.out.print("Nom : ");
                nom = scanner.nextLine();
    
                if (nom.isEmpty()) {
                    throw new IllegalArgumentException("Le champ 'Nom' ne peut pas etre vide.");
                }
    
                System.out.print("Adresse : ");
                adresse = scanner.nextLine();
    
                if (adresse.isEmpty()) {
                    throw new IllegalArgumentException("Le champ 'Adresse' ne peut pas etre vide.");
                }
    
                System.out.print("Numero de passeport : ");
                numeroPasseport = scanner.nextLine();
    
                if (numeroPasseport.isEmpty()) {
                    throw new IllegalArgumentException("Le champ 'Numero de passeport' ne peut pas etre vide.");
                }
    
                if (passagerExists(numeroPasseport)) {
                    throw new IllegalArgumentException("Le numero de passeport existe déja.");
                }
    
                champsValides = true;
            } catch (IllegalArgumentException e) {
                System.out.println("Erreur : " + e.getMessage());
            }
        }
    
        Passager passager = new Passager(nom, adresse, numeroPasseport);
        passagers.add(passager);
    
        System.out.println("Passager enregistre avec succes.");
    }
    
    private static boolean passagerExists(String numeroPasseport) {
        for (Passager passager : passagers) {
            if (passager.getNumeroPasseport().equals(numeroPasseport)) {
                return true;
            }
        }
        return false;
    }
    
    private static void consulterVolsEtPassagers() {
        System.out.println("==== Vols disponibles ====");
        for (Vol vol : vols) {
            System.out.println("Numero de vol : " + vol.getNumeroVol());
            System.out.println("Compagnie aerienne : " + vol.getCompagnieAerienne());
            System.out.println("Date de depart : " + vol.getDateDepart());
            System.out.println("Heure de depart : " + vol.getHeureDepart());
            System.out.println("Destination : " + vol.getDestination());
            System.out.println("Passagers :");
            List<Passager> passagers = vol.getPassagers();
            if (passagers.isEmpty()) {
                System.out.println("Aucun passager n'a reserve ce vol.");
            } else {
                for (Passager passager : passagers) {
                    System.out.println("- " + passager.getNom() + " (" + passager.getNumeroPasseport() + ")");
                }
            }
            System.out.println("--------------------");
        }
    }

    private static void rechercherVolsDisponibles(Scanner scanner) {
        System.out.println("==== Recherche des vols disponibles ====");
    
        String destination = "";
    
        boolean champsValides = false;
    
        while (!champsValides) {
            try {
                System.out.print("Destination : ");
                destination = scanner.nextLine();
    
                if (destination.isEmpty()) {
                    throw new IllegalArgumentException("Le champ 'Destination' ne peut pas être vide.");
                }
    
                champsValides = true;
            } catch (IllegalArgumentException e) {
                System.out.println("Erreur : " + e.getMessage());
            } catch (Exception e) {
                System.out.println("Une erreur s'est produite lors de la saisie de la destination. Veuillez réessayer.");
            }
        }
    
        System.out.println("Vols disponibles pour la destination '" + destination + "' :");
        boolean found = false;
        for (Vol vol : vols) {
            if (vol.getDestination().equalsIgnoreCase(destination)) {
                System.out.println("Numero de vol : " + vol.getNumeroVol());
                System.out.println("Compagnie aérienne : " + vol.getCompagnieAerienne());
                System.out.println("Date de départ : " + formatDate(vol.getDateDepart()));
                System.out.println("Heure de départ : " + vol.getHeureDepart());
                System.out.println("--------------------");
                found = true;
            }
        }
        if (!found) {
            System.out.println("Aucun vol disponible pour la destination '" + destination + "'.");
        }
    }
    
    private static void reserverVol(Scanner scanner) {
        System.out.println("==== Reservation d'un vol ====");
    
        String numeroPasseport = "";
    
        boolean champsValides = false;
    
        while (!champsValides) {
            try {
                System.out.print("Numero de passeport du passager : ");
                numeroPasseport = scanner.nextLine();
    
                if (numeroPasseport.isEmpty()) {
                    throw new IllegalArgumentException("Le champ 'Numero de passeport' ne peut pas etre vide.");
                }
    
                champsValides = true;
            } catch (IllegalArgumentException e) {
                System.out.println("Erreur : " + e.getMessage());
            }
        }
    
        System.out.println("Liste des vols disponibles :");
        for (int i = 0; i < vols.size(); i++) {
            Vol vol = vols.get(i);
            System.out.println((i + 1) + ": " + vol.getNumeroVol() + " - Destination: " + vol.getDestination() + ", heure de depart: " + vol.getHeureDepart());
        }
    
        int choixVol = 0;
        boolean volValide = false;
    
        while (!volValide) {
            try {
                System.out.print("Choisissez un numero de vol : ");
                choixVol = Integer.parseInt(scanner.nextLine());
    
                if (choixVol < 1 || choixVol > vols.size()) {
                    throw new IllegalArgumentException("Numero de vol invalide. Veuillez choisir un numero valide.");
                }
    
                volValide = true;
            } catch (NumberFormatException e) {
                System.out.println("Erreur : Veuillez entrer un nombre valide.");
            } catch (IllegalArgumentException e) {
                System.out.println("Erreur : " + e.getMessage());
            }
        }
    
        Vol volSelectionne = null;
        try {
            volSelectionne = vols.get(choixVol - 1);
        } catch (IndexOutOfBoundsException e) {
            System.out.println("Le vol specifie n'existe pas. Verifiez le numero de vol choisi.");
            return;
        }
    
        Passager passagerSelectionne = null;
        for (Passager passager : passagers) {
            if (passager.getNumeroPasseport().equals(numeroPasseport)) {
                passagerSelectionne = passager;
                break;
            }
        }
    
        if (passagerSelectionne == null) {
            System.out.println("Le passager specifie n'existe pas. Verifiez les informations saisies.");
        } else {
            if (volSelectionne.getPassagers().contains(passagerSelectionne)) {
                System.out.println("Ce passager est deja enregistre sur ce vol.");
            } else {
                volSelectionne.ajouterPassager(passagerSelectionne);
                System.out.println("Reservation effectuee avec succes pour le passager " + passagerSelectionne.getNom() + " sur le vol " + volSelectionne.getNumeroVol() + ".");
            }
        }
    }
        
    private static void annulerReservation(Scanner scanner) {
        System.out.println("==== Annulation d'une reservation ====");

        String numeroPasseport = "";

        boolean champsValides = false;

        while (!champsValides) {
            try {
                System.out.print("Numero de passeport du passager : ");
                numeroPasseport = scanner.nextLine();

                if (numeroPasseport.isEmpty()) {
                    throw new IllegalArgumentException("Le champ 'Numero de passeport' ne peut pas etre vide.");
                }

                champsValides = true;
            } catch (IllegalArgumentException e) {
                System.out.println("Erreur : " + e.getMessage());
            }
        }

        Passager passagerSelectionne = null;
        for (Passager passager : passagers) {
            if (passager.getNumeroPasseport().equals(numeroPasseport)) {
                passagerSelectionne = passager;
                break;
            }
        }

        if (passagerSelectionne == null) {
            System.out.println("Le passager spécifié n'existe pas. Vérifiez les informations saisies.");
        } else {
            System.out.println("Liste des vols réservés par le passager " + passagerSelectionne.getNom() + " :");
            List<Vol> volsReserves = new ArrayList<>();
            int i = 1;
            for (Vol vol : vols) {
                if (vol.getPassagers().contains(passagerSelectionne)) {
                    volsReserves.add(vol);
                    System.out.println(i + ". " + vol.getNumeroVol());
                    i++;
                }
            }

            if (volsReserves.isEmpty()) {
                System.out.println("Le passager n'a effectué aucune réservation.");
            } else {
                boolean volValide = false;
                while (!volValide) {
                    try {
                        System.out.print("Entrez le numéro du vol que vous souhaitez annuler : ");
                        int choix = Integer.parseInt(scanner.nextLine());

                        if (choix < 1 || choix > volsReserves.size()) {
                            throw new IllegalArgumentException("Numéro de vol invalide.");
                        }

                        Vol volSelectionne = volsReserves.get(choix - 1);
                        volSelectionne.supprimerPassager(passagerSelectionne);
                        System.out.println("Réservation annulée avec succès pour le passager " + passagerSelectionne.getNom() + " sur le vol " + volSelectionne.getNumeroVol() + ".");
                        volValide = true;
                    } catch (NumberFormatException e) {
                        System.out.println("Erreur : Veuillez entrer un numéro valide.");
                    } catch (IllegalArgumentException e) {
                        System.out.println("Erreur : " + e.getMessage());
                    }
                }
            }
        }
    }
}