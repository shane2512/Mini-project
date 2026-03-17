let allRates = [];

async function loadForex() {
    const [txRes, custRes, bankRes, cbRes, rateRes] = await Promise.all([
        db.from('forex_transactions').select('*, customers(name)').order('forex_id', { ascending: false }),
        db.from('customers').select('*'),
        db.from('banks').select('*'),
        db.from('country_banks').select('*'),
        db.from('exchange_rates').select('*')
    ]);

    allRates = rateRes.data || [];
    const transactions = txRes.data || [];

    const custSelect = document.getElementById('mCustomer');
    custSelect.innerHTML = '<option value="">Select customer</option>' +
        (custRes.data || []).map(c => `<option value="${c.customer_id}">${c.name}</option>`).join('');

    const bankSelect = document.getElementById('mBank');
    bankSelect.innerHTML = '<option value="">Select bank</option>' +
        (bankRes.data || []).map(b => `<option value="${b.bank_id}">${b.bank_name}</option>`).join('');

    const cbSelect = document.getElementById('mCentralBank');
    cbSelect.innerHTML = '<option value="">Select central bank</option>' +
        (cbRes.data || []).map(b => `<option value="${b.country_bank_id}">${b.bank_name}</option>`).join('');

    const currencies = [...new Set(allRates.flatMap(r => [r.from_currency, r.to_currency]))];
    ['mFrom', 'mTo'].forEach(id => {
        document.getElementById(id).innerHTML = currencies.map(c => `<option value="${c}">${c}</option>`).join('');
    });
    document.getElementById('mTo').value = 'USD';

    const tbody = document.getElementById('forexTable');
    if (!transactions.length) {
        tbody.innerHTML = '<tr><td colspan="8"><div class="empty-state"><div class="empty-icon">💱</div><p>No transactions yet</p></div></td></tr>';
        return;
    }

    tbody.innerHTML = transactions.map(t => `
        <tr>
            <td style="color:var(--text-muted)">#${t.forex_id}</td>
            <td style="font-weight:600;color:var(--text)">${t.customers?.name || '—'}</td>
            <td><span class="badge badge-blue">${t.from_currency}</span></td>
            <td><span class="badge badge-green">${t.to_currency}</span></td>
            <td style="font-weight:700">${parseFloat(t.amount || 0).toLocaleString()}</td>
            <td style="color:var(--accent);font-family:Syne,sans-serif;font-weight:700">${parseFloat(t.converted_amount || 0).toLocaleString()}</td>
            <td style="color:var(--text-muted);font-size:12px">${parseFloat(t.rate_used || 0).toFixed(5)}</td>
            <td style="color:var(--text-muted);font-size:12px">${t.transaction_date?.split('T')[0]}</td>
        </tr>`).join('');
}

function calcConversion(amount, from, to) {
    if (!amount || !from || !to || from === to) return null;

    if (from === 'USD') {
        const r = allRates.find(x => x.from_currency === 'USD' && x.to_currency === to);
        if (!r) return null;
        return { converted: amount * r.rate, rate: r.rate, path: `${from} → ${to}` };
    }
    if (to === 'USD') {
        const r = allRates.find(x => x.from_currency === from && x.to_currency === 'USD');
        if (!r) return null;
        return { converted: amount * r.rate, rate: r.rate, path: `${from} → ${to}` };
    }
    const r1 = allRates.find(x => x.from_currency === from && x.to_currency === 'USD');
    const r2 = allRates.find(x => x.from_currency === 'USD' && x.to_currency === to);
    if (!r1 || !r2) return null;
    const usd = amount * r1.rate;
    return { converted: usd * r2.rate, rate: r1.rate * r2.rate, path: `${from} → USD → ${to}` };
}

function calculatePreview() {
    const amount = parseFloat(document.getElementById('mAmount').value);
    const from = document.getElementById('mFrom').value;
    const to = document.getElementById('mTo').value;
    const result = calcConversion(amount, from, to);
    const preview = document.getElementById('convPreview');

    if (result && amount) {
        preview.style.display = 'block';
        document.getElementById('convPath').textContent = result.path;
        document.getElementById('convAmount').textContent = `${to} ${result.converted.toLocaleString(undefined, { maximumFractionDigits: 2 })}`;
        document.getElementById('convRate').textContent = `Rate: ${result.rate.toFixed(6)}`;
    } else {
        preview.style.display = 'none';
    }
}

function openModal() { document.getElementById('modal').style.display = 'flex'; }
function closeModal() { document.getElementById('modal').style.display = 'none'; }

async function executeForex() {
    const customerId = document.getElementById('mCustomer').value;
    const amount = parseFloat(document.getElementById('mAmount').value);
    const from = document.getElementById('mFrom').value;
    const to = document.getElementById('mTo').value;
    const bankId = document.getElementById('mBank').value;
    const centralBankId = document.getElementById('mCentralBank').value;

    if (!customerId || !amount) { showToast('Customer and amount are required', 'error'); return; }

    const result = calcConversion(amount, from, to);
    if (!result) { showToast('Exchange rate not found for this currency pair', 'error'); return; }

    const { data: txData, error } = await db.from('forex_transactions').insert([{
        customer_id: parseInt(customerId),
        customer_bank_id: bankId || null,
        from_currency: from,
        to_currency: to,
        from_country_bank_id: centralBankId || null,
        amount,
        converted_amount: parseFloat(result.converted.toFixed(2)),
        rate_used: parseFloat(result.rate.toFixed(6)),
        transaction_date: new Date().toISOString()
    }]).select();

    if (error) { showToast(error.message, 'error'); return; }

    await db.from('invoices').insert([{
        customer_id: parseInt(customerId),
        ref_type: 'FOREX',
        ref_id: txData[0].forex_id,
        amount: parseFloat(result.converted.toFixed(2)),
        currency_code: to,
        status: 'PENDING',
        invoice_date: new Date().toISOString().split('T')[0]
    }]);

    showToast('Forex transaction completed! Invoice generated.');
    closeModal();
    loadForex();
}

loadForex();