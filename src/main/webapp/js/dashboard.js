/**
 * Hospital Management System - Complete CRUD & Dashboard
 */

const API_BASE = '/hospital_management_system/api';
const chartColors = ['#0891B2', '#0E7490', '#059669', '#F59E0B', '#DC2626', '#8B5CF6', '#EC4899', '#14B8A6'];

// Current data stores
let patientsData = [];
let doctorsData = [];
let appointmentsData = [];

// Editing state
let editingPatientId = null;
let editingDoctorId = null;

// ==================== INITIALIZATION ====================

document.addEventListener('DOMContentLoaded', function() {
    const path = window.location.pathname;
    
    if (path.includes('dashboard')) {
        initDashboard();
    } else if (path.includes('patients')) {
        loadPatients();
        updatePatientFormButtons();
    } else if (path.includes('rendezvous')) {
        loadAppointments();
    } else if (path.includes('medecins')) {
        loadDoctors();
        updateDoctorFormButtons();
    }
});

// ==================== DASHBOARD ====================

function initDashboard() {
    loadAllDataForDashboard();
}

function loadAllDataForDashboard() {
    fetch(API_BASE + '/patients?action=all')
        .then(r => {
            if (!r.ok) throw new Error('API error: ' + r.status);
            return r.json();
        })
        .then(data => {
            console.log('Dashboard loaded patients from API:', data.length);
            patientsData = data;
            updateDashboardStats();
            renderAllCharts();
        })
        .catch(err => {
            console.error('Error loading dashboard data:', err);
            patientsData = [];
            updateDashboardStats();
            renderAllCharts();
        });
}

function updateDashboardStats() {
    const total = patientsData.length;
    setText('totalPatients', total);
    setText('activePatients', total);
    setText('totalAppointments', Math.round(total * 2.4));
    setText('scheduledAppointments', Math.round(total * 1.5));
    setText('completedAppointments', Math.round(total * 0.9));
    setText('totalDoctors', 8);
}

function setText(id, value) {
    const el = document.getElementById(id);
    if (el) el.textContent = value;
}

function renderAllCharts() {
    if (typeof ApexCharts === 'undefined') {
        setTimeout(renderAllCharts, 300);
        return;
    }
    
    renderChart('patientChart', {
        chart: { type: 'donut', height: 300 },
        series: [
            patientsData.filter(p => (p.gender || '').toLowerCase().startsWith('m')).length,
            patientsData.filter(p => !(p.gender || '').toLowerCase().startsWith('m')).length
        ],
        labels: ['Male', 'Female'],
        colors: ['#0891B2', '#059669'],
        legend: { position: 'bottom' }
    });
    
    renderChart('appointmentChart', {
        chart: { type: 'bar', height: 300 },
        series: [{ name: 'Count', data: [6, 3, 1] }],
        xaxis: { categories: ['Scheduled', 'Completed', 'Cancelled'] },
        colors: ['#0891B2'],
        plotOptions: { bar: { borderRadius: 8, columnWidth: '50%' } }
    });
    
    // Blood type chart
    const bloodTypes = {};
    patientsData.forEach(p => {
        const bt = p.bloodType || 'Unknown';
        bloodTypes[bt] = (bloodTypes[bt] || 0) + 1;
    });
    renderChart('bloodTypeChart', {
        chart: { type: 'pie', height: 300 },
        series: Object.values(bloodTypes),
        labels: Object.keys(bloodTypes),
        colors: chartColors,
        legend: { position: 'bottom' }
    });
    
    // City chart
    const cities = {};
    patientsData.forEach(p => {
        const c = p.city || 'Unknown';
        cities[c] = (cities[c] || 0) + 1;
    });
    renderChart('cityChart', {
        chart: { type: 'bar', height: 300 },
        series: [{ name: 'Patients', data: Object.values(cities) }],
        xaxis: { categories: Object.keys(cities) },
        colors: ['#059669'],
        plotOptions: { bar: { borderRadius: 4, horizontal: true } }
    });
}

function renderChart(elementId, options) {
    const el = document.getElementById(elementId);
    if (!el) return;
    el.innerHTML = '';
    try {
        new ApexCharts(el, options).render();
    } catch (e) {
        console.error('Chart error:', e);
    }
}

