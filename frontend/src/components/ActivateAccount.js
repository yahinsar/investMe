import React, { useEffect } from 'react';
import axios from '../axiosConfig';
import { useSearchParams, useNavigate } from 'react-router-dom';

function ActivateAccount() {
  const [searchParams] = useSearchParams();
  const navigate = useNavigate();
  const token = searchParams.get('token');

  useEffect(() => {
    const activateAccount = async () => {
      try {
        await axios.get(`/api/v1/registration/activate?token=${token}`);
        alert('Аккаунт успешно активирован.');
        navigate('/');
      } catch (error) {
          if (error.response && error.response.data) {
                alert(error.response.data);
          } else {
                alert('Произошла непредвиденная ошибка.');
          }
      }
    };

    activateAccount();
  }, [token, navigate]);

  return <div>Активация аккаунта...</div>;
}

export default ActivateAccount;