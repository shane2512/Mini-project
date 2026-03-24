let products=[];
async function loadOrders(){
    const [orders, customers, loadedProducts, currencies] = await Promise.all([
        supabaseTableSelect('orders'),
        supabaseTableSelect('customers'),
        supabaseTableSelect('products'),
        supabaseTableSelect('currencies')
    ]);

    const customerMap = Object.fromEntries((customers || []).map(c => [c.customer_id, c.name]));
    const productMap = Object.fromEntries((loadedProducts || []).map(p => [p.product_id, p]));
    const sortedOrders = (orders || []).sort((a, b) => (b.order_id || 0) - (a.order_id || 0));

    products=loadedProducts||[];
    document.getElementById('subTitle').textContent=`${sortedOrders.length} import/export orders`;
    document.getElementById('mCustomer').innerHTML='<option value="">Select customer</option>'+(customers||[]).map(c=>`<option value="${c.customer_id}">${c.name} (${c.customer_type})</option>`).join('');
    document.getElementById('mProduct').innerHTML='<option value="">Select product</option>'+products.map(p=>`<option value="${p.product_id}" data-price="${p.unit_price}">${p.product_name} — ₹${p.unit_price}</option>`).join('');
    document.getElementById('mCurrency').innerHTML=(currencies||[]).map(c=>`<option value="${c.currency_code}">${c.currency_code}</option>`).join('');
    const tbody=document.getElementById('ordersTable');
    if(!sortedOrders.length){tbody.innerHTML='<tr><td colspan="10"><div class="empty-state"><div class="empty-icon">📦</div><p>No orders yet</p></div></td></tr>';return;}
    tbody.innerHTML=sortedOrders.map(o=>`<tr><td style="color:var(--text-muted)">#${o.order_id}</td><td style="font-weight:600">${customerMap[o.customer_id] || '—'}</td><td>${productMap[o.product_id]?.product_name || '—'}</td><td>${getBadge(o.order_type)}</td><td>${o.quantity}</td><td style="color:var(--accent);font-weight:700">${parseFloat(o.total_amount||0).toLocaleString()}</td><td>${getBadge(o.currency_code||'USD')}</td><td>${getBadge(o.status)}</td><td style="color:var(--text-muted)">${o.order_date}</td><td>${o.status==='PENDING'?`<button class="btn btn-secondary btn-sm" onclick="updateStatus(${o.order_id},'APPROVED')">✅</button><button class="btn btn-danger btn-sm" onclick="updateStatus(${o.order_id},'REJECTED')">❌</button>`:'—'}</td></tr>`).join('');
}
function updateTotal(){const s=document.getElementById('mProduct'),q=parseInt(document.getElementById('mQty').value)||0,p=parseFloat(s.options[s.selectedIndex]?.dataset?.price||0),t=p*q,cur=document.getElementById('mCurrency').value;if(t>0){document.getElementById('totalPreview').style.display='block';document.getElementById('totalAmount').textContent=`${cur} ${t.toLocaleString()}`;}else{document.getElementById('totalPreview').style.display='none';}}
function openModal(){document.getElementById('modal').style.display='flex';}
function closeModal(){document.getElementById('modal').style.display='none';}
async function placeOrder(){
    const cId=document.getElementById('mCustomer').value,pId=document.getElementById('mProduct').value,oType=document.getElementById('mOrderType').value,qty=parseInt(document.getElementById('mQty').value),cur=document.getElementById('mCurrency').value;
    if(!cId||!pId||!qty){showToast('Please fill all required fields','error');return;}
    const selectedProduct = products.find(p => String(p.product_id) === String(pId));
    const unitPrice = parseFloat(selectedProduct?.unit_price || 0);
    const totalAmount = unitPrice * qty;
    try {
        await supabaseTableInsert('orders', {
            customer_id: parseInt(cId, 10),
            product_id: parseInt(pId, 10),
            order_type: oType,
            quantity: qty,
            total_amount: totalAmount,
            currency_code: cur,
            status: 'PENDING'
        });
    } catch (error) {
        showToast(error.message, 'error');
        return;
    }
    showToast('Order placed!');closeModal();loadOrders();
}
async function updateStatus(id,status){await supabaseTableUpdate('orders',`order_id=eq.${id}`,{status});showToast(`Order ${status.toLowerCase()}`);loadOrders();}
loadOrders();
