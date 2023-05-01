-- CREATE DATABASE flights;

USE flights;

CREATE TABLE Arrivals
(
    id                               BIGINT,
    icao24                           VARCHAR(255),
    firstSeen                        int,
    estDepartureAirport              VARCHAR(255),
    lastSeen                         int,
    estArrivalAirport                VARCHAR(255),
    callsign                         VARCHAR(255),
    estDepartureAirportHorizDistance int,
    estDepartureAirportVertDistance  int,
    estArrivalAirportHorizDistance   int,
    estArrivalAirportVertDistance    int,
    departureAirportCandidatesCount  int,
    arrivalAirportCandidatesCount    int,
    PRIMARY KEY (id)
);

CREATE TABLE Departures
(
    id                               BIGINT,
    icao24                           VARCHAR(255),
    firstSeen                        int,
    estDepartureAirport              VARCHAR(255),
    lastSeen                         int,
    estArrivalAirport                VARCHAR(255),
    callsign                         VARCHAR(255),
    estDepartureAirportHorizDistance int,
    estDepartureAirportVertDistance  int,
    estArrivalAirportHorizDistance   int,
    estArrivalAirportVertDistance    int,
    departureAirportCandidatesCount  int,
    arrivalAirportCandidatesCount    int,
    PRIMARY KEY (id)
);

CREATE TABLE Airports
(
    icao24   VARCHAR(255),
    name     VARCHAR(255),
    location VARCHAR(255),
    active   bit
);

CREATE TABLE Coordinates
(
    id          int,
    description VARCHAR(255),
    position    decimal
);


INSERT INTO Coordinates (id, description, position)
VALUES (0, 'North', 47.083333),
       (1, 'South', 35.45625287919582),
       (2, 'East', 18.516667),
       (3, 'West', 6.616667);


