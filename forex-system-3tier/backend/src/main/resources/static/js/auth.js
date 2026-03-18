async function handleLogin() {
    const email = document.getElementById('email').value.trim();
    const password = document.getElementById('password').value.trim();
    if (!email || !password) { showToast('Please enter email and password', 'error'); return; }
    const { data, error } = await db.auth.signInWithPassword({ email, password });
    if (error) { showToast(error.message, 'error'); return; }
    showToast('Login successful!');
    setTimeout(() => window.location.href = '/dashboard', 800);
}
async function handleRegister() {
    const name = document.getElementById('name').value.trim();
    const phone = document.getElementById('phone').value.trim();
    const email = document.getElementById('email').value.trim();
    const password = document.getElementById('password').value.trim();
    const customerType = document.getElementById('customerType').value;
    const bankId = document.getElementById('bankId').value;
    const address = document.getElementById('address').value.trim();
    if (!name || !email || !password) { showToast('Name, email and password are required', 'error'); return; }
    const { data: authData, error: authError } = await db.auth.signUp({ email, password });
    if (authError) { showToast(authError.message, 'error'); return; }
    const { error: dbError } = await db.from('customers').insert([{ name, phone, email, address, customer_type: customerType, bank_id: bankId || null }]);
    if (dbError) { showToast(dbError.message, 'error'); return; }
    showToast('Account created! Please login.');
    setTimeout(() => window.location.href = '/', 1000);
}
async function handleLogout() { await db.auth.signOut(); window.location.href = '/'; }
async function loadBanksForRegister() {
    const bankSelect = document.getElementById('bankId');
    if (!bankSelect) return;
    const { data } = await db.from('banks').select('*');
    if (data) bankSelect.innerHTML = '<option value="">Select bank</option>' + data.map(b => `<option value="${b.bank_id}">${b.bank_name} — ${b.branch_name}</option>`).join('');
}
async function checkAuth() {
    const { data: { session } } = await db.auth.getSession();
    const isLoginPage = window.location.pathname === '/' || window.location.pathname === '/register';
    if (!session && !isLoginPage) window.location.href = '/';
    if (session && isLoginPage) window.location.href = '/dashboard';
}
checkAuth(); loadBanksForRegister();
