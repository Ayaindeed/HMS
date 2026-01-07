<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Hospital Management System</title>
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
            <a href="<%= request.getContextPath() %>/" class="active">Home</a>
            <a href="<%= request.getContextPath() %>/jsp/dashboard.jsp">Dashboard</a>
            <a href="<%= request.getContextPath() %>/jsp/patients.jsp">Patients</a>
            <a href="<%= request.getContextPath() %>/jsp/medecins.jsp">Doctors</a>
            <a href="<%= request.getContextPath() %>/jsp/rendezvous.jsp">Appointments</a>
            <a href="<%= request.getContextPath() %>/jsp/system-info.jsp">System</a>
        </div>
    </nav>

    <main>
        <div class="hero-section">
            <div class="container">
                <h1>Hospital Management System</h1>
                <p>Streamline your healthcare operations with our comprehensive management platform</p>
                <div class="cta-buttons">
                    <a href="<%= request.getContextPath() %>/jsp/dashboard.jsp" class="btn btn-lg" style="background: white; color: #0891B2;">
                        <i class="fas fa-chart-line"></i> View Dashboard
                    </a>
                    <a href="<%= request.getContextPath() %>/jsp/patients.jsp" class="btn btn-lg btn-outline" style="border-color: white; color: white;">
                        <i class="fas fa-user-plus"></i> Add Patient
                    </a>
                </div>
            </div>
        </div>

        <div class="container">
            <div class="page-header" style="text-align: center; margin-top: 2rem;">
                <h2>Our Services</h2>
                <p>Complete healthcare management solutions</p>
            </div>
            
            <div class="grid grid-3" style="margin-top: 2rem;">
                <div class="card">
                    <div class="card-header">
                        <i class="fas fa-users"></i>
                        <h3>Patient Management</h3>
                    </div>
                    <div class="card-body">
                        <p>Complete patient records with medical history, contact information, and appointment tracking.</p>
                    </div>
                </div>

                <div class="card">
                    <div class="card-header">
                        <i class="fas fa-calendar-check"></i>
                        <h3>Appointments</h3>
                    </div>
                    <div class="card-body">
                        <p>Efficient scheduling system with conflict detection and automated reminders.</p>
                    </div>
                </div>

                <div class="card">
                    <div class="card-header">
                        <i class="fas fa-user-md"></i>
                        <h3>Doctor Directory</h3>
                    </div>
                    <div class="card-body">
                        <p>Comprehensive physician database with specializations and availability tracking.</p>
                    </div>
                </div>

                <div class="card">
                    <div class="card-header">
                        <i class="fas fa-chart-pie"></i>
                        <h3>Analytics</h3>
                    </div>
                    <div class="card-body">
                        <p>Real-time analytics and reporting on patient metrics and hospital performance.</p>
                    </div>
                </div>

                <div class="card">
                    <div class="card-header">
                        <i class="fas fa-shield-alt"></i>
                        <h3>Security</h3>
                    </div>
                    <div class="card-body">
                        <p>Enterprise-grade security with encrypted data storage and access controls.</p>
                    </div>
                </div>

                <div class="card">
                    <div class="card-header">
                        <i class="fas fa-database"></i>
                        <h3>Data Management</h3>
                    </div>
                    <div class="card-body">
                        <p>Integrated database systems with PostgreSQL, MongoDB, and Redis caching.</p>
                    </div>
                </div>
            </div>
        </div>

        <div style="background: var(--surface); padding: 3rem 0; margin-top: 3rem;">
            <div class="container">
                <div class="page-header" style="text-align: center;">
                    <h2>Quick Stats</h2>
                    <p>Current system overview</p>
                </div>
                <div class="dashboard-grid" style="padding: 0; margin-top: 2rem;">
                    <div class="card stat-card">
                        <div class="card-title">Total Patients</div>
                        <div class="stat-number">10</div>
                        <div class="stat-label">Registered patients</div>
                    </div>
                    <div class="card stat-card success">
                        <div class="card-title">Active Doctors</div>
                        <div class="stat-number">8</div>
                        <div class="stat-label">Medical staff</div>
                    </div>
                    <div class="card stat-card warning">
                        <div class="card-title">Appointments</div>
                        <div class="stat-number">24</div>
                        <div class="stat-label">This month</div>
                    </div>
                    <div class="card stat-card">
                        <div class="card-title">Departments</div>
                        <div class="stat-number">6</div>
                        <div class="stat-label">Specializations</div>
                    </div>
                </div>
            </div>
        </div>
    </main>

    <footer>
        <p>Hospital Management System - 2024</p>
        <p>Enterprise Healthcare Platform</p>
    </footer>
</body>
</html>

