import React, { useState, useEffect } from 'react';
import axios from '../axiosConfig';
import { useLocation, useNavigate } from 'react-router-dom';
import './UpdatePassport.css';
import Logo from './Logo';

function UpdatePassport() {
  const location = useLocation();
  const navigate = useNavigate();
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
    user: {}
  });

  useEffect(() => {
    if (location.state && location.state.passport) {
      setPassport(location.state.passport);
    }
  }, [location.state]);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setPassport({ ...passport, [name]: value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const token = localStorage.getItem('token');
      await axios.put(`/api/v1/passport/${passport.id}`, passport, {
        headers: { Authorization: `Bearer ${token}` },
      });
      alert('Паспортные данные успешно обновлены.');
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
        <div className="passportForm-container">
          <Logo />
          <h2>Паспортные данные</h2>
          <form onSubmit={handleSubmit}>
            <div>
              <label>Фамилия:</label>
              <input type="text" name="lastName" value={passport.lastName} onChange={handleChange} />
            </div>
            <div>
              <label>Имя:</label>
              <input type="text" name="firstName" value={passport.firstName} onChange={handleChange} />
            </div>
            <div>
              <label>Отчество:</label>
              <input type="text" name="middleName" value={passport.middleName} onChange={handleChange} />
            </div>
            <div>
              <label>Пол:</label>
              <input type="text" name="gender" value={passport.gender} onChange={handleChange} />
            </div>
            <div>
              <label>Дата рождения:</label>
              <input type="text" name="dateOfBirth" value={passport.dateOfBirth} onChange={handleChange} />
            </div>
            <div>
              <label>Место рождения:</label>
              <input type="text" name="placeOfBirth" value={passport.placeOfBirth} onChange={handleChange} />
            </div>
            <div>
              <label>Серия паспорта:</label>
              <input type="text" name="passportSeries" value={passport.passportSeries} onChange={handleChange} />
            </div>
            <div>
              <label>Номер паспорта:</label>
              <input type="text" name="passportNumber" value={passport.passportNumber} onChange={handleChange} />
            </div>
            <div>
              <label>Дата выдачи:</label>
              <input type="text" name="issueDate" value={passport.issueDate} onChange={handleChange} />
            </div>
            <div>
              <label>Кем выдан:</label>
              <input type="text" name="issuedBy" value={passport.issuedBy} onChange={handleChange} />
            </div>
            <div>
              <label>Код подразделения:</label>
              <input type="text" name="departmentCode" value={passport.departmentCode} onChange={handleChange} />
            </div>
            <div>
              <label>Место регистрации:</label>
              <input type="text" name="registrationPlace" value={passport.registrationPlace} onChange={handleChange} />
            </div>
            <div>
              <label>Место проживания:</label>
              <input type="text" name="residencePlace" value={passport.residencePlace} onChange={handleChange} />
            </div>
            <button type="submit">Сохранить</button>
          </form>
        </div>
      </div>
    );
  }

export default UpdatePassport;