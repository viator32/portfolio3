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

-- Optionally insert initial data
INSERT INTO university (name, country, department_name, department_url, contact_person, max_outgoing_students, max_incoming_students, next_spring_semester_start, next_autumn_semester_start) VALUES
                                                                                                                                                                                                  ('Stanford University', 'USA', 'Computer Science', 'http://stanford.edu', 'Dr. John Doe', 10, 5, '2024-01-10', '2023-09-01'),
                                                                                                                                                                                                  ('Harvard University', 'USA', 'Computer Science', 'http://harvard.edu', 'Dr. Jane Smith', 8, 6, '2024-02-15', '2023-08-20');

INSERT INTO modules (name, semester, credit_points, university_id) VALUES
                                                                       ('Introduction to Computer Science', 1, 5, 1),
                                                                       ('Advanced Algorithms', 2, 5, 1),
                                                                       ('Data Structures', 1, 4, 2),
                                                                       ('Machine Learning', 2, 5, 2);
