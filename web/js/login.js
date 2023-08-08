const loginForm = document.getElementById('login-form');

loginForm.addEventListener('submit', async (event) => {
    event.preventDefault();

    const email = document.getElementById('email').value;
    const password = document.getElementById('password').value;

    const loginData = {
        username: email,
        password: password
    };

  try {
      const response = await fetch('http://3.21.168.204:8000/token', {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json'
          },
          body: JSON.stringify(loginData)
      });

      if (!response.ok) {
          throw new Error('Login failed');
      }

      const data = await response.json();
      const token = data.access_token;
      const uId = data.u_id;
      const name = data.name;

      // Store the token, u_id, and name in cookies
      const expires = new Date(Date.now() + 3600000); // Set expiration time (1 hour)
      document.cookie = `jwtToken=${token}; expires=${expires.toUTCString()}; path=/`;
      document.cookie = `uId=${uId}; expires=${expires.toUTCString()}; path=/`;
      document.cookie = `name=${name}; expires=${expires.toUTCString()}; path=/`;

      // Redirect or navigate to another screen
      window.location.href = '/dashboard'; // Change to your desired URL
    }
    catch (error) {
        console.error('Login failed:', error);
    }
});