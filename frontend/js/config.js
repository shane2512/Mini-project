const CONFIG = {
    // Backend URL - dynamically built based on environment
    API_BASE_URL: (() => {
        // Check if running on Netlify production
        if (window.location.hostname.includes('netlify.app')) {
            return 'https://mini-project-059o.onrender.com';
        }
        // Fallback: localhost development
        if (window.location.hostname === 'localhost' || window.location.hostname === '127.0.0.1') {
            return 'http://localhost:8080';
        }
        // Default fallback
        return 'https://mini-project-059o.onrender.com';
    })(),
    
    EXCHANGE_API_KEY: '6bf11c4b17daa3cc2ccfc96c',  // from exchangerate-api.com
    EXCHANGE_API_URL: 'https://v6.exchangerate-api.com/v6',
    CURRENCIES: ['USD', 'EUR', 'GBP', 'JPY', 'AUD', 'CAD', 'CHF', 'INR', 'SGD', 'HKD', 'CNY', 'AED'],
    
    // USDC Bridge Currency Configuration
    BRIDGE_CURRENCY: 'USDC',
    USE_BRIDGE_CONVERSION: true
};

console.log('CONFIG.API_BASE_URL =', CONFIG.API_BASE_URL);
console.log('Bridge Currency (Real-world forex) =', CONFIG.BRIDGE_CURRENCY);
