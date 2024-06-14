import React, { useState, useContext } from 'react';
import './login.css';
import { FaRegUser } from "react-icons/fa";
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

  const timeout = setTimeout(() => setError(""), 3000);

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (errorTimeout) {
      clearTimeout(errorTimeout); // Clear existing timeout if any
    }

    if (!email || !password) { 
        setError('Please enter both username and password.'); 
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
      } else if (response.status === 403) {
        setError("the username or password is incorrect.");
        setSuccess("");
        setErrorTimeout(timeout);
      } else {
        setError("An error occurred. Please try again.");
        setSuccess(""); 
        setErrorTimeout(timeout);
      }
    } catch (error) {
      setError("An error occurred. Please try again.");
      setSuccess("");
      setErrorTimeout(timeout);
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
            <MdEmail className='icon'/>
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
        
        {error && <div className="error-message">{error}</div>}
        {success && <div className="success-message">{success}</div>}

        <div className='submit-container'>
          <div 
            className='submit'
            onClick={(e) => {
              handleSubmit(e); 
            }}
          >Login</div>
        </div>

        <div className='auth-toggle-login'>
          <div onClick={(e) => {
              navigate('/signup'); 
            }}>
            Don't have an account? <span> Click Here to Signup! </span>
          </div>
          <div className='auth-toggle-password' >
            Forgot Password? <span> Click Here! </span>
          </div>
        </div>
      </div>
    </section>
  );
};

export default Login;