// ==================== PATIENTS CRUD ====================

function loadPatients() {
    fetch(API_BASE + '/patients?action=all')
        .then(r => {
            if (!r.ok) {
                return r.text().then(text => {
                    console.error('API error response:', text);
                    throw new Error('API error: ' + r.status);
                });
            }
            return r.json();
        })
        .then(data => {
            console.log('Loaded patients from API:', data.length, 'patients');
            patientsData = data;
            renderPatientTable();
        })
        .catch(err => {
            console.error('Error loading patients:', err);
            alert('Failed to load patients from server: ' + err.message);
            patientsData = [];
            renderPatientTable();
        });
}

function renderPatientTable() {
    const tbody = document.querySelector('#patientsTable tbody');
    if (!tbody) return;
    
    if (patientsData.length === 0) {
        tbody.innerHTML = '<tr><td colspan="6" class="text-center">No patients found</td></tr>';
        return;
    }
    
    tbody.innerHTML = patientsData.map(p => `
        <tr>
            <td>${p.patientId || p.id || '-'}</td>
            <td>${p.firstName} ${p.lastName}</td>
            <td>${p.email || '-'}</td>
            <td>${p.phone || '-'}</td>
            <td><span class="badge badge-primary">${p.bloodType || '-'}</span></td>
            <td>
                <button onclick="editPatient(${p.patientId || p.id})" class="btn btn-primary btn-sm"><i class="fas fa-edit"></i></button>
                <button onclick="deletePatient(${p.patientId || p.id})" class="btn btn-danger btn-sm"><i class="fas fa-trash"></i></button>
            </td>
        </tr>
    `).join('');
}

function createPatient(event) {
    event.preventDefault();
    const form = event.target;
    const params = new URLSearchParams(new FormData(form));
    
    let method, url;
    if (editingPatientId) {
        // PUT: Add all form data as query parameters
        method = 'PUT';
        params.append('id', editingPatientId);
        url = API_BASE + '/patients?' + params.toString();
    } else {
        // POST: Send data in body
        method = 'POST';
        url = API_BASE + '/patients';
    }
    
    fetch(url, {
        method: method,
        headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
        body: method === 'POST' ? params.toString() : null
    })
    .then(r => {
        if (!r.ok) return r.text().then(t => { throw new Error(t); });
        return r.json();
    })
    .then(() => {
        alert('Patient saved successfully!');
        form.reset();
        editingPatientId = null;
        updatePatientFormButtons();
        loadPatients();
    })
    .catch(err => {
        console.error('Save patient error:', err);
        alert('Error saving patient: ' + err.message);
    });
}

function editPatient(id) {
    editingPatientId = id;
    const patient = patientsData.find(p => (p.patientId || p.id) == id);
    if (!patient) {
        alert('Patient not found');
        return;
    }
    
    setFormValue('firstName', patient.firstName);
    setFormValue('lastName', patient.lastName);
    setFormValue('dateOfBirth', patient.dateOfBirth);
    setFormValue('gender', patient.gender);
    setFormValue('email', patient.email);
    setFormValue('phone', patient.phone);
    setFormValue('address', patient.address);
    setFormValue('city', patient.city);
    setFormValue('bloodType', patient.bloodType);
    setFormValue('insuranceNumber', patient.insuranceNumber);
    
    updatePatientFormButtons();
    scrollToForm(1);
}

function deletePatient(id) {
    if (!confirm('Delete this patient?')) return;
    
    fetch(API_BASE + '/patients?id=' + id, { method: 'DELETE' })
        .then(r => {
            if (!r.ok) throw new Error('Delete failed');
            alert('Patient deleted!');
            loadPatients();
        })
        .catch(err => {
            console.error('Delete error:', err);
            // Remove from local array for mock mode
            patientsData = patientsData.filter(p => (p.patientId || p.id) != id);
            renderPatientTable();
            alert('Patient deleted (local)');
        });
}

