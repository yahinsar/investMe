import React from 'react';
import { useNavigate } from 'react-router-dom';
import logo from './images/logo.png';
import './Logo.css';

function Header() {
  const navigate = useNavigate();

  const handleLogoClick = () => {
    navigate('/');
  };

  return (
    <header className="header">
      <img
        src={logo}
        alt="Logo"
        className="logo"
        onClick={handleLogoClick}
      />
    </header>
  );
}

export default Header;