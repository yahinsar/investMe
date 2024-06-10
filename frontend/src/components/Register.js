import React, { useState } from 'react';
import axios from '../axiosConfig';
import { useNavigate } from 'react-router-dom';
import './Register.css';
import Logo from './Logo';

function Register() {
  const [username, setUsername] = useState('');
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const navigate = useNavigate();

  const handleSubmit = async (event) => {
    event.preventDefault();
    try {
      await axios.post('/api/v1/registration', { username, email, password });
      alert('Пользователь зарегистрирован, необходимо активировать учётную запись, нажав на ссылку в письме.');
      navigate('/');
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
      <div className="register-container">
      <Logo />
      <h2>Регистрация</h2>

      <form onSubmit={handleSubmit}>
        <div>
          <label>Логин:</label>
          <input type="text" value={username} onChange={(e) => setUsername(e.target.value)} />
        </div>
        <div>
          <label>Email:</label>
          <input type="email" value={email} onChange={(e) => setEmail(e.target.value)} />
        </div>
        <div>
          <label>Пароль:</label>
          <input type="password" value={password} onChange={(e) => setPassword(e.target.value)} />
        </div>
        <button type="submit">Зарегистрироваться</button>
      </form>
          </div>
    </div>
  );
}

export default Register;