import React, { useState, useEffect } from 'react';
import axios from '../axiosConfig';
import Logo from './Logo';
import './Home.css';
import { useNavigate } from 'react-router-dom';

function Home() {
  const [passport, setPassport] = useState(null);
  const [welcomeMessage, setWelcomeMessage] = useState('');
  const navigate = useNavigate();

  useEffect(() => {
    const fetchPassport = async () => {
      try {
        const token = localStorage.getItem('token');
        const response = await axios.get('/api/v1/passport/user', {
          headers: { Authorization: `Bearer ${token}` },
        });
        setPassport(response.data);

        const welcomeResponse = await axios.get('/api/v1/passport/welcome-message', {
          headers: { Authorization: `Bearer ${token}` },
        });
        setWelcomeMessage(welcomeResponse.data);
      } catch (error) {
        alert('Произошла непредвиденная ошибка.');
      }
    };

    fetchPassport();
  }, []);

  const handleUpdateClick = () => {
    navigate('/update', { state: { passport } });
  };

  if (!passport) {
    return <div>Загрузка...</div>;
  }

  return (
    <div>
      <Logo />
      <div className="home-container">
        <div className="welcome-message-container">
          <h1 className="welcome-message">{welcomeMessage}</h1>
        </div>
        <div className="passport-info">
          <h2>Данные паспорта</h2>
          <p><strong>Фамилия:</strong> {passport.lastName}</p>
          <p><strong>Имя:</strong> {passport.firstName}</p>
          <p><strong>Отчество:</strong> {passport.middleName}</p>
          <p><strong>Пол:</strong> {passport.gender}</p>
          <p><strong>Дата рождения:</strong> {passport.dateOfBirth}</p>
          <p><strong>Место рождения:</strong> {passport.placeOfBirth}</p>
          <p><strong>Серия паспорта:</strong> {passport.passportSeries}</p>
          <p><strong>Номер паспорта:</strong> {passport.passportNumber}</p>
          <p><strong>Дата выдачи:</strong> {passport.issueDate}</p>
          <p><strong>Кем выдан:</strong> {passport.issuedBy}</p>
          <p><strong>Код подразделения:</strong> {passport.departmentCode}</p>
          <p><strong>Место регистрации:</strong> {passport.registrationPlace}</p>
          <p><strong>Место проживания:</strong> {passport.residencePlace}</p>
          <button onClick={handleUpdateClick}>Обновить информацию</button>
        </div>
      </div>
    </div>
  );
}

export default Home;
