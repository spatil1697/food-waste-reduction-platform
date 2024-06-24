import React, { createContext, useState, useContext, useEffect } from 'react';
import AuthContext from './AuthContext';

const ProfileContext = createContext();

export const ProfileProvider = ({children})=> {
    const {user} = useContext(AuthContext);
    const [profileData, setProfileData] = useState({
        firstName: '',
        lastName: '',
        userType: '',
        contactNumber: '',
        address: {
          streetAddress: '',
          postalCode: '',
          city: '',
          state: '',
          country: ''
        }
    });

    useEffect(() => {
        if (user) {
          setProfileData({
            firstName: user.firstName || '',
            lastName: user.lastName || '',
            userType: user.userType || '',
            contactNumber: user.contactNumber || '',
            address: user.address || {
              streetAddress: '',
              postalCode: '',
              city: '',
              state: '',
              country: ''
            }
          });
        }
    }, [user]);

    const updateProfileData = (field, value) => {
        setProfileData((prevProfileData) => ({
          ...prevProfileData,
          [field]: value,
        }));
    };
    
    const updateAddressData = (field, value) => {
        setProfileData((prevProfileData) => ({
          ...prevProfileData,
          address: {
            ...prevProfileData.address,
            [field]: value,
          },
        }));
    };

    const saveProfile = async () => {
        try {
          if (user) {
            const response = await fetch(`http://localhost:8080/user/${user.userID}`, {
              method: 'PUT',
              headers: {
                'Content-Type': 'application/json'
              },
              body: JSON.stringify(profileData)
            });
      
            if (!response.ok) {
              throw new Error(`HTTP error! Status: ${response.status}`);
            }
      
            const data = await response.json();
            console.log('Profile updated successfully');
            window.location.reload();
          } else {
            throw new Error('User information not available.');
          }
        } catch (error) {
          console.error('Error updating profile:', error);
        }
      };
      
      const validateProfileData = (data) => {
        if (!data || !data.address) {
            return false;
        }
        return (
            data.address.streetAddress.trim() !== '' &&
            data.address.postalCode.trim() !== '' &&
            data.address.city.trim() !== '' &&
            data.address.state.trim() !== '' &&
            data.address.country.trim() !== ''
        );
    };

    useEffect(() => {
        const fetchProfileData = async () => {
          try {
            if(user){
            const response = await fetch(`http://localhost:8080/user/${user.userID}`);
            if (!response.ok) {
              throw new Error('Failed to fetch profile data');
            }
            const data = await response.json();
            if (validateProfileData(data)) {
              setProfileData(data);
            }
          }
          } catch (error) {
            console.error('Error fetching profile data:', error);
          }
        };
    
        fetchProfileData();
      }, [user]);

    return (
        <ProfileContext.Provider value={{ profileData, updateProfileData, updateAddressData, saveProfile}}>
          {children}
        </ProfileContext.Provider>
    );
};

export const useProfile = () => useContext(ProfileContext);


