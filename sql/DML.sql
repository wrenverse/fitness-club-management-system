-- Initial DML
-- Start out with some population already in the database. 

-- Insert Members
INSERT INTO members (name, date_of_birth, gender, email, phone, join_date) VALUES
('Lily Yue', '2004-03-03', 'Female', 'lily.yue@gmail.com', '123-456-7890', '2025-09-27'),
('Emma Lee', '2008-07-17', 'Other', 'emma.lee@gmail.com', '134-678-7830', '2025-03-25'),
('Jasmine Smith', '1987-08-11', 'Female', 'jasmine.smith@gmail.com', '987-654-3210', '2024-01-16'),
('Jack Windship', '2001-05-05', 'Male', 'jack.windship@gmail.com', '983-475-6461', '2023-11-04,'),
('Will Leung', '1993-12-30', 'Male', 'will.leung@gmail.com', '130-986-5742', '2023-10-18');

-- Insert Trainers
INSERT INTO trainers (name, email, phone, specialization, hire_date)
VALUES
('Cassie Min', 'cassie.min@gmail.com', '243-890-3487', 'Yoga & Pilates', '2022-12-01'),
('Howard Brown', 'howard.brown@hotmail.com', '478-263-0850', 'Strength Training', '2022-12-02'),
('Michelle Anderson', 'michelle.anderson@gmail.com', '642-789-1900', 'Cardio & HITT', '2022-12-03');

-- Insert Rooms
INSERT INTO rooms (location) VALUES
('Studio A'),
('Studio B'),
('Weight Room'),
('Combatives Room');


-- Insert Equipment
INSERT INTO equipment (room_id, name, is_operational) VALUES
(1, 'Yoga Mats', TRUE),
(1, 'Ballet Barres', TRUE),
(2, 'Treadmill', TRUE),
(2, 'Rowing Machine', TRUE),
(3, 'Weight Bench 1', TRUE),
(3, 'Weight Bench 2', TRUE),
(4, 'Boxing Gloves', TRUE),
(4, 'Mock Weapons', TRUE);

-- Goal Types
INSERT INTO goal_types (name, unit) VALUES
    ('Weight Loss', 'kg'),
    ('Body Fat Reduction', '%'),
    ('Strength (Bench Press)', 'kg');

-- Fitness Goals
INSERT INTO fitness_goals (member_id, type_id, target_value, target_date, start_date, is_completed) VALUES
    (1, 1, 5.0, '2025-12-01', '2025-10-10', FALSE), 
    (2, 2, 3.0, '2025-6-15', '2025-04-11', FALSE);

-- Health Metrics
INSERT INTO health_metrics (member_id, timestamp, heart_rate, body_fat, weight, height) VALUES
    (1, '2025-10-10 09:00', 72, 25.5, 70.0, 170.0),
    (1, '2025-11-10 09:00', 70, 24.8, 68.5, 170.0);

-- Insert Trainer Availability
INSERT INTO trainer_availability (trainer_id, start_timestamp, end_timestamp, recurrences) VALUES
    (1, '2025-12-15 09:00', '2025-12-15 11:00', 4),
    (1, '2025-12-16 14:00', '2025-12-16 16:00', 0),
    (2, '2025-12-17 08:30', '2025-12-17 10:00', 4),
    (2, '2025-12-18 08:30', '2025-12-18 10:00', 7),
    (3, '2025-12-17 14:30', '2025-12-17 16:00', 4),
    (3, '2025-12-18 13:30', '2025-12-18 15:00', 7);

-- Insert Private Training Sessions
INSERT INTO pt_sessions (trainer_id, member_id, room_id, start_timestamp, end_timestamp) VALUES
(1, 1, 1, '2025-12-15 10:00', '2025-12-15 11:00'),
(1, 2, 1, '2025-12-22 10:00', '2025-12-22 11:00');

-- Insert Classes

INSERT INTO classes (trainer_id, room_id, name, capacity, start_timestamp, end_timestamp) VALUES
    (1, 1, 'Morning Yoga',       20, '2025-12-20 07:00', '2025-12-22 08:00'),
    (3, 2, 'Evening HIIT',       25, '2025-12-22 18:00', '2025-01-22 18:45'),
    (2, 3, 'Strength Basics',    15, '2025-12-23 17:00', '2025-01-23 18:00');


-- Insert Class Registrations
INSERT INTO class_registration (class_id, member_id, register_date) VALUES
    (1, 1, '2025-12-18'),
    (1, 2, '2025-12-18'),
    (2, 3, '2025-12-19'),
    (2, 4, '2025-12-19'),
    (3, 5, '2025-12-20');

-- Insert Maintenance Ticket
INSERT INTO maintenance_tickets (equipment_id, report_date, description, resolved_date, is_completed) VALUES
    (2, '2025-11-25', 'Unusual noise from motor', NULL, TRUE);

-- Insert Invoice 
INSERT INTO invoices (member_id, issue_timestamp, total, is_paid) VALUES
    (1, '2025-11-30 10:00', 80.00, FALSE);

-- Insert Invoice Items
INSERT INTO invoice_items (invoice_id, item_num, item_type, quantity, unit_price, total_price) VALUES
    (1, 1, 'PT Session', 2, 40.00, 80.00);

-- Insert Payment

INSERT INTO payments (invoice_id, amount_paid, method, payment_date) VALUES
    (1, 80.00, 'Credit Card', '2025-12-01');

