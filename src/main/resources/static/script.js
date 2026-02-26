// ============================================
// FOREX MANAGEMENT SYSTEM - MAIN SCRIPT
// ============================================

let cardsConfig = {};
let currentUser = {
    role: localStorage.getItem('userRole') || null,
    name: localStorage.getItem('userName') || 'User'
};

// Initialize application
document.addEventListener('DOMContentLoaded', function() {
    // Load config
    loadConfig();

    // Setup event listeners
    setupEventListeners();

    // Check authentication
    if (window.location.pathname.includes('dashboard') || 
        window.location.pathname.includes('exchange_rates') ||
        window.location.pathname.includes('transactions') ||
        window.location.pathname.includes('invoices') ||
        window.location.pathname.includes('orders') ||
        window.location.pathname.includes('profile') ||
        window.location.pathname.includes('customer_') ||
        window.location.pathname.includes('bank_') ||
        window.location.pathname.includes('user_') ||
        window.location.pathname.includes('currency_') ||
        window.location.pathname.includes('product_') ||
        window.location.pathname.includes('monitoring') ||
        window.location.pathname.includes('reports') ||
        window.location.pathname.includes('swift_')) {
        checkAuthentication();
    }

    // Initialize dashboard if on dashboard page
    if (document.querySelector('.dashboard-title')) {
        initializeDashboard();
    }
});

// ============================================
// CONFIGURATION LOADER
// ============================================

function loadConfig() {
    fetch('config.json')
        .then(response => response.json())
        .then(data => {
            cardsConfig = data;
        })
        .catch(error => console.error('Error loading config:', error));
}

// ============================================
// AUTHENTICATION & ROUTING
// ============================================

function checkAuthentication() {
    if (!currentUser.role) {
        window.location.href = 'index.html';
        return false;
    }
    return true;
}

function handleLogin(event) {
    event.preventDefault();

    const username = document.getElementById('username').value;
    const password = document.getElementById('password').value;
    const role = document.getElementById('role').value;

    if (!username || !password || !role) {
        alert('Please fill in all fields');
        return;
    }

    // Store user info in localStorage
    localStorage.setItem('userName', username);
    localStorage.setItem('userRole', role);
    localStorage.setItem('loginTime', new Date().toISOString());

    // Redirect to appropriate dashboard
    redirectToDashboard(role);
}

function redirectToDashboard(role) {
    switch(role) {
        case 'admin':
            window.location.href = 'admin_dashboard.html';
            break;
        case 'customer':
            window.location.href = 'customer_dashboard.html';
            break;
        case 'bank':
            window.location.href = 'bank_dashboard.html';
            break;
        default:
            window.location.href = 'index.html';
    }
}

// ============================================
// LOGOUT FUNCTION
// ============================================

function logout() {
    localStorage.removeItem('userName');
    localStorage.removeItem('userRole');
    localStorage.removeItem('loginTime');
    currentUser.role = null;
    window.location.href = 'index.html';
}

// ============================================
// SIDEBAR MANAGEMENT
// ============================================

function toggleSidebar() {
    const mainContainer = document.querySelector('.main-container');
    const sidebar = document.querySelector('.sidebar');

    if (window.innerWidth <= 768) {
        mainContainer.classList.toggle('sidebar-open');
        sidebar.classList.toggle('active');
    } else {
        mainContainer.classList.toggle('sidebar-collapsed');
        sidebar.classList.toggle('collapsed');
        document.querySelector('.topbar').classList.toggle('sidebar-collapsed');
    }
}

// ============================================
// PROFILE DROPDOWN
// ============================================

function toggleProfileDropdown() {
    const dropdown = document.querySelector('.dropdown-menu');
    if (dropdown) {
        dropdown.classList.toggle('active');
    }
}

// Close dropdown when clicking outside
document.addEventListener('click', function(event) {
    const profileDropdown = document.querySelector('.profile-dropdown');
    const dropdown = document.querySelector('.dropdown-menu');

    if (profileDropdown && !profileDropdown.contains(event.target) && dropdown) {
        dropdown.classList.remove('active');
    }
});

// ============================================
// DASHBOARD CARD GENERATION
// ============================================

function initializeDashboard() {
    const cardsContainer = document.querySelector('.cards-grid');
    if (!cardsContainer) return;

    const role = currentUser.role;
    const roleCards = cardsConfig.cards[role] || [];

    // Update profile display
    updateProfileDisplay();

    // Generate cards
    generateCards(roleCards, cardsContainer);

    // Setup search functionality
    setupSearchFunctionality(roleCards);
}

