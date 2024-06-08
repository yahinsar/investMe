import React, { useState, useEffect } from 'react';
import axios from '../axiosConfig';
import Logo from './Logo';

function Home() {
  const [passport, setPassport] = useState(null);
  const [welcomeMessage, setWelcomeMessage] = useState('');

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
          if (error.response && error.response.data) {
              alert(error.response.data);
          } else {
              alert('Произошла непредвиденная ошибка.');
          }
      }
    };

    fetchPassport();
  }, []);

  if (!passport) {
    return <div>Загрузка...</div>;
  }

  return (
    <div>
      <Logo />
      <h1 style={{ fontWeight: 'bold' }}>{welcomeMessage}</h1>
      <h2>Passport Information</h2>
      <p><strong>Last Name:</strong> {passport.lastName}</p>
      <p><strong>First Name:</strong> {passport.firstName}</p>
      <p><strong>Middle Name:</strong> {passport.middleName}</p>
      <p><strong>Gender:</strong> {passport.gender}</p>
      <p><strong>Date of Birth:</strong> {passport.dateOfBirth}</p>
      <p><strong>Place of Birth:</strong> {passport.placeOfBirth}</p>
      <p><strong>Passport Series:</strong> {passport.passportSeries}</p>
      <p><strong>Passport Number:</strong> {passport.passportNumber}</p>
      <p><strong>Issue Date:</strong> {passport.issueDate}</p>
      <p><strong>Issued By:</strong> {passport.issuedBy}</p>
      <p><strong>Department Code:</strong> {passport.departmentCode}</p>
      <p><strong>Registration Place:</strong> {passport.registrationPlace}</p>
      <p><strong>Residence Place:</strong> {passport.residencePlace}</p>
    </div>
  );
}

export default Home;
