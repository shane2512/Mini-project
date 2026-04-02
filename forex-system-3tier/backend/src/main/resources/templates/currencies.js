async function loadCurrencies(){
    const[cR,coR,cbR]=await Promise.all([db.from('currencies').select('*,countries(country_name),country_banks(bank_name)').order('currency_code'),db.from('countries').select('*'),db.from('country_banks').select('*')]);
    document.getElementById('subTitle').textContent=`${(cR.data||[]).length} currencies`;
    document.getElementById('mCountry').innerHTML='<option value="">Select country</option>'+(coR.data||[]).map(c=>`<option value="${c.country_id}">${c.country_name}</option>`).join('');
    document.getElementById('mCentralBank').innerHTML='<option value="">Select central bank</option>'+(cbR.data||[]).map(b=>`<option value="${b.country_bank_id}">${b.bank_name}</option>`).join('');
    document.getElementById('currenciesTable').innerHTML=(cR.data||[]).map(c=>`<tr><td><span class="badge badge-blue">${c.currency_code}</span></td><td style="font-weight:600">${c.currency_name}</td><td>${c.countries?.country_name||'—'}</td><td>${c.country_banks?.bank_name||'—'}</td><td><button class="btn btn-danger btn-sm" onclick="deleteCurrency('${c.currency_code}')">🗑️</button></td></tr>`).join('')||'<tr><td colspan="5"><div class="empty-state"><p>No currencies</p></div></td></tr>';
}
function openModal(){document.getElementById('modal').style.display='flex';}
function closeModal(){document.getElementById('modal').style.display='none';}
async function addCurrency(){const code=document.getElementById('mCode').value.trim().toUpperCase(),name=document.getElementById('mName').value.trim(),cId=document.getElementById('mCountry').value,cbId=document.getElementById('mCentralBank').value;if(!code||!name){showToast('Code and name required','error');return;}const{error}=await db.from('currencies').insert([{currency_code:code,currency_name:name,country_id:cId||null,country_bank_id:cbId||null}]);if(error){showToast(error.message,'error');return;}showToast('Currency added!');closeModal();loadCurrencies();}
async function deleteCurrency(code){if(!confirm('Delete?'))return;await db.from('currencies').delete().eq('currency_code',code);showToast('Deleted');loadCurrencies();}
loadCurrencies();