function generateCards(cards, container) {
    container.innerHTML = '';

    cards.forEach(card => {
        const cardElement = createCardElement(card);
        container.appendChild(cardElement);
    });
}

function createCardElement(card) {
    const cardDiv = document.createElement('div');
    cardDiv.className = 'dashboard-card';
    cardDiv.dataset.cardId = card.id;

    cardDiv.innerHTML = `
        <div class="card-icon">${card.icon}</div>
        <h3 class="card-title">${card.title}</h3>
        <p class="card-description">${card.description}</p>
        <div class="card-action">
            <button class="btn" onclick="navigateToPage('${card.page}')">Open</button>
        </div>
    `;

    return cardDiv;
}

function navigateToPage(page) {
    window.location.href = page;
}

// ============================================
// SEARCH FUNCTIONALITY
// ============================================

function setupSearchFunctionality(cards) {
    const searchInput = document.querySelector('.search-bar input');
    if (!searchInput) return;

    searchInput.addEventListener('input', function(e) {
        const searchTerm = e.target.value.toLowerCase();
        filterCards(searchTerm, cards);
    });
}

function filterCards(searchTerm, allCards) {
    const cardElements = document.querySelectorAll('.dashboard-card');

    cardElements.forEach(element => {
        const cardId = element.dataset.cardId;
        const card = allCards.find(c => c.id === cardId);

        if (!card) return;

        const matchesSearch =
            card.title.toLowerCase().includes(searchTerm) ||
            card.description.toLowerCase().includes(searchTerm) ||
            card.id.toLowerCase().includes(searchTerm);

        element.style.display = matchesSearch ? '' : 'none';
    });

    // Show empty state if no results
    const visibleCards = Array.from(cardElements).filter(el => el.style.display !== 'none');
    const cardsGrid = document.querySelector('.cards-grid');

    if (visibleCards.length === 0 && searchTerm) {
        if (!document.querySelector('.empty-state-search')) {
            const emptyState = document.createElement('div');
            emptyState.className = 'empty-state empty-state-search';
            emptyState.style.gridColumn = '1 / -1';
            emptyState.innerHTML = `
                <div class="icon">🔍</div>
                <h3>No cards found</h3>
                <p>Try a different search term</p>
            `;
            cardsGrid.appendChild(emptyState);
        }
    } else {
        const emptyState = document.querySelector('.empty-state-search');
        if (emptyState) {
            emptyState.remove();
        }
    }
}

// ============================================
// PROFILE DISPLAY UPDATE
// ============================================

function updateProfileDisplay() {
    const profileBtn = document.querySelector('.profile-btn');
    const profileAvatar = document.querySelector('.profile-avatar');

    if (profileBtn && currentUser.name) {
        const initials = currentUser.name
            .split(' ')
            .map(n => n[0])
            .join('')
            .toUpperCase()
            .substring(0, 2);

        if (profileAvatar) {
            profileAvatar.textContent = initials;
        }

        const nameSpan = profileBtn.querySelector('span:not(.profile-avatar)');
        if (nameSpan) {
            nameSpan.textContent = currentUser.name;
        }
    }
}

// ============================================
// EVENT LISTENERS SETUP
// ============================================

function setupEventListeners() {
    // Sidebar toggle
    const sidebarToggle = document.querySelector('.sidebar-toggle');
    if (sidebarToggle) {
        sidebarToggle.addEventListener('click', toggleSidebar);
    }

    // Profile dropdown
    const profileBtn = document.querySelector('.profile-btn');
    if (profileBtn) {
        profileBtn.addEventListener('click', toggleProfileDropdown);
    }

    // Login form
    const loginForm = document.querySelector('.login-box');
    if (loginForm) {
        loginForm.addEventListener('submit', handleLogin);
    }

    // Dropdown menu items
    const dropdownItems = document.querySelectorAll('.dropdown-item');
    dropdownItems.forEach(item => {
        if (item.textContent.includes('Logout')) {
            item.addEventListener('click', logout);
        }
    });

    // Mobile menu close on nav item click
    const navItems = document.querySelectorAll('.nav-item');
    navItems.forEach(item => {
        item.addEventListener('click', function() {
            if (window.innerWidth <= 768) {
                toggleSidebar();
            }
        });
    });
}

