const APP_CONFIG = window.__APP_CONFIG || {};
const SUPABASE_URL = APP_CONFIG.SUPABASE_URL || '';
const SUPABASE_ANON_KEY = APP_CONFIG.SUPABASE_ANON_KEY || '';
const { createClient } = supabase;
const db = SUPABASE_URL && SUPABASE_ANON_KEY ? createClient(SUPABASE_URL, SUPABASE_ANON_KEY) : null;
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