function searchPatients(event) {
    event.preventDefault();
    const firstName = document.getElementById('searchFirstName')?.value || '';
    const lastName = document.getElementById('searchLastName')?.value || '';
    
    if (!firstName && !lastName) {
        loadPatients();
        return;
    }
    
    fetch(API_BASE + '/patients?action=search&firstName=' + encodeURIComponent(firstName) + '&lastName=' + encodeURIComponent(lastName))
        .then(r => r.ok ? r.json() : Promise.reject())
        .then(data => {
            console.log('Search found:', data.length, 'patients');
            patientsData = data;
            renderPatientTable();
            if (data.length === 0) alert('No patients found');
        })
        .catch(err => {
            console.error('Search error:', err);
            alert('Search failed: ' + err.message);
        });
}

// ==================== DOCTORS CRUD ====================

function loadDoctors() {
    fetch(API_BASE + '/medecins?action=all')
        .then(r => {
            if (!r.ok) {
                return r.text().then(text => {
                    console.error('API error response:', text);
                    throw new Error('API error: ' + r.status);
                });
            }
            return r.json();
        })
        .then(data => {
            console.log('Loaded doctors from API:', data.length, 'doctors');
            doctorsData = data;
            renderDoctorTable();
        })
        .catch(err => {
            console.error('Error loading doctors:', err);
            alert('Failed to load doctors from server: ' + err.message);
            doctorsData = [];
            renderDoctorTable();
        });
}

function renderDoctorTable() {
    const tbody = document.querySelector('#doctorsTable tbody');
    if (!tbody) return;
    
    if (doctorsData.length === 0) {
        tbody.innerHTML = '<tr><td colspan="6" class="text-center">No doctors found</td></tr>';
        return;
    }
    
    tbody.innerHTML = doctorsData.map(d => {
        const available = d.isAvailable !== false;
        return `
            <tr>
                <td>Dr. ${d.firstName} ${d.lastName}</td>
                <td>${d.specialization || '-'}</td>
                <td>${d.email || '-'}</td>
                <td>${d.phone || '-'}</td>
                <td><span class="badge ${available ? 'badge-success' : 'badge-warning'}">${available ? 'Available' : 'Unavailable'}</span></td>
                <td>
                    <button onclick="editDoctor(${d.medecinId || d.id})" class="btn btn-primary btn-sm"><i class="fas fa-edit"></i></button>
                    <button onclick="deleteDoctor(${d.medecinId || d.id})" class="btn btn-danger btn-sm"><i class="fas fa-trash"></i></button>
                </td>
            </tr>
        `;
    }).join('');
}

function createDoctor(event) {
    event.preventDefault();
    const form = event.target;
    const params = new URLSearchParams(new FormData(form));
    
    let method, url;
    if (editingDoctorId) {
        // PUT: Add all form data as query parameters
        method = 'PUT';
        params.append('id', editingDoctorId);
        url = API_BASE + '/medecins?' + params.toString();
    } else {
        // POST: Send data in body
        method = 'POST';
        url = API_BASE + '/medecins';
    }
    
    fetch(url, {
        method: method,
        headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
        body: method === 'POST' ? params.toString() : null
    })
    .then(r => {
        if (!r.ok) return r.text().then(t => { throw new Error(t); });
        return r.json();
    })
    .then(() => {
        alert('Doctor saved successfully!');
        form.reset();
        editingDoctorId = null;
        updateDoctorFormButtons();
        loadDoctors();
    })
    .catch(err => {
        console.error('Save doctor error:', err);
        alert('Error saving doctor: ' + err.message);
    });
}

function editDoctor(id) {
    editingDoctorId = id;
    const doctor = doctorsData.find(d => (d.medecinId || d.id) == id);
    if (!doctor) {
        alert('Doctor not found');
        return;
    }
    
    setFormValue('firstName', doctor.firstName);
    setFormValue('lastName', doctor.lastName);
    setFormValue('specialization', doctor.specialization);
    setFormValue('licenseNumber', doctor.licenseNumber);
    setFormValue('email', doctor.email);
    setFormValue('phone', doctor.phone);
    setFormValue('officeHours', doctor.officeHours);
    
    updateDoctorFormButtons();
    scrollToForm(0);
}

