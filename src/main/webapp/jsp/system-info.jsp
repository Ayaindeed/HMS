<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>System Info - Hospital Management System</title>
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
            <a href="<%= request.getContextPath() %>/jsp/rendezvous.jsp">Appointments</a>
            <a href="<%= request.getContextPath() %>/jsp/system-info.jsp" class="active">System</a>
        </div>
    </nav>

    <main>
        <div class="container">
            <div class="page-header">
                <h2><i class="fas fa-server" style="color: var(--primary); margin-right: 10px;"></i>System Information</h2>
                <p>Technical architecture and configuration details</p>
            </div>

            <div class="grid grid-2">
                <div class="card">
                    <div class="card-header">
                        <i class="fas fa-code"></i>
                        <h3>Application Stack</h3>
                    </div>
                    <div class="card-body">
                        <p><strong>Runtime:</strong> Java 19 (Eclipse Temurin)</p>
                        <p><strong>Server:</strong> Apache Tomcat 10.1</p>
                        <p><strong>Framework:</strong> Jakarta EE 10</p>
                        <p><strong>ORM:</strong> Hibernate 5.6.14</p>
                        <p><strong>Build:</strong> Maven 3.9</p>
                    </div>
                </div>

                <div class="card">
                    <div class="card-header">
                        <i class="fas fa-database"></i>
                        <h3>Database Systems</h3>
                    </div>
                    <div class="card-body">
                        <p><strong>Primary DB:</strong> PostgreSQL 15</p>
                        <p><strong>Document Store:</strong> MongoDB 6.0</p>
                        <p><strong>Cache:</strong> Redis 7</p>
                        <p><strong>Port:</strong> 5432 (PostgreSQL)</p>
                    </div>
                </div>

                <div class="card">
                    <div class="card-header">
                        <i class="fab fa-docker"></i>
                        <h3>Container Services</h3>
                    </div>
                    <div class="card-body">
                        <p><strong>Tomcat:</strong> Port 8080</p>
                        <p><strong>PostgreSQL:</strong> Port 5432</p>
                        <p><strong>MongoDB:</strong> Port 27017</p>
                        <p><strong>Redis:</strong> Port 6379</p>
                        <p><strong>PgAdmin:</strong> Port 5050</p>
                        <p><strong>Mongo Express:</strong> Port 8081</p>
                    </div>
                </div>

                <div class="card">
                    <div class="card-header">
                        <i class="fas fa-cogs"></i>
                        <h3>API Endpoints</h3>
                    </div>
                    <div class="card-body">
                        <p><strong>Patients:</strong> /api/patients</p>
                        <p><strong>Appointments:</strong> /api/rendezvous</p>
                        <p><strong>Dashboard:</strong> /api/dashboard</p>
                        <p><strong>Format:</strong> JSON (REST)</p>
                    </div>
                </div>
            </div>

            <div class="form-section" style="margin-top: 2rem;">
                <h3><i class="fas fa-layer-group" style="color: var(--primary); margin-right: 8px;"></i>Architecture Overview</h3>
                <div class="grid grid-3" style="margin-top: 1rem;">
                    <div style="text-align: center; padding: 1.5rem; background: var(--background); border-radius: var(--radius-sm);">
                        <i class="fas fa-desktop" style="font-size: 2rem; color: var(--primary); margin-bottom: 0.5rem;"></i>
                        <h4 style="margin: 0.5rem 0;">Presentation Layer</h4>
                        <p style="font-size: 0.875rem; color: var(--text-secondary);">JSP, HTML5, CSS3, JavaScript</p>
                    </div>
                    <div style="text-align: center; padding: 1.5rem; background: var(--background); border-radius: var(--radius-sm);">
                        <i class="fas fa-cog" style="font-size: 2rem; color: var(--secondary); margin-bottom: 0.5rem;"></i>
                        <h4 style="margin: 0.5rem 0;">Business Layer</h4>
                        <p style="font-size: 0.875rem; color: var(--text-secondary);">Servlets, Services, DAOs</p>
                    </div>
                    <div style="text-align: center; padding: 1.5rem; background: var(--background); border-radius: var(--radius-sm);">
                        <i class="fas fa-database" style="font-size: 2rem; color: var(--accent); margin-bottom: 0.5rem;"></i>
                        <h4 style="margin: 0.5rem 0;">Data Layer</h4>
                        <p style="font-size: 0.875rem; color: var(--text-secondary);">PostgreSQL, MongoDB, Redis</p>
                    </div>
                </div>
            </div>

            <div class="dashboard-grid" style="padding: 0; margin-top: 2rem;">
                <div class="card stat-card success">
                    <div class="card-title">Status</div>
                    <div class="stat-number" style="font-size: 1.5rem;">Online</div>
                    <div class="stat-label">All services running</div>
                </div>
                <div class="card stat-card">
                    <div class="card-title">Uptime</div>
                    <div class="stat-number" style="font-size: 1.5rem;">99.9%</div>
                    <div class="stat-label">Last 30 days</div>
                </div>
                <div class="card stat-card warning">
                    <div class="card-title">Response</div>
                    <div class="stat-number" style="font-size: 1.5rem;">45ms</div>
                    <div class="stat-label">Avg response time</div>
                </div>
                <div class="card stat-card">
                    <div class="card-title">Memory</div>
                    <div class="stat-number" style="font-size: 1.5rem;">512MB</div>
                    <div class="stat-label">JVM heap size</div>
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
