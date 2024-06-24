import React, { useState, useContext } from 'react';
import './profile.css';
import AuthContext from '../../context/AuthContext';
import { useNavigate } from 'react-router-dom';

const DeleteAccount = () => {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [error, setError] = useState("");
  const [success, setSuccess] = useState("");
  const [errorTimeout, setErrorTimeout] = useState(null);
  const { deleteUser } = useContext(AuthContext);
  const navigate = useNavigate();
  


  const handleDeleteAccount = async (e) => {
    e.preventDefault();

    if (!email || !password) {
      setError("All fields are required.");
      setSuccess("");
      return;
    } 
    
    try {
      const response = await deleteUser(email, password);
  
      if (response.status === 400) {
        setError("Please enter a valid email address and/or password.");
        setSuccess("");
      } else {
        setError("An error occurred. Please try again later.");
        setSuccess("");
      }
  
    } catch (error) {
      setError("An error occurred. Please try again later.");
      setSuccess("");
    }
  };

  return (
    <div>
      <h2>Delete Account</h2>
    <div className= "profile-input"> 
      <div className="input-field">
        <p>Email*</p>
        <input
          type="text"
          className="input-border"
          value= {email}
          onChange={(e) => {
            setEmail(e.target.value);
          }}
        />
    </div>
    <div className="input-field">
        <p>Password*</p>
        <input
          type="password"
          className="input-border"
          value= {password}
          onChange={(e) => {
            setPassword(e.target.value);
          }}
        />
    </div>
      <div className={`error-message-profile ${error ? 'show' : ''}`}>{error}</div>
        <div className={`success-message-profile ${success ? 'show' : ''}`}>{success}</div>
    </div>
    <div className='submit-profile-container'>
          <div className='btn' onClick = {handleDeleteAccount}>Delete Account</div>
    </div>
    </div>
  
  );
};

export default DeleteAccount;
