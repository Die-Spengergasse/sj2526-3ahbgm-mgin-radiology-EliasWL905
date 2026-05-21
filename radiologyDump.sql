-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Erstellungszeit: 23. Apr 2026 um 09:36
-- Server-Version: 10.4.32-MariaDB
-- PHP-Version: 8.1.25

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Datenbank: `radiology_web`
--
CREATE DATABASE IF NOT EXISTS `radiology_web` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
USE `radiology_web`;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `device`
--

CREATE TABLE `device` (
  `id` int(11) NOT NULL,
  `location` varchar(255) DEFAULT NULL,
  `type` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Daten für Tabelle `device`
--

INSERT INTO `device` (`id`, `location`, `type`, `name`) VALUES
(1, 'Raum 101', 'Magnetresonanztomographie', 'MRT'),
(2, 'Raum 201', 'Computertomographie', 'CT'),
(3, 'Raum 301', 'Positronenemissionstomographie', 'PET'),
(4, 'Raum 401', 'Magnetoencephalographie', 'MEG'),
(5, 'Raum 501', 'Elektroencephalographie', 'EEG');

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `patient`
--

CREATE TABLE `patient` (
  `id` int(11) NOT NULL,
  `birth` date DEFAULT NULL,
  `firstname` varchar(255) DEFAULT NULL,
  `gender` varchar(255) DEFAULT NULL,
  `lastname` varchar(255) DEFAULT NULL,
  `svnr` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Daten für Tabelle `patient`
--

INSERT INTO `patient` (`id`, `birth`, `firstname`, `gender`, `lastname`, `svnr`) VALUES
(2, '2026-04-16', 'Max', 'MALE', 'Muster', '39485853'),
(3, '2009-05-15', 'Elias', 'MALE', 'Leustek', '9384853567'),
(4, '1797-10-12', 'Sascha', 'MALE', 'Kalinka', '854352454'),
(5, '1980-10-20', 'Christian', 'MALE', 'Camrda', '1528 150385'),
(6, '1955-05-05', 'Susi', 'FEMALE', 'Sorglos', '9102 010170');

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `reservation`
--

CREATE TABLE `reservation` (
  `id` int(11) NOT NULL,
  `body_part` varchar(255) DEFAULT NULL,
  `comment` varchar(255) DEFAULT NULL,
  `end_time` datetime(6) DEFAULT NULL,
  `start_time` datetime(6) DEFAULT NULL,
  `device_id` int(11) DEFAULT NULL,
  `patient_id` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Daten für Tabelle `reservation`
--

INSERT INTO `reservation` (`id`, `body_part`, `comment`, `end_time`, `start_time`, `device_id`, `patient_id`) VALUES
(4, 'LIMBS', 'Umgeknickt durch Verkettung unklücklicher Umstände', '2026-05-24 03:50:00.000000', '2026-05-24 03:30:00.000000', 1, 4),
(5, 'HEAD', 'MEG des Gehirns für Überprüfung des Hirnzusatndes und zur Abschätzung der Verletzung', '2026-04-25 18:40:00.000000', '2026-04-25 17:45:00.000000', 4, 2),
(6, 'SPINE', 'Abklärung der Symptome', '2027-05-30 11:00:00.000000', '2027-05-30 10:10:00.000000', 2, 3);

--
-- Indizes der exportierten Tabellen
--

--
-- Indizes für die Tabelle `device`
--
ALTER TABLE `device`
  ADD PRIMARY KEY (`id`);

--
-- Indizes für die Tabelle `patient`
--
ALTER TABLE `patient`
  ADD PRIMARY KEY (`id`);

--
-- Indizes für die Tabelle `reservation`
--
ALTER TABLE `reservation`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FKcjc9b9oyt5lwt049mjoiqtb7r` (`device_id`),
  ADD KEY `FKrrjvkskqqxgliwmqgbl3ijc4n` (`patient_id`);

--
-- AUTO_INCREMENT für exportierte Tabellen
--

--
-- AUTO_INCREMENT für Tabelle `device`
--
ALTER TABLE `device`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT für Tabelle `patient`
--
ALTER TABLE `patient`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- AUTO_INCREMENT für Tabelle `reservation`
--
ALTER TABLE `reservation`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- Constraints der exportierten Tabellen
--

--
-- Constraints der Tabelle `reservation`
--
ALTER TABLE `reservation`
  ADD CONSTRAINT `FKcjc9b9oyt5lwt049mjoiqtb7r` FOREIGN KEY (`device_id`) REFERENCES `device` (`id`),
  ADD CONSTRAINT `FKrrjvkskqqxgliwmqgbl3ijc4n` FOREIGN KEY (`patient_id`) REFERENCES `patient` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
