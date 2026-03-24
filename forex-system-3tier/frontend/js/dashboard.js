async function loadDashboard() {
    const [customers, orders, forex, invoices, rates] = await Promise.all([
        supabaseTableSelect('customers'),
        supabaseTableSelect('orders'),
        supabaseTableSelect('forex_transactions'),
        supabaseTableSelect('invoices'),
        supabaseTableSelect('exchange_rates')
    ]);

    const recentRates = (rates || []).sort((a, b) => {
        const ad = new Date(a.rate_date || 0).getTime();
        const bd = new Date(b.rate_date || 0).getTime();
        return bd - ad;
    }).slice(0, 6);

    const recentOrders = (orders || []).sort((a, b) => (b.order_id || 0) - (a.order_id || 0)).slice(0, 5);

    document.getElementById('statCustomers').textContent = (customers || []).length;
    document.getElementById('statOrders').textContent = (orders || []).length;
    document.getElementById('statForex').textContent = (forex || []).length;
    document.getElementById('statInvoices').textContent = (invoices || []).length;
    document.getElementById('ratesTable').innerHTML = recentRates.length ? recentRates.map(x=>`<tr><td>${getBadge(x.from_currency)}</td><td>${getBadge(x.to_currency)}</td><td style="color:var(--accent);font-weight:700">${parseFloat(x.rate).toFixed(4)}</td><td style="color:var(--text-muted)">${x.rate_date}</td></tr>`).join('') : '<tr><td colspan="4" class="empty-state">No rates</td></tr>';
    document.getElementById('ordersTable').innerHTML = recentOrders.length ? recentOrders.map(x=>`<tr><td style="color:var(--text-muted)">#${x.order_id}</td><td>${getBadge(x.order_type)}</td><td style="color:var(--accent);font-weight:700">${parseFloat(x.total_amount||0).toLocaleString()}</td><td>${getBadge(x.status)}</td></tr>`).join('') : '<tr><td colspan="4" class="empty-state">No orders</td></tr>';
}
loadDashboard();
