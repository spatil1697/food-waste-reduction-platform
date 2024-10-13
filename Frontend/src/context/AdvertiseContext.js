import React, { createContext, useState, useContext, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import AuthContext from './AuthContext';

const AdvertiseContext = createContext();

export const AdvertiseProvider = ({ children }) => {
  const { user } = useContext(AuthContext);
  const navigate = useNavigate();

  const [advertise, setAdvertise] = useState({
    foodItem: '',
    description: '',
    pickupLocation: '',
    quantity: 1,
    expiryDate: new Date(),
    listingStatus: '',
    description:'',
    image:''
  });

  const updateAdvertiseData = (name, value) => {
    setAdvertise({
      ...advertise,
      [name]: value,
    });
  };

  const saveAdvertiseData = async () => {
    try {
      if (user) {
        const dataToSubmit = {
          ...advertise,
          expiryDate: advertise.expiryDate.toISOString(),
          
        };

        const response = await fetch(`http://localhost:8080/food/${user.userID}`, {
          method: 'POST',
          body: dataToSubmit,
        });

        if (!response.ok) {
          throw new Error('Network response was not ok');
        }

        const result = await response.json();
        console.log('Submission successful', result);
        navigate('/'); // Redirect to a success page or another route after submission
      } else {
        throw new Error('User information not available.');
      }
    } catch (error) {
      console.error('There was a problem with the submission', error);
    }
  };

  return (
    <AdvertiseContext.Provider
      value={{
        advertise,
        updateAdvertiseData,
        saveAdvertiseData,
      }}
    >
      {children}
    </AdvertiseContext.Provider>
  );
};

export const useAdvertise = () => useContext(AdvertiseContext);
