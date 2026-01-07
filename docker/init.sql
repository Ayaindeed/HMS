-- Hospital Management System Database Initialization Script
-- PostgreSQL Database Setup

-- Create enum types
CREATE TYPE gender_type AS ENUM ('Male', 'Female', 'Other');
CREATE TYPE appointment_status AS ENUM ('SCHEDULED', 'COMPLETED', 'CANCELLED');

-- Create tables
CREATE TABLE services_hospitaliers (
    service_id SERIAL PRIMARY KEY,
    service_name VARCHAR(100) NOT NULL UNIQUE,
    description VARCHAR(500),
    floor_number INT,
    phone VARCHAR(20),
    is_active BOOLEAN DEFAULT true,
    created_at DATE NOT NULL,
    updated_at DATE NOT NULL
);

CREATE TABLE medecins (
    medecin_id SERIAL PRIMARY KEY,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    specialization VARCHAR(100) NOT NULL,
    license_number VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(150) UNIQUE,
    phone VARCHAR(20),
    office_hours VARCHAR(255),
    service_id INT NOT NULL,
    is_available BOOLEAN DEFAULT true,
    created_at DATE NOT NULL,
    updated_at DATE NOT NULL,
    FOREIGN KEY (service_id) REFERENCES services_hospitaliers(service_id)
);

CREATE TABLE patients (
    patient_id SERIAL PRIMARY KEY,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    date_of_birth DATE,
    gender VARCHAR(10),
    email VARCHAR(150) UNIQUE,
    phone VARCHAR(20),
    address VARCHAR(255),
    city VARCHAR(100),
    blood_type VARCHAR(5),
    insurance_number VARCHAR(50),
    is_active BOOLEAN DEFAULT true,
    created_at DATE NOT NULL,
    updated_at DATE NOT NULL
);

CREATE TABLE rendezvous (
    rendezvous_id SERIAL PRIMARY KEY,
    patient_id INT NOT NULL,
    medecin_id INT NOT NULL,
    appointment_date DATE NOT NULL,
    appointment_time TIME NOT NULL,
    status VARCHAR(20) DEFAULT 'SCHEDULED',
    reason VARCHAR(500),
    notes VARCHAR(1000),
    created_at DATE NOT NULL,
    updated_at DATE NOT NULL,
    FOREIGN KEY (patient_id) REFERENCES patients(patient_id),
    FOREIGN KEY (medecin_id) REFERENCES medecins(medecin_id)
);

CREATE TABLE dossiers_medicaux (
    dossier_id SERIAL PRIMARY KEY,
    patient_id INT NOT NULL UNIQUE,
    allergies VARCHAR(1000),
    chronic_diseases VARCHAR(1000),
    medications VARCHAR(1000),
    surgical_history VARCHAR(2000),
    family_history VARCHAR(1000),
    vaccination_history VARCHAR(1000),
    height_cm DECIMAL(5,2),
    weight_kg DECIMAL(5,2),
    blood_pressure VARCHAR(20),
    notes VARCHAR(2000),
    created_at DATE NOT NULL,
    updated_at DATE NOT NULL,
    FOREIGN KEY (patient_id) REFERENCES patients(patient_id)
);

-- Create indexes for better query performance
CREATE INDEX idx_patients_email ON patients(email);
CREATE INDEX idx_patients_city ON patients(city);
CREATE INDEX idx_patients_blood_type ON patients(blood_type);
CREATE INDEX idx_medecins_specialization ON medecins(specialization);
CREATE INDEX idx_medecins_license ON medecins(license_number);
CREATE INDEX idx_rendezvous_patient ON rendezvous(patient_id);
CREATE INDEX idx_rendezvous_medecin ON rendezvous(medecin_id);
CREATE INDEX idx_rendezvous_date ON rendezvous(appointment_date);
CREATE INDEX idx_rendezvous_status ON rendezvous(status);

-- Insert sample hospital services
INSERT INTO services_hospitaliers (service_name, description, floor_number, phone, created_at, updated_at) 
VALUES 
    ('Cardiology', 'Heart and cardiovascular diseases', 3, '+212 522 111 001', NOW(), NOW()),
    ('Pediatrics', 'Child health and development', 4, '+212 522 111 002', NOW(), NOW()),
    ('Surgery', 'General and specialized surgery', 2, '+212 522 111 003', NOW(), NOW()),
    ('General Practice', 'Primary healthcare', 1, '+212 522 111 004', NOW(), NOW()),
    ('Neurology', 'Nervous system disorders', 5, '+212 522 111 005', NOW(), NOW()),
    ('Dermatology', 'Skin conditions and treatments', 3, '+212 522 111 006', NOW(), NOW()),
    ('Orthopedics', 'Bone and joint care', 2, '+212 522 111 007', NOW(), NOW()),
    ('Psychiatry', 'Mental health services', 6, '+212 522 111 008', NOW(), NOW());

