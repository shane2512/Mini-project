async function loadRates(){
    const [rates, currencies] = await Promise.all([
        apiGet('/api/exchange-rates'),
        apiGet('/api/currencies')
    ]);

    ['mFrom','mTo'].forEach(id=>{document.getElementById(id).innerHTML='<option value="">Select currency</option>'+(currencies||[]).map(c=>`<option value="${c.currencyCode}">${c.currencyCode} — ${c.currencyName}</option>`).join('');});
    document.getElementById('mDate').value=new Date().toISOString().split('T')[0];
    const sorted = (rates || []).sort((a, b) => (b.rateId || 0) - (a.rateId || 0));
    document.getElementById('ratesTable').innerHTML=sorted.map(r=>`<tr><td style="color:var(--text-muted)">#${r.rateId}</td><td><span class="badge badge-blue">${r.fromCurrency}</span></td><td><span class="badge badge-green">${r.toCurrency}</span></td><td style="color:var(--accent);font-weight:700;font-size:16px">${parseFloat(r.rate).toFixed(6)}</td><td style="color:var(--text-muted)">${r.rateDate}</td></tr>`).join('')||'<tr><td colspan="5"><div class="empty-state"><p>No rates</p></div></td></tr>';
}
function openModal(){document.getElementById('modal').style.display='flex';}
function closeModal(){document.getElementById('modal').style.display='none';}
async function addRate(){const from=document.getElementById('mFrom').value,to=document.getElementById('mTo').value,rate=parseFloat(document.getElementById('mRate').value),date=document.getElementById('mDate').value;if(!from||!to||!rate){showToast('All fields required','error');return;}if(from===to){showToast('From and To cannot be same','error');return;}try{await apiPost('/api/exchange-rates',{fromCurrency:from,toCurrency:to,rate,rateDate:date});}catch(error){showToast(error.message,'error');return;}showToast('Rate added!');closeModal();loadRates();}
loadRates();
