CREATE TABLE Vols (
    id INT AUTO_INCREMENT PRIMARY KEY,
    numeroVol VARCHAR(50) NOT NULL,
    compagnieAerienne VARCHAR(100) NOT NULL,
    destinationDepart VARCHAR(100) NOT NULL,
    destinationArrivee VARCHAR(100) NOT NULL,
    dateDepart DATE NOT NULL,
    heureDepart VARCHAR(5) NOT NULL,
    dateArrivee DATE NOT NULL,
    heureArrivee VARCHAR(5) NOT NULL,
    placesDisponibles INT NOT NULL
);

CREATE TABLE Passagers (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nom VARCHAR(100) NOT NULL,
    adresse VARCHAR(255) NOT NULL,
    numeroPasseport VARCHAR(20) NOT NULL UNIQUE
);

CREATE TABLE Reservations (
    id INT AUTO_INCREMENT PRIMARY KEY,
    idVol INT NOT NULL,
    idPassager INT NOT NULL,
    FOREIGN KEY (idVol) REFERENCES Vols(id),
    FOREIGN KEY (idPassager) REFERENCES Passagers(id),
    UNIQUE KEY unique_reservation (idVol, idPassager)
);