// ============================================
// RESPONSIVE SIDEBAR HANDLER
// ============================================

window.addEventListener('resize', function() {
    const mainContainer = document.querySelector('.main-container');
    if (window.innerWidth > 768) {
        mainContainer.classList.remove('sidebar-open');
        const sidebar = document.querySelector('.sidebar');
        if (sidebar) sidebar.classList.remove('active');
    }
});

// ============================================
// NAVIGATION ACTIVE STATE
// ============================================

function setActiveNavigation() {
    const currentPage = window.location.pathname.split('/').pop() || 'index.html';
    const navItems = document.querySelectorAll('.nav-item');

    navItems.forEach(item => {
        const link = item.getAttribute('href') || item.dataset.link || '';
        if (link.includes(currentPage) || (currentPage === '' && link.includes('dashboard'))) {
            item.classList.add('active');
        } else {
            item.classList.remove('active');
        }
    });
}

// Call on page load
if (document.readyState === 'loading') {
    document.addEventListener('DOMContentLoaded', setActiveNavigation);
} else {
    setActiveNavigation();
}

// ============================================
// DATE/TIME UTILITIES
// ============================================

function formatDate(date) {
    return new Date(date).toLocaleDateString('en-US', {
        year: 'numeric',
        month: 'short',
        day: 'numeric'
    });
}

function formatTime(date) {
    return new Date(date).toLocaleTimeString('en-US', {
        hour: '2-digit',
        minute: '2-digit'
    });
}

function formatDateTime(date) {
    return formatDate(date) + ' ' + formatTime(date);
}

// ============================================
// STORAGE UTILITIES
// ============================================

const StorageManager = {
    set: function(key, value) {
        localStorage.setItem(key, JSON.stringify(value));
    },

    get: function(key) {
        const item = localStorage.getItem(key);
        return item ? JSON.parse(item) : null;
    },

    remove: function(key) {
        localStorage.removeItem(key);
    },

    clear: function() {
        localStorage.clear();
    }
};

// ============================================
// API SIMULATION FOR DEMO
// ============================================

const MockAPI = {
    // Exchange rates
    getExchangeRates: function() {
        return {
            USD: 1,
            EUR: 0.92,
            GBP: 0.79,
            JPY: 149.50,
            INR: 83.12,
            CAD: 1.36,
            AUD: 1.53
        };
    },

    // Transaction data
    getTransactions: function(limit = 10) {
        const transactions = [];
        for (let i = 0; i < limit; i++) {
            transactions.push({
                id: `TXN${String(i + 1).padStart(5, '0')}`,
                date: new Date(Date.now() - Math.random() * 30 * 24 * 60 * 60 * 1000),
                type: ['Buy', 'Sell'][Math.floor(Math.random() * 2)],
                fromCurrency: ['USD', 'EUR', 'GBP', 'JPY', 'INR'][Math.floor(Math.random() * 5)],
                toCurrency: ['USD', 'EUR', 'GBP', 'JPY', 'INR'][Math.floor(Math.random() * 5)],
                amount: Math.random() * 100000 + 1000,
                rate: Math.random() + 0.5,
                status: ['Completed', 'Pending', 'Failed'][Math.floor(Math.random() * 3)]
            });
        }
        return transactions;
    },

    // Orders
    getOrders: function(limit = 10) {
        const orders = [];
        for (let i = 0; i < limit; i++) {
            orders.push({
                id: `ORD${String(i + 1).padStart(5, '0')}`,
                date: new Date(Date.now() - Math.random() * 30 * 24 * 60 * 60 * 1000),
                type: ['Import', 'Export'][Math.floor(Math.random() * 2)],
                product: ['Electronics', 'Clothing', 'Machinery', 'Chemicals'][Math.floor(Math.random() * 4)],
                quantity: Math.floor(Math.random() * 1000) + 10,
                value: Math.random() * 500000 + 10000,
                currency: ['USD', 'EUR', 'GBP'][Math.floor(Math.random() * 3)],
                status: ['Processing', 'Shipped', 'Delivered', 'Pending'][Math.floor(Math.random() * 4)]
            });
        }
        return orders;
    },

    // Invoices
    getInvoices: function(limit = 10) {
        const invoices = [];
        for (let i = 0; i < limit; i++) {
            invoices.push({
                id: `INV${String(i + 1).padStart(5, '0')}`,
                date: new Date(Date.now() - Math.random() * 30 * 24 * 60 * 60 * 1000),
                amount: Math.random() * 100000 + 5000,
                currency: ['USD', 'EUR', 'GBP'][Math.floor(Math.random() * 3)],
                status: ['Paid', 'Pending', 'Overdue'][Math.floor(Math.random() * 3)],
                dueDate: new Date(Date.now() + Math.random() * 30 * 24 * 60 * 60 * 1000)
            });
        }
        return invoices;
    }
};

