async function loadDashboard() {
    const [c,o,f,i,r,ro] = await Promise.all([
        db.from('customers').select('*',{count:'exact',head:true}),
        db.from('orders').select('*',{count:'exact',head:true}),
        db.from('forex_transactions').select('*',{count:'exact',head:true}),
        db.from('invoices').select('*',{count:'exact',head:true}),
        db.from('exchange_rates').select('*').order('rate_date',{ascending:false}).limit(6),
        db.from('orders').select('*').order('order_id',{ascending:false}).limit(5)
    ]);
    document.getElementById('statCustomers').textContent = c.count||0;
    document.getElementById('statOrders').textContent = o.count||0;
    document.getElementById('statForex').textContent = f.count||0;
    document.getElementById('statInvoices').textContent = i.count||0;
    document.getElementById('ratesTable').innerHTML = r.data?.length ? r.data.map(x=>`<tr><td>${getBadge(x.from_currency)}</td><td>${getBadge(x.to_currency)}</td><td style="color:var(--accent);font-weight:700">${parseFloat(x.rate).toFixed(4)}</td><td style="color:var(--text-muted)">${x.rate_date}</td></tr>`).join('') : '<tr><td colspan="4" class="empty-state">No rates</td></tr>';
    document.getElementById('ordersTable').innerHTML = ro.data?.length ? ro.data.map(x=>`<tr><td style="color:var(--text-muted)">#${x.order_id}</td><td>${getBadge(x.order_type)}</td><td style="color:var(--accent);font-weight:700">${parseFloat(x.total_amount||0).toLocaleString()}</td><td>${getBadge(x.status)}</td></tr>`).join('') : '<tr><td colspan="4" class="empty-state">No orders</td></tr>';
}
loadDashboard();
