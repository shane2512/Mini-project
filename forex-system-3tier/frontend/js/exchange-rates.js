async function loadRates(){
    const [rates, currencies] = await Promise.all([
        supabaseTableSelect('exchange_rates'),
        supabaseTableSelect('currencies')
    ]);

    ['mFrom','mTo'].forEach(id=>{document.getElementById(id).innerHTML='<option value="">Select currency</option>'+(currencies||[]).map(c=>`<option value="${c.currency_code}">${c.currency_code} — ${c.currency_name}</option>`).join('');});
    document.getElementById('mDate').value=new Date().toISOString().split('T')[0];
    const sorted = (rates || []).sort((a, b) => (b.rate_id || 0) - (a.rate_id || 0));
    document.getElementById('ratesTable').innerHTML=sorted.map(r=>`<tr><td style="color:var(--text-muted)">#${r.rate_id}</td><td><span class="badge badge-blue">${r.from_currency}</span></td><td><span class="badge badge-green">${r.to_currency}</span></td><td style="color:var(--accent);font-weight:700;font-size:16px">${parseFloat(r.rate).toFixed(6)}</td><td style="color:var(--text-muted)">${r.rate_date}</td></tr>`).join('')||'<tr><td colspan="5"><div class="empty-state"><p>No rates</p></div></td></tr>';
}
function openModal(){document.getElementById('modal').style.display='flex';}
function closeModal(){document.getElementById('modal').style.display='none';}
async function addRate(){const from=document.getElementById('mFrom').value,to=document.getElementById('mTo').value,rate=parseFloat(document.getElementById('mRate').value),date=document.getElementById('mDate').value;if(!from||!to||!rate){showToast('All fields required','error');return;}if(from===to){showToast('From and To cannot be same','error');return;}try{await supabaseTableInsert('exchange_rates',{from_currency:from,to_currency:to,rate,rate_date:date});}catch(error){showToast(error.message,'error');return;}showToast('Rate added!');closeModal();loadRates();}
loadRates();