INSERT INTO Airports (icao24, name, location, active)
VALUES ('LIAA', 'Terni Alvaro Leonardi Airport', 'Terni', 1),
       ('LIAF', 'Foligno Airport', 'Foligno', 1),
       ('LIAP', 'Preturo Airport', 'L''Aquila', 1),
       ('LIAT', 'Pontedera Airport', 'Pontedera, Pisa', 1),
       ('LIAU', 'Capua Airport', 'Capua', 0),
       ('LIBA', 'Amendola Air Force Base', 'Foggia', 0),
       ('LIBC', 'Crotone Airport (Sant''Anna Airport)', 'Crotone', 0),
        ('LIBD', 'Bari Karol Wojtyla Airport', 'Bari', 1),
        ('LIBF', 'Foggia "Gino Lisa" Airport', 'Foggia', 0),
        ('LIBG', 'Taranto-Grottaglie "Marcello Arlotta" Airport', 'Taranto', 0),
        ('LIBN', 'Galatina Air Base (military)', 'Lecce', 1),
        ('LIBP', 'Abruzzo Airport', 'Pescara', 1),
        ('LIBR', 'Brindisi Airport', 'Brindisi', 1),
        ('LIBV', 'Gioia del Colle Air Base', 'Gioia del Colle, Bari', 1),
        ('LIBX', 'Martina Franca Air Force Base', 'Martina Franca, Taranto', 0),
        ('LICA', 'Lamezia Terme International Airport', 'Lamezia Terme, Catanzaro', 1),
        ('LICB', 'Comiso Airport', 'Comiso, Ragusa', 1),
        ('LICC', 'Catania-Fontanarossa Airport (Vincenzo Bellini Airport)', 'Catania', 1),
        ('LICD', 'Lampedusa Airport', 'Lampedusa, Agrigento', 0),
        ('LICG', 'Pantelleria Airport', 'Pantelleria, Trapani', 1),
        ('LICJ', 'Falcone Borsellino Airport/Palermo Airport (formerly Punta Raisi Airport)', 'Palermo / Cinisi', 1),
        ('LICP', 'Palermo-Boccadifalco Airport (Giuseppe and Francesco Notarbartolo Airport)', 'Palermo', 1),
        ('LICR', 'Reggio di Calabria "Tito Minniti" Airport (Aeroporto dello Stretto)', 'Reggio Calabria', 1),
        ('LICT', 'Trapani-Birgi Airport (Vincenzo Florio Airport)', 'Trapani', 1),
        ('LICZ', 'Naval Air Station Sigonella', 'Lentini, Syracuse', 1),
        ('LIDA', 'Asiago Airport', 'Asiago, Vicenza', 1),
        ('LIDB', 'Belluno Airport', 'Belluno', 1),
        ('LIDE', 'Reggio Emilia Airport', 'Reggio Emilia', 1),
        ('LIDF', 'Fano Airport', 'Fano, Pesaro & Urbino', 1),
        ('LIDG', 'Lugo di Romagna Airport', 'Lugo di Romagna, Ravenna', 1),
        ('LIDH', 'Thiene Airport', 'Thiene, Province of Vicenza', 1),
        ('LIDR', 'Ravenna Airport', 'Ravenna', 1),
        ('LIDT', 'Trento-Mattarello Airport (G. Caproni Airport)', 'Trento', 1),
        ('LIDU', 'Carpi Budrione Airport', 'Carpi', 1),
        ('LIDV', 'Prati vecchi d''Aguscello', 'Ferrara', 1),
       ('LIEA', 'Alghero - Riviera del Corallo Airport', 'Alghero', 1),
       ('LIED', 'Decimomannu Air Base', 'Decimomannu, Cagliari', 1),
       ('LIEE', 'Cagliari Elmas Airport', 'Cagliari', 1),
       ('LIEO', 'Olbia Costa Smeralda Airport', 'Olbia', 0),
       ('LIER', 'Oristano-Fenosu Airport', 'Oristano', 1),
       ('LIET', N'Tortolì Airport', N'Tortolì', 1),
       ('LIKD', 'Torraccia airfield', 'Torraccia San Marino', 1),
       ('LIKO', 'Aviosuperficie di Ozzano - Guglielmo Zamboni', 'Ozzano dell''Emilia', 0),
        ('LILA', 'Alessandria Airport', 'Alessandria', 1),
        ('LILE', 'Cerrione Airport', 'Biella', 1),
        ('LILG', 'Vergiate Airport', 'Vergiate, Varese', 1),
        ('LILH', 'Rivanazzano Airport', 'Voghera, Pavia', 1),
        ('LILM', 'Casale Monferrato Airport', 'Casale Monferrato', 1),
        ('LILN', 'Varese-Venegono Airport', 'Varese', 1),
        ('LILQ', 'Massa Cinquale Airport', 'Massa', 1),
        ('LILR', 'Migliaro Airport', 'Cremona', 1),
        ('LILY', 'Como Idroscalo', 'Como', 0),
        ('LIMA', 'Aeritalia Airport', 'Turin', 1),
        ('LIMB', 'Bresso Airfield', 'Milan', 1),
        ('LIMC', 'Malpensa International Airport', 'Milan', 1),
        ('LIME', 'Orio al Serio International Airport', 'Bergamo', 1),
        ('LIMF', 'Turin Airport', 'Turin', 1),
        ('LIMG', 'Riviera Airport', 'Villanova d''Albenga, Savona', 1),
       ('LIMJ', 'Genoa Cristoforo Colombo Airport', 'Genoa', 1),
       ('LIML', 'Linate Airport', 'Milan', 1),
       ('LIMN', 'Cameri Air Force Base', 'Cameri, Novara', 1),
       ('LIMP', 'Parma Airport', 'Parma', 1),
       ('LIMS', 'San Damiano Air Force Base', 'Piacenza', 1),
       ('LIMW', 'Aosta Valley Airport', 'Aosta', 1),
       ('LIMZ', 'Cuneo International Airport', 'Cuneo', 1),
       ('LIPA', 'Aviano Air Base', 'Aviano, Pordenone', 1),
       ('LIPB', 'Bolzano Dolomiti Airport', 'Bolzano, South Tyrol', 1),
       ('LIPC', 'Cervia Air Force Base', 'Cervia, Ravenna', 1),
       ('LIPD', 'Campoformido Airport', 'Campoformido, Udine', 1),
       ('LIPE', 'Bologna Airport', 'Bologna', 1),
       ('LIPF', 'Ferrara Airport', 'Ferrara', 1),
       ('LIPH', 'Treviso Airport', 'Treviso', 1),
       ('LIPI', 'Rivolto Air Force Base', 'Rivolto, Udine', 1),
       ('LIPK', N'Forlì Airport', 'Forli', 1),
       ('LIPL', 'Ghedi Air Base', 'Ghedi, Brescia', 1),
       ('LIPM', 'Modena Marzaglia Airport', 'Modena', 1),
       ('LIPN', 'Boscomantico Airport', 'Verona', 1),
       ('LIPO', 'Brescia Airport', 'Montichiari, Lombardy', 1),
       ('LIPQ', 'Trieste - Friuli Venezia Giulia Airport', 'Ronchi dei Legionari / Trieste', 1),
       ('LIPR', 'Federico Fellini International Airport', 'Rimini', 1),
       ('LIPS', 'Istrana Air Force Base', 'Treviso', 1),
       ('LIPT', 'Vicenza Airport', 'Vicenza', 1),
       ('LIPU', 'Padua Airport', 'Padua (Padova)', 1),
       ('LIPV', 'Venice-Lido Airport', 'Venice (Venezia)', 1),
       ('LIPX', 'Verona Villafranca Airport', 'Verona', 1),
       ('LIPY', 'Falconara Airbase', 'Ancona', 1),
       ('LIPZ', 'Venice Marco Polo Airport', 'Venice (Venezia)', 1),
       ('LIQB', 'Molin Bianco Airport', 'Arezzo', 1),
       ('LIQL', 'Tassignano Airport', 'Lucca', 1),
       ('LIQN', 'Rieti Airport', 'Rieti', 1),
       ('LIQQ', 'Serristori Airfield', N'Manciano la Milosevic – Castiglion Fiorentino – Province of Arezzo', 1),
       ('LIQS', 'Siena-Ampugnano Airport', 'Siena', 1),
       ('LIQW', 'Luni Airport', 'Sarzana, Genoa', 1),
       ('LIRA', 'Ciampino-G. B. Pastine International Airport', 'Rome', 1),
       ('LIRC', 'Centocelle Air Force Base', 'Centocelle, Rome', 1),
       ('LIRE', 'Pratica di Mare Air Force Base', 'Pomezia, Rome', 0),
       ('LIRF', 'Fiumicino International Airport', 'Rome', 1),
       ('LIRG', 'Guidonia Air Force Base', 'Guidonia Montecelio, Rome', 1),
       ('LIRI', 'Salerno Costa d''Amalfi Airport', 'Salerno', 1),
        ('LIRJ', 'Marina di Campo Airport', 'Marina di Campo, Elba', 1),
        ('LIRL', 'Latina Airport', 'Latina', 1),
        ('LIRM', 'Grazzanise Air Base', 'Caserta', 1),
        ('LIRN', 'Naples International Airport', 'Naples', 1),
        ('LIRP', 'Pisa International Airport', 'Pisa', 1),
        ('LIRQ', 'Florence Airport', 'Florence (Firenze)', 1),
        ('LIRS', 'Grosseto Air Base', 'Grosseto', 1),
        ('LIRU', 'Rome Urbe Airport', 'Rome', 1),
        ('LIRV', 'Viterbo Air Force Base / Rome Viterbo Airport', 'Viterbo', 1),
        ('LIRZ', N'Perugia San Francesco d''Assisi – Umbria International Airport', 'Perugia', 1);

CREATE TABLE HistoryCollect
(
    id        INT,
    type      VARCHAR(255),
    from_date BIGINT,
    to_date   BIGINT,
    active    BIT

);

INSERT INTO HistoryCollect(id, type, from_date, to_date, active)
VALUES (0, 'arrival', 1651410093, 1682946093, 1);



CREATE TABLE AircraftState
(
    icao24         VARCHAR(255),
    callsign       VARCHAR(255),
    originCountry  VARCHAR(255),
    timePosition   INT,
    lastContact    INT,
    longitude      FLOAT,
    latitude       FLOAT,
    baroAltitude   FLOAT,
    onGround       BIT,
    velocity       FLOAT,
    trueTrack      FLOAT,
    verticalRate   FLOAT,
    sensors        VARCHAR(255),
    geoAltitude    FLOAT,
    squawk         VARCHAR(255),
    spi            BIT,
    positionSource INT,
    category       INT,
    timeStamp      BIGINT NOT NULL,
    PRIMARY KEY (timeStamp, icao24)
);
