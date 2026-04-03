const Auth = {
  isLoggedIn() { return !!localStorage.getItem('ftms_token'); },
  getToken() { return localStorage.getItem('ftms_token'); },
  getRole() { return localStorage.getItem('ftms_role'); },
  getFullName() { return localStorage.getItem('ftms_name') || 'User'; },
  getUserId() { return localStorage.getItem('ftms_id'); },

  getAuthHeader() {
    return {
      'Content-Type': 'application/json',
      'Authorization': 'Bearer ' + this.getToken()
    };
  },

  saveSession(token, role, name, id) {
    localStorage.setItem('ftms_token', token);
    localStorage.setItem('ftms_role', role);
    localStorage.setItem('ftms_name', name);
    localStorage.setItem('ftms_id', id);
  },

  logout() {
    localStorage.removeItem('ftms_token');
    localStorage.removeItem('ftms_role');
    localStorage.removeItem('ftms_name');
    localStorage.removeItem('ftms_id');
    window.location.href = '/login.html';
  },

  requireRole(role) {
    if (!this.isLoggedIn()) { window.location.href = '/login.html'; return false; }
    if (this.getRole() !== role) { alert('Access denied.'); this.logout(); return false; }
    return true;
  },

  requireUser() {
    if (!this.isLoggedIn()) { window.location.href = '/login.html'; return false; }
    const r = this.getRole();
    if (['ADMIN','CENTRAL_BANK','COMMERCIAL_BANK'].includes(r)) {
      alert('Access denied.'); this.logout(); return false;
    }
    return true;
  }
};