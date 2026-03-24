let allCustomers=[];
async function loadCustomers() {
    const [customers, banks] = await Promise.all([
        supabaseTableSelect('customers'),
        supabaseTableSelect('banks')
    ]);

    const bankMap = Object.fromEntries((banks || []).map(b => [b.bank_id, b.bank_name]));
    allCustomers=(customers||[]).sort((a, b) => (b.customer_id || 0) - (a.customer_id || 0));
    document.getElementById('subTitle').textContent=`${allCustomers.length} registered traders`;
    document.getElementById('mBank').innerHTML='<option value="">Select bank</option>'+(banks||[]).map(b=>`<option value="${b.bank_id}">${b.bank_name} — ${b.branch_name || 'Main'}</option>`).join('');
    renderTable(allCustomers, bankMap);
}

function renderTable(data, bankMap = {}) {
    const tbody=document.getElementById('customersTable');
    if(!data.length){tbody.innerHTML='<tr><td colspan="7"><div class="empty-state"><div class="empty-icon">👤</div><p>No customers found</p></div></td></tr>';return;}
    tbody.innerHTML=data.map(c=>`<tr><td style="color:var(--text-muted)">#${c.customer_id}</td><td style="font-weight:600">${c.name}</td><td>${c.email}</td><td>${c.phone||'—'}</td><td>${getBadge(c.customer_type)}</td><td>${bankMap[c.bank_id] || '—'}</td><td><button class="btn btn-danger btn-sm" onclick="deleteCustomer(${c.customer_id})">🗑️</button></td></tr>`).join('');
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
        await supabaseTableInsert('customers', { name, email, phone, address, customer_type: customerType, bank_id: bankId || null });
    } catch (error) {
        showToast(error.message,'error');
        return;
    }
    showToast('Customer added!');closeModal();loadCustomers();
}

async function deleteCustomer(id){if(!confirm('Delete?'))return;await supabaseTableDelete('customers',`customer_id=eq.${id}`);showToast('Deleted');loadCustomers();}
loadCustomers();