-- Insert sample doctors (8 Moroccan doctors)
INSERT INTO medecins (first_name, last_name, specialization, license_number, email, phone, service_id, is_available, created_at, updated_at)
VALUES
    ('Ahmed', 'Benali', 'Cardiology', 'LIC001', 'ahmed.benali@hospital.ma', '+212 600 111 222', 1, true, NOW(), NOW()),
    ('Fatima', 'Alaoui', 'Pediatrics', 'LIC002', 'fatima.alaoui@hospital.ma', '+212 600 222 333', 2, true, NOW(), NOW()),
    ('Mohammed', 'Tazi', 'Surgery', 'LIC003', 'mohammed.tazi@hospital.ma', '+212 600 333 444', 3, false, NOW(), NOW()),
    ('Amina', 'Berrada', 'Dermatology', 'LIC004', 'amina.berrada@hospital.ma', '+212 600 444 555', 6, true, NOW(), NOW()),
    ('Youssef', 'Idrissi', 'Neurology', 'LIC005', 'youssef.idrissi@hospital.ma', '+212 600 555 666', 5, false, NOW(), NOW()),
    ('Salma', 'Chraibi', 'General Practice', 'LIC006', 'salma.chraibi@hospital.ma', '+212 600 666 777', 4, true, NOW(), NOW()),
    ('Karim', 'Ouazzani', 'Orthopedics', 'LIC007', 'karim.ouazzani@hospital.ma', '+212 600 777 888', 7, true, NOW(), NOW()),
    ('Laila', 'Fassi', 'Psychiatry', 'LIC008', 'laila.fassi@hospital.ma', '+212 600 888 999', 8, true, NOW(), NOW());

-- Insert 10 Moroccan patients (mock data)
INSERT INTO patients (first_name, last_name, date_of_birth, gender, email, phone, address, city, blood_type, insurance_number, is_active, created_at, updated_at)
VALUES
    ('Fatima', 'Bennani', '1985-05-15', 'Female', 'fatima.bennani@email.com', '+212 600 112 233', '123 Rue Hassan II', 'Casablanca', 'O+', 'INS001', true, NOW(), NOW()),
    ('Mohammed', 'Alaoui', '1990-08-22', 'Male', 'mohammed.alaoui@email.com', '+212 600 223 344', '45 Avenue Mohammed V', 'Rabat', 'A-', 'INS002', true, NOW(), NOW()),
    ('Amina', 'Belkasmi', '1988-03-10', 'Female', 'amina.belkasmi@email.com', '+212 600 334 455', '78 Rue Ibn Sina', 'Marrakech', 'B+', 'INS003', true, NOW(), NOW()),
    ('Hassan', 'Tafat', '1975-12-01', 'Male', 'hassan.tafat@email.com', '+212 600 445 566', '12 Boulevard Zerktouni', 'Fes', 'AB-', 'INS004', true, NOW(), NOW()),
    ('Noor', 'Bouafia', '1992-07-18', 'Female', 'noor.bouafia@email.com', '+212 600 556 677', '34 Rue Atlas', 'Tangier', 'O-', 'INS005', true, NOW(), NOW()),
    ('Ahmed', 'Zahra', '1983-11-25', 'Male', 'ahmed.zahra@email.com', '+212 600 667 788', '56 Avenue Hassan I', 'Agadir', 'A+', 'INS006', true, NOW(), NOW()),
    ('Layla', 'Rachid', '1995-02-14', 'Female', 'layla.rachid@email.com', '+212 600 778 899', '89 Rue Fes', 'Oujda', 'B-', 'INS007', true, NOW(), NOW()),
    ('Karim', 'Ouadda', '1980-09-08', 'Male', 'karim.ouadda@email.com', '+212 600 889 900', '23 Boulevard Moulay Ismail', 'Meknes', 'AB+', 'INS008', true, NOW(), NOW()),
    ('Samira', 'Assala', '1987-04-30', 'Female', 'samira.assala@email.com', '+212 600 990 011', '67 Rue Tetouan', 'Tetouan', 'O+', 'INS009', true, NOW(), NOW()),
    ('Youssef', 'Machroo', '1978-06-20', 'Male', 'youssef.machroo@email.com', '+212 600 101 122', '90 Avenue Kenitra', 'Kenitra', 'A-', 'INS010', true, NOW(), NOW());

-- Insert sample appointments
INSERT INTO rendezvous (patient_id, medecin_id, appointment_date, appointment_time, status, reason, created_at, updated_at)
VALUES
    (1, 1, CURRENT_DATE + INTERVAL '1 day', '09:00', 'SCHEDULED', 'General checkup', NOW(), NOW()),
    (2, 2, CURRENT_DATE + INTERVAL '1 day', '10:30', 'SCHEDULED', 'Heart examination', NOW(), NOW()),
    (3, 3, CURRENT_DATE + INTERVAL '2 days', '14:00', 'SCHEDULED', 'Follow-up visit', NOW(), NOW()),
    (4, 4, CURRENT_DATE + INTERVAL '2 days', '15:30', 'SCHEDULED', 'Pre-surgery consultation', NOW(), NOW()),
    (5, 1, CURRENT_DATE + INTERVAL '3 days', '11:00', 'SCHEDULED', 'Annual checkup', NOW(), NOW());
