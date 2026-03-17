let products=[];
async function loadOrders(){
    const[oR,cR,pR,curR]=await Promise.all([db.from('orders').select('*,customers(name),products(product_name,unit_price)').order('order_id',{ascending:false}),db.from('customers').select('*'),db.from('products').select('*'),db.from('currencies').select('*')]);
    products=pR.data||[];const orders=oR.data||[];
    document.getElementById('subTitle').textContent=`${orders.length} import/export orders`;
    document.getElementById('mCustomer').innerHTML='<option value="">Select customer</option>'+(cR.data||[]).map(c=>`<option value="${c.customer_id}">${c.name} (${c.customer_type})</option>`).join('');
    document.getElementById('mProduct').innerHTML='<option value="">Select product</option>'+products.map(p=>`<option value="${p.product_id}" data-price="${p.unit_price}">${p.product_name} — ₹${p.unit_price}</option>`).join('');
    document.getElementById('mCurrency').innerHTML=(curR.data||[]).map(c=>`<option value="${c.currency_code}">${c.currency_code}</option>`).join('');
    const tbody=document.getElementById('ordersTable');
    if(!orders.length){tbody.innerHTML='<tr><td colspan="10"><div class="empty-state"><div class="empty-icon">📦</div><p>No orders yet</p></div></td></tr>';return;}
    tbody.innerHTML=orders.map(o=>`<tr><td style="color:var(--text-muted)">#${o.order_id}</td><td style="font-weight:600">${o.customers?.name||'—'}</td><td>${o.products?.product_name||'—'}</td><td>${getBadge(o.order_type)}</td><td>${o.quantity}</td><td style="color:var(--accent);font-weight:700">${parseFloat(o.total_amount||0).toLocaleString()}</td><td>${getBadge(o.currency_code||'USD')}</td><td>${getBadge(o.status)}</td><td style="color:var(--text-muted)">${o.order_date}</td><td>${o.status==='PENDING'?`<button class="btn btn-secondary btn-sm" onclick="updateStatus(${o.order_id},'APPROVED')">✅</button><button class="btn btn-danger btn-sm" onclick="updateStatus(${o.order_id},'REJECTED')">❌</button>`:'—'}</td></tr>`).join('');
}
function updateTotal(){const s=document.getElementById('mProduct'),q=parseInt(document.getElementById('mQty').value)||0,p=parseFloat(s.options[s.selectedIndex]?.dataset?.price||0),t=p*q,cur=document.getElementById('mCurrency').value;if(t>0){document.getElementById('totalPreview').style.display='block';document.getElementById('totalAmount').textContent=`${cur} ${t.toLocaleString()}`;}else{document.getElementById('totalPreview').style.display='none';}}
function openModal(){document.getElementById('modal').style.display='flex';}
function closeModal(){document.getElementById('modal').style.display='none';}
async function placeOrder(){
    const cId=document.getElementById('mCustomer').value,pId=document.getElementById('mProduct').value,oType=document.getElementById('mOrderType').value,qty=parseInt(document.getElementById('mQty').value),cur=document.getElementById('mCurrency').value;
    if(!cId||!pId||!qty){showToast('Please fill all required fields','error');return;}
    const prod=products.find(p=>p.product_id==pId),total=prod.unit_price*qty;
    const{data:od,error}=await db.from('orders').insert([{customer_id:parseInt(cId),product_id:parseInt(pId),order_type:oType,quantity:qty,total_amount:total,currency_code:cur,status:'PENDING',order_date:new Date().toISOString().split('T')[0]}]).select();
    if(error){showToast(error.message,'error');return;}
    await db.from('invoices').insert([{customer_id:parseInt(cId),ref_type:'ORDER',ref_id:od[0].order_id,amount:total,currency_code:cur,status:'PENDING',invoice_date:new Date().toISOString().split('T')[0]}]);
    showToast('Order placed!');closeModal();loadOrders();
}
async function updateStatus(id,status){await db.from('orders').update({status}).eq('order_id',id);showToast(`Order ${status.toLowerCase()}`);loadOrders();}
loadOrders();
