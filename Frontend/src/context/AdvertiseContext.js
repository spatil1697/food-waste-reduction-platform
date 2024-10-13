import React, { createContext, useState, useContext, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import AuthContext from './AuthContext';

const AdvertiseContext = createContext();

export const AdvertiseProvider = ({ children }) => {
  const { user } = useContext(AuthContext);
  const navigate = useNavigate();

  const [advertise, setAdvertise] = useState({
    description:'',
    foodItem: '',
    description: '',
    quantity: 1,
    expiryDate: new Date(),
    pickupLocation: '',
    listingStatus: '',
  });

  const updateAdvertiseData = (name, value) => {
    setAdvertise({
      ...advertise,
      [name]: value,
    });
  };

  const saveAdvertiseData = async (fileUploadRef) => {
    try {
      if (!user) throw new Error('User information not available.');
  
      const formData = new FormData();
      const { image, ...advertiseData } = advertise;

      advertiseData.expiryDate = advertise.expiryDate.toISOString();
      formData.append("foodRequestDTO", JSON.stringify(advertiseData));

      const uploadedFile = fileUploadRef.current?.files[0];
      if (uploadedFile) {
        formData.append("image", uploadedFile);
      } else {
        console.warn('No image uploaded.');
      }
  
      // Make the POST request
      const response = await fetch(`http://localhost:8080/food/${user.userID}`, {
        method: 'POST',
        body: formData, // Content-Type will be automatically set by FormData
      });
  
      if (!response.ok) {
        throw new Error('Network response was not ok');
      }
  
      const result = await response.json();
      console.log('Submission successful', result);
      navigate('/'); // Redirect after successful submission
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