// ============================================
// TABLE GENERATION UTILITY
// ============================================

function generateTable(data, columns, containerId) {
    const container = document.getElementById(containerId);
    if (!container) return;

    const table = document.createElement('table');
    const thead = document.createElement('thead');
    const tbody = document.createElement('tbody');

    // Create header
    const headerRow = document.createElement('tr');
    columns.forEach(col => {
        const th = document.createElement('th');
        th.textContent = col.label;
        headerRow.appendChild(th);
    });
    thead.appendChild(headerRow);

    // Create body rows
    data.forEach(item => {
        const row = document.createElement('tr');
        columns.forEach(col => {
            const td = document.createElement('td');
            let value = item[col.key];

            if (col.format === 'date' && value) {
                value = formatDate(value);
            } else if (col.format === 'currency' && value) {
                value = '$' + parseFloat(value).toFixed(2);
            } else if (col.format === 'status' && value) {
                const statusClass = value === 'Completed' || value === 'Paid' ? 'success' :
                                   value === 'Failed' || value === 'Overdue' ? 'danger' : 'warning';
                td.innerHTML = `<span style="color: ${getStatusColor(value)}">${value}</span>`;
            }

            td.textContent = value || '-';
            row.appendChild(td);
        });
        tbody.appendChild(row);
    });

    table.appendChild(thead);
    table.appendChild(tbody);

    const tableContainer = document.createElement('div');
    tableContainer.className = 'table-container';
    tableContainer.appendChild(table);

    container.appendChild(tableContainer);
}

function getStatusColor(status) {
    switch(status) {
        case 'Completed':
        case 'Paid':
        case 'Delivered':
            return '#31a24c';
        case 'Failed':
        case 'Overdue':
            return '#d9534f';
        case 'Pending':
        case 'Processing':
            return '#f0ad4e';
        default:
            return '#a0a3a8';
    }
}

// ============================================
// EXPORT FUNCTIONALITY
// ============================================

function exportToCSV(data, filename = 'export.csv') {
    if (!data || data.length === 0) {
        alert('No data to export');
        return;
    }

    const headers = Object.keys(data[0]);
    let csv = headers.join(',') + '\n';

    data.forEach(row => {
        const values = headers.map(header => {
            const value = row[header];
            return typeof value === 'string' && value.includes(',') ? `"${value}"` : value;
        });
        csv += values.join(',') + '\n';
    });

    const blob = new Blob([csv], { type: 'text/csv' });
    const url = window.URL.createObjectURL(blob);
    const a = document.createElement('a');
    a.href = url;
    a.download = filename;
    a.click();
    window.URL.revokeObjectURL(url);
}

function exportToPDF(content, filename = 'export.pdf') {
    alert('PDF export functionality would be implemented with a library like jsPDF');
    // In production, use jsPDF library
}

// ============================================
// NOTIFICATION SYSTEM
// ============================================

function showNotification(message, type = 'info', duration = 3000) {
    const notification = document.createElement('div');
    notification.className = `notification notification-${type}`;
    notification.textContent = message;
    notification.style.cssText = `
        position: fixed;
        bottom: 20px;
        right: 20px;
        padding: 15px 20px;
        background-color: var(--${type === 'success' ? 'success' : type === 'error' ? 'danger' : 'accent'}-color);
        color: white;
        border-radius: 5px;
        z-index: 2000;
        animation: slideInUp 0.3s ease;
    `;
    document.body.appendChild(notification);

    setTimeout(() => {
        notification.style.animation = 'slideOutDown 0.3s ease';
        setTimeout(() => notification.remove(), 300);
    }, duration);
}

// ============================================
// FORM VALIDATION
// ============================================

function validateEmail(email) {
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    return emailRegex.test(email);
}

function validatePhone(phone) {
    const phoneRegex = /^[0-9]{10,}$/;
    return phoneRegex.test(phone.replace(/\D/g, ''));
}

function validateCurrency(amount) {
    return !isNaN(parseFloat(amount)) && parseFloat(amount) > 0;
}