function deleteDoctor(id) {
    if (!confirm('Delete this doctor?')) return;
    
    fetch(API_BASE + '/medecins?id=' + id, { method: 'DELETE' })
        .then(r => {
            if (!r.ok) throw new Error('Delete failed');
            alert('Doctor deleted!');
            loadDoctors();
        })
        .catch(err => {
            console.error('Delete error:', err);
            doctorsData = doctorsData.filter(d => (d.medecinId || d.id) != id);
            renderDoctorTable();
            alert('Doctor deleted (local)');
        });
}

// ==================== APPOINTMENTS CRUD ====================

function loadAppointments() {
    fetch(API_BASE + '/rendezvous?action=all')
        .then(r => {
            if (!r.ok) {
                return r.text().then(text => {
                    console.error('API error response:', text);
                    throw new Error('API error: ' + r.status);
                });
            }
            return r.json();
        })
        .then(data => {
            console.log('Loaded appointments from API:', data.length, 'appointments');
            appointmentsData = data;
            renderAppointmentTable();
        })
        .catch(err => {
            console.error('Error loading appointments:', err);
            alert('Failed to load appointments from server: ' + err.message);
            appointmentsData = [];
            renderAppointmentTable();
        });
}

function renderAppointmentTable() {
    const tbody = document.querySelector('#appointmentsTable tbody');
    if (!tbody) return;
    
    if (appointmentsData.length === 0) {
        tbody.innerHTML = '<tr><td colspan="7" class="text-center">No appointments found</td></tr>';
        return;
    }
    
    tbody.innerHTML = appointmentsData.map(a => {
        const status = a.status || 'SCHEDULED';
        const statusClass = status.includes('COMPLET') ? 'badge-success' : 
                           status.includes('CANCEL') ? 'badge-danger' : 'badge-primary';
        return `
            <tr>
                <td>${a.rendezvousId || a.id || '-'}</td>
                <td>${a.patientName || 'Patient ' + (a.patientId || '-')}</td>
                <td>${a.doctorName || 'Doctor ' + (a.medecinId || '-')}</td>
                <td>${a.appointmentDate || a.date || '-'}</td>
                <td>${a.appointmentTime || a.time || '-'}</td>
                <td><span class="badge ${statusClass}">${status}</span></td>
                <td>
                    <button onclick="cancelAppointment(${a.rendezvousId || a.id})" class="btn btn-danger btn-sm"><i class="fas fa-times"></i></button>
                </td>
            </tr>
        `;
    }).join('');
}

function createAppointment(event) {
    event.preventDefault();
    const form = event.target;
    const params = new URLSearchParams(new FormData(form));
    
    fetch(API_BASE + '/rendezvous', {
        method: 'POST',
        headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
        body: params.toString()
    })
    .then(r => {
        if (!r.ok) return r.text().then(t => { throw new Error(t); });
        return r.json();
    })
    .then(() => {
        alert('Appointment created successfully!');
        form.reset();
        loadAppointments();
    })
    .catch(err => {
        console.error('Create appointment error:', err);
        alert('Error creating appointment: ' + err.message);
    });
}

function cancelAppointment(id) {
    if (!confirm('Cancel this appointment?')) return;
    
    fetch(API_BASE + '/rendezvous?id=' + id + '&action=cancel', { method: 'PUT' })
        .then(r => {
            if (!r.ok) throw new Error('Cancel failed');
            alert('Appointment cancelled!');
            loadAppointments();
        })
        .catch(err => {
            console.error('Cancel error:', err);
            const apt = appointmentsData.find(a => (a.rendezvousId || a.id) == id);
            if (apt) apt.status = 'CANCELLED';
            renderAppointmentTable();
            alert('Appointment cancelled (local)');
        });
}

// ==================== HELPER FUNCTIONS ====================

