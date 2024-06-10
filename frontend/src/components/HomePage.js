import React from 'react';
import { useNavigate } from 'react-router-dom';
import './HomePage.css';
import Logo from './Logo';

function HomePage() {
  const navigate = useNavigate();

  const handleLoginClick = () => {
    navigate('/login');
  };

  const handleRegisterClick = () => {
    navigate('/register');
  };

  return (
    <div>
      <Logo />
      <div className="home-container">
        <button onClick={handleLoginClick} className="home-button">Вход</button>
        <button onClick={handleRegisterClick} className="home-button">Регистрация</button>
      </div>
    </div>
  );
}

export default HomePage;