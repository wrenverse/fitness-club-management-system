-- Users
CREATE TABLE members (
    member_id       SERIAL PRIMARY KEY,
    name            VARCHAR(100) NOT NULL,
    date_of_birth   DATE NOT NULL,
    gender          VARCHAR(20) NOT NULL,
    email           VARCHAR(150) NOT NULL UNIQUE,
    phone           VARCHAR(20) NOT NULL,
    join_date       DATE DEFAULT CURRENT_DATE
);

CREATE TABLE trainers (
    trainer_id      SERIAL PRIMARY KEY,
    name            VARCHAR(100) NOT NULL,
    email           VARCHAR(150) NOT NULL UNIQUE,
    phone           VARCHAR(20),
    specialization  VARCHAR(50),
    hire_date       DATE DEFAULT CURRENT_DATE
);

-- Rooms & Equipment
CREATE TABLE rooms (
    room_id         SERIAL PRIMARY KEY,
    location        VARCHAR(50)
);

CREATE TABLE equipment (
    equipment_id    SERIAL PRIMARY KEY,
    room_id         INT NOT NULL,
    name            VARCHAR(100) NOT NULL,
    is_operational  BOOLEAN NOT NULL DEFAULT TRUE,
    FOREIGN KEY (room_id)
        REFERENCES rooms(room_id)
);

-- Member Goals & Health Metrics
CREATE TABLE goal_types(
    type_id         SERIAL PRIMARY KEY,
    name            VARCHAR(50),
    unit            VARCHAR(20)
);

CREATE TABLE fitness_goal (
    goal_id         SERIAL PRIMARY KEY,
    member_id       INT NOT NULL,
    type_id         INT NOT NULL,  
    target_value    FLOAT NOT NULL,
    target_date     DATE NOT NULL,
    start_date      DATE DEFAULT CURRENT_DATE,
    is_completed    BOOLEAN NOT NULL DEFAULT FALSE,
    FOREIGN KEY (member_id)
        REFERENCES members(member_id),
    FOREIGN KEY (type_id)
        REFERENCES goal_types(type_id) 
);

CREATE TABLE health_metrics (
    metric_id       SERIAL PRIMARY KEY,
    member_id       INT NOT NULL,
    timestamp       TIMESTAMP,
    heart_rate      INT NOT NULL,
    body_fat        FLOAT NOT NULL,
    weight          FLOAT NOT NULL,
    height          FLOAT NOT NULL,
    FOREIGN KEY (member_id)
        REFERENCES members(member_id)
);

-- Trainer Availability & PT-Sessions
CREATE TABLE trainer_availability (
    availability_id SERIAL PRIMARY KEY,
    trainer_id      INT NOT NULL,
    start_timestamp TIMESTAMP NOT NULL,
    end_timestamp   TIMESTAMP NOT NULL,
    recurrences      INT NOT NULL DEFAULT 0,
    FOREIGN KEY (trainer_id)
        REFERENCES trainers(trainer_id)
);

CREATE TABLE pt_sessions (
    session_id      SERIAL PRIMARY KEY,
    trainer_id      INT NOT NULL,
    member_id       INT NOT NULL,
    room_id         INT NOT NULL,
    start_timestamp TIMESTAMP NOT NULL,
    end_timestamp 	TIMESTAMP NOT NULL,
    FOREIGN KEY (trainer_id)
        REFERENCES trainers(trainer_id),
    FOREIGN KEY (member_id)
        REFERENCES members(member_id),
    FOREIGN KEY (room_id)
        REFERENCES rooms(room_id)
);

-- Classes & Registration (weak entity)
CREATE TABLE classes (
    class_id        SERIAL PRIMARY KEY,
    trainer_id      INT NOT NULL,
    room_id         INT NOT NULL,
    name            VARCHAR(50) NOT NULL,
    capacity        INT NOT NULL,
    start_timestamp TIMESTAMP NOT NULL,
    end_timestamp   TIMESTAMP NOT NULL,
    FOREIGN KEY (trainer_id)
        REFERENCES trainers(trainer_id),
    FOREIGN KEY (room_id)
        REFERENCES rooms(room_id)
);

CREATE TABLE class_registration (
    class_id        INT NOT NULL,
    member_id       INT NOT NULL,
    register_date   DATE NOT NULL,
    PRIMARY KEY (class_id, member_id),
    FOREIGN KEY (class_id)
        REFERENCES classes(class_id),
    FOREIGN KEY (member_id)
        REFERENCES members(member_id)
);

-- Maintenance Tickets

CREATE TABLE maintenance_ticket (
    ticket_id       SERIAL PRIMARY KEY,
    equipment_id    INT,
    report_date     DATE NOT NULL,
    description     VARCHAR(150),
    resolved_date   DATE,
    is_completed    BOOLEAN NOT NULL DEFAULT FALSE,
    FOREIGN KEY (equipment_id)
        REFERENCES equipment(equipment_id)
);

-- Invoice, Invoice Items (weak entity), and Payments
CREATE TABLE invoice (
    invoice_id      SERIAL PRIMARY KEY,
    member_id       INT NOT NULL,
    issue_timestamp TIMESTAMP NOT NULL,
    total           FLOAT NOT NULL,
    is_paid         BOOLEAN NOT NULL DEFAULT FALSE,
    FOREIGN KEY (member_id)
        REFERENCES members(member_id)
);

CREATE TABLE invoice_items (
    invoice_id      INT NOT NULL,
    item_type       VARCHAR(50) NOT NULL,
    quantity        INT NOT NULL,
    unit_price      FLOAT NOT NULL,
    total_price     FLOAT NOT NULL,
    PRIMARY KEY (invoice_id),
    FOREIGN KEY (invoice_id)
        REFERENCES invoice(invoice_id)
);

CREATE TABLE payments (
    payment_id      SERIAL PRIMARY KEY,
    invoice_id      INT NOT NULL,
    amount_paid     FLOAT NOT NULL,
    method          VARCHAR(20) NOT NULL,
    payment_date    DATE NOT NULL,
    FOREIGN KEY (invoice_id)
        REFERENCES invoice(invoice_id)
);
