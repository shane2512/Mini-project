async function loadCurrencies(){
    const [currencies, countries, centralBanks] = await Promise.all([
        apiGet('/api/currencies'),
        apiGet('/api/countries'),
        apiGet('/api/central-banks')
    ]);

    const countryMap = Object.fromEntries((countries || []).map(c => [c.countryId, c.countryName]));
    const bankMap = Object.fromEntries((centralBanks || []).map(b => [b.countryBankId, b.bankName]));

    document.getElementById('subTitle').textContent=`${(currencies||[]).length} currencies`;
    document.getElementById('mCountry').innerHTML='<option value="">Select country</option>'+(countries||[]).map(c=>`<option value="${c.countryId}">${c.countryName}</option>`).join('');
    document.getElementById('mCentralBank').innerHTML='<option value="">Select central bank</option>'+(centralBanks||[]).map(b=>`<option value="${b.countryBankId}">${b.bankName}</option>`).join('');
    document.getElementById('currenciesTable').innerHTML=(currencies||[]).map(c=>`<tr><td><span class="badge badge-blue">${c.currencyCode}</span></td><td style="font-weight:600">${c.currencyName}</td><td>${countryMap[c.countryId]||'—'}</td><td>${bankMap[c.countryBankId]||'—'}</td><td><button class="btn btn-danger btn-sm" onclick="deleteCurrency('${c.currencyCode}')">🗑️</button></td></tr>`).join('')||'<tr><td colspan="5"><div class="empty-state"><p>No currencies</p></div></td></tr>';
}
function openModal(){document.getElementById('modal').style.display='flex';}
function closeModal(){document.getElementById('modal').style.display='none';}
async function addCurrency(){const code=document.getElementById('mCode').value.trim().toUpperCase(),name=document.getElementById('mName').value.trim(),cId=document.getElementById('mCountry').value,cbId=document.getElementById('mCentralBank').value;if(!code||!name){showToast('Code and name required','error');return;}try{await apiPost('/api/currencies',{currencyCode:code,currencyName:name,countryId:cId||null,countryBankId:cbId||null});}catch(error){showToast(error.message,'error');return;}showToast('Currency added!');closeModal();loadCurrencies();}
async function deleteCurrency(code){if(!confirm('Delete?'))return;await apiDelete(`/api/currencies/${code}`);showToast('Deleted');loadCurrencies();}
loadCurrencies();
