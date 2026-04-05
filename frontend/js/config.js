const CONFIG = {
    // Dynamically set API URL based on environment
    API_BASE_URL: window.location.hostname === 'localhost' 
        ? 'http://localhost:8080'
        : window.location.hostname === '127.0.0.1'
        ? 'http://127.0.0.1:8080'
        : 'https://ftms-backend.onrender.com', // Production Render backend
    EXCHANGE_API_KEY: '6bf11c4b17daa3cc2ccfc96c',  // from exchangerate-api.com
    EXCHANGE_API_URL: 'https://v6.exchangerate-api.com/v6',
    CURRENCIES: ['USD', 'EUR', 'GBP', 'JPY', 'AUD', 'CAD', 'CHF', 'INR', 'SGD', 'HKD', 'CNY', 'AED']
};