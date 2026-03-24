async function handleLogin() {
    const email = document.getElementById('email').value.trim();
    const password = document.getElementById('password').value.trim();
    if (!email || !password) { showToast('Please enter email and password', 'error'); return; }

    try {
        const authResult = await supabaseSignIn(email, password);
        const user = authResult && authResult.user ? authResult.user : null;

        // Keep a minimal client session based on successful Supabase authentication.
        setSession({
            customerId: user && user.id ? user.id : email,
            name: user && user.user_metadata && user.user_metadata.name ? user.user_metadata.name : (email.split('@')[0] || 'User'),
            email,
            loggedAt: new Date().toISOString()
        });

        showToast('Login successful!');
        setTimeout(() => window.location.href = '../pages/index.html', 800);
    } catch (error) {
        showToast(error.message, 'error');
    }
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

    try {
        await supabaseSignUp(email, password);

        await apiPost('/api/customers', {
            name,
            phone,
            email,
            address,
            customerType,
            bankId: bankId || null
        });

        showToast('Account created! Please login.');
        setTimeout(() => window.location.href = '../pages/login.html', 1000);
    } catch (error) {
        showToast(error.message, 'error');
    }
}

async function handleLogout() {
    clearSession();
    window.location.href = '../pages/login.html';
}

async function loadBanksForRegister() {
    const bankSelect = document.getElementById('bankId');
    if (!bankSelect) return;

    try {
        const banks = await apiGet('/api/banks');

        if (banks) {
            bankSelect.innerHTML = '<option value="">Select bank</option>' +
                banks.map(b => `<option value="${b.bankId}">${b.bankName} — ${b.branchName || 'Main'}</option>`).join('');
        }
    } catch {
        bankSelect.innerHTML = '<option value="">Unable to load banks</option>';
    }
}

async function checkAuth() {
    const session = getSession();
    const path = window.location.pathname.toLowerCase().replace(/\/+$/, '');

    // Support both explicit .html routes and clean URL variants from static servers.
    const publicPaths = new Set([
        '',
        '/',
        '/login',
        '/login.html',
        '/register',
        '/register.html',
        '/pages/login',
        '/pages/login.html',
        '/pages/register',
        '/pages/register.html'
    ]);
    const isPublicPage = publicPaths.has(path);

    if (!session && !isPublicPage) {
        const target = '/pages/login.html';
        if (window.location.pathname.toLowerCase() !== target) {
            window.location.href = target;
        }
        return;
    }

    if (session && isPublicPage) {
        const target = '/pages/index.html';
        if (window.location.pathname.toLowerCase() !== target) {
            window.location.href = target;
        }
    }
}

checkAuth();
loadBanksForRegister();
