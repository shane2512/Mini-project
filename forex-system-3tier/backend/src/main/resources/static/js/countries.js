async function loadCountries(){
    const{data}=await db.from('countries').select('*').order('country_id');
    document.getElementById('subTitle').textContent=`${(data||[]).length} countries`;
    document.getElementById('countriesTable').innerHTML=(data||[]).map(c=>`<tr><td style="color:var(--text-muted)">#${c.country_id}</td><td style="font-weight:600">${c.country_name}</td><td><span class="badge badge-blue">${c.iso_code}</span></td><td><button class="btn btn-danger btn-sm" onclick="deleteCountry(${c.country_id})">🗑️</button></td></tr>`).join('')||'<tr><td colspan="4"><div class="empty-state"><p>No countries</p></div></td></tr>';
}
function openModal(){document.getElementById('modal').style.display='flex';}
function closeModal(){document.getElementById('modal').style.display='none';}
async function addCountry(){const name=document.getElementById('mName').value.trim(),iso=document.getElementById('mIso').value.trim().toUpperCase();if(!name||!iso){showToast('All fields required','error');return;}const{error}=await db.from('countries').insert([{country_name:name,iso_code:iso}]);if(error){showToast(error.message,'error');return;}showToast('Country added!');closeModal();loadCountries();}
async function deleteCountry(id){if(!confirm('Delete?'))return;await db.from('countries').delete().eq('country_id',id);showToast('Deleted');loadCountries();}
loadCountries();
