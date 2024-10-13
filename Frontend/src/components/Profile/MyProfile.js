import React, { useState } from 'react';
import { useProfile } from '../../context/ProfileContext';
import PhoneInput from 'react-phone-input-2';
import 'react-phone-input-2/lib/style.css';
import './profile.css';

const MyProfile = () => {
  const { profileData, updateProfileData, updateAddressData, saveProfile } = useProfile();
  const [errors, setErrors] = useState({});

  const profileFields = [
    { label: 'First Name*', value: profileData.firstName, name: 'firstName', placeholder: 'Enter your first name', error: 'Please enter your first name.' },
    { label: 'Last Name*', value: profileData.lastName, name: 'lastName', placeholder: 'Enter your last name', error: 'Please enter your last name.' },
    { label: 'User Type*', value: profileData.userType, name: 'userType', placeholder: 'Enter your user type', error: 'Please enter your User Type.' },
  ];
  
  const addressFields = [
    { label: 'Street & House Number*', value: profileData.address.streetAddress, name: 'streetAddress', placeholder: 'Enter your street address', error: 'Please indicate your place of residence.' },
    { label: 'Postal Code*', value: profileData.address.postalCode, name: 'postalCode', placeholder: 'Enter your postal code', error: 'Please enter the zip code of your place.' },
    { label: 'City*', value: profileData.address.city, name: 'city', placeholder: 'Enter your city', error: 'Please indicate your City.' },
    { label: 'State*', value: profileData.address.state, name: 'state', placeholder: 'Enter your state', error: 'Please indicate your State.' },
    { label: 'Country*', value: profileData.address.country, name: 'country', placeholder: 'Enter your country', error: 'Please indicate your Country.' },
  ];

  const validateFields = () => {
    const newErrors = {};

    profileFields.forEach(field => {
      if (!field.value) {
        newErrors[field.name] = `${field.error}`;
      }
    });

    addressFields.forEach(field => {
      if (!field.value) {
        newErrors[field.name] = `${field.error}`;
      }
    });

    setErrors(newErrors);
    return Object.keys(newErrors).length === 0;
  };

  const handleSaveProfile = () => {
    if (validateFields()) {
      saveProfile();
    }
  };

  const renderInputField = (field, handleChange) => (
    <div className="input-field" key={field.label}>
      <p>{field.label}</p>
      <input
        type="text"
        name={field.name}
        placeholder={field.placeholder}
        className="input-border"
        value={field.value}
        onChange={(e) => handleChange(e.target.name, e.target.value)}
      />
      {errors[field.name] && <span className={`error-message ${errors ? 'show' : ''}`}>{errors[field.name]}</span>}
    </div>
  );

  return (
    <>
      <h2>Edit Profile</h2>
      <div className='profile-input'>
        {profileFields.map(field => renderInputField(field, updateProfileData))}
        <div className="input-field">
          <p>Contact Number:</p>
          <PhoneInput
            country={'us'}
            value={profileData.contactNumber}
            onChange={(phone) => updateProfileData('contactNumber', phone)}
            inputStyle={{
              width: '100%',
              padding: '10px',
              borderRadius: '4px',
              border: '1px solid #ccc',
              color: 'black',
              paddingLeft: '50px',
              height: '40px'
            }}
          />
        </div>
      </div>
      <div className="profile-address">
        <h4>Address</h4>
        <div className='profile-input'>
          {addressFields.map(field => renderInputField(field, updateAddressData))}
        </div>
        <div className='submit-profile-container'>
          <button className='btn' onClick={handleSaveProfile}>Apply Changes</button>
        </div>
      </div>
    </>
  );
};

export default MyProfile;
