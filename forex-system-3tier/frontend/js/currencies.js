async function loadCurrencies(){
    const [currencies, countries, centralBanks] = await Promise.all([
        supabaseTableSelect('currencies'),
        supabaseTableSelect('countries'),
        supabaseTableSelect('country_banks')
    ]);

    const countryMap = Object.fromEntries((countries || []).map(c => [c.country_id, c.country_name]));
    const bankMap = Object.fromEntries((centralBanks || []).map(b => [b.country_bank_id, b.bank_name]));

    document.getElementById('subTitle').textContent=`${(currencies||[]).length} currencies`;
    document.getElementById('mCountry').innerHTML='<option value="">Select country</option>'+(countries||[]).map(c=>`<option value="${c.country_id}">${c.country_name}</option>`).join('');
    document.getElementById('mCentralBank').innerHTML='<option value="">Select central bank</option>'+(centralBanks||[]).map(b=>`<option value="${b.country_bank_id}">${b.bank_name}</option>`).join('');
    document.getElementById('currenciesTable').innerHTML=(currencies||[]).map(c=>`<tr><td><span class="badge badge-blue">${c.currency_code}</span></td><td style="font-weight:600">${c.currency_name}</td><td>${countryMap[c.country_id]||'—'}</td><td>${bankMap[c.country_bank_id]||'—'}</td><td><button class="btn btn-danger btn-sm" onclick="deleteCurrency('${c.currency_code}')">🗑️</button></td></tr>`).join('')||'<tr><td colspan="5"><div class="empty-state"><p>No currencies</p></div></td></tr>';
}
function openModal(){document.getElementById('modal').style.display='flex';}
function closeModal(){document.getElementById('modal').style.display='none';}
async function addCurrency(){const code=document.getElementById('mCode').value.trim().toUpperCase(),name=document.getElementById('mName').value.trim(),cId=document.getElementById('mCountry').value,cbId=document.getElementById('mCentralBank').value;if(!code||!name){showToast('Code and name required','error');return;}try{await supabaseTableInsert('currencies',{currency_code:code,currency_name:name,country_id:cId||null,country_bank_id:cbId||null});}catch(error){showToast(error.message,'error');return;}showToast('Currency added!');closeModal();loadCurrencies();}
async function deleteCurrency(code){if(!confirm('Delete?'))return;await supabaseTableDelete('currencies',`currency_code=eq.${encodeURIComponent(code)}`);showToast('Deleted');loadCurrencies();}
loadCurrencies();
