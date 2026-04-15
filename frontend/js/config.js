const CONFIG = {
    API_BASE_URL: (() => {
        // Production (Netlify)
        if (window.location.hostname.includes('netlify.app')) {
            return 'https://mini-project-pnj7.onrender.com';
        }

        // Local development
        if (window.location.hostname === 'localhost') {
            return 'http://localhost:8080';
        }

        // Default fallback
        return 'https://mini-project-pnj7.onrender.com';
    })(),

    EXCHANGE_API_KEY: '6bf11c4b17daa3cc2ccfc96c',
    EXCHANGE_API_URL: 'https://v6.exchangerate-api.com/v6',
    CURRENCIES: ['USD', 'EUR', 'GBP', 'JPY', 'AUD', 'CAD', 'CHF', 'INR', 'SGD', 'HKD', 'CNY', 'AED'],

    BRIDGE_CURRENCY: 'USD',
    USE_BRIDGE_CONVERSION: true
};

console.log('CONFIG.API_BASE_URL =', CONFIG.API_BASE_URL);