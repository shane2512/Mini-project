async function loadDashboard() {
    const [customers, orders, forex, invoices, rates] = await Promise.all([
        apiGet('/api/customers'),
        apiGet('/api/orders'),
        apiGet('/api/forex'),
        apiGet('/api/invoices'),
        apiGet('/api/exchange-rates')
    ]);

    const recentRates = (rates || []).sort((a, b) => {
        const ad = new Date(a.rateDate || 0).getTime();
        const bd = new Date(b.rateDate || 0).getTime();
        return bd - ad;
    }).slice(0, 6);

    const recentOrders = (orders || []).sort((a, b) => (b.orderId || 0) - (a.orderId || 0)).slice(0, 5);

    document.getElementById('statCustomers').textContent = (customers || []).length;
    document.getElementById('statOrders').textContent = (orders || []).length;
    document.getElementById('statForex').textContent = (forex || []).length;
    document.getElementById('statInvoices').textContent = (invoices || []).length;
    document.getElementById('ratesTable').innerHTML = recentRates.length ? recentRates.map(x=>`<tr><td>${getBadge(x.fromCurrency)}</td><td>${getBadge(x.toCurrency)}</td><td style="color:var(--accent);font-weight:700">${parseFloat(x.rate).toFixed(4)}</td><td style="color:var(--text-muted)">${x.rateDate}</td></tr>`).join('') : '<tr><td colspan="4" class="empty-state">No rates</td></tr>';
    document.getElementById('ordersTable').innerHTML = recentOrders.length ? recentOrders.map(x=>`<tr><td style="color:var(--text-muted)">#${x.orderId}</td><td>${getBadge(x.orderType)}</td><td style="color:var(--accent);font-weight:700">${parseFloat(x.totalAmount||0).toLocaleString()}</td><td>${getBadge(x.status)}</td></tr>`).join('') : '<tr><td colspan="4" class="empty-state">No orders</td></tr>';
}
loadDashboard();
