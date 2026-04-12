const CONFIG = {
    // Backend URL - dynamically built based on environment
    API_BASE_URL: (() => {
        // Check if running on Netlify production
        if (window.location.hostname === 'ftms-frontend.netlify.app') {
            return 'https://ftms-backend.onrender.com';
        }
        // Check if we have an environment variable (for Netlify build)
        if (typeof process !== 'undefined' && process.env && process.env.REACT_APP_API_URL) {
            return process.env.REACT_APP_API_URL;
        }
        // Fallback: localhost development
        const backendPort = 8080;
        const host = window.location.hostname === 'localhost' ? 'localhost' : window.location.hostname;
        return `${window.location.protocol}//${host}:${backendPort}`;
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
