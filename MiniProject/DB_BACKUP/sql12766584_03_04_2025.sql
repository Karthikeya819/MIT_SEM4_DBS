-- phpMyAdmin SQL Dump
-- version 4.7.1
-- https://www.phpmyadmin.net/
--
-- Host: sql12.freemysqlhosting.net
-- Generation Time: Apr 03, 2025 at 09:39 AM
-- Server version: 5.5.62-0ubuntu0.14.04.1
-- PHP Version: 7.0.33-0ubuntu0.16.04.16

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `sql12766584`
--

-- --------------------------------------------------------

--
-- Table structure for table `mst_bookings`
--

CREATE TABLE `mst_bookings` (
  `booking_id` int(11) NOT NULL,
  `user_id` int(11) DEFAULT NULL,
  `screening_id` int(11) DEFAULT NULL,
  `seats_booked` int(11) DEFAULT NULL,
  `booking_date` date DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `mst_bookings`
--

INSERT INTO `mst_bookings` (`booking_id`, `user_id`, `screening_id`, `seats_booked`, `booking_date`) VALUES
(1, 1, 1, 2, '2025-03-17'),
(2, 4, 1, 1, '2025-03-17'),
(3, 4, 1, 1, '2025-03-17'),
(4, 4, 1, 1, '2025-03-17');

-- --------------------------------------------------------

--
-- Table structure for table `mst_genre`
--

CREATE TABLE `mst_genre` (
  `genre_id` int(11) NOT NULL,
  `genre_name` varchar(25) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `mst_genre`
--

INSERT INTO `mst_genre` (`genre_id`, `genre_name`) VALUES
(1, 'Action'),
(2, 'Drama');

-- --------------------------------------------------------

--
-- Table structure for table `mst_movies`
--

CREATE TABLE `mst_movies` (
  `movie_id` int(11) NOT NULL,
  `title` varchar(25) DEFAULT NULL,
  `genre_id` int(11) DEFAULT NULL,
  `release_date` date DEFAULT NULL,
  `duration` int(11) DEFAULT NULL,
  `rating` float DEFAULT NULL,
  `img_url` varchar(150) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `mst_movies`
--

INSERT INTO `mst_movies` (`movie_id`, `title`, `genre_id`, `release_date`, `duration`, `rating`, `img_url`) VALUES
(1, 'Mirchi', 1, '0000-00-00', 160, 7.3, 'https://csv2contact.groupspend.in/img/mms/Mirchi.jpg'),
(2, 'Jersey', 2, '0000-00-00', 160, 8.5, 'https://csv2contact.groupspend.in/img/mms/Jersey.jpg');

-- --------------------------------------------------------

--
-- Table structure for table `mst_screenings`
--

CREATE TABLE `mst_screenings` (
  `screening_id` int(11) NOT NULL,
  `movie_id` int(11) DEFAULT NULL,
  `screen_number` varchar(3) DEFAULT NULL,
  `show_time` varchar(10) DEFAULT NULL,
  `available_seats` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `mst_screenings`
--

INSERT INTO `mst_screenings` (`screening_id`, `movie_id`, `screen_number`, `show_time`, `available_seats`) VALUES
(1, 1, 'B4', '6:00 PM', 197),
(3, 1, 'B5', '6:30 PM', 98);

-- --------------------------------------------------------

--
-- Table structure for table `mst_user`
--

CREATE TABLE `mst_user` (
  `user_id` int(11) NOT NULL,
  `username` varchar(25) NOT NULL,
  `password` varchar(50) NOT NULL,
  `role` int(1) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `mst_user`
--

INSERT INTO `mst_user` (`user_id`, `username`, `password`, `role`) VALUES
(1, 'admin', 'admin', 0),
(2, 'davasam', 'davasam', 0),
(3, 'test', 'test', 1),
(4, 'Karthikeya819', '123', 1);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `mst_bookings`
--
ALTER TABLE `mst_bookings`
  ADD PRIMARY KEY (`booking_id`),
  ADD KEY `user_id` (`user_id`),
  ADD KEY `screening_id` (`screening_id`);

--
-- Indexes for table `mst_genre`
--
ALTER TABLE `mst_genre`
  ADD PRIMARY KEY (`genre_id`);

--
-- Indexes for table `mst_movies`
--
ALTER TABLE `mst_movies`
  ADD PRIMARY KEY (`movie_id`),
  ADD KEY `genre_id` (`genre_id`);

--
-- Indexes for table `mst_screenings`
--
ALTER TABLE `mst_screenings`
  ADD PRIMARY KEY (`screening_id`),
  ADD KEY `movie_id` (`movie_id`);

--
-- Indexes for table `mst_user`
--
ALTER TABLE `mst_user`
  ADD PRIMARY KEY (`user_id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `mst_bookings`
--
ALTER TABLE `mst_bookings`
  MODIFY `booking_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;
--
-- AUTO_INCREMENT for table `mst_genre`
--
ALTER TABLE `mst_genre`
  MODIFY `genre_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;
--
-- AUTO_INCREMENT for table `mst_movies`
--
ALTER TABLE `mst_movies`
  MODIFY `movie_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;
--
-- AUTO_INCREMENT for table `mst_screenings`
--
ALTER TABLE `mst_screenings`
  MODIFY `screening_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;
--
-- AUTO_INCREMENT for table `mst_user`
--
ALTER TABLE `mst_user`
  MODIFY `user_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;
--
-- Constraints for dumped tables
--

--
-- Constraints for table `mst_bookings`
--
ALTER TABLE `mst_bookings`
  ADD CONSTRAINT `mst_bookings_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `mst_user` (`user_id`),
  ADD CONSTRAINT `mst_bookings_ibfk_2` FOREIGN KEY (`screening_id`) REFERENCES `mst_screenings` (`screening_id`);

--
-- Constraints for table `mst_movies`
--
ALTER TABLE `mst_movies`
  ADD CONSTRAINT `mst_movies_ibfk_1` FOREIGN KEY (`genre_id`) REFERENCES `mst_genre` (`genre_id`);

--
-- Constraints for table `mst_screenings`
--
ALTER TABLE `mst_screenings`
  ADD CONSTRAINT `mst_screenings_ibfk_1` FOREIGN KEY (`movie_id`) REFERENCES `mst_movies` (`movie_id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
