-- Create the university table first
CREATE TABLE IF NOT EXISTS university (
                                          id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                          name VARCHAR(255) NOT NULL,
    country VARCHAR(255) NOT NULL,
    department_name VARCHAR(255),
    department_url VARCHAR(255),
    contact_person VARCHAR(255),
    max_outgoing_students INT,
    max_incoming_students INT,
    next_spring_semester_start DATE,
    next_autumn_semester_start DATE
    );

-- Create the modules table with a foreign key constraint on university_id
CREATE TABLE IF NOT EXISTS modules (
                                       id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                       name VARCHAR(255) NOT NULL,
    semester INT NOT NULL,
    credit_points INT NOT NULL,
    university_id BIGINT,
    FOREIGN KEY (university_id) REFERENCES university(id)
    );

