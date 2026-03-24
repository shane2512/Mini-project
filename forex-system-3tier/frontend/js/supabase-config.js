const AUTH_KEY = 'ftms_session';
const APP_CONFIG = window.__APP_CONFIG || {};
const API_BASE_URL =
    APP_CONFIG.API_BASE_URL ||
    window.API_BASE_URL ||
    (window.location.hostname === 'localhost' || window.location.hostname === '127.0.0.1'
        ? 'http://localhost:8080'
        : '');
const SUPABASE_URL = APP_CONFIG.SUPABASE_URL || '';
const SUPABASE_ANON_KEY = APP_CONFIG.SUPABASE_ANON_KEY || '';

async function apiRequest(path, options = {}) {
    const requestOptions = {
        method: options.method || 'GET',
        headers: {
            'Content-Type': 'application/json',
            ...(options.headers || {})
        }
    };

    if (options.body !== undefined) {
        requestOptions.body = JSON.stringify(options.body);
    }

    const response = await fetch(`${API_BASE_URL}${path}`, requestOptions);

    if (!response.ok) {
        const errorText = await response.text();
        throw new Error(errorText || `Request failed (${response.status})`);
    }

    if (response.status === 204) {
        return null;
    }

    const responseText = await response.text();
    return responseText ? JSON.parse(responseText) : null;
}

function apiGet(path) { return apiRequest(path); }
function apiPost(path, body) { return apiRequest(path, { method: 'POST', body }); }
function apiPut(path, body) { return apiRequest(path, { method: 'PUT', body }); }
function apiDelete(path) { return apiRequest(path, { method: 'DELETE' }); }

async function supabaseSignIn(email, password) {
    if (!SUPABASE_URL || !SUPABASE_ANON_KEY) {
        throw new Error('Supabase auth is not configured. Set values in app-config.js.');
    }

    const response = await fetch(`${SUPABASE_URL}/auth/v1/token?grant_type=password`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            apikey: SUPABASE_ANON_KEY
        },
        body: JSON.stringify({ email, password })
    });

    if (!response.ok) {
        const errorText = await response.text();
        throw new Error(errorText || 'Invalid credentials');
    }

    return response.json();
}

async function supabaseSignUp(email, password) {
    if (!SUPABASE_URL || !SUPABASE_ANON_KEY) {
        throw new Error('Supabase auth is not configured. Set values in app-config.js.');
    }

    const response = await fetch(`${SUPABASE_URL}/auth/v1/signup`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            apikey: SUPABASE_ANON_KEY
        },
        body: JSON.stringify({ email, password })
    });

    if (!response.ok) {
        const errorText = await response.text();
        throw new Error(errorText || 'Signup failed');
    }

    return response.json();
}

async function supabaseRestRequest(path, options = {}) {
    if (!SUPABASE_URL || !SUPABASE_ANON_KEY) {
        throw new Error('Supabase auth is not configured. Set values in app-config.js.');
    }

    const response = await fetch(`${SUPABASE_URL}${path}`, {
        method: options.method || 'GET',
        headers: {
            'Content-Type': 'application/json',
            apikey: SUPABASE_ANON_KEY,
            Authorization: `Bearer ${SUPABASE_ANON_KEY}`,
            ...(options.headers || {})
        },
        body: options.body !== undefined ? JSON.stringify(options.body) : undefined
    });

    if (!response.ok) {
        const errorText = await response.text();
        throw new Error(errorText || `Supabase request failed (${response.status})`);
    }

    if (response.status === 204) {
        return null;
    }

    const text = await response.text();
    return text ? JSON.parse(text) : null;
}

function supabaseTableSelect(table, filters = '', columns = '*') {
    const filterSuffix = filters ? `&${filters}` : '';
    return supabaseRestRequest(`/rest/v1/${table}?select=${encodeURIComponent(columns)}${filterSuffix}`);
}

function supabaseTableInsert(table, payload) {
    return supabaseRestRequest(`/rest/v1/${table}`, {
        method: 'POST',
        headers: {
            Prefer: 'return=representation'
        },
        body: payload
    });
}

function supabaseTableUpdate(table, filters, payload) {
    const filterSuffix = filters ? `?${filters}` : '';
    return supabaseRestRequest(`/rest/v1/${table}${filterSuffix}`, {
        method: 'PATCH',
        headers: {
            Prefer: 'return=representation'
        },
        body: payload
    });
}

function supabaseTableDelete(table, filters) {
    const filterSuffix = filters ? `?${filters}` : '';
    return supabaseRestRequest(`/rest/v1/${table}${filterSuffix}`, {
        method: 'DELETE',
        headers: {
            Prefer: 'return=minimal'
        }
    });
}

function getSession() {
    try {
        const raw = localStorage.getItem(AUTH_KEY);
        return raw ? JSON.parse(raw) : null;
    } catch {
        return null;
    }
}

function setSession(session) {
    localStorage.setItem(AUTH_KEY, JSON.stringify(session));
}

function clearSession() {
    localStorage.removeItem(AUTH_KEY);
}

function showToast(message, type = 'success') {
    const existing = document.querySelector('.toast');
    if (existing) existing.remove();
    const toast = document.createElement('div');
    toast.className = `toast ${type}`;
    toast.innerHTML = `${type === 'success' ? '✅' : '❌'} ${message}`;
    document.body.appendChild(toast);
    setTimeout(() => toast.remove(), 3000);
}
function getBadge(value) {
    const map = { IMPORTER:'blue',EXPORTER:'green',FOREX_TRADER:'gold',IMPORT:'blue',EXPORT:'green',PENDING:'gold',COMPLETED:'green',FAILED:'red',APPROVED:'green',REJECTED:'red',ORDER:'blue',FOREX:'gold' };
    return `<span class="badge badge-${map[value]||'gray'}">${value}</span>`;
}
function setActive(page) {
    document.querySelectorAll('.sidebar-nav a').forEach(a => { a.classList.remove('active'); if (a.dataset.page === page) a.classList.add('active'); });
}
