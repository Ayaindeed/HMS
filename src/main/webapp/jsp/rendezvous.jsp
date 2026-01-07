<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Appointments - Hospital Management System</title>
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
            <a href="<%= request.getContextPath() %>/jsp/medecins.jsp">Doctors</a>
            <a href="<%= request.getContextPath() %>/jsp/rendezvous.jsp" class="active">Appointments</a>
            <a href="<%= request.getContextPath() %>/jsp/system-info.jsp">System</a>
        </div>
    </nav>

    <main>
        <div class="container">
            <div class="page-header">
                <h2><i class="fas fa-calendar-alt" style="color: var(--primary); margin-right: 10px;"></i>Appointment Management</h2>
                <p>Schedule and manage patient appointments</p>
            </div>

            <div class="form-section">
                <h3><i class="fas fa-calendar-plus" style="color: var(--primary); margin-right: 8px;"></i>Schedule New Appointment</h3>
                <form onsubmit="createAppointment(event)" class="form-grid">
                    <div class="form-group">
                        <label for="patientId">Patient ID *</label>
                        <input type="number" id="patientId" name="patientId" required placeholder="Enter patient ID">
                    </div>
                    <div class="form-group">
                        <label for="medecinId">Doctor ID *</label>
                        <input type="number" id="medecinId" name="medecinId" required placeholder="Enter doctor ID">
                    </div>
                    <div class="form-group">
                        <label for="appointmentDate">Date *</label>
                        <input type="date" id="appointmentDate" name="appointmentDate" required>
                    </div>
                    <div class="form-group">
                        <label for="appointmentTime">Time *</label>
                        <input type="time" id="appointmentTime" name="appointmentTime" required>
                    </div>
                    <div class="form-group" style="grid-column: 1 / -1;">
                        <label for="reason">Reason for Visit</label>
                        <textarea id="reason" name="reason" placeholder="Enter reason for the appointment"></textarea>
                    </div>
                    <div class="form-actions" style="grid-column: 1 / -1; border-top: none; padding-top: 0; margin-top: 0.5rem;">
                        <button type="submit" class="btn btn-primary">
                            <i class="fas fa-calendar-check"></i> Schedule
                        </button>
                        <button type="reset" class="btn btn-secondary">
                            <i class="fas fa-redo"></i> Reset
                        </button>
                    </div>
                </form>
            </div>

            <div class="table-section">
                <div class="table-header">
                    <h3><i class="fas fa-list" style="color: var(--primary); margin-right: 8px;"></i>Scheduled Appointments</h3>
                </div>
                <div class="table-wrapper">
                    <table id="appointmentsTable">
                        <thead>
                            <tr>
                                <th>ID</th>
                                <th>Patient</th>
                                <th>Doctor</th>
                                <th>Date</th>
                                <th>Time</th>
                                <th>Status</th>
                                <th>Actions</th>
                            </tr>
                        </thead>
                        <tbody>
                            <tr>
                                <td colspan="7" class="text-center">Loading appointments...</td>
                            </tr>
                        </tbody>
                    </table>
                </div>
            </div>

            <div class="dashboard-grid" style="padding: 0; margin-top: 2rem;">
                <div class="card stat-card">
                    <div class="card-title">Today</div>
                    <div class="stat-number">5</div>
                    <div class="stat-label">Appointments today</div>
                </div>
                <div class="card stat-card success">
                    <div class="card-title">This Week</div>
                    <div class="stat-number">18</div>
                    <div class="stat-label">Upcoming</div>
                </div>
                <div class="card stat-card warning">
                    <div class="card-title">Pending</div>
                    <div class="stat-number">3</div>
                    <div class="stat-label">Awaiting confirmation</div>
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