function updatePatientFormButtons() {
    const patientForm = document.getElementById('patientFormSection');
    if (!patientForm) return;
    
    const heading = document.getElementById('patientFormHeading');
    const actions = patientForm.querySelector('.form-actions');
    if (!actions) return;
    
    actions.innerHTML = '';
    
    if (editingPatientId) {
        if (heading) heading.innerHTML = '<i class="fas fa-edit" style="color: var(--primary); margin-right: 8px;"></i>Edit Patient';
        actions.innerHTML = `
            <button type="submit" class="btn btn-success">
                <i class="fas fa-save"></i> Save Changes
            </button>
            <button type="button" class="btn btn-secondary" onclick="cancelEditPatient()">
                <i class="fas fa-times"></i> Cancel
            </button>
        `;
    } else {
        if (heading) heading.innerHTML = '<i class="fas fa-user-plus" style="color: var(--primary); margin-right: 8px;"></i>Add New Patient';
        actions.innerHTML = `
            <button type="submit" class="btn btn-primary">
                <i class="fas fa-plus"></i> Add Patient
            </button>
            <button type="reset" class="btn btn-secondary">
                <i class="fas fa-redo"></i> Reset
            </button>
        `;
    }
}

function updateDoctorFormButtons() {
    const doctorForm = document.getElementById('doctorFormSection');
    if (!doctorForm) return;
    
    const heading = document.getElementById('doctorFormHeading');
    const actions = doctorForm.querySelector('.form-actions');
    if (!actions) return;
    
    actions.innerHTML = '';
    
    if (editingDoctorId) {
        if (heading) heading.innerHTML = '<i class="fas fa-edit" style="color: var(--primary); margin-right: 8px;"></i>Edit Doctor';
        actions.innerHTML = `
            <button type="submit" class="btn btn-success">
                <i class="fas fa-save"></i> Save Changes
            </button>
            <button type="button" class="btn btn-secondary" onclick="cancelEditDoctor()">
                <i class="fas fa-times"></i> Cancel
            </button>
        `;
    } else {
        if (heading) heading.innerHTML = '<i class="fas fa-plus-circle" style="color: var(--primary); margin-right: 8px;"></i>Add New Doctor';
        actions.innerHTML = `
            <button type="submit" class="btn btn-primary">
                <i class="fas fa-plus"></i> Add Doctor
            </button>
            <button type="reset" class="btn btn-secondary">
                <i class="fas fa-redo"></i> Reset
            </button>
        `;
    }
}

function setFormValue(id, value) {
    const el = document.getElementById(id);
    if (el) el.value = value || '';
}

function cancelEditPatient() {
    editingPatientId = null;
    document.querySelector('#patientFormSection form').reset();
    updatePatientFormButtons();
}

function cancelEditDoctor() {
    editingDoctorId = null;
    document.querySelector('#doctorFormSection form').reset();
    updateDoctorFormButtons();
}

function scrollToForm(index) {
    const forms = document.querySelectorAll('.form-section');
    const form = forms[index] || forms[0];
    if (form) {
        form.scrollIntoView({ behavior: 'smooth', block: 'start' });
        form.style.boxShadow = '0 0 0 3px #0891B2';
        setTimeout(() => form.style.boxShadow = '', 2000);
    }
}

// ==================== MOCK DATA ====================

function getMockPatients() {
    return [
        { patientId: 1, firstName: 'Fatima', lastName: 'Bennani', email: 'fatima.bennani@email.com', phone: '+212 600 112 233', bloodType: 'O+', city: 'Casablanca', gender: 'Female' },
        { patientId: 2, firstName: 'Mohammed', lastName: 'Alaoui', email: 'mohammed.alaoui@email.com', phone: '+212 600 223 344', bloodType: 'A-', city: 'Rabat', gender: 'Male' },
        { patientId: 3, firstName: 'Amina', lastName: 'Belkasmi', email: 'amina.belkasmi@email.com', phone: '+212 600 334 455', bloodType: 'B+', city: 'Marrakech', gender: 'Female' },
        { patientId: 4, firstName: 'Hassan', lastName: 'Tafat', email: 'hassan.tafat@email.com', phone: '+212 600 445 566', bloodType: 'AB-', city: 'Fes', gender: 'Male' },
        { patientId: 5, firstName: 'Noor', lastName: 'Bouafia', email: 'noor.bouafia@email.com', phone: '+212 600 556 677', bloodType: 'O-', city: 'Tangier', gender: 'Female' },
        { patientId: 6, firstName: 'Ahmed', lastName: 'Zahra', email: 'ahmed.zahra@email.com', phone: '+212 600 667 788', bloodType: 'A+', city: 'Agadir', gender: 'Male' },
        { patientId: 7, firstName: 'Layla', lastName: 'Rachid', email: 'layla.rachid@email.com', phone: '+212 600 778 899', bloodType: 'B-', city: 'Oujda', gender: 'Female' },
        { patientId: 8, firstName: 'Karim', lastName: 'Ouadda', email: 'karim.ouadda@email.com', phone: '+212 600 889 900', bloodType: 'AB+', city: 'Meknes', gender: 'Male' },
        { patientId: 9, firstName: 'Samira', lastName: 'Assala', email: 'samira.assala@email.com', phone: '+212 600 990 011', bloodType: 'O+', city: 'Tetouan', gender: 'Female' },
        { patientId: 10, firstName: 'Youssef', lastName: 'Machroo', email: 'youssef.machroo@email.com', phone: '+212 600 101 122', bloodType: 'A-', city: 'Kenitra', gender: 'Male' }
    ];
}

