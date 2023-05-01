CREATE DATABASE flights;

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
    active   BOOLEAN
);

CREATE TABLE Coordinates
(
    id          int,
    description VARCHAR(255),
    position    double
);


INSERT INTO Coordinates (id, description, position)
VALUES (0, 'North', 47.083333),
       (1, 'South', 35.45625287919582),
       (2, 'East', 18.516667),
       (3, 'West', 6.616667);


INSERT INTO Airports (icao24, name, location, active)
VALUES ('LIAA', 'Terni Alvaro Leonardi Airport', 'Terni', true),
       ('LIAF', 'Foligno Airport', 'Foligno', true),
       ('LIAP', 'Preturo Airport', 'L\'Aquila', true),
       ('LIAT', 'Pontedera Airport', 'Pontedera, Pisa', true),
       ('LIAU', 'Capua Airport', 'Capua', false),
       ('LIBA', 'Amendola Air Force Base', 'Foggia', false),
       ('LIBC', 'Crotone Airport (Sant\'Anna Airport)', 'Crotone', false),
       ('LIBD', 'Bari Karol Wojtyla Airport', 'Bari', true),
       ('LIBF', 'Foggia "Gino Lisa" Airport', 'Foggia', false),
       ('LIBG', 'Taranto-Grottaglie "Marcello Arlotta" Airport', 'Taranto', false),
       ('LIBN', 'Galatina Air Base (military)', 'Lecce', true),
       ('LIBP', 'Abruzzo Airport', 'Pescara', true),
       ('LIBR', 'Brindisi Airport', 'Brindisi', true),
       ('LIBV', 'Gioia del Colle Air Base', 'Gioia del Colle, Bari', true),
       ('LIBX', 'Martina Franca Air Force Base', 'Martina Franca, Taranto', false),
       ('LICA', 'Lamezia Terme International Airport', 'Lamezia Terme, Catanzaro', true),
       ('LICB', 'Comiso Airport', 'Comiso, Ragusa', true),
       ('LICC', 'Catania-Fontanarossa Airport (Vincenzo Bellini Airport)', 'Catania', true),
       ('LICD', 'Lampedusa Airport', 'Lampedusa, Agrigento', false),
       ('LICG', 'Pantelleria Airport', 'Pantelleria, Trapani', true),
       ('LICJ', 'Falcone Borsellino Airport/Palermo Airport (formerly Punta Raisi Airport)', 'Palermo / Cinisi', true),
       ('LICP', 'Palermo-Boccadifalco Airport (Giuseppe and Francesco Notarbartolo Airport)', 'Palermo', true),
       ('LICR', 'Reggio di Calabria "Tito Minniti" Airport (Aeroporto dello Stretto)', 'Reggio Calabria', true),
       ('LICT', 'Trapani-Birgi Airport (Vincenzo Florio Airport)', 'Trapani', true),
       ('LICZ', 'Naval Air Station Sigonella', 'Lentini, Syracuse', true),
       ('LIDA', 'Asiago Airport', 'Asiago, Vicenza', true),
       ('LIDB', 'Belluno Airport', 'Belluno', true),
       ('LIDE', 'Reggio Emilia Airport', 'Reggio Emilia', true),
       ('LIDF', 'Fano Airport', 'Fano, Pesaro & Urbino', true),
       ('LIDG', 'Lugo di Romagna Airport', 'Lugo di Romagna, Ravenna', true),
       ('LIDH', 'Thiene Airport', 'Thiene, Province of Vicenza', true),
       ('LIDR', 'Ravenna Airport', 'Ravenna', true),
       ('LIDT', 'Trento-Mattarello Airport (G. Caproni Airport)', 'Trento', true),
       ('LIDU', 'Carpi Budrione Airport', 'Carpi', true),
       ('LIDV', 'Prati vecchi d\'Aguscello', 'Ferrara', true),
       ('LIEA', 'Alghero - Riviera del Corallo Airport', 'Alghero', true),
       ('LIED', 'Decimomannu Air Base', 'Decimomannu, Cagliari', true),
       ('LIEE', 'Cagliari Elmas Airport', 'Cagliari', true),
       ('LIEO', 'Olbia Costa Smeralda Airport', 'Olbia', false),
       ('LIER', 'Oristano-Fenosu Airport', 'Oristano', true),
       ('LIET', 'Tortolì Airport', 'Tortolì', true),
       ('LIKD', 'Torraccia airfield', 'Torraccia San Marino', true),
       ('LIKO', 'Aviosuperficie di Ozzano - Guglielmo Zamboni', 'Ozzano dell\'Emilia', false),
       ('LILA', 'Alessandria Airport', 'Alessandria', true),
       ('LILE', 'Cerrione Airport', 'Biella', true),
       ('LILG', 'Vergiate Airport', 'Vergiate, Varese', true),
       ('LILH', 'Rivanazzano Airport', 'Voghera, Pavia', true),
       ('LILM', 'Casale Monferrato Airport', 'Casale Monferrato', true),
       ('LILN', 'Varese-Venegono Airport', 'Varese', true),
       ('LILQ', 'Massa Cinquale Airport', 'Massa', true),
       ('LILR', 'Migliaro Airport', 'Cremona', true),
       ('LILY', 'Como Idroscalo', 'Como', false),
       ('LIMA', 'Aeritalia Airport', 'Turin', true),
       ('LIMB', 'Bresso Airfield', 'Milan', true),
       ('LIMC', 'Malpensa International Airport', 'Milan', true),
       ('LIME', 'Orio al Serio International Airport', 'Bergamo', true),
       ('LIMF', 'Turin Airport', 'Turin', true),
       ('LIMG', 'Riviera Airport', 'Villanova d\'Albenga, Savona', true),
       ('LIMJ', 'Genoa Cristoforo Colombo Airport', 'Genoa', true),
       ('LIML', 'Linate Airport', 'Milan', true),
       ('LIMN', 'Cameri Air Force Base', 'Cameri, Novara', true),
       ('LIMP', 'Parma Airport', 'Parma', true),
       ('LIMS', 'San Damiano Air Force Base', 'Piacenza', true),
       ('LIMW', 'Aosta Valley Airport', 'Aosta', true),
       ('LIMZ', 'Cuneo International Airport', 'Cuneo', true),
       ('LIPA', 'Aviano Air Base', 'Aviano, Pordenone', true),
       ('LIPB', 'Bolzano Dolomiti Airport', 'Bolzano, South Tyrol', true),
       ('LIPC', 'Cervia Air Force Base', 'Cervia, Ravenna', true),
       ('LIPD', 'Campoformido Airport', 'Campoformido, Udine', true),
       ('LIPE', 'Bologna Airport', 'Bologna', true),
       ('LIPF', 'Ferrara Airport', 'Ferrara', true),
       ('LIPH', 'Treviso Airport', 'Treviso', true),
       ('LIPI', 'Rivolto Air Force Base', 'Rivolto, Udine', true),
       ('LIPK', 'Forlì Airport', 'Forlì', true),
       ('LIPL', 'Ghedi Air Base', 'Ghedi, Brescia', true),
       ('LIPM', 'Modena Marzaglia Airport', 'Modena', true),
       ('LIPN', 'Boscomantico Airport', 'Verona', true),
       ('LIPO', 'Brescia Airport', 'Montichiari, Lombardy', true),
       ('LIPQ', 'Trieste - Friuli Venezia Giulia Airport', 'Ronchi dei Legionari / Trieste', true),
       ('LIPR', 'Federico Fellini International Airport', 'Rimini', true),
       ('LIPS', 'Istrana Air Force Base', 'Treviso', true),
       ('LIPT', 'Vicenza Airport', 'Vicenza', true),
       ('LIPU', 'Padua Airport', 'Padua (Padova)', true),
       ('LIPV', 'Venice-Lido Airport', 'Venice (Venezia)', true),
       ('LIPX', 'Verona Villafranca Airport', 'Verona', true),
       ('LIPY', 'Falconara Airbase', 'Ancona', true),
       ('LIPZ', 'Venice Marco Polo Airport', 'Venice (Venezia)', true),
       ('LIQB', 'Molin Bianco Airport', 'Arezzo', true),
       ('LIQL', 'Tassignano Airport', 'Lucca', true),
       ('LIQN', 'Rieti Airport', 'Rieti', true),
       ('LIQQ', 'Serristori Airfield', 'Manciano la Misericordia – Castiglion Fiorentino – Province of Arezzo', true),
       ('LIQS', 'Siena-Ampugnano Airport', 'Siena', true),
       ('LIQW', 'Luni Airport', 'Sarzana, Genoa', true),
       ('LIRA', 'Ciampino-G. B. Pastine International Airport', 'Rome', true),
       ('LIRC', 'Centocelle Air Force Base', 'Centocelle, Rome', true),
       ('LIRE', 'Pratica di Mare Air Force Base', 'Pomezia, Rome', false),
       ('LIRF', 'Fiumicino International Airport', 'Rome', true),
       ('LIRG', 'Guidonia Air Force Base', 'Guidonia Montecelio, Rome', true),
       ('LIRI', 'Salerno Costa d\'Amalfi Airport', 'Salerno', true),
       ('LIRJ', 'Marina di Campo Airport', 'Marina di Campo, Elba', true),
       ('LIRL', 'Latina Airport', 'Latina', true),
       ('LIRM', 'Grazzanise Air Base', 'Caserta', true),
       ('LIRN', 'Naples International Airport', 'Naples', true),
       ('LIRP', 'Pisa International Airport', 'Pisa', true),
       ('LIRQ', 'Florence Airport', 'Florence (Firenze)', true),
       ('LIRS', 'Grosseto Air Base', 'Grosseto', true),
       ('LIRU', 'Rome Urbe Airport', 'Rome', true),
       ('LIRV', 'Viterbo Air Force Base / Rome Viterbo Airport', 'Viterbo', true),
       ('LIRZ', 'Perugia San Francesco d\'Assisi – Umbria International Airport', 'Perugia', true);

CREATE TABLE HistoryCollect
(
    id        INT,
    type      VARCHAR(255),
    from_date BIGINT,
    to_date   BIGINT,
    active    BOOLEAN

);

INSERT INTO HistoryCollect(id, type, from_date, to_date, active)
VALUES (0, 'arrival', 1651410093, 1682946093, true);



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
    onGround       BOOLEAN,
    velocity       FLOAT,
    trueTrack      FLOAT,
    verticalRate   FLOAT,
    sensors        TEXT,
    geoAltitude    FLOAT,
    squawk         VARCHAR(255),
    spi            BOOLEAN,
    positionSource INT,
    category       INT,
    timeStamp      BIGINT NOT NULL,
    PRIMARY KEY (timeStamp, icao24)
);
