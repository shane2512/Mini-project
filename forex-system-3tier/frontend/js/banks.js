async function loadBanks(){
    const [banks, centralBanks, countries] = await Promise.all([
        supabaseTableSelect('banks'),
        supabaseTableSelect('country_banks'),
        supabaseTableSelect('countries')
    ]);

    const sortedBanks = (banks || []).sort((a, b) => (a.bank_id || 0) - (b.bank_id || 0));
    const sortedCentralBanks = (centralBanks || []).sort((a, b) => (a.country_bank_id || 0) - (b.country_bank_id || 0));

    document.getElementById('mCountry').innerHTML='<option value="">Select country</option>'+(countries||[]).map(c=>`<option value="${c.country_id}">${c.country_name}</option>`).join('');
    document.getElementById('banksTable').innerHTML=sortedBanks.map(b=>`<tr><td style="color:var(--text-muted)">#${b.bank_id}</td><td style="font-weight:600">${b.bank_name}</td><td>${b.branch_name||'—'}</td><td><code style="background:var(--bg-card2);padding:2px 8px;border-radius:4px;font-size:12px">${b.ifsc_code||'—'}</code></td><td>${b.contact_number||'—'}</td><td><button class="btn btn-danger btn-sm" onclick="deleteBank(${b.bank_id})">🗑️</button></td></tr>`).join('')||'<tr><td colspan="6"><div class="empty-state"><p>No banks</p></div></td></tr>';
    document.getElementById('centralBanksTable').innerHTML=sortedCentralBanks.map(b=>`<tr><td style="color:var(--text-muted)">#${b.country_bank_id}</td><td style="font-weight:600">${b.bank_name}</td><td><span class="badge badge-blue">${b.swift_code||'—'}</span></td><td>${b.contact_number||'—'}</td></tr>`).join('')||'<tr><td colspan="4"><div class="empty-state"><p>No central banks</p></div></td></tr>';
}
function openModal(){document.getElementById('modal').style.display='flex';}
function closeModal(){document.getElementById('modal').style.display='none';}
async function addBank(){const name=document.getElementById('mName').value.trim(),branch=document.getElementById('mBranch').value.trim(),ifsc=document.getElementById('mIfsc').value.trim(),contact=document.getElementById('mContact').value.trim(),cId=document.getElementById('mCountry').value;if(!name){showToast('Bank name required','error');return;}try{await supabaseTableInsert('banks',{bank_name:name,branch_name:branch,ifsc_code:ifsc,contact_number:contact,country_id:cId||null});}catch(error){showToast(error.message,'error');return;}showToast('Bank added!');closeModal();loadBanks();}
async function deleteBank(id){if(!confirm('Delete?'))return;await supabaseTableDelete('banks',`bank_id=eq.${id}`);showToast('Deleted');loadBanks();}
loadBanks();