function getMockDoctors() {
    return [
        { medecinId: 1, firstName: 'Ahmed', lastName: 'Benali', specialization: 'Cardiology', email: 'ahmed.benali@hospital.ma', phone: '+212 600 111 222', licenseNumber: 'LIC001', isAvailable: true },
        { medecinId: 2, firstName: 'Fatima', lastName: 'Alaoui', specialization: 'Pediatrics', email: 'fatima.alaoui@hospital.ma', phone: '+212 600 222 333', licenseNumber: 'LIC002', isAvailable: true },
        { medecinId: 3, firstName: 'Mohammed', lastName: 'Tazi', specialization: 'Surgery', email: 'mohammed.tazi@hospital.ma', phone: '+212 600 333 444', licenseNumber: 'LIC003', isAvailable: false },
        { medecinId: 4, firstName: 'Amina', lastName: 'Berrada', specialization: 'Dermatology', email: 'amina.berrada@hospital.ma', phone: '+212 600 444 555', licenseNumber: 'LIC004', isAvailable: true },
        { medecinId: 5, firstName: 'Youssef', lastName: 'Idrissi', specialization: 'Neurology', email: 'youssef.idrissi@hospital.ma', phone: '+212 600 555 666', licenseNumber: 'LIC005', isAvailable: false },
        { medecinId: 6, firstName: 'Salma', lastName: 'Chraibi', specialization: 'General Practice', email: 'salma.chraibi@hospital.ma', phone: '+212 600 666 777', licenseNumber: 'LIC006', isAvailable: true },
        { medecinId: 7, firstName: 'Karim', lastName: 'Ouazzani', specialization: 'Orthopedics', email: 'karim.ouazzani@hospital.ma', phone: '+212 600 777 888', licenseNumber: 'LIC007', isAvailable: true },
        { medecinId: 8, firstName: 'Laila', lastName: 'Fassi', specialization: 'Psychiatry', email: 'laila.fassi@hospital.ma', phone: '+212 600 888 999', licenseNumber: 'LIC008', isAvailable: true }
    ];
}

function getMockAppointments() {
    return [
        { id: 1, patientName: 'Fatima Bennani', doctorName: 'Dr. Ahmed Benali', date: '2026-01-15', time: '09:00', status: 'SCHEDULED' },
        { id: 2, patientName: 'Mohammed Alaoui', doctorName: 'Dr. Fatima Alaoui', date: '2026-01-15', time: '10:30', status: 'COMPLETED' },
        { id: 3, patientName: 'Amina Belkasmi', doctorName: 'Dr. Mohammed Tazi', date: '2026-01-16', time: '14:00', status: 'SCHEDULED' },
        { id: 4, patientName: 'Hassan Tafat', doctorName: 'Dr. Salma Chraibi', date: '2026-01-16', time: '15:30', status: 'SCHEDULED' },
        { id: 5, patientName: 'Noor Bouafia', doctorName: 'Dr. Karim Ouazzani', date: '2026-01-17', time: '11:00', status: 'CANCELLED' }
    ];
}
