let allCustomers=[];
async function loadCustomers() {
    const [customers, banks] = await Promise.all([
        apiGet('/api/customers'),
        apiGet('/api/banks')
    ]);

    const bankMap = Object.fromEntries((banks || []).map(b => [b.bankId, b.bankName]));
    allCustomers=(customers||[]).sort((a, b) => (b.customerId || 0) - (a.customerId || 0));
    document.getElementById('subTitle').textContent=`${allCustomers.length} registered traders`;
    document.getElementById('mBank').innerHTML='<option value="">Select bank</option>'+(banks||[]).map(b=>`<option value="${b.bankId}">${b.bankName} — ${b.branchName || 'Main'}</option>`).join('');
    renderTable(allCustomers, bankMap);
}

function renderTable(data, bankMap = {}) {
    const tbody=document.getElementById('customersTable');
    if(!data.length){tbody.innerHTML='<tr><td colspan="7"><div class="empty-state"><div class="empty-icon">👤</div><p>No customers found</p></div></td></tr>';return;}
    tbody.innerHTML=data.map(c=>`<tr><td style="color:var(--text-muted)">#${c.customerId}</td><td style="font-weight:600">${c.name}</td><td>${c.email}</td><td>${c.phone||'—'}</td><td>${getBadge(c.customerType)}</td><td>${bankMap[c.bankId] || '—'}</td><td><button class="btn btn-danger btn-sm" onclick="deleteCustomer(${c.customerId})">🗑️</button></td></tr>`).join('');
}

function filterTable(){
    const q=document.getElementById('searchInput').value.toLowerCase();
    renderTable(allCustomers.filter(c=>c.name.toLowerCase().includes(q)||c.email.toLowerCase().includes(q)));
}

function openModal(){document.getElementById('modal').style.display='flex';}
function closeModal(){document.getElementById('modal').style.display='none';}
async function addCustomer(){
    const name=document.getElementById('mName').value.trim(),email=document.getElementById('mEmail').value.trim(),phone=document.getElementById('mPhone').value.trim(),customerType=document.getElementById('mType').value,address=document.getElementById('mAddress').value.trim(),bankId=document.getElementById('mBank').value;
    if(!name||!email){showToast('Name and Email are required','error');return;}
    try {
        await apiPost('/api/customers', { name, email, phone, address, customerType, bankId: bankId || null });
    } catch (error) {
        showToast(error.message,'error');
        return;
    }
    showToast('Customer added!');closeModal();loadCustomers();
}

async function deleteCustomer(id){if(!confirm('Delete?'))return;await apiDelete(`/api/customers/${id}`);showToast('Deleted');loadCustomers();}
loadCustomers();
