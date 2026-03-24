async function loadCountries(){
    const data=(await apiGet('/api/countries')) || [];
    data.sort((a, b) => (a.countryId || 0) - (b.countryId || 0));
    document.getElementById('subTitle').textContent=`${data.length} countries`;
    document.getElementById('countriesTable').innerHTML=data.map(c=>`<tr><td style="color:var(--text-muted)">#${c.countryId}</td><td style="font-weight:600">${c.countryName}</td><td><span class="badge badge-blue">${c.isoCode}</span></td><td><button class="btn btn-danger btn-sm" onclick="deleteCountry(${c.countryId})">🗑️</button></td></tr>`).join('')||'<tr><td colspan="4"><div class="empty-state"><p>No countries</p></div></td></tr>';
}
function openModal(){document.getElementById('modal').style.display='flex';}
function closeModal(){document.getElementById('modal').style.display='none';}
async function addCountry(){const name=document.getElementById('mName').value.trim(),iso=document.getElementById('mIso').value.trim().toUpperCase();if(!name||!iso){showToast('All fields required','error');return;}try{await apiPost('/api/countries',{countryName:name,isoCode:iso});}catch(error){showToast(error.message,'error');return;}showToast('Country added!');closeModal();loadCountries();}
async function deleteCountry(id){if(!confirm('Delete?'))return;await apiDelete(`/api/countries/${id}`);showToast('Deleted');loadCountries();}
loadCountries();
