import requests
import json

print("Testing FTMS Backend Endpoints...\n")

# Base URL
BASE_URL = "http://localhost:8080"

try:
    # Test 1: Get rates
    print("1️⃣ Testing /api/forex/rates")
    response = requests.get(f"{BASE_URL}/api/forex/rates", timeout=10)
    print(f"   Status: {response.status_code}")
    if response.status_code == 200:
        data = response.json()
        if "rates" in data:
            rates = data.get("rates", {})
            print(f"   ✅ Rates received: {len(rates)} currencies")
            # Show first few
            currencies = list(rates.keys())[:5]
            for curr in currencies:
                print(f"      {curr}: {rates[curr]}")
        else:
            print(f"   ⚠️ No 'rates' key in response. Keys: {list(data.keys())}")
            print(f"   Response: {json.dumps(data, indent=2)}")
    else:
        print(f"   ❌ Error: {response.text}")
    
    # Test 2: Convert bridge
    print("\n2️⃣ Testing /api/forex/convert-bridge")
    params = {
        "fromCurrency": "EUR",
        "toCurrency": "INR",
        "amount": "100"
    }
    response = requests.get(f"{BASE_URL}/api/forex/convert-bridge", params=params, timeout=10)
    print(f"   Status: {response.status_code}")
    if response.status_code == 200:
        data = response.json()
        if data.get("success"):
            conversion = data.get("data", {})
            print(f"   ✅ Conversion successful")
            print(f"      100 EUR = {conversion.get('toAmount')} {conversion.get('toCurrency')}")
            print(f"      Rate: {conversion.get('rate')}")
        else:
            print(f"   ❌ API returned: {data}")
    else:
        print(f"   ❌ Error: {response.text}")
        
except requests.exceptions.ConnectionError:
    print("❌ Cannot connect to backend at http://localhost:8080")
    print("   Make sure the backend is running!")
except Exception as e:
    print(f"❌ Error: {e}")
