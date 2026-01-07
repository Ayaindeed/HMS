<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Patients - Hospital Management System</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/style.css">
</head>
<body>
    <nav>
        <div>
            <i class="fas fa-hospital" style="font-size: 1.5rem; color: white;"></i>
            <span>HMS</span>
        </div>
        <div>
            <a href="<%= request.getContextPath() %>/">Home</a>
            <a href="<%= request.getContextPath() %>/jsp/dashboard.jsp">Dashboard</a>
            <a href="<%= request.getContextPath() %>/jsp/patients.jsp" class="active">Patients</a>
            <a href="<%= request.getContextPath() %>/jsp/medecins.jsp">Doctors</a>
            <a href="<%= request.getContextPath() %>/jsp/rendezvous.jsp">Appointments</a>
            <a href="<%= request.getContextPath() %>/jsp/system-info.jsp">System</a>
        </div>
    </nav>

    <main>
        <div class="container">
            <div class="page-header">
                <h2><i class="fas fa-users" style="color: var(--primary); margin-right: 10px;"></i>Patient Management</h2>
                <p>Create, update, and manage patient records</p>
            </div>

            <div class="form-section">
                <h3><i class="fas fa-search" style="color: var(--primary); margin-right: 8px;"></i>Search Patients</h3>
                <form onsubmit="searchPatients(event)" class="form-grid">
                    <div class="form-group">
                        <label for="searchFirstName">First Name</label>
                        <input type="text" id="searchFirstName" name="firstName" placeholder="Enter first name">
                    </div>
                    <div class="form-group">
                        <label for="searchLastName">Last Name</label>
                        <input type="text" id="searchLastName" name="lastName" placeholder="Enter last name">
                    </div>
                    <div class="form-group" style="display: flex; align-items: flex-end;">
                        <button type="submit" class="btn btn-primary" style="width: 100%;">
                            <i class="fas fa-search"></i> Search
                        </button>
                    </div>
                </form>
            </div>

            <div class="form-section" id="patientFormSection">
                <h3 id="patientFormHeading"><i class="fas fa-user-plus" style="color: var(--primary); margin-right: 8px;"></i>Add New Patient</h3>
                <form onsubmit="createPatient(event)" class="form-grid">
                    <div class="form-group">
                        <label for="firstName">First Name *</label>
                        <input type="text" id="firstName" name="firstName" required placeholder="Enter first name">
                    </div>
                    <div class="form-group">
                        <label for="lastName">Last Name *</label>
                        <input type="text" id="lastName" name="lastName" required placeholder="Enter last name">
                    </div>
                    <div class="form-group">
                        <label for="dateOfBirth">Date of Birth *</label>
                        <input type="date" id="dateOfBirth" name="dateOfBirth" required>
                    </div>
                    <div class="form-group">
                        <label for="gender">Gender *</label>
                        <select id="gender" name="gender" required>
                            <option value="">Select Gender</option>
                            <option value="Male">Male</option>
                            <option value="Female">Female</option>
                            <option value="Other">Other</option>
                        </select>
                    </div>
                    <div class="form-group">
                        <label for="email">Email</label>
                        <input type="email" id="email" name="email" placeholder="patient@example.com">
                    </div>
                    <div class="form-group">
                        <label for="phone">Phone</label>
                        <input type="tel" id="phone" name="phone" placeholder="+212 600 000 000">
                    </div>
                    <div class="form-group">
                        <label for="address">Address</label>
                        <input type="text" id="address" name="address" placeholder="Street address">
                    </div>
                    <div class="form-group">
                        <label for="city">City</label>
                        <input type="text" id="city" name="city" placeholder="City">
                    </div>
                    <div class="form-group">
                        <label for="bloodType">Blood Type</label>
                        <select id="bloodType" name="bloodType">
                            <option value="">Select Blood Type</option>
                            <option value="O+">O+</option>
                            <option value="O-">O-</option>
                            <option value="A+">A+</option>
                            <option value="A-">A-</option>
                            <option value="B+">B+</option>
                            <option value="B-">B-</option>
                            <option value="AB+">AB+</option>
                            <option value="AB-">AB-</option>
                        </select>
                    </div>
                    <div class="form-group">
                        <label for="insuranceNumber">Insurance Number</label>
                        <input type="text" id="insuranceNumber" name="insuranceNumber" placeholder="Insurance ID">
                    </div>
                    <div class="form-actions" style="grid-column: 1 / -1; border-top: none; padding-top: 0; margin-top: 0.5rem;">
                        <button type="submit" class="btn btn-primary">
                            <i class="fas fa-plus"></i> Add Patient
                        </button>
                        <button type="reset" class="btn btn-secondary">
                            <i class="fas fa-redo"></i> Reset
                        </button>
                    </div>
                </form>
            </div>

            <div class="table-section">
                <div class="table-header">
                    <h3><i class="fas fa-list" style="color: var(--primary); margin-right: 8px;"></i>Patient Records</h3>
                </div>
                <div class="table-wrapper">
                    <table id="patientsTable">
                        <thead>
                            <tr>
                                <th>ID</th>
                                <th>Name</th>
                                <th>Email</th>
                                <th>Phone</th>
                                <th>Blood Type</th>
                                <th>Actions</th>
                            </tr>
                        </thead>
                        <tbody>
                            <tr>
                                <td colspan="6" class="text-center">Loading patient data...</td>
                            </tr>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </main>

    <footer>
        <p>Hospital Management System - 2024</p>
        <p>Enterprise Healthcare Platform</p>
    </footer>

    <script src="<%= request.getContextPath() %>/js/dashboard.js"></script>
</body>
</html>
