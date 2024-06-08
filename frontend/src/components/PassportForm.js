import React, { useState } from 'react';
import axios from '../axiosConfig';
import { useNavigate } from 'react-router-dom';
import Logo from './Logo';

function PassportForm() {
  const [passport, setPassport] = useState({
    lastName: '',
    firstName: '',
    middleName: '',
    gender: '',
    dateOfBirth: '',
    placeOfBirth: '',
    passportSeries: '',
    passportNumber: '',
    issueDate: '',
    issuedBy: '',
    departmentCode: '',
    registrationPlace: '',
    residencePlace: '',
  });

  const navigate = useNavigate();

  const handleChange = (e) => {
    const { name, value } = e.target;
    setPassport({ ...passport, [name]: value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const token = localStorage.getItem('token');
      await axios.post('/api/v1/passport', passport, {
        headers: { Authorization: `Bearer ${token}` },
      });
      alert('Паспортные данные успешно сохранены');
      navigate('/home');
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
      <h2>Паспортные данные</h2>
      <form onSubmit={handleSubmit}>
        {Object.keys(passport).map((key) => (
          <div key={key}>
            <label>{key.replace(/([A-Z])/g, ' $1').toUpperCase()}:</label>
            <input type="text" name={key} value={passport[key]} onChange={handleChange} />
          </div>
        ))}
        <button type="submit">Сохранить</button>
      </form>
    </div>
  );
}

export default PassportForm;