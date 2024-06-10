import React, { useState, useContext } from 'react';
import './signup.css';
import { FaRegUser } from "react-icons/fa";
import { MdEmail } from "react-icons/md";
import { RiLockPasswordLine } from "react-icons/ri";
import AuthContext from "../../context/AuthContext";

const Signup = () => {
  const [action, setAction] = useState('Sign Up');
  const [firstName, setFirstName] = useState("");
  const [lastName, setLastName] = useState("");
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [userType, setUserType] = useState("");
  const [error, setError] = useState("");
  const [success, setSuccess] = useState("");
  const [errorTimeout, setErrorTimeout] = useState(null);
  const { registerUser, loginUser } = useContext(AuthContext);

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
    if (errorTimeout) {
      clearTimeout(errorTimeout); // Clear existing timeout if any
    }

    if (action === 'Sign Up') {
      if (!firstName || !lastName || !email || !password || !userType) {
        setError("All fields are required.");
        setSuccess("");
        const timeout = setTimeout(() => setError(""), 3000);
        setErrorTimeout(timeout);
        return;
      }

      if (!validateEmail(email)) {
        setError("Please enter a valid email address 'example@gmail.com'");
        setSuccess("");
        const timeout = setTimeout(() => setError(""), 3000); 
        setErrorTimeout(timeout);
        return;
      }

      if (!validatePassword(password)) {
        setError("Password must be at least 6 characters, one number, one uppercase letter, and one special character.");
        setSuccess("");
        const timeout = setTimeout(() => setError(""), 3000); // Clear error after 5 seconds
        setErrorTimeout(timeout);
        return;
      }

      try {
        const response = await registerUser(firstName, lastName, email, password, userType);
        if (response.status === 200) {
          setError("");
          setSuccess("Signup Successful");
          setTimeout(() => {
            window.location.reload();
          }, 2000);
        } else if (response.status === 400) {
          const errorMessage = await response.text();
          setError(errorMessage);
          setSuccess("");
          const timeout = setTimeout(() => setError(""), 3000); // Clear error after 5 seconds
          setErrorTimeout(timeout);
        } else {
          setError("An error occurred. Please try again.");
          setSuccess(""); 
          const timeout = setTimeout(() => setError(""), 3000); // Clear error after 5 seconds
          setErrorTimeout(timeout);
        }
      } catch (error) {
        setError("An error occurred. Please try again.");
        setSuccess(""); 
        const timeout = setTimeout(() => setError(""), 3000); // Clear error after 5 seconds
        setErrorTimeout(timeout);
      }
    } else {
      loginUser(email, password);
    }
  };

  const handleActionToggle = (newAction) => {
    setAction(newAction);
    setError("");
    setSuccess("");
    if (errorTimeout) {
      clearTimeout(errorTimeout); // Clear timeout when switching actions
    }
  };

  return (
    <section id='Signup' className='signup'>
      <div className='signup-div'> 
        <div className='signup-text'>
          <div className='text'>{action}</div>
          <div className='underline'></div>
        </div>
        <div className='inputs'>
          {action === 'Login' ? <></> : 
            <>
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
            </>
          }

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
       
        {action === 'Login' ? <></> : 
          <div className='userType' id="userType-select" >
            <label htmlFor="userType-select">Select a User Type:</label>
            <select 
              value={userType} 
              onChange={(e) => {
                setUserType(e.target.value);
              }}
            >
              <option value="">--Please choose an option--</option>
              <option value="Individual">Individual</option>
              <option value="Organization">Organization</option>
            </select>
          </div>
        }
        
        {error && <div className="error-message">{error}</div>}
        {success && <div className="success-message">{success}</div>}

        <div className='submit-container'>
          <div className={action === 'Login' ? 'hide' : 'submit'} 
            onClick={(e) => {
              handleSubmit(e);
            }}
          >Sign Up</div>
          <div className={action === 'Sign Up' ? 'hide' : 'submit'} 
            onClick={(e) => {
              handleSubmit(e); 
            }}
          >Login</div>
        </div>

        {action === "Sign Up" ? (
          <div
            className='auth-toggle'
            onClick={() => {
              setAction('Login');
              handleActionToggle('Login');
            }}
          >
            Already signed up? <span> Click Here To Login! </span>
          </div>
        ) : (
          <>
            <div 
              className='auth-toggle'
              onClick={() => {
                setAction('Sign Up');
                handleActionToggle('Sign Up');
              }}
            >
              Don't have an account? <span> Click Here to Signup! </span>
            </div>
            <div className='auth-toggle-password'>
              Forgot Password? <span> Click Here! </span>
            </div>
          </>
        )}
      </div>
    </section>
  );
};

export default Signup;