let allRates=[];
async function loadForex(){
    const [transactions, customers, banks, centralBanks, rates] = await Promise.all([
        supabaseTableSelect('forex_transactions'),
        supabaseTableSelect('customers'),
        supabaseTableSelect('banks'),
        supabaseTableSelect('country_banks'),
        supabaseTableSelect('exchange_rates')
    ]);

    const customerMap = Object.fromEntries((customers || []).map(c => [c.customer_id, c.name]));
    allRates=rates||[];
    document.getElementById('mCustomer').innerHTML='<option value="">Select customer</option>'+(customers||[]).map(c=>`<option value="${c.customer_id}">${c.name}</option>`).join('');
    document.getElementById('mBank').innerHTML='<option value="">Select bank</option>'+(banks||[]).map(b=>`<option value="${b.bank_id}">${b.bank_name}</option>`).join('');
    document.getElementById('mCentralBank').innerHTML='<option value="">Select central bank</option>'+(centralBanks||[]).map(b=>`<option value="${b.country_bank_id}">${b.bank_name}</option>`).join('');
    const currencies=[...new Set(allRates.flatMap(r=>[r.from_currency,r.to_currency]))];
    ['mFrom','mTo'].forEach(id=>{document.getElementById(id).innerHTML=currencies.map(c=>`<option value="${c}">${c}</option>`).join('');});
    document.getElementById('mTo').value='USD';
    const tbody=document.getElementById('forexTable');
    const txs=(transactions||[]).sort((a, b) => (b.forex_id || 0) - (a.forex_id || 0));
    if(!txs.length){tbody.innerHTML='<tr><td colspan="8"><div class="empty-state"><div class="empty-icon">💱</div><p>No transactions yet</p></div></td></tr>';return;}
    tbody.innerHTML=txs.map(t=>`<tr><td style="color:var(--text-muted)">#${t.forex_id}</td><td style="font-weight:600">${customerMap[t.customer_id]||'—'}</td><td><span class="badge badge-blue">${t.from_currency}</span></td><td><span class="badge badge-green">${t.to_currency}</span></td><td style="font-weight:700">${parseFloat(t.amount||0).toLocaleString()}</td><td style="color:var(--accent);font-weight:700">${parseFloat(t.converted_amount||0).toLocaleString()}</td><td style="color:var(--text-muted);font-size:12px">${parseFloat(t.rate_used||0).toFixed(5)}</td><td style="color:var(--text-muted);font-size:12px">${t.transaction_date?.split('T')[0]}</td></tr>`).join('');
}
function calcConversion(amount,from,to){
    if(!amount||!from||!to||from===to)return null;
    if(from==='USD'){const r=allRates.find(x=>x.from_currency==='USD'&&x.to_currency===to);if(!r)return null;return{converted:amount*r.rate,rate:r.rate,path:`${from} → ${to}`};}
    if(to==='USD'){const r=allRates.find(x=>x.from_currency===from&&x.to_currency==='USD');if(!r)return null;return{converted:amount*r.rate,rate:r.rate,path:`${from} → ${to}`};}
    const r1=allRates.find(x=>x.from_currency===from&&x.to_currency==='USD'),r2=allRates.find(x=>x.from_currency==='USD'&&x.to_currency===to);
    if(!r1||!r2)return null;return{converted:amount*r1.rate*r2.rate,rate:r1.rate*r2.rate,path:`${from} → USD → ${to}`};
}
function calculatePreview(){
    const amount=parseFloat(document.getElementById('mAmount').value),from=document.getElementById('mFrom').value,to=document.getElementById('mTo').value,result=calcConversion(amount,from,to),preview=document.getElementById('convPreview');
    if(result&&amount){preview.style.display='block';document.getElementById('convPath').textContent=result.path;document.getElementById('convAmount').textContent=`${to} ${result.converted.toLocaleString(undefined,{maximumFractionDigits:2})}`;document.getElementById('convRate').textContent=`Rate: ${result.rate.toFixed(6)}`;}
    else{preview.style.display='none';}
}
function openModal(){document.getElementById('modal').style.display='flex';}
function closeModal(){document.getElementById('modal').style.display='none';}
async function executeForex(){
    const cId=document.getElementById('mCustomer').value,amount=parseFloat(document.getElementById('mAmount').value),from=document.getElementById('mFrom').value,to=document.getElementById('mTo').value,bId=document.getElementById('mBank').value,cbId=document.getElementById('mCentralBank').value;
    if(!cId||!amount){showToast('Customer and amount are required','error');return;}
    const result=calcConversion(amount,from,to);
    if(!result){showToast('Exchange rate not found','error');return;}
    try {
        await supabaseTableInsert('forex_transactions', {
            customer_id: parseInt(cId, 10),
            customer_bank_id: bId || null,
            from_currency: from,
            to_currency: to,
            from_country_bank_id: cbId || null,
            to_country_bank_id: null,
            amount,
            converted_amount: result.converted,
            rate_used: result.rate
        });
    } catch (error) {
        showToast(error.message, 'error');
        return;
    }
    showToast('Transaction completed! Invoice generated.');closeModal();loadForex();
}
loadForex();
