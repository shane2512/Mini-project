// API Interceptor for handling global responses
// Wraps all fetch calls to automatically handle 401 Unauthorized responses (session expired)

const APIClient = {
  /**
   * Fetch wrapper with automatic 401 handling
   * If any API call returns 401, it clears the JWT token and redirects to login
   */
  fetch: async function(url, options = {}) {
    try {
      const response = await fetch(url, options);
      
      // Handle 401 Unauthorized - session expired
      if (response.status === 401) {
        console.warn('⏱️  Session expired (401) - clearing token and redirecting to login');
        // Clear stored session data
        localStorage.removeItem('token');
        localStorage.removeItem('role');
        localStorage.removeItem('userId');
        localStorage.removeItem('fullName');
        localStorage.removeItem('kycStatus');
        localStorage.removeItem('accountStatus');
        
        // Redirect to login with session_expired parameter
        window.location.href = 'login.html?reason=session_expired';
        return null;
      }
      
      return response;
    } catch (error) {
      console.error('Fetch error:', error);
      throw error;
    }
  }
};

// Override global fetch if needed - but we'll use APIClient.fetch() explicitly instead
// This is cleaner and doesn't affect existing code
