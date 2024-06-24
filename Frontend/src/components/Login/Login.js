import React, { useState, useContext } from 'react';
import './login.css';
import { MdEmail } from "react-icons/md";
import { RiLockPasswordLine } from "react-icons/ri";
import AuthContext from "../../context/AuthContext";
import { useNavigate } from 'react-router-dom';

const Login = () => {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [error, setError] = useState("");
  const [success, setSuccess] = useState("");
  const [errorTimeout, setErrorTimeout] = useState(null);
  const { loginUser } = useContext(AuthContext);
  const navigate = useNavigate();


  const handleSubmit = async (e) => {
    e.preventDefault();
    
    const timeout = setTimeout(() => setError(""), 5000);
    
    if (errorTimeout) {
      clearTimeout(errorTimeout); 
    }

    if (!email || !password) {
      setError('Please enter both email and password.');
      setSuccess("");
      return;
    }

    try {
      const response = await loginUser(email, password);
      if (response.status === 200) {
        setError("");
        setSuccess("Login Successful");
        setTimeout(() => {
          navigate('/')
        }, 2000);
      } else if(response.status == 400){
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
    <section id='Login' className='login'>
      <div className='login-div'>
        <div className='login-text'>
          <div className='text'>Login</div>
          <div className='underline'></div>
        </div>

        <div className='inputs'>
          <div className='input'>
            <MdEmail className='icon' />
            <input
              type='email'
              placeholder='Email Id'
              required
              value={email}
              onChange={(e) => {
                setEmail(e.target.value);
              }}
            />
          </div>

          <div className='input'>
            <RiLockPasswordLine className='icon' />
            <input
              type='password'
              placeholder='Password'
              value={password}
              onChange={(e) => {
                setPassword(e.target.value);
              }}
            />
          </div>
        </div>

        <div className={`error-message ${error ? 'show' : ''}`}>{error}</div>
        <div className={`success-message ${success ? 'show' : ''}`}>{success}</div>

        <div className='submit-container'>
          <div
            className='btn'
            onClick={(e) => {
              handleSubmit(e);
            }}
          >Login</div>
        </div>

        <div className='auth-toggle-login'>
          <div onClick={() => {
            navigate('/signup');
          }}>
            Don't have an account? <span>Click Here to Signup!</span>
          </div>
          <div className='auth-toggle-password'>
            Forgot Password? <span>Click Here!</span>
          </div>
        </div>
      </div>
    </section>
  );
};

export default Login;
