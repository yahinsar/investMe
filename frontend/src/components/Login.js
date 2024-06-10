import React, { useState } from 'react';
import axios from '../axiosConfig';
import { useNavigate } from 'react-router-dom';
import './Login.css';
import Logo from './Logo';

function Login() {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const navigate = useNavigate();

  const handleSubmit = async (event) => {
    event.preventDefault();
    try {
      const response = await axios.post('/api/v1/authenticate', { username, password });
      const token = response.data.jwtToken;
      localStorage.setItem('token', token);

      const userResponse = await axios.get(`/api/v1/users/isEnabled?username=${username}`);
      const isEnabled = userResponse.data;

      if (!isEnabled) {
        alert('Пользователь не активирован');
        return;
      }

      const passportResponse = await axios.get('/api/v1/passport/user', {
        headers: { Authorization: `Bearer ${token}` },
      });

      if (passportResponse.data) {
        navigate('/home');
      } else {
        navigate('/passport');
      }
    } catch (error) {
        if (error.response && error.response.data) {
            alert(error.response.data);
        } else {
            alert('Произошла непредвиденная ошибка.');
        }
    }
  };

  return (
    <div>
      <Logo />
      <div className="login-container">
        <Logo />
        <h2>Вход</h2>
        <form onSubmit={handleSubmit}>
          <div>
            <label>Логин:</label>
            <input type="text" value={username} onChange={(e) => setUsername(e.target.value)} />
          </div>
          <div>
            <label>Пароль:</label>
            <input type="password" value={password} onChange={(e) => setPassword(e.target.value)} />
          </div>
          <button type="submit">Войти</button>
        </form>
      </div>
    </div>
  );
}

export default Login;