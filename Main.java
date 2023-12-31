import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.*;
import java.sql.*;

public class Main{
    private static final String DB_URL = "jdbc:mysql://localhost:3306/bd_reservation_vols";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";
    private static  final Connection connection;

    static {
        try {
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static List<Vol> vols = new ArrayList<>();
    private static List<Passager> passagers = new ArrayList<>();
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean exit = false;

        while (!exit) {
            
            System.out.println("======================================");
            System.out.println("==== Système de Réservation de Vols ===");
            System.out.println("======================================");
            System.out.println("1. Enregistrement d'un vol");
            System.out.println("2. Enregistrement d'un passager");
            System.out.println("3. Consultation des vols disponibles et des passagers ayant réservé");
            System.out.println("4. Recherche des vols disponibles");
            System.out.println("5. Réservation d'un vol");
            System.out.println("6. Annulation d'une réservation");
            System.out.println("0. Quitter");
            System.out.println("======================================");
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
                    
                    enregistrerVol(scanner);
                    break;
                case 2:
                    
                    enregistrerPassager(scanner);

                    break;
                case 3:
                    
                    consulterVolsEtPassagers();

                    break;
                case 4:
                    
                    rechercherVolsDisponibles(scanner);

                    break;
                case 5:
                    
                    reserverVol(scanner);

                    break;
                case 6:
                    
                    annulerReservation(scanner);

                    break;
                case 0:
                    exit = true;
                    break;
                default:

                    System.out.println("Option invalide. Veuillez choisir une option valide.");

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
        String destinationDepart = "";
        String destinationArrivee = "";
        Date dateDepart = null;
        String heureDepart = "";
        Date dateArrivee = null;
        String heureArrivee = "";
        int placesDisponibles = 0;

        boolean champsValides = false;

        while (!champsValides) {
            try {
                System.out.print("Numero de vol : ");
                numeroVol = scanner.nextLine();

                if (numeroVol.isEmpty()) {
                    throw new IllegalArgumentException("Le champ 'Numero de vol' ne peut pas etre vide.");
                }

                if (volExists(numeroVol)) {
                    throw new IllegalArgumentException("Le numero de vol existe deja.");
                }

                System.out.print("Compagnie aerienne : ");
                compagnieAerienne = scanner.nextLine();

                if (compagnieAerienne.isEmpty()) {
                    throw new IllegalArgumentException("Le champ 'Compagnie aerienne' ne peut pas etre vide.");
                }

                System.out.print("Destination de départ : ");
                destinationDepart = scanner.nextLine();

                if (destinationDepart.isEmpty()) {
                    throw new IllegalArgumentException("Le champ 'Destination de départ' ne peut pas etre vide.");
                }

                System.out.print("Destination d'arrivée : ");
                destinationArrivee = scanner.nextLine();

                if (destinationArrivee.isEmpty()) {
                    throw new IllegalArgumentException("Le champ 'Destination d'arrivée' ne peut pas etre vide.");
                }

                System.out.print("Date de depart (jj/mm/aaaa) : ");
                String dateDepartStr = scanner.nextLine();

                if (dateDepartStr.isEmpty()) {
                    throw new IllegalArgumentException("Le champ 'Date de depart' ne peut pas etre vide.");
                }

                if (!DateValide(dateDepartStr)) {
                    throw new IllegalArgumentException("Le format de la date est invalide. Veuillez utiliser le format 'jj/mm/aaaa'.");
                }

                dateDepart = parseDate(dateDepartStr);
                Date currentDate = new Date();

                if (dateDepart.compareTo(currentDate) <= 0) {
                    throw new IllegalArgumentException("La date de depart doit etre ulterieure a la date actuelle.");
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

                System.out.print("Date d'arrivee (jj/mm/aaaa) : ");
                String dateArriveeStr = scanner.nextLine();

                if (dateArriveeStr.isEmpty()) {
                    throw new IllegalArgumentException("Le champ 'Date d'arrivee' ne peut pas etre vide.");
                }

                if (!DateValide(dateArriveeStr)) {
                    throw new IllegalArgumentException("Le format de la date d'arrivee est invalide. Veuillez utiliser le format 'jj/mm/aaaa'.");
                }

                dateArrivee = parseDate(dateArriveeStr);

                System.out.print("Heure d'arrivee (hh:mm) : ");
                heureArrivee = scanner.nextLine();

                System.out.print("Nombre de places disponibles : ");
                placesDisponibles = scanner.nextInt();

                if (placesDisponibles <= 0) {
                    throw new IllegalArgumentException("Le nombre de places disponibles doit etre superieur a zero.");
                }

                champsValides = true;
            } catch (IllegalArgumentException e) {
                System.out.println("Erreur : " + e.getMessage());
            }
        }

        try (connection) {
            connection.setAutoCommit(false);

            try {
                String insertSql = "INSERT INTO Vols (numeroVol, compagnieAerienne, destinationDepart, destinationArrivee, dateDepart, heureDepart, dateArrivee, heureArrivee, placesDisponibles) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

                try (PreparedStatement preparedStatement = connection.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS)) {
                    preparedStatement.setString(1, numeroVol);
                    preparedStatement.setString(2, compagnieAerienne);
                    preparedStatement.setString(3, destinationDepart);
                    preparedStatement.setString(4, destinationArrivee);
                    preparedStatement.setDate(5, new java.sql.Date(dateDepart.getTime()));
                    preparedStatement.setString(6, heureDepart);
                    preparedStatement.setDate(7, new java.sql.Date(dateArrivee.getTime()));
                    preparedStatement.setString(8, heureArrivee);
                    preparedStatement.setInt(9, placesDisponibles);

                    int rowsAffected = preparedStatement.executeUpdate();
                    if (rowsAffected > 0) {
                        System.out.println("Vol enregistré avec succès.");
                    } else {
                        System.out.println("Erreur lors de l'enregistrement du vol.");
                    }
                }
                connection.commit();
            } catch (SQLException e) {
                connection.rollback();
                System.out.println("Erreur lors de l'insertion dans la table Vols : " + e.getMessage());
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la connexion à la base de données : " + e.getMessage());
        }
    }
    private static boolean volExists(String numeroVol) {
        try (connection) {
            String sql = "SELECT COUNT(*) FROM Vols WHERE numeroVol = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, numeroVol);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        int count = resultSet.getInt(1);
                        return count > 0;
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la vérification de l'existence du vol : " + e.getMessage());
        }
        return false;
    }
    private static boolean DateValide(String dateStr) {
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

                if (passagerExiste(numeroPasseport)) {
                    throw new IllegalArgumentException("Le numero de passeport existe deja.");
                }

                champsValides = true;
            } catch (IllegalArgumentException e) {
                System.out.println("Erreur : " + e.getMessage());
            }
        }

        try (connection) {
            // Prepare SQL statement for inserting data into Passagers table
            String insertSql = "INSERT INTO Passagers (nom, adresse, numeroPasseport) " +
                    "VALUES (?, ?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(insertSql)) {
                preparedStatement.setString(1, nom);
                preparedStatement.setString(2, adresse);
                preparedStatement.setString(3, numeroPasseport);

                // Execute the SQL statement
                int rowsAffected = preparedStatement.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println("Passager enregistre avec succes.");
                } else {
                    System.out.println("Erreur lors de l'enregistrement du passager.");
                }
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la connexion a la base de donnees : " + e.getMessage());
        }
    }
    private static boolean passagerExiste(String numeroPasseport) {
        try (connection) {
            String selectSql = "SELECT id FROM Passagers WHERE numeroPasseport = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(selectSql)) {
                preparedStatement.setString(1, numeroPasseport);

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    return resultSet.next(); // Return true if the result set has at least one row
                }
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la connexion a la base de donnees : " + e.getMessage());
            return false; // Handle the error and return false
        }
    }
    private static void consulterVolsEtPassagers() {
        try (connection) {
            String selectVolsSql = "SELECT * FROM Vols";
            try (PreparedStatement selectVolsStatement = connection.prepareStatement(selectVolsSql)) {
                try (ResultSet volsResultSet = selectVolsStatement.executeQuery()) {
                    while (volsResultSet.next()) {
                        System.out.println("==== Vol ====");
                        System.out.println("Numero de vol : " + volsResultSet.getString("numeroVol"));
                        System.out.println("Compagnie aerienne : " + volsResultSet.getString("compagnieAerienne"));
                        System.out.println("Destination de départ : " + volsResultSet.getString("destinationDepart"));
                        System.out.println("Destination d'arrivée : " + volsResultSet.getString("destinationArrivee"));
                        System.out.println("Date de départ : " + formatDate(volsResultSet.getDate("dateDepart")));
                        System.out.println("Heure de départ : " + volsResultSet.getString("heureDepart"));
                        System.out.println("Date d'arrivée : " + formatDate(volsResultSet.getDate("dateArrivee")));
                        System.out.println("Heure d'arrivée : " + volsResultSet.getString("heureArrivee"));
                        System.out.println("Places disponibles : " + volsResultSet.getInt("placesDisponibles"));
                        System.out.println("Passagers :");

                        int volId = volsResultSet.getInt("id");
                        List<Passager> passagers = getPassagersForVol(connection, volId);
                        if (passagers.isEmpty()) {
                            System.out.println("Aucun passager n'a réservé ce vol.");
                        } else {
                            for (Passager passager : passagers) {
                                System.out.println("- " + passager.getNom() + " (" + passager.getNumeroPasseport() + ")");
                            }
                        }
                        System.out.println("--------------------");
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la connexion à la base de données : " + e.getMessage());
        }
    }
    private static List<Passager> getPassagersForVol(Connection connection, int volId) {
        List<Passager> passagers = new ArrayList<>();

        try {
            String selectPassagersSql = "SELECT Passagers.* FROM Passagers " +
                    "JOIN Reservations ON Passagers.id = Reservations.idPassager " +
                    "WHERE Reservations.idVol = ?";

            try (PreparedStatement selectPassagersStatement = connection.prepareStatement(selectPassagersSql)) {
                selectPassagersStatement.setInt(1, volId);

                try (ResultSet passagersResultSet = selectPassagersStatement.executeQuery()) {
                    while (passagersResultSet.next()) {
                        String nom = passagersResultSet.getString("nom");
                        String adresse = passagersResultSet.getString("adresse");
                        String numeroPasseport = passagersResultSet.getString("numeroPasseport");

                        Passager passager = new Passager(nom, adresse, numeroPasseport);
                        passagers.add(passager);
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération des passagers pour le vol : " + e.getMessage());
        }

        return passagers;
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

        try (connection) {
            String selectVolsSql = "SELECT * FROM Vols WHERE destinationArrivee LIKE ?";
            try (PreparedStatement selectVolsStatement = connection.prepareStatement(selectVolsSql)) {
                selectVolsStatement.setString(1, "%" + destination + "%");

                try (ResultSet volsResultSet = selectVolsStatement.executeQuery()) {
                    boolean found = false;
                    while (volsResultSet.next()) {
                        System.out.println("Numero de vol : " + volsResultSet.getString("numeroVol"));
                        System.out.println("Compagnie aerienne : " + volsResultSet.getString("compagnieAerienne"));
                        System.out.println("Date de depart : " + formatDate(volsResultSet.getDate("dateDepart")));
                        System.out.println("Heure de depart : " + volsResultSet.getString("heureDepart"));
                        System.out.println("Date d'arrivée : " + formatDate(volsResultSet.getDate("dateArrivee")));
                        System.out.println("Heure d'arrivée : " + volsResultSet.getString("heureArrivee"));
                        System.out.println("--------------------");
                        found = true;
                    }
                    if (!found) {
                        System.out.println("Aucun vol disponible pour la destination '" + destination + "'.");
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la connexion à la base de données : " + e.getMessage());
        }
    }
    private static String formatDate(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        return dateFormat.format(date);
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

                // Vérifier si le passager existe dans la base de données
                if (!passagerExiste(numeroPasseport)) {
                    throw new IllegalArgumentException("Le passager avec le numéro de passeport spécifié n'existe pas dans la base de données.");
                }

                // Vérifier si le passager a déjà réservé le vol
                if (VerfiPassagerReservation(numeroPasseport)) {
                    throw new IllegalArgumentException("Le passager a déjà réservé un vol.");
                }

                champsValides = true;
            } catch (IllegalArgumentException e) {
                System.out.println("Erreur : " + e.getMessage());
            }
        }

        // ...
        int volCount = 0;
        System.out.println("Liste des vols disponibles :");

        try (connection) {
            // Sélectionner les vols disponibles depuis la base de données
            String selectVolsSql = "SELECT * FROM Vols";
            try (PreparedStatement selectVolsStatement = connection.prepareStatement(selectVolsSql)) {
                try (ResultSet volsResultSet = selectVolsStatement.executeQuery()) {
                    volCount = 1; // Utilisé pour numéroter les vols
                    boolean found = false;
                    while (volsResultSet.next()) {
                        System.out.println(volCount + ". Vol " + volsResultSet.getString("numeroVol"));
                        System.out.println("   Compagnie aérienne : " + volsResultSet.getString("compagnieAerienne"));
                        System.out.println("   Date de départ : " + formatDate(volsResultSet.getDate("dateDepart")));
                        System.out.println("   Heure de départ : " + volsResultSet.getString("heureDepart"));
                        System.out.println("   Date d'arrivée : " + formatDate(volsResultSet.getDate("dateArrivee")));
                        System.out.println("   Heure d'arrivée : " + volsResultSet.getString("heureArrivee"));
                        System.out.println("   Places restantes : " + volsResultSet.getInt("placesDisponibles"));
                        System.out.println("--------------------");
                        volCount++;
                        found = true;
                    }
                    if (!found) {
                        System.out.println("Aucun vol disponible.");
                    }
                }
            }

            int choixVol = 0;
            boolean volValide = false;

            while (!volValide) {
                try {
                    System.out.print("Choisissez un numéro de vol : ");
                    choixVol = Integer.parseInt(scanner.nextLine());

                    // Vérifier si le numéro de vol est valide
                    if (choixVol < 1 || choixVol >= volCount) {
                        throw new IllegalArgumentException("Numéro de vol invalide. Veuillez choisir un numéro valide.");
                    }
                    volValide = true;
                } catch (NumberFormatException e) {
                    System.out.println("Erreur : Veuillez entrer un nombre valide.");
                } catch (IllegalArgumentException e) {
                    System.out.println("Erreur : " + e.getMessage());
                }
            }

            // Réserver le vol dans la base de données
            try {
                // Vérifier le nombre de places disponibles
                int placesDisponibles = getPlacesLibresDuVols(connection, choixVol);
                if (placesDisponibles > 0) {
                    String insertReservationSql = "INSERT INTO Reservations (idVol, idPassager) VALUES (?, " +
                            "(SELECT id FROM Passagers WHERE numeroPasseport = ?))";

                    try (PreparedStatement preparedStatement = connection.prepareStatement(insertReservationSql)) {
                        preparedStatement.setInt(1, choixVol);
                        preparedStatement.setString(2, numeroPasseport);

                        int rowsAffected = preparedStatement.executeUpdate();
                        if (rowsAffected > 0) {
                            // Mettre à jour le nombre de places disponibles après la réservation
                            updatePlaceDisponible(connection, choixVol, placesDisponibles - 1);

                            System.out.println("Réservation effectuée avec succès pour le passager avec le numéro de passeport " + numeroPasseport +
                                    " sur le vol numéro " + choixVol + ".");
                        } else {
                            System.out.println("Erreur lors de la réservation du vol.");
                        }
                    }
                } else {
                    System.out.println("Désolé, il n'y a plus de places disponibles pour ce vol.");
                }
            } catch (SQLException e) {
                System.out.println("Erreur lors de la réservation du vol : " + e.getMessage());
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la connexion à la base de données : " + e.getMessage());
        }
    }
    private static boolean VerfiPassagerReservation(String numeroPasseport) {
        try (connection) {
            String selectSql = "SELECT COUNT(*) FROM Reservations " +
                    "JOIN Passagers ON Reservations.idPassager = Passagers.id " +
                    "WHERE Passagers.numeroPasseport = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(selectSql)) {
                preparedStatement.setString(1, numeroPasseport);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        int count = resultSet.getInt(1);
                        return count > 0;
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la vérification de la réservation du passager : " + e.getMessage());
        }
        return false;
    }
    private static int getPlacesLibresDuVols(Connection connection, int volId) {
        try {
            String selectSql = "SELECT placesDisponibles FROM Vols WHERE id = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(selectSql)) {
                preparedStatement.setInt(1, volId);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        return resultSet.getInt("placesDisponibles");
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération du nombre de places disponibles pour le vol : " + e.getMessage());
        }
        return 0;
    }
    private static void updatePlaceDisponible(Connection connection, int volId, int newAvailableSeats) {
        try {
            String updateSql = "UPDATE Vols SET placesDisponibles = ? WHERE id = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(updateSql)) {
                preparedStatement.setInt(1, newAvailableSeats);
                preparedStatement.setInt(2, volId);

                int rowsAffected = preparedStatement.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println("Nombre de places disponibles mis à jour avec succès.");
                } else {
                    System.out.println("Erreur lors de la mise à jour du nombre de places disponibles.");
                }
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la mise à jour du nombre de places disponibles pour le vol : " + e.getMessage());
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

        try (connection) {
            List<Reservation> reservations = getReservationsForPassager(connection, numeroPasseport);

            if (reservations.isEmpty()) {
                System.out.println("Le passager n'a effectué aucune réservation.");
            } else {
                System.out.println("Liste des réservations du passager avec le numéro de passeport " + numeroPasseport + " :");

                for (int i = 0; i < reservations.size(); i++) {
                    Reservation reservation = reservations.get(i);
                    int idVol = reservation.getIdVol();
                    int idPassager = reservation.getIdPassager();

                    // Affichez les détails de la réservation (vous pouvez adapter cela selon vos besoins)
                    System.out.println((i + 1) + ". Réservation pour le vol avec l'ID " + idVol + " et le passager avec l'ID " + idPassager);
                }

                int choixReservation = 0;
                boolean reservationValide = false;

                while (!reservationValide) {
                    try {
                        System.out.print("Choisissez le numéro de la réservation que vous souhaitez annuler : ");
                        choixReservation = Integer.parseInt(scanner.nextLine());

                        // Vérifier si le numéro de réservation est valide
                        if (choixReservation < 1 || choixReservation > reservations.size()) {
                            throw new IllegalArgumentException("Numero de réservation invalide. Veuillez choisir un numero valide.");
                        }

                        reservationValide = true;
                    } catch (NumberFormatException e) {
                        System.out.println("Erreur : Veuillez entrer un nombre valide.");
                    } catch (IllegalArgumentException e) {
                        System.out.println("Erreur : " + e.getMessage());
                    }
                }

                // Annuler la réservation dans la base de données
                try {
                    Reservation reservationSelectionnee = reservations.get(choixReservation - 1);
                    int idVol = reservationSelectionnee.getIdVol();
                    int idPassager = reservationSelectionnee.getIdPassager();

                    String deleteReservationSql = "DELETE FROM Reservations WHERE idVol = ? AND idPassager = ?";
                    try (PreparedStatement preparedStatement = connection.prepareStatement(deleteReservationSql)) {
                        preparedStatement.setInt(1, idVol);
                        preparedStatement.setInt(2, idPassager);

                        int rowsAffected = preparedStatement.executeUpdate();
                        if (rowsAffected > 0) {
                            System.out.println("Réservation annulée avec succès pour le passager avec le numéro de passeport " + numeroPasseport +
                                    " sur le vol avec l'ID " + idVol + ".");
                        } else {
                            System.out.println("Erreur lors de l'annulation de la réservation.");
                        }
                    }
                } catch (SQLException e) {
                    System.out.println("Erreur lors de l'annulation de la réservation : " + e.getMessage());
                }
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la connexion à la base de données : " + e.getMessage());
        }
    }
    private static List<Reservation> getReservationsForPassager(Connection connection, String numeroPasseport) {
        List<Reservation> reservations = new ArrayList<>();

        try {
            String selectReservationsSql = "SELECT Reservations.* FROM Reservations " +
                    "JOIN Passagers ON Reservations.idPassager = Passagers.id " +
                    "WHERE Passagers.numeroPasseport = ?";

            try (PreparedStatement selectReservationsStatement = connection.prepareStatement(selectReservationsSql)) {
                selectReservationsStatement.setString(1, numeroPasseport);

                try (ResultSet reservationsResultSet = selectReservationsStatement.executeQuery()) {
                    while (reservationsResultSet.next()) {
                        int idVol = reservationsResultSet.getInt("idVol");
                        int idPassager = reservationsResultSet.getInt("idPassager");

                        Reservation reservation = new Reservation(idVol, idPassager);
                        reservations.add(reservation);
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération des réservations pour le passager : " + e.getMessage());
        }

        return reservations;
    }
    public static void clearConsoleWithDelay(int millis) {
        System.out.println("Chargement en cours.......");
    
        try {
            Thread.sleep(millis);
            clearConsole();
        } catch (InterruptedException e) {
            
        }
    }
    public static void clearConsoleWithDelayAndPrompt() {
        System.out.println("Appuyez sur une touche pour continuer...");

        try {
            Scanner scanner = new Scanner(System.in);
            scanner.nextLine(); // Attendre que l'utilisateur appuie sur une touche
            scanner.close();

            clearConsole();
        } catch (Exception e) {
            // Gerer les exceptions appropriees ici
        }
    }
    public static void clearConsole() {
        try {
            if (System.getProperty("os.name").contains("Windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                Runtime.getRuntime().exec("clear");
            }
        } catch (Exception e) {
            // Gerer les exceptions appropriees ici
        }
    }
}