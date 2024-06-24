import React, { useState, useContext } from 'react';
import './signup.css';
import { FaRegUser } from "react-icons/fa";
import { MdEmail } from "react-icons/md";
import { RiLockPasswordLine } from "react-icons/ri";
import AuthContext from "../../context/AuthContext";
import { useNavigate } from 'react-router-dom';

const Signup = () => {
  const [firstName, setFirstName] = useState("");
  const [lastName, setLastName] = useState("");
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [userType, setUserType] = useState("");
  const [error, setError] = useState("");
  const [success, setSuccess] = useState("");
  const [errorTimeout, setErrorTimeout] = useState(null);
  const { registerUser} = useContext(AuthContext);
  const navigate = useNavigate();

  const validateEmail = (email) => {
    const re = /\S+@\S+\.\S+/;
    return re.test(email);
  };

  const validatePassword = (password) => {
    const re = /^(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{6,}$/;
    return re.test(password);
  };



  const handleSubmit = async (e) => {
    e.preventDefault();
    
    const timeout = setTimeout(() => setError(""), 5000);
    if (errorTimeout) {
      clearTimeout(errorTimeout); 
    }

    if (!firstName || !lastName || !email || !password || !userType) {
      setError("All fields are required.");
      setSuccess("");
      return;
    }

    if (!validateEmail(email)) {
      setError("Please enter a valid email address 'example@gmail.com'");
      setSuccess("");
      return;
    }

    if (!validatePassword(password)) {
      setError("Password must be at least 6 characters, one number, one uppercase letter, and one special character.");
      setSuccess("");
      return;
    }

    try {
      const response = await registerUser(firstName, lastName, email, password, userType);
      if (response.status === 200) {
        setError("");
        setSuccess("Signup Successful");
        setTimeout(() => {
          navigate('/login')
          }, 3000);
      } else if (response.status === 400) {
          const errorMessage = await response.json();
          setError(errorMessage.message);
          setSuccess("");
      }else {
        setError("An error occurred. Please try again later.");
        setSuccess("");
      }
    } catch (error) {
        setError("An error occurred. Please try again later.");
        setSuccess("");
    }
  };

  return (
    <section id='Signup' className='signup'>
      <div className='signup-div'> 
        <div className='signup-text'>
          <div className='text'>SignUp</div>
          <div className='underline'></div>
        </div>
        <div className='inputs'>
          
          <div className='input'>
            <FaRegUser className='icon' />
            <input 
              type='text' 
              placeholder='First Name' 
              value={firstName}
              onChange={(e) => {
              setFirstName(e.target.value);
              }}
            />
          </div>
          
          <div className='input'>
            <FaRegUser className='icon' />
            <input 
              type='text' 
              placeholder='Last Name' 
              value={lastName}
              onChange={(e) => {
              setLastName(e.target.value);
              }}
            />
          </div>

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
       
 
        <div className='userType' id="userType-select" >
          <label htmlFor="userType-select">User Type:</label>
          <select 
            value={userType} 
            onChange={(e) => {
            setUserType(e.target.value);
          }}
          >
            <option value="">Please choose an option</option>
            <option value="Individual">Individual</option>
            <option value="Organization">Organization</option>
          </select>
        </div>
    
        
        <div className={`error-message-signup ${error ? 'show' : ''}`}>{error}</div>
        <div className={`success-message-signup ${success ? 'show' : ''}`}>{success}</div>

        <div className='submit-container'>
          <div className='btn' 
            onClick={(e) => {
              handleSubmit(e);
            }}
          >Sign Up</div>
        </div>

       
        <div
          className='auth-toggle'
          onClick={() => {
            navigate('/login');
          }}
          >
            Already signed up? <span> Click Here To Login! </span>
        </div>
      </div>
    </section>
  );
};

export default Signup;