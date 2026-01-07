<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Doctors - Hospital Management System</title>
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
            <a href="<%= request.getContextPath() %>/jsp/patients.jsp">Patients</a>
            <a href="<%= request.getContextPath() %>/jsp/medecins.jsp" class="active">Doctors</a>
            <a href="<%= request.getContextPath() %>/jsp/rendezvous.jsp">Appointments</a>
            <a href="<%= request.getContextPath() %>/jsp/system-info.jsp">System</a>
        </div>
    </nav>

    <main>
        <div class="container">
            <div class="page-header">
                <h2><i class="fas fa-user-md" style="color: var(--primary); margin-right: 10px;"></i>Doctor Management</h2>
                <p>Manage medical staff and their specializations</p>
            </div>

            <div class="form-section" id="doctorFormSection">
                <h3 id="doctorFormHeading"><i class="fas fa-plus-circle" style="color: var(--primary); margin-right: 8px;"></i>Add New Doctor</h3>
                <form onsubmit="createDoctor(event)" class="form-grid">
                    <div class="form-group">
                        <label for="firstName">First Name *</label>
                        <input type="text" id="firstName" name="firstName" required placeholder="Enter first name">
                    </div>
                    <div class="form-group">
                        <label for="lastName">Last Name *</label>
                        <input type="text" id="lastName" name="lastName" required placeholder="Enter last name">
                    </div>
                    <div class="form-group">
                        <label for="specialization">Specialization *</label>
                        <select id="specialization" name="specialization" required>
                            <option value="">Select Specialization</option>
                            <option value="Cardiology">Cardiology</option>
                            <option value="Dermatology">Dermatology</option>
                            <option value="General Practice">General Practice</option>
                            <option value="Neurology">Neurology</option>
                            <option value="Pediatrics">Pediatrics</option>
                            <option value="Psychiatry">Psychiatry</option>
                            <option value="Surgery">Surgery</option>
                            <option value="Orthopedics">Orthopedics</option>
                        </select>
                    </div>
                    <div class="form-group">
                        <label for="licenseNumber">License Number *</label>
                        <input type="text" id="licenseNumber" name="licenseNumber" required placeholder="Medical license ID">
                    </div>
                    <div class="form-group">
                        <label for="email">Email</label>
                        <input type="email" id="email" name="email" placeholder="doctor@hospital.com">
                    </div>
                    <div class="form-group">
                        <label for="phone">Phone</label>
                        <input type="tel" id="phone" name="phone" placeholder="+212 600 000 000">
                    </div>
                    <div class="form-group" style="grid-column: 1 / -1;">
                        <label for="officeHours">Office Hours</label>
                        <input type="text" id="officeHours" name="officeHours" placeholder="e.g., Mon-Fri: 9:00 AM - 5:00 PM">
                    </div>
                    <div class="form-actions" style="grid-column: 1 / -1; border-top: none; padding-top: 0; margin-top: 0.5rem;">
                        <button type="submit" class="btn btn-primary">
                            <i class="fas fa-plus"></i> Add Doctor
                        </button>
                        <button type="reset" class="btn btn-secondary">
                            <i class="fas fa-redo"></i> Reset
                        </button>
                    </div>
                </form>
            </div>

            <div class="table-section">
                <div class="table-header">
                    <h3><i class="fas fa-stethoscope" style="color: var(--primary); margin-right: 8px;"></i>Medical Staff Directory</h3>
                </div>
                <div class="table-wrapper">
                    <table id="doctorsTable">
                        <thead>
                            <tr>
                                <th>Name</th>
                                <th>Specialization</th>
                                <th>Email</th>
                                <th>Phone</th>
                                <th>Status</th>
                                <th>Actions</th>
                            </tr>
                        </thead>
                        <tbody>
                            <tr>
                                <td colspan="6" class="text-center">Loading doctors...</td>
                            </tr>
                        </tbody>
                    </table>
                </div>
            </div>

            <div class="grid grid-4" style="margin-top: 2rem;">
                <div class="card">
                    <div class="card-header">
                        <i class="fas fa-heart"></i>
                        <h3>Cardiology</h3>
                    </div>
                    <div class="card-body">
                        <p><strong>Staff:</strong> 2 doctors</p>
                        <p><strong>Floor:</strong> 3rd Floor</p>
                    </div>
                </div>
                <div class="card">
                    <div class="card-header">
                        <i class="fas fa-baby"></i>
                        <h3>Pediatrics</h3>
                    </div>
                    <div class="card-body">
                        <p><strong>Staff:</strong> 2 doctors</p>
                        <p><strong>Floor:</strong> 4th Floor</p>
                    </div>
                </div>
                <div class="card">
                    <div class="card-header">
                        <i class="fas fa-cut"></i>
                        <h3>Surgery</h3>
                    </div>
                    <div class="card-body">
                        <p><strong>Staff:</strong> 3 doctors</p>
                        <p><strong>Floor:</strong> 2nd Floor</p>
                    </div>
                </div>
                <div class="card">
                    <div class="card-header">
                        <i class="fas fa-brain"></i>
                        <h3>Neurology</h3>
                    </div>
                    <div class="card-body">
                        <p><strong>Staff:</strong> 1 doctor</p>
                        <p><strong>Floor:</strong> 5th Floor</p>
                    </div>
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
