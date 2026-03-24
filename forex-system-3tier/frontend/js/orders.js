let products=[];
async function loadOrders(){
    const [orders, customers, loadedProducts, currencies] = await Promise.all([
        apiGet('/api/orders'),
        apiGet('/api/customers'),
        apiGet('/api/products'),
        apiGet('/api/currencies')
    ]);

    const customerMap = Object.fromEntries((customers || []).map(c => [c.customerId, c.name]));
    const productMap = Object.fromEntries((loadedProducts || []).map(p => [p.productId, p]));
    const sortedOrders = (orders || []).sort((a, b) => (b.orderId || 0) - (a.orderId || 0));

    products=loadedProducts||[];
    document.getElementById('subTitle').textContent=`${sortedOrders.length} import/export orders`;
    document.getElementById('mCustomer').innerHTML='<option value="">Select customer</option>'+(customers||[]).map(c=>`<option value="${c.customerId}">${c.name} (${c.customerType})</option>`).join('');
    document.getElementById('mProduct').innerHTML='<option value="">Select product</option>'+products.map(p=>`<option value="${p.productId}" data-price="${p.unitPrice}">${p.productName} — ₹${p.unitPrice}</option>`).join('');
    document.getElementById('mCurrency').innerHTML=(currencies||[]).map(c=>`<option value="${c.currencyCode}">${c.currencyCode}</option>`).join('');
    const tbody=document.getElementById('ordersTable');
    if(!sortedOrders.length){tbody.innerHTML='<tr><td colspan="10"><div class="empty-state"><div class="empty-icon">📦</div><p>No orders yet</p></div></td></tr>';return;}
    tbody.innerHTML=sortedOrders.map(o=>`<tr><td style="color:var(--text-muted)">#${o.orderId}</td><td style="font-weight:600">${customerMap[o.customerId] || '—'}</td><td>${productMap[o.productId]?.productName || '—'}</td><td>${getBadge(o.orderType)}</td><td>${o.quantity}</td><td style="color:var(--accent);font-weight:700">${parseFloat(o.totalAmount||0).toLocaleString()}</td><td>${getBadge(o.currencyCode||'USD')}</td><td>${getBadge(o.status)}</td><td style="color:var(--text-muted)">${o.orderDate}</td><td>${o.status==='PENDING'?`<button class="btn btn-secondary btn-sm" onclick="updateStatus(${o.orderId},'APPROVED')">✅</button><button class="btn btn-danger btn-sm" onclick="updateStatus(${o.orderId},'REJECTED')">❌</button>`:'—'}</td></tr>`).join('');
}
function updateTotal(){const s=document.getElementById('mProduct'),q=parseInt(document.getElementById('mQty').value)||0,p=parseFloat(s.options[s.selectedIndex]?.dataset?.price||0),t=p*q,cur=document.getElementById('mCurrency').value;if(t>0){document.getElementById('totalPreview').style.display='block';document.getElementById('totalAmount').textContent=`${cur} ${t.toLocaleString()}`;}else{document.getElementById('totalPreview').style.display='none';}}
function openModal(){document.getElementById('modal').style.display='flex';}
function closeModal(){document.getElementById('modal').style.display='none';}
async function placeOrder(){
    const cId=document.getElementById('mCustomer').value,pId=document.getElementById('mProduct').value,oType=document.getElementById('mOrderType').value,qty=parseInt(document.getElementById('mQty').value),cur=document.getElementById('mCurrency').value;
    if(!cId||!pId||!qty){showToast('Please fill all required fields','error');return;}
    try {
        await apiPost('/api/orders', {
            customerId: parseInt(cId, 10),
            productId: parseInt(pId, 10),
            orderType: oType,
            quantity: qty,
            currencyCode: cur
        });
    } catch (error) {
        showToast(error.message, 'error');
        return;
    }
    showToast('Order placed!');closeModal();loadOrders();
}
async function updateStatus(id,status){await apiPut(`/api/orders/${id}/status?status=${encodeURIComponent(status)}`);showToast(`Order ${status.toLowerCase()}`);loadOrders();}
loadOrders();
