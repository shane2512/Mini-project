async function loadBanks(){
    const [banks, centralBanks, countries] = await Promise.all([
        apiGet('/api/banks'),
        apiGet('/api/central-banks'),
        apiGet('/api/countries')
    ]);

    const sortedBanks = (banks || []).sort((a, b) => (a.bankId || 0) - (b.bankId || 0));
    const sortedCentralBanks = (centralBanks || []).sort((a, b) => (a.countryBankId || 0) - (b.countryBankId || 0));

    document.getElementById('mCountry').innerHTML='<option value="">Select country</option>'+(countries||[]).map(c=>`<option value="${c.countryId}">${c.countryName}</option>`).join('');
    document.getElementById('banksTable').innerHTML=sortedBanks.map(b=>`<tr><td style="color:var(--text-muted)">#${b.bankId}</td><td style="font-weight:600">${b.bankName}</td><td>${b.branchName||'—'}</td><td><code style="background:var(--bg-card2);padding:2px 8px;border-radius:4px;font-size:12px">${b.ifscCode||'—'}</code></td><td>${b.contactNumber||'—'}</td><td><button class="btn btn-danger btn-sm" onclick="deleteBank(${b.bankId})">🗑️</button></td></tr>`).join('')||'<tr><td colspan="6"><div class="empty-state"><p>No banks</p></div></td></tr>';
    document.getElementById('centralBanksTable').innerHTML=sortedCentralBanks.map(b=>`<tr><td style="color:var(--text-muted)">#${b.countryBankId}</td><td style="font-weight:600">${b.bankName}</td><td><span class="badge badge-blue">${b.swiftCode||'—'}</span></td><td>${b.contactNumber||'—'}</td></tr>`).join('')||'<tr><td colspan="4"><div class="empty-state"><p>No central banks</p></div></td></tr>';
}
function openModal(){document.getElementById('modal').style.display='flex';}
function closeModal(){document.getElementById('modal').style.display='none';}
async function addBank(){const name=document.getElementById('mName').value.trim(),branch=document.getElementById('mBranch').value.trim(),ifsc=document.getElementById('mIfsc').value.trim(),contact=document.getElementById('mContact').value.trim(),cId=document.getElementById('mCountry').value;if(!name){showToast('Bank name required','error');return;}try{await apiPost('/api/banks',{bankName:name,branchName:branch,ifscCode:ifsc,contactNumber:contact,countryId:cId||null});}catch(error){showToast(error.message,'error');return;}showToast('Bank added!');closeModal();loadBanks();}
async function deleteBank(id){if(!confirm('Delete?'))return;await apiDelete(`/api/banks/${id}`);showToast('Deleted');loadBanks();}
loadBanks();
