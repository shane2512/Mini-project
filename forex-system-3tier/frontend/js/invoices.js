let allInvoices=[];
let customerMap = {};

async function loadInvoices(){
    const [invoices, customers] = await Promise.all([
        apiGet('/api/invoices'),
        apiGet('/api/customers')
    ]);

    customerMap = Object.fromEntries((customers || []).map(c => [c.customerId, c.name]));
    allInvoices=(invoices||[]).sort((a, b) => (b.invoiceId || 0) - (a.invoiceId || 0));
    document.getElementById('subTitle').textContent=`${allInvoices.length} total invoices`;
    document.getElementById('totalCount').textContent=allInvoices.length;
    document.getElementById('pendingCount').textContent=allInvoices.filter(i=>i.status==='PENDING').length;
    document.getElementById('completedCount').textContent=allInvoices.filter(i=>i.status==='COMPLETED').length;
    renderInvoices(allInvoices);
}
function filterInvoices(status){renderInvoices(status==='ALL'?allInvoices:allInvoices.filter(i=>i.status===status));}
function renderInvoices(data){
    const tbody=document.getElementById('invoicesTable');
    if(!data.length){tbody.innerHTML='<tr><td colspan="9"><div class="empty-state"><div class="empty-icon">🧾</div><p>No invoices found</p></div></td></tr>';return;}
    tbody.innerHTML=data.map(inv=>`<tr><td style="color:var(--text-muted)">#${inv.invoiceId}</td><td style="font-weight:600">${customerMap[inv.customerId]||'—'}</td><td>${getBadge(inv.refType)}</td><td>#${inv.refId}</td><td style="color:var(--accent);font-weight:700">${parseFloat(inv.amount||0).toLocaleString()}</td><td><span class="badge badge-gray">${inv.currencyCode}</span></td><td style="color:var(--text-muted)">${inv.invoiceDate}</td><td>${getBadge(inv.status)}</td><td>${inv.status==='PENDING'?`<button class="btn btn-secondary btn-sm" onclick="updateInvoice(${inv.invoiceId},'COMPLETED')">✅ Complete</button><button class="btn btn-danger btn-sm" onclick="updateInvoice(${inv.invoiceId},'FAILED')">❌ Fail</button>`:'—'}</td></tr>`).join('');
}
async function updateInvoice(id,status){try{await apiPut(`/api/invoices/${id}/status?status=${encodeURIComponent(status)}`);}catch(error){showToast(error.message,'error');return;}showToast(`Invoice marked as ${status}`);loadInvoices();}
loadInvoices();
