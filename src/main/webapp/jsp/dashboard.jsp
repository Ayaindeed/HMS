<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Dashboard - Hospital Management System</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/style.css">
    <script src="https://cdn.jsdelivr.net/npm/apexcharts@latest"></script>
</head>
<body>
    <nav>
        <div>
            <i class="fas fa-hospital" style="font-size: 1.5rem; color: white;"></i>
            <span>HMS</span>
        </div>
        <div>
            <a href="<%= request.getContextPath() %>/">Home</a>
            <a href="<%= request.getContextPath() %>/jsp/dashboard.jsp" class="active">Dashboard</a>
            <a href="<%= request.getContextPath() %>/jsp/patients.jsp">Patients</a>
            <a href="<%= request.getContextPath() %>/jsp/medecins.jsp">Doctors</a>
            <a href="<%= request.getContextPath() %>/jsp/rendezvous.jsp">Appointments</a>
            <a href="<%= request.getContextPath() %>/jsp/system-info.jsp">System</a>
        </div>
    </nav>

    <main>
        <div class="container">
            <div class="page-header">
                <h2><i class="fas fa-chart-line" style="color: var(--primary); margin-right: 10px;"></i>Analytics Dashboard</h2>
                <p>Monitor key metrics and hospital performance</p>
            </div>
        </div>

        <div class="dashboard-grid">
            <div class="card stat-card success">
                <div class="card-title">Total Patients</div>
                <div class="stat-number" id="totalPatients">0</div>
                <div class="stat-label">Registered in system</div>
            </div>

            <div class="card stat-card">
                <div class="card-title">Active Patients</div>
                <div class="stat-number" id="activePatients">0</div>
                <div class="stat-label">Currently active</div>
            </div>

            <div class="card stat-card warning">
                <div class="card-title">Appointments</div>
                <div class="stat-number" id="totalAppointments">0</div>
                <div class="stat-label">Total scheduled</div>
            </div>

            <div class="card stat-card">
                <div class="card-title">Scheduled</div>
                <div class="stat-number" id="scheduledAppointments">0</div>
                <div class="stat-label">Upcoming</div>
            </div>

            <div class="card stat-card success">
                <div class="card-title">Completed</div>
                <div class="stat-number" id="completedAppointments">0</div>
                <div class="stat-label">Finished</div>
            </div>

            <div class="card stat-card">
                <div class="card-title">Doctors</div>
                <div class="stat-number" id="totalDoctors">8</div>
                <div class="stat-label">On staff</div>
            </div>
        </div>

        <div class="charts-grid">
            <div class="chart-card">
                <h3><i class="fas fa-venus-mars" style="color: var(--primary); margin-right: 8px;"></i>Patient Distribution</h3>
                <div id="patientChart"></div>
            </div>

            <div class="chart-card">
                <h3><i class="fas fa-calendar-alt" style="color: var(--primary); margin-right: 8px;"></i>Appointment Status</h3>
                <div id="appointmentChart"></div>
            </div>
        </div>

        <div class="charts-grid">
            <div class="chart-card">
                <h3><i class="fas fa-tint" style="color: var(--primary); margin-right: 8px;"></i>Blood Type Distribution</h3>
                <div id="bloodTypeChart"></div>
            </div>

            <div class="chart-card">
                <h3><i class="fas fa-map-marker-alt" style="color: var(--primary); margin-right: 8px;"></i>Patients by City</h3>
                <div id="cityChart"></div>
            </div>
        </div>

        <div class="container">
            <div class="form-section">
                <h3><i class="fas fa-bolt" style="color: var(--primary); margin-right: 8px;"></i>Quick Actions</h3>
                <div style="display: flex; gap: 1rem; flex-wrap: wrap;">
                    <a href="<%= request.getContextPath() %>/jsp/patients.jsp" class="btn btn-primary">
                        <i class="fas fa-user-plus"></i> Add Patient
                    </a>
                    <a href="<%= request.getContextPath() %>/jsp/rendezvous.jsp" class="btn btn-secondary">
                        <i class="fas fa-calendar-plus"></i> Schedule Appointment
                    </a>
                    <a href="<%= request.getContextPath() %>/jsp/medecins.jsp" class="btn btn-outline">
                        <i class="fas fa-user-md"></i> View Doctors
                    </a>
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
