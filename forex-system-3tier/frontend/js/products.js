async function loadProducts(){
    const data=(await supabaseTableSelect('products')) || [];
    data.sort((a, b) => (a.product_id || 0) - (b.product_id || 0));
    document.getElementById('subTitle').textContent=`${data.length} products`;
    const tbody=document.getElementById('productsTable');
    if(!data?.length){tbody.innerHTML='<tr><td colspan="5"><div class="empty-state"><div class="empty-icon">🏭</div><p>No products</p></div></td></tr>';return;}
    tbody.innerHTML=data.map(p=>`<tr><td style="color:var(--text-muted)">#${p.product_id}</td><td style="font-weight:600">${p.product_name}</td><td><code style="background:var(--bg-card2);padding:2px 8px;border-radius:4px;font-size:12px">${p.hs_code||'—'}</code></td><td style="color:var(--accent);font-weight:700">₹${parseFloat(p.unit_price).toLocaleString()}</td><td><button class="btn btn-danger btn-sm" onclick="deleteProduct(${p.product_id})">🗑️</button></td></tr>`).join('');
}
function openModal(){document.getElementById('modal').style.display='flex';}
function closeModal(){document.getElementById('modal').style.display='none';}
async function addProduct(){const name=document.getElementById('mName').value.trim(),hs=document.getElementById('mHs').value.trim(),price=parseFloat(document.getElementById('mPrice').value);if(!name||!price){showToast('Name and price required','error');return;}try{await supabaseTableInsert('products',{product_name:name,hs_code:hs,unit_price:price});}catch(error){showToast(error.message,'error');return;}showToast('Product added!');closeModal();loadProducts();}
async function deleteProduct(id){if(!confirm('Delete?'))return;await supabaseTableDelete('products',`product_id=eq.${id}`);showToast('Deleted');loadProducts();}
loadProducts();
